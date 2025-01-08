package me.JortVlaming.entity;

import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.GameState;
import me.JortVlaming.game.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public abstract class Entity {
    // CONFIG STUFF
    public GamePanel gp;
    public int worldX, worldY;
    public int speed;

    // SPRITE STUFF
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public int direction; // 0 = up, 1 = right, 2 = down, 3 = left
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // COLLISION STUFF
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn;

    // ACTION STUFF
    public int actionLockTimer = 0;

    // CHARACTER STAT STUFF
    public int maxLife;
    public int life;

    public Entity(GamePanel gp) {
        this.gp = gp;

        solidArea = new Rectangle(0, 0, gp.getTileSize(), gp.getTileSize());
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        loadImages();
    }

    public abstract void loadImages();

    public abstract void update();
    public abstract void setAction();

    public void speak() {
        gp.currentState = GameState.DIALOGUE;

        switch (gp.getPlayer().direction) {
            default:
            case 0: {
                direction = 2;
                break;
            }
            case 1: {
                direction = 3;
                break;
            }
            case 2: {
                direction = 0;
                break;
            }
            case 3: {
                direction = 1;
                break;
            }
        }
    }

    public void incrementSpriteCounter() {
        spriteCounter++;
        if (spriteCounter > 15) {
            spriteNum = spriteNum == 1 ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2D) {
        BufferedImage image;

        switch (direction) {
            case 0:
            default: {
                image = spriteNum == 1 ? up1 : up2;
                break;
            }
            case 1: {
                image = spriteNum == 1 ? right1 : right2;
                break;
            }
            case 2: {
                image = spriteNum == 1 ? down1 : down2;
                break;
            }
            case 3: {
                image = spriteNum == 1 ? left1 : left2;
                break;
            }
        }

        if (Util.isOnScreen(worldX, worldY, gp)) {
            int screenX = worldX - gp.getPlayer().worldX + gp.getPlayer().screenX;
            int screenY = worldY - gp.getPlayer().worldY + gp.getPlayer().screenY;

            g2D.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);

            if (GamePanel.DEBUG) {
                g2D.setColor(Color.RED);
                g2D.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
                g2D.setColor(Color.WHITE);
                FontMetrics fm = g2D.getFontMetrics();
                g2D.setFont(new Font("Arial", Font.PLAIN, 20));
                Rectangle2D bounds = fm.getStringBounds(direction + "", g2D);
                g2D.drawString(direction + "", (int) (screenX + solidArea.x + (double) solidArea.width / 2 - bounds.getWidth()/2), (int) (screenY + solidArea.y + bounds.getHeight()/2 + (double) solidArea.height / 2));
            }
        }
    }

    public BufferedImage loadImage(String imageName, String IMAGE_PATH) {
        try {
            InputStream is = getClass().getResourceAsStream(IMAGE_PATH + imageName);
            if (is != null) return ImageIO.read(is);
            System.out.println("Failed to load image: " + imageName + " from path: " + IMAGE_PATH);
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Could not load image at path: " + IMAGE_PATH + imageName, e);
        }
    }

    public void moveWithCurrentDirection() {
        if (!collisionOn) {
            switch (direction) {
                case 0:
                default: {
                    worldY -= speed;
                    break;
                }
                case 1: {
                    worldX += speed;
                    break;
                }
                case 2: {
                    worldY += speed;
                    break;
                }
                case 3: {
                    worldX -= speed;
                    break;
                }
            }
        }
    }

    public void takeDamage(int i) {
        life -= i;
        // TODO death
    }
}
