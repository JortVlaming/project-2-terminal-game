package me.JortVlaming.game;

import me.JortVlaming.entity.Entity;
import me.JortVlaming.entity.Player;

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
        int entityBottomRow;

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

    public int checkObject(Entity entity) {
        if (!GamePanel.DO_OBJECTS) return -1;
        int index = -1;

        boolean player = entity instanceof Player;

        for (int i = 0; i < gp.objectManager.getActiveObjects().size(); i++) {
            if (gp.objectManager.getActiveObjects().get(i) != null) {
                if (!Util.isOnScreen(gp.objectManager.getActiveObjects().get(i).worldX, gp.objectManager.getActiveObjects().get(i).worldY, gp)) continue;
                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's solid area position
                gp.objectManager.getActiveObjects().get(i).solidArea.x = gp.objectManager.getActiveObjects().get(i).worldX + gp.objectManager.getActiveObjects().get(i).solidArea.x;
                gp.objectManager.getActiveObjects().get(i).solidArea.y = gp.objectManager.getActiveObjects().get(i).worldY + gp.objectManager.getActiveObjects().get(i).solidArea.y;

                switch (entity.direction) {
                    default:
                    case 0: {
                        // omhoog
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.objectManager.getActiveObjects().get(i).solidArea)) {
                            if (gp.objectManager.getActiveObjects().get(i).collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    }
                    case 1: {
                        // rechts
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.objectManager.getActiveObjects().get(i).solidArea)) {
                            if (gp.objectManager.getActiveObjects().get(i).collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    }
                    case 2: {
                        // omlaag
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.objectManager.getActiveObjects().get(i).solidArea)) {
                            if (gp.objectManager.getActiveObjects().get(i).collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    }
                    case 3: {
                        // links
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.objectManager.getActiveObjects().get(i).solidArea)) {
                            if (gp.objectManager.getActiveObjects().get(i).collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.objectManager.getActiveObjects().get(i).solidArea.x = gp.objectManager.getActiveObjects().get(i).solidAreaDefaultX;
                gp.objectManager.getActiveObjects().get(i).solidArea.y = gp.objectManager.getActiveObjects().get(i).solidAreaDefaultY;
            }
        }

        return index;
    }
}
