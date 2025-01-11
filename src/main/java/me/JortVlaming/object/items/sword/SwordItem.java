package me.JortVlaming.object.items.sword;

import me.JortVlaming.object.SuperObject;

import java.io.InputStream;

public class SwordItem extends SuperObject {
    public int attackValue = 1;

    public SwordItem(String name, String imageName) {
        super(name, imageName);
    }

    public SwordItem(String name, InputStream is) {
        super(name, is);
    }
}
