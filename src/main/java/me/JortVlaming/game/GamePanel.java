package me.JortVlaming.game;

import me.JortVlaming.entity.Entity;
import me.JortVlaming.entity.EntityManager;
import me.JortVlaming.entity.Player;
import me.JortVlaming.events.EventHandler;
import me.JortVlaming.object.ObjectManager;
import me.JortVlaming.tile.TileManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    public static GamePanel instance = null;
    public static boolean CHECK_COLLISION = true;
    public static boolean DO_OBJECTS = true;
    public static boolean DO_ENTITIES = true;
    public static boolean ALLOW_DEBUG = false;

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

    public GameState currentState = GameState.TITLE_SCREEN;

    // TITLE SCREEN STUFF
    public static BufferedImage titleScreenImage;

    // SYSTEMS
    Input input = new Input(scale);
    Thread gameThread;
    Player player;
    EntityManager entityManager = new EntityManager(this);
    TileManager tileManager = new TileManager(this);
    CollisionChecker collisionChecker = new CollisionChecker(this);
    ObjectManager objectManager;
    Sound music = new Sound();
    Sound effects = new Sound();
    GUI GUI;
    EventHandler events;

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

        InputStream titleScreenImageStream = getClass().getResourceAsStream("/title_screen_background.png");
        if (titleScreenImageStream == null) {
            System.out.println("Title screen image not found!");
            titleScreenImage = null;
        } else {
            try {
                titleScreenImage = ImageIO.read(titleScreenImageStream);
            } catch (IOException e) {
                System.out.println("Failed to load title screen image!");
                titleScreenImage = null;
            }
        }

        objectManager = new ObjectManager(this);
        player = new Player(this, input);
        GUI = new GUI(this);
        events = new EventHandler(this);

        tileManager.loadMap_csv("test");
        objectManager.loadObject_csv("test");
        events.loadEventsFromMap_csv("test");
        entityManager.loadEntities_csv("test");

        player.worldX = ObjectManager.playerStartX;
        player.worldY = ObjectManager.playerStartY;
    }

    public void startGameThread() {
        playMusic(Sound.Clips.BLUEBOYADVENTURE);

        currentState = GameState.TITLE_SCREEN;

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
        if (currentState == GameState.TITLE_SCREEN) {
            if (input.isKeyDown(KeyEvent.VK_SPACE)) {
                System.out.println("Starting game...");
                currentState = GameState.PLAYING;
            }

            input.update();

            return;
        }
        boolean skipPlayerThisFrame = false;

        if (input.isKeyDown(KeyEvent.VK_P) || input.isKeyDown(KeyEvent.VK_ESCAPE)) {
            System.out.println("TOGGLE PAUSE");
            if (currentState == GameState.PLAYING)
                currentState = GameState.PAUSED;
            else if (currentState == GameState.PAUSED)
                currentState = GameState.PLAYING;
        }

        if ((input.isKeyDown(KeyEvent.VK_F3) && ALLOW_DEBUG) || (input.isKey(KeyEvent.VK_SHIFT) && input.isKey(KeyEvent.VK_CONTROL) && input.isKeyDown(KeyEvent.VK_F3))) {
            DEBUG = !DEBUG;
            if (DEBUG) {
                System.out.println("DEBUG MODE ON");
            } else {
                System.out.println("DEBUG MODE OFF");
            }
        }

        if (DEBUG) {
            if (input.isKeyDown(KeyEvent.VK_K) && player.life > 0) {
                player.life--;
            }
            if (input.isKeyDown(KeyEvent.VK_L) && player.life < player.maxLife) {
                player.life++;
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
                entityManager.updateEntities();
            }

            player.update();
        }

        input.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        if (DEBUG) {
            events.draw(g2D);
        }

        if (currentState == GameState.TITLE_SCREEN) {
            if (titleScreenImage != null) {
                g2D.drawImage(titleScreenImage, 0, 0, null);
            }

            GUI.draw(g2D);
        } else {
            //tileManager.drawAll(g2D);
            tileManager.draw(g2D);

            if (DO_OBJECTS)
                objectManager.draw(g2D);

            entityManager.drawEntities(g2D);

            GUI.draw(g2D);
        }

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

    public ArrayList<Entity> getNPCs() {
        return (ArrayList<Entity>) entityManager.activeNPCEntities;
    }
}
