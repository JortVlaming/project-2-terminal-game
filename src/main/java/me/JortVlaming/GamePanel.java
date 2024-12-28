package me.JortVlaming;

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

    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null && gameThread.isAlive()) {
            // 1 UPDATE: update information such as character position
            update();

            // 2 RENDER: render the screen with the updated information
            repaint();
        }
    }

    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        g2D.setColor(Color.WHITE);

        g2D.fillRect(100, 100, tileSize, tileSize);

        g2D.dispose();
    }
}
