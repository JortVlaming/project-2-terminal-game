package me.JortVlaming.game;

import me.JortVlaming.object.OBJ_Chest;
import me.JortVlaming.object.OBJ_Door;
import me.JortVlaming.object.OBJ_Key;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        gp.getObjects()[0] = new OBJ_Key();
        gp.getObjects()[0].worldX = 23 * gp.getTileSize();
        gp.getObjects()[0].worldY = 7 * gp.getTileSize();

        gp.getObjects()[1] = new OBJ_Key();
        gp.getObjects()[1].worldX = 23 * gp.getTileSize();
        gp.getObjects()[1].worldY = 40 * gp.getTileSize();

        gp.getObjects()[2] = new OBJ_Key();
        gp.getObjects()[2].worldX = 38 * gp.getTileSize();
        gp.getObjects()[2].worldY = 8 * gp.getTileSize();

        gp.getObjects()[3] = new OBJ_Door();
        gp.getObjects()[3].worldX = 10 * gp.getTileSize();
        gp.getObjects()[3].worldY = 11 * gp.getTileSize();

        gp.getObjects()[4] = new OBJ_Door();
        gp.getObjects()[4].worldX = 8 * gp.getTileSize();
        gp.getObjects()[4].worldY = 28 * gp.getTileSize();

        gp.getObjects()[5] = new OBJ_Door();
        gp.getObjects()[5].worldX = 12 * gp.getTileSize();
        gp.getObjects()[5].worldY = 22 * gp.getTileSize();

        gp.getObjects()[6] = new OBJ_Chest();
        gp.getObjects()[6].worldX = 10 * gp.getTileSize();
        gp.getObjects()[6].worldY = 7 * gp.getTileSize();
    }
}
