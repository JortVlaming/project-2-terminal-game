package me.JortVlaming.object.items.sword;

import me.JortVlaming.object.ObjectMap;

public class GoldSword extends SwordItem{
    public GoldSword() {
        super("gold sword", ObjectMap.GOLD_SWORD.getFileName().split("\\.")[0]);

        attackValue = 4;
    }
}
