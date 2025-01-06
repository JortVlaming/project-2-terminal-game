package me.JortVlaming.game;

import me.JortVlaming.entity.Entity;
import me.JortVlaming.entity.Player;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

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

        try {
            switch (entity.direction) {
                case 0:
                default: {
                    entityTopRow = (entityTopWorldY - entity.speed) / gp.getTileSize();
                    tileNum1 = gp.tileManager.getMapTileNum()[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileManager.getMapTileNum()[entityRightCol][entityTopRow];
                    if (gp.tileManager.getTiles()[tileNum1].collision || gp.tileManager.getTiles()[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                    break;
                }
                case 1: {
                    entityRightCol = (entityRightWorldX + entity.speed) / gp.getTileSize();
                    tileNum1 = gp.tileManager.getMapTileNum()[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileManager.getMapTileNum()[entityRightCol][entityTopRow];
                    if (gp.tileManager.getTiles()[tileNum1].collision || gp.tileManager.getTiles()[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                    break;
                }
                case 2: {
                    entityBottomRow = (entityBottomWorldY + entity.speed) / gp.getTileSize();
                    tileNum1 = gp.tileManager.getMapTileNum()[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileManager.getMapTileNum()[entityRightCol][entityBottomRow];
                    if (gp.tileManager.getTiles()[tileNum1].collision || gp.tileManager.getTiles()[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                    break;
                }
                case 3: {
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gp.getTileSize();
                    tileNum1 = gp.tileManager.getMapTileNum()[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileManager.getMapTileNum()[entityLeftCol][entityTopRow];
                    if (gp.tileManager.getTiles()[tileNum1].collision || gp.tileManager.getTiles()[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                    break;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Player outside world");
        }
    }

    public int checkObject(Entity entity) {
        if (!GamePanel.DO_OBJECTS) return -1;
        int index = -1;

        boolean player = entity instanceof Player;

        for (int i = 0; i < gp.objectManager.getActiveObjects().size(); i++) {
            if (gp.objectManager.getActiveObjects().get(i) != null) {
                if (!Util.isOnScreen(gp.objectManager.getActiveObjects().get(i).worldX, gp.objectManager.getActiveObjects().get(i).worldY, gp))
                    continue;
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

    public int checkEntity(Entity entity, ArrayList<Entity> targets) {
        if (!GamePanel.DO_ENTITIES) return -1;
        int index = -1;

        for (int i = 0; i < targets.size(); i++) {
            Entity target = gp.getNPCs().get(i);
            if (entity == target) continue;
            if (target != null) {
                if (!Util.isOnScreen(target.worldX, target.worldY, gp)) continue;
                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object's solid area position
                target.solidArea.x = target.worldX + target.solidArea.x;
                target.solidArea.y = target.worldY + target.solidArea.y;

                switch (entity.direction) {
                    default:
                    case 0: {
                        // omhoog
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(target.solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    }
                    case 1: {
                        // rechts
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(target.solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    }
                    case 2: {
                        // omlaag
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(target.solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    }
                    case 3: {
                        // links
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(target.solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target.solidArea.x = target.solidAreaDefaultX;
                target.solidArea.y = target.solidAreaDefaultY;
            }
        }

        return index;
    }

    public void checkPlayer(Entity entity) {
        if (!GamePanel.DO_ENTITIES) return;
        int index = -1;

        if (!Util.isOnScreen(entity.worldX, entity.worldY, gp) || Util.getDistanceFromPlayer(entity.worldX, entity.worldY, gp) > 200) return;

        Player target = gp.getPlayer();

        // Get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        // Get the object's solid area position
        target.solidArea.x = target.worldX + target.solidArea.x;
        target.solidArea.y = target.worldY + target.solidArea.y;

        switch (entity.direction) {
            default:
            case 0: {
                // omhoog
                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(target.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
            }
            case 1: {
                // rechts
                entity.solidArea.x += entity.speed;
                if (entity.solidArea.intersects(target.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
            }
            case 2: {
                // omlaag
                entity.solidArea.y += entity.speed;
                if (entity.solidArea.intersects(target.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
            }
            case 3: {
                // links
                entity.solidArea.x -= entity.speed;
                if (entity.solidArea.intersects(target.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
            }
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        target.solidArea.x = target.solidAreaDefaultX;
        target.solidArea.y = target.solidAreaDefaultY;
    }

    public void checkEvents(Entity entity) {
        if (!(entity instanceof Player)) return;

        int i = 0;

        for (Rectangle2D pits : gp.events.getDamagePitRectangles()) {
            if (Util.getDistanceFromPlayer(pits, gp) < 100) {
                if (hit(pits, -1)) {
                    gp.events.handleDamagePit(i);
                    gp.getPlayer().frozenFor = 120;
                }

                gp.getPlayer().solidArea.x = gp.getPlayer().solidAreaDefaultX;
                gp.getPlayer().solidArea.y = gp.getPlayer().solidAreaDefaultY;
            }
            i++;
        }

        gp.events.disposeMarkedForDispose();
    }

    public boolean hit(Rectangle2D col, int reqDirection) {
        boolean hit = false;

        gp.getPlayer().solidArea.x = gp.getPlayer().worldX + gp.player.solidArea.x;
        gp.getPlayer().solidArea.y = gp.getPlayer().worldY + gp.player.solidArea.y;

        if (gp.getPlayer().solidArea.intersects(col)) {
            if (gp.getPlayer().direction == reqDirection || reqDirection == -1) {
                hit = true;
            }
        }

        return hit;
    }
}
