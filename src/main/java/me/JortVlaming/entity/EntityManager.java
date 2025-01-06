package me.JortVlaming.entity;

import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.Util;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EntityManager {
    GamePanel gp;

    public List<Entity> activeEntities;
    public List<Entity> activeNPCEntities;

    public int entitiesDrawnCount = 0;
    public int entitiesUpdatedCount = 0;
    public int averageEntityActionLockTimer = 0;

    public EntityManager(GamePanel gp) {
        this.gp = gp;
        activeNPCEntities = new ArrayList<>();
        activeEntities = new ArrayList<>();
    }

    public void loadEntities_csv(String map) {
        try {
            InputStream is = getClass().getResourceAsStream("/worlds/" + map + "/" + map + "_NPCS.csv");

            if (is == null) {
                System.out.println("Map " + map + " does not exist!");
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0, row = 0;

            while (row < gp.getMaxWorldRow()) {
                String line = br.readLine();

                if (line == null) {
                    break;
                }

                String[] nums = line.split(",");

                while (col < gp.getMaxWorldCol()) {
                    int num = 0;
                    if (col < nums.length) {
                        try {
                            num = Integer.parseInt(nums[col]);
                            switch (num) {
                                case 1: {
                                    NPC_OldMan oldMan = new NPC_OldMan(gp);
                                    oldMan.worldX = col*gp.getTileSize();
                                    oldMan.worldY = row*gp.getTileSize();
                                    addEntityToWorld(oldMan);
                                    break;
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format at column " + col + ", row " + row + ". Defaulting to 0.");
                            num = 0;
                        }
                    }

                    col++;
                }

                if (col == gp.getMaxWorldCol()) {
                    col = 0;
                    row++;
                }
            }

            br.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        activeEntities.add(gp.getPlayer());
    }

    public void addEntityToWorld(Entity entity) {
        activeNPCEntities.add(entity);
        activeEntities.add(entity);
    }

    public void removeEntityFromWorld(Entity entity) {
        activeNPCEntities.remove(entity);
        activeEntities.remove(entity);
    }

    public void updateEntities() {
        entitiesUpdatedCount = 0;
        averageEntityActionLockTimer = 0;
        for (Entity activeEntity : activeEntities) {
            if (activeEntity instanceof Player) continue;
            if (Util.getDistanceFromPlayer(activeEntity.worldX, activeEntity.worldY, gp) < 1000) {
                activeEntity.update();
                entitiesUpdatedCount++;
                averageEntityActionLockTimer += activeEntity.actionLockTimer;
            }
        }
        
        averageEntityActionLockTimer /= (entitiesUpdatedCount > 0 ? entitiesUpdatedCount : 1);
    }

    public void drawEntities(Graphics2D g2D) {
        entitiesDrawnCount = 0;
        activeEntities.sort((o1, o2) -> {
            return Integer.compare(o1.worldY, o2.worldY);
        });
        for (Entity e : activeEntities) {
            if (Util.isOnScreen(e.worldX, e.worldY, gp)) {
                e.draw(g2D);
                entitiesDrawnCount++;
            }
        }
    }
}
