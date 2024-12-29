package me.JortVlaming.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public int direction; // 0 = up, 1 = right, 2 = down, 3 = left

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public abstract void update();

    public abstract void draw(Graphics2D g2D);
}
