package me.JortVlaming.object.items.shield;

import me.JortVlaming.object.ObjectMap;

public class IronShield extends ShieldItem{
    public IronShield() {
        super("iron shield", ObjectMap.BLUE_SHIELD.getFileName().split("\\.")[0]);

        defenseValue = 2.5f;
    }
}
