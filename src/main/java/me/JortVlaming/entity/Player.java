package me.JortVlaming.entity;

import me.JortVlaming.game.*;
import me.JortVlaming.object.*;
import java.util.Objects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gp;
    Input i;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, Input i) {
        this.gp = gp;
        this.i = i;
        this.reset();
        screenX = gp.getScreenWidth()/2-gp.getTileSize()/2;
        screenY = gp.getScreenHeight()/2-gp.getTileSize()/2;

        solidArea = new Rectangle(12, 24, 40, 40);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    int normalSpeed = 4;
    public static final String IMAGE_PATH = "/player/walking/";

    public void reset() {
        speed = 4;

        direction = 2;

        loadPlayerImages();
    }

    public void loadPlayerImages() {
        try {
            up1 = loadImage("boy_up_1.png");
            up2 = loadImage("boy_up_2.png");
            down1 = loadImage("boy_down_1.png");
            down2 = loadImage("boy_down_2.png");
            left1 = loadImage("boy_left_1.png");
            left2 = loadImage("boy_left_2.png");
            right1 = loadImage("boy_right_1.png");
            right2 = loadImage("boy_right_2.png");
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to load player images: " + e.getMessage(), e);
        }
    }

    public BufferedImage loadImage(String imageName) {
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH + imageName)));
        } catch (IOException e) {
            throw new RuntimeException("Could not load image at path: " + IMAGE_PATH + imageName, e);
        }
    }
    @Override
    public void update() {
        boolean moved = false;

        if (i.isButton(6)) {
            speed = normalSpeed*2;
        } else {
            speed = normalSpeed;
        }

        if (i.isKey(KeyEvent.VK_W)) {
            direction = 0;
            moved = true;
        }
        if (i.isKey(KeyEvent.VK_S)) {
            direction = 2;
            moved = true;
        }
        if (i.isKey(KeyEvent.VK_A)) {
            direction = 3;
            moved = true;
        }
        if (i.isKey(KeyEvent.VK_D)) {
            direction = 1;
            moved = true;
        }

        collisionOn = false;
        if (!i.isButton(7) && GamePanel.CHECK_COLLISION) {
            gp.getCollisionChecker().checkTile(this);
            int objIndex = gp.getCollisionChecker().checkObject(this);
            pickupObject(objIndex);
        }

        if (moved && !collisionOn) {
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

        if (moved) {
            spriteCounter++;
            if (spriteCounter > 15) {
                spriteNum = spriteNum == 1 ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void pickupObject(int i) {
        if (i < 0 || i > gp.getObjectManager().getActiveObjects().size()) return;


    }

    @Override
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

        g2D.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
        //g2D.fillRect(screenX+solidArea.x, screenY+solidArea.y, solidArea.width, solidArea.height);
    }
}
