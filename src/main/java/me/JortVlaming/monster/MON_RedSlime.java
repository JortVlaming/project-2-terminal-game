package me.JortVlaming.monster;

import me.JortVlaming.game.GamePanel;

import java.util.Random;

public class MON_RedSlime extends HostileEntity{
    public MON_RedSlime(GamePanel gp) {
        super(gp);

        speed = 2;
        maxLife = 6;
        life = maxLife;

        solidArea.x = gp.getScale();
        solidArea.y = 6 * gp.getScale();
        solidArea.width = 14 * gp.getScale();
        solidArea.height = 10 * gp.getScale();

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        expOnKill = 4;
    }

    @Override
    public void loadImages() {
        up1 = loadImage("redslime_down_1.png", "/monster/redslime/");
        up2 = loadImage("redslime_down_2.png", "/monster/redslime/");
        left1 = loadImage("redslime_down_1.png", "/monster/redslime/");
        left2 = loadImage("redslime_down_2.png", "/monster/redslime/");
        down1 = loadImage("redslime_down_1.png", "/monster/redslime/");
        down2 = loadImage("redslime_down_2.png", "/monster/redslime/");
        right1 = loadImage("redslime_down_1.png", "/monster/redslime/");
        right2 = loadImage("redslime_down_2.png", "/monster/redslime/");
    }
}
