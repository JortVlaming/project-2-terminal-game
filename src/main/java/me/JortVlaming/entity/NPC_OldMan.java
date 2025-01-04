package me.JortVlaming.entity;

import me.JortVlaming.game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class NPC_OldMan extends Entity {
    public NPC_OldMan(GamePanel gp) {
        super(gp);
        direction = 2;
        speed = 2;
    }

    public static final String IMAGE_PATH = "/npc/";

    @Override
    public void loadImages() {
        try {
            System.out.println("Loaded up1");
            up1 = loadImage("oldman_up_1.png", IMAGE_PATH);
            System.out.println("Loaded up2");
            up2 = loadImage("oldman_up_2.png", IMAGE_PATH);
            System.out.println("Loaded down1");
            down1 = loadImage("oldman_down_1.png", IMAGE_PATH);
            System.out.println("Loaded down2");
            down2 = loadImage("oldman_down_2.png", IMAGE_PATH);
            System.out.println("Loaded left1");
            left1 = loadImage("oldman_left_1.png", IMAGE_PATH);
            System.out.println("Loaded left2");
            left2 = loadImage("oldman_left_2.png", IMAGE_PATH);
            System.out.println("Loaded right1");
            right1 = loadImage("oldman_right_1.png", IMAGE_PATH);
            System.out.println("Loaded right2");
            right2 = loadImage("oldman_right_2.png", IMAGE_PATH);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to load oldman images: " + e.getMessage(), e);
        }
    }

    @Override
    public void update() {

    }
}
