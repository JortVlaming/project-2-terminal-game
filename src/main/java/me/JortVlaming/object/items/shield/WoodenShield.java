package me.JortVlaming.object.items.shield;

import me.JortVlaming.object.ObjectMap;

public class WoodenShield extends ShieldItem{
    public WoodenShield() {
        super("shield", ObjectMap.WOOD_SHIELD.getFileName().split("\\.")[0]);

        defenseValue = 1.5f;
    }
}
