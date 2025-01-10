package me.JortVlaming.entity;

import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.Util;
import me.JortVlaming.monster.*;

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
        activeNPCEntities = new ArrayList<>();
        activeEntities = new ArrayList<>();
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
                                case 2: {
                                    MON_GreenSlime slime = new MON_GreenSlime(gp);
                                    slime.worldX = col*gp.getTileSize();
                                    slime.worldY = row*gp.getTileSize();
                                    addEntityToWorld(slime);
                                    break;
                                }
                                case 3: {
                                    MON_Bat bat = new MON_Bat(gp);
                                    bat.worldX = col * gp.getTileSize();
                                    bat.worldY = row * gp.getTileSize();
                                    addEntityToWorld(bat);
                                    break;
                                }
                                case 4: {
                                    MON_RedSlime slime = new MON_RedSlime(gp);
                                    slime.worldX = col*gp.getTileSize();
                                    slime.worldY = row*gp.getTileSize();
                                    addEntityToWorld(slime);
                                    break;
                                }
                                case 5: {
                                    MON_Orc orc = new MON_Orc(gp);
                                    orc.worldX = col*gp.getTileSize();
                                    orc.worldY = row*gp.getTileSize();
                                    addEntityToWorld(orc);
                                    break;
                                }
                                case 6: {
                                    BOSS_Skeletonlord skeletonlord = new BOSS_Skeletonlord(gp);
                                    skeletonlord.worldX = col * gp.getTileSize();
                                    skeletonlord.worldY = row * gp.getTileSize();
                                    addEntityToWorld(skeletonlord);
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
        if (!(entity instanceof Player))
            activeNPCEntities.add(entity);
        activeEntities.add(entity);
    }

    List<Entity> markedForDelete = new ArrayList<>();

    public void removeEntityFromWorld(Entity entity) {
        markedForDelete.add(entity);
    }

    public void updateEntities() {
        entitiesUpdatedCount = 0;
        averageEntityActionLockTimer = 0;
        for (Entity activeEntity : activeNPCEntities) {
            if (Util.getDistanceFromPlayer((int) activeEntity.worldX, (int) activeEntity.worldY, gp) < 1000) {
                activeEntity.update();
                entitiesUpdatedCount++;
                averageEntityActionLockTimer += activeEntity.actionLockTimer;
            }
        }

        for (Entity entity : markedForDelete) {
            activeNPCEntities.remove(entity);
            activeEntities.remove(entity);
        }
        
        averageEntityActionLockTimer /= (entitiesUpdatedCount > 0 ? entitiesUpdatedCount : 1);
    }

    public void drawEntities(Graphics2D g2D) {
        entitiesDrawnCount = 0;
        activeEntities.sort(Comparator.comparingDouble(o -> o.worldY));
        for (Entity e : activeEntities) {
            if (e instanceof Player) {
                e.draw(g2D);
            }
            else if (Util.isOnScreen((int) e.worldX, (int) e.worldY, gp)) {
                e.draw(g2D);
                entitiesDrawnCount++;
            }
        }
    }
}
