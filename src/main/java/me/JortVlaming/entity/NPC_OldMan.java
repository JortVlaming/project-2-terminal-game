package me.JortVlaming.entity;

import me.JortVlaming.game.GUI;
import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

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
            up1 = loadImage("oldman_up_1.png", IMAGE_PATH);
            up2 = loadImage("oldman_up_2.png", IMAGE_PATH);
            down1 = loadImage("oldman_down_1.png", IMAGE_PATH);
            down2 = loadImage("oldman_down_2.png", IMAGE_PATH);
            left1 = loadImage("oldman_left_1.png", IMAGE_PATH);
            left2 = loadImage("oldman_left_2.png", IMAGE_PATH);
            right1 = loadImage("oldman_right_1.png", IMAGE_PATH);
            right2 = loadImage("oldman_right_2.png", IMAGE_PATH);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to load oldman images: " + e.getMessage(), e);
        }
    }

    @Override
    public void update() {
        setAction();
        collisionOn = false;
        gp.getCollisionChecker().checkTile(this);
        gp.getCollisionChecker().checkPlayer(this);

        moveWithCurrentDirection();

        incrementSpriteCounter();
    }

    @Override
    public void setAction() {
        actionLockTimer++;

        if (actionLockTimer >= 90) {
            actionLockTimer = 0;
            Random r = new Random();
            int i = r.nextInt(5);

            switch (i) {
                case 1: {
                    direction = 0;
                    break;
                }
                case 2: {
                    direction = 1;
                    break;
                }
                case 3: {
                    direction = 2;
                    break;
                }
                case 4: {
                    direction = 3;
                }
            }
        }
    }

    @Override
    public void speak() {
        gp.getGUI().setCurrentDialogue(
                new GUI.Dialogue(
                        getClass().getSimpleName().split("_")[1])
                        .addMessage("Lorum ipsum")
                        .addMessage("Lorum ipsum 2")
                        .addMessage("Lorum ipsum 3\nLorum ipsum 4")
        );

        super.speak();
    }
}
