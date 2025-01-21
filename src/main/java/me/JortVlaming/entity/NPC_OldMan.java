package me.JortVlaming.entity;

import com.crystalcoding.pathfinding.Point;
import me.JortVlaming.game.GUI;
import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.Util;
import org.apache.commons.lang3.ArrayUtils;

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

        if (currentPointPath != null)
            moveWithCurrentDirection();

        incrementSpriteCounter();
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
