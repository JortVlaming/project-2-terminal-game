package me.JortVlaming.game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Util {
    public static boolean isOnScreen(int worldX, int worldY, GamePanel gp) {
        return worldX + gp.getTileSize() > gp.getPlayer().worldX - gp.getPlayer().screenX &&
                worldX - gp.getTileSize() < gp.getPlayer().worldX + gp.getPlayer().screenX &&
                worldY + gp.getTileSize() > gp.getPlayer().worldY - gp.getPlayer().screenY &&
                worldY - gp.getTileSize() < gp.getPlayer().worldY + gp.getPlayer().screenY;
    }

    public static int getDistanceFromPlayer(int x, int y, GamePanel gp) {
        return (int) Math.sqrt(Math.pow(x - gp.getPlayer().worldX, 2) + Math.pow(y - gp.getPlayer().worldY, 2));
    }

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, original.getType());
        Graphics2D g2D = scaled.createGraphics();
        g2D.drawImage(original, 0, 0, width, height, null);
        g2D.dispose();

        return scaled;
    }

    public static boolean isOnScreen(Rectangle2D pits, GamePanel gp) {
        return isOnScreen((int) pits.getX(), (int) pits.getY(), gp);
    }

    public static int getDistanceFromPlayer(Rectangle2D pits, GamePanel gp) {
        return getDistanceFromPlayer((int) pits.getX(), (int) pits.getY(), gp);
    }
}
