package me.JortVlaming.game;

import me.JortVlaming.object.*;

@Deprecated
public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        SuperObject key_1 = gp.objectManager.createObject(ObjectMap.KEY);
        key_1.worldX = 23 * gp.getTileSize();
        key_1.worldY = 7 * gp.getTileSize();

        SuperObject key_2 = gp.objectManager.createObject(ObjectMap.KEY);
        key_2.worldX = 23 * gp.getTileSize();
        key_2.worldY = 40 * gp.getTileSize();

        SuperObject key_3 = gp.objectManager.createObject(ObjectMap.KEY);
        key_3.worldX = 38 * gp.getTileSize();
        key_3.worldY = 8 * gp.getTileSize();

        SuperObject door_1 = gp.objectManager.createObject(ObjectMap.DOOR);
        door_1.worldX = 10 * gp.getTileSize();
        door_1.worldY = 11 * gp.getTileSize();

        SuperObject door_2 = gp.objectManager.createObject(ObjectMap.DOOR);
        door_2.worldX = 8 * gp.getTileSize();
        door_2.worldY = 28 * gp.getTileSize();

        SuperObject door_3 = gp.objectManager.createObject(ObjectMap.DOOR);
        door_3.worldX = 12 * gp.getTileSize();
        door_3.worldY = 22 * gp.getTileSize();

        SuperObject chest = gp.objectManager.createObject(ObjectMap.CHEST);
        chest.worldX = 10 * gp.getTileSize();
        chest.worldY = 7 * gp.getTileSize();

        SuperObject boots = gp.objectManager.createObject(ObjectMap.BOOTS);
        boots.worldX = 37 * gp.getTileSize();
        boots.worldY = 42 * gp.getTileSize();

        gp.objectManager.addObjectToWorld(key_1);
        gp.objectManager.addObjectToWorld(key_2);
        gp.objectManager.addObjectToWorld(key_3);

        gp.objectManager.addObjectToWorld(door_1);
        gp.objectManager.addObjectToWorld(door_2);
        gp.objectManager.addObjectToWorld(door_3);

        gp.objectManager.addObjectToWorld(chest);
        gp.objectManager.addObjectToWorld(boots);
    }
}
