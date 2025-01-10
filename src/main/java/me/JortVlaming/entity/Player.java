package me.JortVlaming.entity;

import me.JortVlaming.game.*;
import me.JortVlaming.monster.HostileEntity;
import me.JortVlaming.monster.MON_GreenSlime;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    Input i;

    public final int screenX;
    public final int screenY;

    public int frozenFor;

    public int IFrames = 0;
    public int IFramesWhenHit = 90;

    // ATTACKING STUFF
    boolean canAttack = true;
    boolean attacking = false;
    int attackingTime = 0;
    public Rectangle attackCollisionArea;

    // <editor-fold desc="additional sprites">

    private BufferedImage attack_down1;
    private BufferedImage attack_down2;
    private BufferedImage attack_left1;
    private BufferedImage attack_left2;
    private BufferedImage attack_right1;
    private BufferedImage attack_right2;
    private BufferedImage attack_up1;
    private BufferedImage attack_up2;

    // </editor-fold>

    public Player(GamePanel gp, Input i) {
        super(gp);
        this.i = i;
        this.reset();
        screenX = gp.getScreenWidth()/2-gp.getTileSize()/2;
        screenY = gp.getScreenHeight()/2-gp.getTileSize()/2;

        solidArea = new Rectangle(12, 24, 40, 40);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackCollisionArea = new Rectangle(0,0,0,0);
    }

    int normalSpeed = 4;
    public static final String IMAGE_PATH = "/player/";

    public void reset() {
        speed = 4;

        direction = 2;

        maxLife = 6;
        life = maxLife;
    }

    @Override
    public void loadImages() {
        try {
            up1 = loadImage("boy_up_1.png", IMAGE_PATH + "walking/");
            up2 = loadImage("boy_up_2.png", IMAGE_PATH + "walking/");
            down1 = loadImage("boy_down_1.png", IMAGE_PATH + "walking/");
            down2 = loadImage("boy_down_2.png", IMAGE_PATH + "walking/");
            left1 = loadImage("boy_left_1.png", IMAGE_PATH + "walking/");
            left2 = loadImage("boy_left_2.png", IMAGE_PATH + "walking/");
            right1 = loadImage("boy_right_1.png", IMAGE_PATH + "walking/");
            right2 = loadImage("boy_right_2.png", IMAGE_PATH + "walking/");

            attack_up1 = loadImage("boy_attack_up_1.png", IMAGE_PATH + "attacking/");
            attack_up2 = loadImage("boy_attack_up_2.png", IMAGE_PATH + "attacking/");
            attack_down1 = loadImage("boy_attack_down_1.png", IMAGE_PATH + "attacking/");
            attack_down2 = loadImage("boy_attack_down_2.png", IMAGE_PATH + "attacking/");
            attack_left1 = loadImage("boy_attack_left_1.png", IMAGE_PATH + "attacking/");
            attack_left2 = loadImage("boy_attack_left_2.png", IMAGE_PATH + "attacking/");
            attack_right1 = loadImage("boy_attack_right_1.png", IMAGE_PATH + "attacking/");
            attack_right2 = loadImage("boy_attack_right_2.png", IMAGE_PATH + "attacking/");
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to load player images: " + e.getMessage(), e);
        }
    }

    @Override
    public void update() {
        if (attacking) {
            canAttack = false;
            attackingTime++;

            if (attackingTime >= 14) {
                attackingTime = 0;
                canAttack = true;
                attacking = false;

                // RESET THAT MOTHERFUCKER
                spriteWidth = gp.getTileSize();
                spriteHeight = gp.getTileSize();
                spriteOffsetX = 0;
                spriteOffsetY = 0;
            }
        }

        boolean moved = false;

        if (i.isButton(6)) {
            speed = normalSpeed*2;
        } else {
            speed = normalSpeed;
        }

        if (!attacking) {
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

        if (!interactedWithNPCThisFrame && !attacking) {
            if (i.isKeyDown(KeyEvent.VK_SPACE)) {
                attacking = true;
                spriteCounter = 0;
                spriteNum = 1;
            }
        }

        interactedWithNPCThisFrame = false;

        if (IFrames > 0) IFrames--;

        if (attacking) {
            incrementSpriteCounter();
        } else if (moved && frozenFor <= 0) {
            moveWithCurrentDirection();
            incrementSpriteCounter();
        } else if (frozenFor > 0) {
            frozenFor--;
        }
    }

    boolean interactedWithNPCThisFrame = false;

    private void interactNPC(int npcIndex) {
        if (npcIndex == -1) return;
        if (npcIndex > gp.getNPCs().size()) return;
        if (gp.getNPCs().get(npcIndex) == null) return;

        interactedWithNPCThisFrame = true;

        Entity e = gp.getNPCs().get(npcIndex);

        if (e instanceof HostileEntity) {
            HostileEntity he = (HostileEntity) e;
            if (he.dying) return;
            takeDamage(1);
        } else if (e instanceof NPC_OldMan) {
            if (gp.getInput().isKeyDown(KeyEvent.VK_SPACE)) {
                e.speak();
            }
        }
    }

    public void pickupObject(int i) {
        if (i < 0 || i > gp.getObjectManager().getActiveObjects().size()) return;

        return;
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

    void setAttackCollider() {
        if (!attacking || spriteNum == 1) return;

        switch (direction) {
            case 0: {
                attackCollisionArea.x = 8 * gp.getScale();
                attackCollisionArea.y = -9 * gp.getScale();
                attackCollisionArea.width = 3 * gp.getScale();
                attackCollisionArea.height = 9 * gp.getScale();
                break;
            }
            case 1: {
                attackCollisionArea.x = 14 * gp.getScale();
                attackCollisionArea.y = 9 * gp.getScale();
                attackCollisionArea.width = 10 * gp.getScale();
                attackCollisionArea.height = 3 * gp.getScale();
                break;
            }
            case 2: {
                attackCollisionArea.x = 6 * gp.getScale();
                attackCollisionArea.y = 16 * gp.getScale();
                attackCollisionArea.width = 3 * gp.getScale();
                attackCollisionArea.height = 9 * gp.getScale();
                break;
            }
            case 3: {
                attackCollisionArea.x = -8 * gp.getScale();
                attackCollisionArea.y = 9 * gp.getScale();
                attackCollisionArea.width = 9 * gp.getScale();
                attackCollisionArea.height = 3 * gp.getScale();
                break;
            }
        }
    }

    @Override
    public BufferedImage getCurrentImage() {
        if (!attacking) {
            return super.getCurrentImage();
        }

        setAttackCollider();

        BufferedImage image = null;

        switch (direction) {
            case 0: {
                image = spriteNum == 1 ? attack_up1 : attack_up2;
                spriteHeight = gp.getTileSize() * 2;
                spriteOffsetY = -gp.getTileSize();
                break;
            }
            case 1: {
                image = spriteNum == 1 ? attack_right1 : attack_right2;
                spriteWidth = gp.getTileSize() * 2;
                break;
            }
            case 2: {
                image = spriteNum == 1 ? attack_down1 : attack_down2;
                spriteHeight = gp.getTileSize() * 2;
                break;
            }
            case 3: {
                image = spriteNum == 1 ? attack_left1 : attack_left2;
                spriteWidth = gp.getTileSize() * 2;
                spriteOffsetX = -gp.getTileSize();
                break;
            }
        }

        return image;
    }

    public boolean isAttackColliderActive() {
        return attacking && spriteNum == 2;
    }
}
