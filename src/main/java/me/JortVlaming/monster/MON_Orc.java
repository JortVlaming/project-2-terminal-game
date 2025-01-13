package me.JortVlaming.monster;

import me.JortVlaming.game.GamePanel;

import java.util.Random;

public class MON_Orc extends HostileEntity{
    public MON_Orc(GamePanel gp) {
        super(gp);

        speed = 2;
        maxLife = 6;
        life = maxLife;

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.getTileSize();
        solidArea.height = gp.getTileSize();

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        healthBarOffsetY = 15;

        expOnKill = 4;
        attack = 4;
    }

    @Override
    public void loadImages() {
        up1 = loadImage("orc_up_1.png", "/monster/orc/");
        up2 = loadImage("orc_up_2.png", "/monster/orc/");
        left1 = loadImage("orc_left_1.png", "/monster/orc/");
        left2 = loadImage("orc_left_2.png", "/monster/orc/");
        down1 = loadImage("orc_down_1.png", "/monster/orc/");
        down2 = loadImage("orc_down_2.png", "/monster/orc/");
        right1 = loadImage("orc_right_1.png", "/monster/orc/");
        right2 = loadImage("orc_right_2.png", "/monster/orc/");
    }
}
