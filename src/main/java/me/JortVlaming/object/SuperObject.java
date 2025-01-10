package me.JortVlaming.object;

import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SuperObject implements Cloneable {
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

    public SuperObject(String name, InputStream is) {
        this.name = name;
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        solidArea = new Rectangle(0, 0, GamePanel.getInstance().getTileSize(), GamePanel.getInstance().getTileSize());
    }

    public void draw(Graphics2D g2D, GamePanel gp) {
        int screenX = (int) (worldX - gp.getPlayer().worldX + gp.getPlayer().screenX);
        int screenY = (int) (worldY - gp.getPlayer().worldY + gp.getPlayer().screenY);

        if (Util.isOnScreen(worldX, worldY, gp)) {
            g2D.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
        }
    }

    @Override
    public SuperObject clone() {
        try {
            SuperObject clone = (SuperObject) super.clone();

            clone.image = (image != null)
                    ? deepCopyImage(image)
                    : null;

            clone.solidArea = (solidArea != null)
                    ? new Rectangle(solidArea)
                    : null;

            clone.name = name;
            clone.collision = collision;
            clone.solidAreaDefaultX = solidAreaDefaultX;
            clone.solidAreaDefaultY = solidAreaDefaultY;
            clone.worldX = worldX;
            clone.worldY = worldY;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning failed, although Cloneable is implemented.", e);
        }
    }

    private BufferedImage deepCopyImage(BufferedImage source) {
        BufferedImage copy = new BufferedImage(
                source.getWidth(),
                source.getHeight(),
                source.getType()
        );
        Graphics2D g2d = copy.createGraphics();
        g2d.drawImage(source, 0, 0, null);
        g2d.dispose();
        return copy;
    }
}
