package me.JortVlaming.monster;

import me.JortVlaming.entity.Entity;
import me.JortVlaming.game.GamePanel;

import java.awt.*;

public abstract class HostileEntity extends Entity {

    public int IFrames = 0;
    public int IFramesWhenHit = 90;

    public HostileEntity(GamePanel gp) {
        super(gp);
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
    public void takeDamage(int i) {
        super.takeDamage(i);

        IFrames = IFramesWhenHit;
    }
}
