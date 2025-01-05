package me.JortVlaming.game;

import me.JortVlaming.entity.Entity;
import me.JortVlaming.entity.NPC_OldMan;
import me.JortVlaming.entity.Player;
import me.JortVlaming.object.ObjectManager;
import me.JortVlaming.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements Runnable {
    public static GamePanel instance = null;
    public static boolean CHECK_COLLISION = true;
    public static boolean DO_OBJECTS = true;
    public static boolean DO_ENTITIES = true;

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 4;

    final int tileSize = originalTileSize * scale; // 64*64 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 1028 pixels
    final int screenHeight = tileSize * maxScreenRow; // 768 pixels

    // WORLD SETTINGS
    final int maxWorldCol = 50, maxWorldRow = 50;

    // GAMELOOP SETTINGS
    boolean running = false;
    boolean update_player = true;
    final int TARGET_FPS = 60;
    final boolean LIMIT_FPS = true;

    public GameState currentState;

    // SYSTEMS
    Input input = new Input(scale);
    Thread gameThread;
    Player player;
    Entity[] npcs = new Entity[10];
    TileManager tileManager = new TileManager(this);
    CollisionChecker collisionChecker = new CollisionChecker(this);
    ObjectManager objectManager;
    Sound music = new Sound();
    Sound effects = new Sound();
    GUI GUI;

    public GamePanel() {
        instance = this;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(input);
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        this.addMouseWheelListener(input);

        this.setFocusable(true);

        this.requestFocusInWindow();

        objectManager = new ObjectManager(this);
        player = new Player(this, input);
        GUI = new GUI(this);

        tileManager.loadMap_csv("empty");
        objectManager.loadObject_csv("empty");

        player.worldX = ObjectManager.playerStartX;
        player.worldY = ObjectManager.playerStartY;
    }

    public void startGameThread() {
        if (DO_ENTITIES) {
            npcs[0] = new NPC_OldMan(this);
            npcs[0].worldX = player.worldX - 5 * tileSize;
            npcs[0].worldY = player.worldY - 5 * tileSize;
            npcs[0].direction = 2;
        }

        playMusic(Sound.Clips.BLUEBOYADVENTURE);

        currentState = GameState.PLAYING;

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        running = true;

        double drawInterval = (double) 1000000000/TARGET_FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        while (running) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);

            lastTime = currentTime;

            if (delta >= 1 || !LIMIT_FPS) {
                update();
                repaint();
                delta--;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }

        System.exit(0);
    }

    public static boolean DEBUG = false;

    public void update() {
        boolean skipPlayerThisFrame = false;

        if (input.isKeyDown(KeyEvent.VK_P) || input.isKeyDown(KeyEvent.VK_ESCAPE)) {
            System.out.println("TOGGLE PAUSE");
            if (currentState == GameState.PLAYING)
                currentState = GameState.PAUSED;
            else if (currentState == GameState.PAUSED)
                currentState = GameState.PLAYING;
        }

        if (input.isKeyDown(KeyEvent.VK_F3)) {
            DEBUG = !DEBUG;
            if (DEBUG) {
                System.out.println("DEBUG MODE ON");
            } else {
                System.out.println("DEBUG MODE OFF");
            }
        }

        if (currentState == GameState.DIALOGUE) {
            if (input.isKeyDown(KeyEvent.VK_SPACE)) {
                if (!GUI.getCurrentDialogue().hasNextMessage()) {
                    skipPlayerThisFrame = true;
                    currentState = GameState.PLAYING;
                    GUI.clearDialogue();
                } else {
                    GUI.getCurrentDialogue().nextMessage();
                }
            }
        }

        if (currentState == GameState.PLAYING && !skipPlayerThisFrame) {
            if (DO_ENTITIES) {
                entitiesUpdatedCount = 0;
                averageEntityActionLockTimer = 0;
                for (Entity e : npcs) {
                    if (e == null) continue;
                    if (Util.getDistanceFromPlayer(e.worldX, e.worldY, this) < 1000) {
                        e.update();
                        entitiesUpdatedCount++;
                        averageEntityActionLockTimer += e.actionLockTimer;
                    }
                    averageEntityActionLockTimer /= entitiesUpdatedCount;
                }
            }

            player.update();
        }

        input.update();
    }

    int entitiesDrawnCount = 0;
    int entitiesUpdatedCount = 0;
    int averageEntityActionLockTimer = 0;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        //tileManager.drawAll(g2D);
        tileManager.draw(g2D);

        if (DO_OBJECTS)
            objectManager.draw(g2D);

        entitiesDrawnCount = 0;
        for (Entity e : npcs) {
            if (e == null) continue;
            if (Util.isOnScreen(e.worldX, e.worldY, this)) {
                e.draw(g2D);
                entitiesDrawnCount++;
            }
        }

        player.draw(g2D);

        GUI.draw(g2D);

        g2D.dispose();
    }

    public void playMusic(Sound.Clips clips) {
        music.setFile(clips.index);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(Sound.Clips clips) {
        effects.setFile(clips.index);
        effects.play();
    }

    //<editor-fold desc="getters and such">

    public int getTileSize() {
        return tileSize;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getMaxWorldCol() {
        return maxWorldCol;
    }

    public int getMaxWorldRow() {
        return maxWorldRow;
    }

    public Player getPlayer() {
        return player;
    }

    public int getScale() {
        return scale;
    }

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    public GUI getGUI() {
        return GUI;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }
    //</editor-fold>


    public static GamePanel getInstance() {
        return instance;
    }

    public Input getInput() {
        return input;
    }

    public Entity[] getNPCs() {
        return npcs;
    }
}
