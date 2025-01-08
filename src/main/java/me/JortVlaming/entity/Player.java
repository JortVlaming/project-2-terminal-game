package me.JortVlaming.entity;

import me.JortVlaming.game.*;
import me.JortVlaming.monster.HostileEntity;
import me.JortVlaming.monster.MON_GreenSlime;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Entity {
    Input i;

    public final int screenX;
    public final int screenY;

    public int frozenFor;

    public int IFrames = 0;
    public int IFramesWhenHit = 90;

    public Player(GamePanel gp, Input i) {
        super(gp);
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

        maxLife = 6;
        life = maxLife;
    }

    @Override
    public void loadImages() {
        try {
            up1 = loadImage("boy_up_1.png", IMAGE_PATH);
            up2 = loadImage("boy_up_2.png", IMAGE_PATH);
            down1 = loadImage("boy_down_1.png", IMAGE_PATH);
            down2 = loadImage("boy_down_2.png", IMAGE_PATH);
            left1 = loadImage("boy_left_1.png", IMAGE_PATH);
            left2 = loadImage("boy_left_2.png", IMAGE_PATH);
            right1 = loadImage("boy_right_1.png", IMAGE_PATH);
            right2 = loadImage("boy_right_2.png", IMAGE_PATH);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to load player images: " + e.getMessage(), e);
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

            int npcIndex = gp.getCollisionChecker().checkEntity(this, gp.getNPCs());
            interactNPC(npcIndex);

            gp.getCollisionChecker().checkEvents(this);
        }

        if (IFrames > 0) IFrames--;

        if (moved && frozenFor <= 0) {
            moveWithCurrentDirection();
            incrementSpriteCounter();
        } else if (frozenFor > 0) {
            frozenFor--;
        }
    }

    private void interactNPC(int npcIndex) {
        if (npcIndex == -1) return;
        if (npcIndex > gp.getNPCs().size()) return;
        if (gp.getNPCs().get(npcIndex) == null) return;

        Entity e = gp.getNPCs().get(npcIndex);

        if (e instanceof HostileEntity) {
            takeDamage(1);
        } else if (e instanceof NPC_OldMan)
            if (gp.getInput().isKeyDown(KeyEvent.VK_SPACE))
                e.speak();
    }

    public void pickupObject(int i) {
        if (i < 0 || i > gp.getObjectManager().getActiveObjects().size()) return;


    }

    @Override
    public void takeDamage(int i) {
        if (IFrames > 0) return;
        super.takeDamage(i);

        IFrames = IFramesWhenHit;
    }

    @Override
    public void draw(Graphics2D g2D) {
        int blinkInterval = 20;

        if (IFrames > 0) {
            if ((IFrames / (blinkInterval / 2)) % 2 == 0) {
                super.draw(g2D);
            }
        } else {
            super.draw(g2D);
        }
    }


    @Override
    public void setAction() {
        throw new UnsupportedOperationException("Player does not have AI capabilities");
    }
}
