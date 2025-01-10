package me.JortVlaming.monster;

import me.JortVlaming.game.GamePanel;

public class BOSS_Skeletonlord extends HostileEntity{
    public BOSS_Skeletonlord(GamePanel gp) {
        super(gp);

        maxLife = 32;
        life = maxLife;

        solidArea.width = gp.getTileSize() * 2;
        solidArea.height = gp.getTileSize() * 2;
    }

    @Override
    public void loadImages() {
        up1 = loadImage("skeletonlord_up_1.png", "/monster/skeletonlord/");
        up2 = loadImage("skeletonlord_up_2.png", "/monster/skeletonlord/");
        left1 = loadImage("skeletonlord_left_1.png", "/monster/skeletonlord/");
        left2 = loadImage("skeletonlord_left_2.png", "/monster/skeletonlord/");
        down1 = loadImage("skeletonlord_down_1.png", "/monster/skeletonlord/");
        down2 = loadImage("skeletonlord_down_2.png", "/monster/skeletonlord/");
        right1 = loadImage("skeletonlord_right_1.png", "/monster/skeletonlord/");
        right2 = loadImage("skeletonlord_right_2.png", "/monster/skeletonlord/");

        spriteWidth = gp.getTileSize() * 2;
        spriteHeight = gp.getTileSize() * 2;
    }
}
