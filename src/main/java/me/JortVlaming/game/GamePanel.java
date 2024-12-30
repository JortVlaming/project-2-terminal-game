package me.JortVlaming.game;

import me.JortVlaming.entity.Player;
import me.JortVlaming.tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

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
    final int worldWith = tileSize * maxWorldCol, worldHeight = tileSize * maxWorldRow;

    // GAMELOOP SETTINGS
    boolean running = false;
    final int TARGET_FPS = 60;
    final boolean LIMIT_FPS = true;

    Input input = new Input(scale);
    Thread gameThread;
    Player player = new Player(this, input);
    TileManager tileManager = new TileManager(this);
    CollisionChecker collisionChecker = new CollisionChecker(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(input);
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        this.addMouseWheelListener(input);

        this.setFocusable(true);

        tileManager.loadMap("test");
    }

    public void startGameThread() {
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
        int drawCount = 0;

        while (running) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);

            lastTime = currentTime;

            if (delta >= 1 || !LIMIT_FPS) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        //tileManager.drawAll(g2D);
        tileManager.draw(g2D);

        player.draw(g2D);

        g2D.dispose();
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxScreenCol() {
        return maxScreenCol;
    }

    public int getMaxScreenRow() {
        return maxScreenRow;
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
}
