package me.JortVlaming.monster;

import me.JortVlaming.entity.Entity;
import me.JortVlaming.game.GUI;
import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.Sound;

import java.awt.*;
import java.util.Random;

public abstract class HostileEntity extends Entity {

    public int IFrames = 0;
    public int IFramesWhenHit = 90;

    public int healthBarOffsetY = 0;

    public boolean dying = false;
    public int deathCounter = -1;
    public int timeToDie = 90;

    public int expOnKill = 0;
    public int attack = 0;

    public HostileEntity(GamePanel gp) {
        super(gp);
    }

    @Override
    public void draw(Graphics2D g2D) {
        int blinkInterval = 20;

        if (deathCounter >= 0) {
            if ((deathCounter / (blinkInterval / 2)) % 2 == 0) {
                super.draw(g2D);
            }
        } else if (IFrames > 0) {
            if ((IFrames / (blinkInterval / 2)) % 2 == 0) {
                super.draw(g2D);
            }
        } else if (!dying) {
            super.draw(g2D);
        }
    }


    @Override
    public void takeDamage(int i) {
        super.takeDamage(i);

        IFrames = IFramesWhenHit;
        if (life <= 0) {
            dying = true;
            deathCounter = timeToDie;

            gp.getPlayer().exp += expOnKill;

            while (gp.getPlayer().exp >= gp.getPlayer().nextLevelExp && gp.getPlayer().level < 20) {
                gp.getPlayer().exp -= gp.getPlayer().nextLevelExp;
                gp.getPlayer().level += 1;
                gp.getPlayer().nextLevelExp = 5 * gp.getPlayer().level;
                gp.getPlayer().powerPoints++;
                gp.getGUI().showMessage(new GUI.Message("You levelled up!", 2500));
            }
        }
    }

    @Override
    public void update() {
        if (dying) {
            if (deathCounter > 0) deathCounter--;
            if (deathCounter == 0) {
                gp.getEntityManager().removeEntityFromWorld(this);
            }
            return;
        }
        setAction();
        collisionOn = false;
        if (IFrames > 0)
            IFrames--;
        else
            gp.getCollisionChecker().checkPlayerAttack(this);
        gp.getCollisionChecker().checkPlayer(this);
        if (collisionOn) {
            if (gp.getPlayer().IFrames <= 0) {
                gp.playSE(Sound.Clips.GAMEOVER);

                int damage = attack - gp.getPlayer().getDefense();
                if (damage < 0) damage = 0;

                gp.getPlayer().takeDamage(damage);
            }
        }
        gp.getCollisionChecker().checkTile(this);

        moveWithCurrentDirection();

        incrementSpriteCounter();
    }

    @Override
    public void setAction() {
        if (dying) return;
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
}
