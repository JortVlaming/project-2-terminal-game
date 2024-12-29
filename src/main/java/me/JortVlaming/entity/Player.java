package me.JortVlaming.entity;

import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.Input;

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
    }

    int normalSpeed = 4;
    public void reset() {
        worldX = 23*gp.getTileSize();
        worldY = 21*gp.getTileSize();
        speed = 4;

        direction = 2;

        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/Player/walking/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Player/walking/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Player/walking/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Player/walking/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Player/walking/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Player/walking/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Player/walking/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Player/walking/boy_right_2.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            worldY -= speed;
            moved = true;
        }
        if (i.isKey(KeyEvent.VK_S)) {
            direction = 2;
            worldY += speed;
            moved = true;
        }
        if (i.isKey(KeyEvent.VK_A)) {
            direction = 3;
            worldX -= speed;
            moved = true;
        }
        if (i.isKey(KeyEvent.VK_D)) {
            direction = 1;
            worldX += speed;
            moved = true;
        }

        if (moved) {
            spriteCounter++;
            if (spriteCounter > 15) {
                spriteNum = spriteNum == 1 ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2D) {
        BufferedImage image = null;

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
    }
}
