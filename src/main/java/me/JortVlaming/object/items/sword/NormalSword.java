package me.JortVlaming.object.items.sword;

import me.JortVlaming.object.ObjectMap;

public class NormalSword extends SwordItem{
    public NormalSword() {
        super("sword", ObjectMap.SWORD.getFileName().split("\\.")[0]);

        attackValue = 1;
    }
}
