package me.JortVlaming.game;

import me.JortVlaming.entity.Player;
import me.JortVlaming.object.ObjectManager;
import me.JortVlaming.object.SuperObject;
import me.JortVlaming.tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public static GamePanel instance = null;
    public static boolean CHECK_COLLISION = true;
    public static boolean DO_OBJECTS = true;

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

    Input input = new Input(scale);
    Thread gameThread;
    Player player = new Player(this, input);
    TileManager tileManager = new TileManager(this);
    CollisionChecker collisionChecker = new CollisionChecker(this);
    SuperObject[] objects = new SuperObject[10];
    ObjectManager objectManager;
    AssetSetter aSetter = new AssetSetter(this);
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

        objectManager = new ObjectManager(this);
        GUI = new GUI(this);

        tileManager.loadMap_csv("test");
        objectManager.loadObject_csv("test");
    }

    public void startGameThread() {
        //aSetter.setObject();

        playMusic(Sound.Clips.BLUEBOYADVENTURE);

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

    public void update() {
        input.update();
        if (update_player)
            player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        //tileManager.drawAll(g2D);
        tileManager.draw(g2D);

        if (DO_OBJECTS)
            objectManager.draw(g2D);

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

    public SuperObject[] getObjects() {
        return objects;
    }

    public GUI getGUI() {
        return GUI;
    }

    //</editor-fold>


    public static GamePanel getInstance() {
        return instance;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }
}
