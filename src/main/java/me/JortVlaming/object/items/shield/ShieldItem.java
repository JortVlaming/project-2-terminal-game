package me.JortVlaming.object.items.shield;

import me.JortVlaming.object.SuperObject;

import java.io.InputStream;

public class ShieldItem extends SuperObject {
    public float defenseValue = 1;

    public ShieldItem(String name, String imageName) {
        super(name, imageName);
    }

    public ShieldItem(String name, InputStream is) {
        super(name, is);
    }
}
