package me.JortVlaming.monster;

import me.JortVlaming.game.GamePanel;

import java.util.Random;

public class MON_GreenSlime extends HostileEntity{
    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        speed = 1;
        maxLife = 4;
        life = maxLife;

        solidArea.x = gp.getScale();
        solidArea.y = 6 * gp.getScale();
        solidArea.width = 14 * gp.getScale();
        solidArea.height = 10 * gp.getScale();

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        expOnKill = 2;
        attack = 1;
    }

    @Override
    public void loadImages() {
        up1 = loadImage("greenslime_down_1.png", "/monster/greenslime/");
        up2 = loadImage("greenslime_down_2.png", "/monster/greenslime/");
        left1 = loadImage("greenslime_down_1.png", "/monster/greenslime/");
        left2 = loadImage("greenslime_down_2.png", "/monster/greenslime/");
        down1 = loadImage("greenslime_down_1.png", "/monster/greenslime/");
        down2 = loadImage("greenslime_down_2.png", "/monster/greenslime/");
        right1 = loadImage("greenslime_down_1.png", "/monster/greenslime/");
        right2 = loadImage("greenslime_down_2.png", "/monster/greenslime/");
    }
}
