package me.JortVlaming.game;

import me.JortVlaming.entity.Entity;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.getTileSize();
        int entityRightCol = entityRightWorldX / gp.getTileSize();
        int entityTopRow = entityTopWorldY / gp.getTileSize();
        int entityBottomRow = entityBottomWorldY / gp.getTileSize();

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case 0:
            default: {
                entityTopRow = (entityTopWorldY - entity.speed)/gp.getTileSize();
                tileNum1 = gp.tileManager.getMapTileNum()[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManager.getMapTileNum()[entityRightCol][entityTopRow];
                if (gp.tileManager.getTiles()[tileNum1].collision || gp.tileManager.getTiles()[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            }
            case 1: {
                entityRightCol = (entityRightWorldX + entity.speed)/gp.getTileSize();
                tileNum1 = gp.tileManager.getMapTileNum()[entityRightCol][entityTopRow];
                tileNum2 = gp.tileManager.getMapTileNum()[entityRightCol][entityTopRow];
                if (gp.tileManager.getTiles()[tileNum1].collision || gp.tileManager.getTiles()[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            }
            case 2: {
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.getTileSize();
                tileNum1 = gp.tileManager.getMapTileNum()[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileManager.getMapTileNum()[entityRightCol][entityBottomRow];
                if (gp.tileManager.getTiles()[tileNum1].collision || gp.tileManager.getTiles()[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            }
            case 3: {
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.getTileSize();
                tileNum1 = gp.tileManager.getMapTileNum()[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManager.getMapTileNum()[entityLeftCol][entityTopRow];
                if (gp.tileManager.getTiles()[tileNum1].collision || gp.tileManager.getTiles()[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            }
        }
    }
}
