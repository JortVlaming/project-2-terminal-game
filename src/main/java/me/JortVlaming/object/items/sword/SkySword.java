package me.JortVlaming.object.items.sword;

import me.JortVlaming.object.ObjectMap;

public class SkySword extends SwordItem{
    public SkySword() {
        super("sky sword", ObjectMap.SKY_SWORD.getFileName().split("\\.")[0]);

        attackValue = 2;
    }
}
