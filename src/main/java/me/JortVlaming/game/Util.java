package me.JortVlaming.game;

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
}
