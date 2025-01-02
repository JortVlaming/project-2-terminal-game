package me.JortVlaming.object;

import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;

    public SuperObject(String name, String imageName) {
        this.name = name;
        try {
            InputStream is = getClass().getResourceAsStream("/objects/" + imageName + ".png");

            if (is == null) {
                System.out.println("Object image " + imageName + " not found!");
                return;
            }

            image = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        solidArea = new Rectangle(0, 0, GamePanel.getInstance().getTileSize(), GamePanel.getInstance().getTileSize());
    }

    public void draw(Graphics2D g2D, GamePanel gp) {
        int screenX = worldX - gp.getPlayer().worldX + gp.getPlayer().screenX;
        int screenY = worldY - gp.getPlayer().worldY + gp.getPlayer().screenY;

        if (Util.isOnScreen(worldX, worldY, gp)) {
            g2D.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
        }
    }
}
