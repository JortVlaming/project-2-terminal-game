package me.JortVlaming.object;

import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.Util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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

    public void addObjectToWorld(SuperObject obj) {
        activeObjects.add(obj);
    }

    public void clearActiveObjects() {
        activeObjects.clear();
    }

    public void draw(Graphics2D g2D) {
        for (SuperObject obj : activeObjects) {
            obj.draw(g2D, gp);
        }
    }

    public SuperObject[] getObjects() {
        return objects;
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