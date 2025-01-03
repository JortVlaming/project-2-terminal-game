package me.JortVlaming.object;

import me.JortVlaming.game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectManager {
    GamePanel gp;
    SuperObject[] objects;
    List<SuperObject> activeObjects;

    public ObjectManager(GamePanel gp) {
        this.gp = gp;
        this.activeObjects = new ArrayList<>();

        loadObjectTemplates();
    }

    void loadObjectTemplates() {
        List<ObjectMap> objectMapEntries = Arrays.asList(ObjectMap.values());

        final int[] length = {0};

        objectMapEntries.forEach(objectMap -> {
            if (objectMap.index > length[0]) {
                length[0] = objectMap.index;
            }
        });

        length[0]++;

        objects = new SuperObject[length[0]];

        for (ObjectMap objectMap : objectMapEntries) {
            int index = objectMap.getIndex();

            if (index >= objects.length) {
                System.err.println("Error: Index " + index + " is out of bounds for the object array.");
                continue;
            }

            String imageName = objectMap.getFileName();
            String name = objectMap.name;

            try (InputStream is = getClass().getResourceAsStream("/objects/" + imageName)) {
                if (is == null) {
                    System.out.println("Failed to load object image: " + imageName);
                    if (index != 0) {
                        objects[index] = objects[0];
                    }
                    continue;
                }

                SuperObject obj = new SuperObject(name, is);
                obj.collision = objectMap.hasCollision();

                objects[index] = obj;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                objects[i] = new SuperObject("Default", "000");
                System.out.println("Filled empty object index " + i + " with a default object.");
            }
        }
    }

    public void loadObject_csv(String map) {
        try {
            InputStream is = getClass().getResourceAsStream("/worlds/" + map + "/" + map + "_Objects.csv");

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
                            if (num >= objects.length || num <= 0 && (num != 0 && num != -1) || !objectWithIndexExists(num) ) {
                                System.err.println("Invalid object index " + num + " at position (" + col + ", " + row + "). Defaulting to 0.");
                                num = 0;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format at column " + col + ", row " + row + ". Defaulting to 0.");
                            num = 0;
                        }
                    }

                    ObjectMap mapOBJ = getObjectMapFromIndex(num);

                    if (mapOBJ != null) {
                        SuperObject obj = createObject(mapOBJ);
                        obj.worldX = col * gp.getTileSize();
                        obj.worldY = row * gp.getTileSize();
                        activeObjects.add(obj);
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
    }

    public boolean objectWithIndexExists(int index) {
        for (ObjectMap objectMap : ObjectMap.values()) {
            if (objectMap.getIndex() == index) {
                return true;
            }
        }
        return false;
    }

    public ObjectMap getObjectMapFromIndex(int index) {
        for (ObjectMap objectMap : ObjectMap.values()) {
            if (objectMap.getIndex() == index) {
                return objectMap;
            }
        }
        return null;
    }

    public SuperObject getObject(ObjectMap objectMap) {
        int index = objectMap.getIndex();
        if (index >= objects.length) {
            System.err.println("Error: Index " + index + " is out of bounds for the object array.");
            return null;
        }
        return objects[index];
    }

    public SuperObject createObject(ObjectMap objectMap) {
        int index = objectMap.getIndex();
        if (index >= objects.length) {
            System.err.println("Error: Index " + index + " is out of bounds for the object array.");
            return null;
        }
        return objects[index].clone();
    }

    public void draw(Graphics2D g2D) {
        for (SuperObject obj : activeObjects) {
            obj.draw(g2D, gp);
        }
    }

    public List<SuperObject> getActiveObjects() {
        return activeObjects;
    }

    public BufferedImage getObjectImage(ObjectMap objectMap) {
        int index = objectMap.getIndex();
        if (index >= objects.length) {
            System.err.println("Error: Index " + index + " is out of bounds for the object array.");
            return null;
        }
        return objects[index].image;
    }
}