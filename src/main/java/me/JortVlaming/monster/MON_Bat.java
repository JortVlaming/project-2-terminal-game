package me.JortVlaming.monster;

import me.JortVlaming.game.GamePanel;

import java.util.Random;

public class MON_Bat extends HostileEntity{
    public MON_Bat(GamePanel gp) {
        super(gp);

        speed = 1;
        maxLife = 2;
        life = maxLife;

        solidArea.x = gp.getScale();
        solidArea.y = 6 * gp.getScale();
        solidArea.width = 14 * gp.getScale();
        solidArea.height = 8 * gp.getScale();

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        expOnKill = 1;
    }

    @Override
    public void loadImages() {
        up1 = loadImage("bat_down_1.png", "/monster/bat/");
        up2 = loadImage("bat_down_2.png", "/monster/bat/");
        left1 = loadImage("bat_down_1.png", "/monster/bat/");
        left2 = loadImage("bat_down_2.png", "/monster/bat/");
        down1 = loadImage("bat_down_1.png", "/monster/bat/");
        down2 = loadImage("bat_down_2.png", "/monster/bat/");
        right1 = loadImage("bat_down_1.png", "/monster/bat/");
        right2 = loadImage("bat_down_2.png", "/monster/bat/");
    }
}
