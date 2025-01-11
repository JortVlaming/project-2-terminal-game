package me.JortVlaming.object;

public enum ObjectMap {
    AXE("axe", 1),
    BLUEHEART("blueheart", 2),
    BOOTS("boots", 3),
    CHEST("chest", 4, true),
    CHEST_OLD("chest_old", 5, true),
    CHEST_OPEN("chest_open", 6, true),
    COIN_BRONZE("coin_bronze", 7),
    DOOR("door", 8, true),
    IRON_DOOR("iron_door", 9, true),
    EMPTY_HEART("empty_heart", 10),
    FULL_HEART("full_heart", 11),
    HALF_HEART("half_heart", 12),
    KEY("key", 13),
    LANTERN("lantern", 14),
    EMPTY_MANACRYSTAL("empty_manacrystal", 15),
    FULL_MANACRYSTAL("full_manacrystal", 16),
    PICKAXE("pickaxe", 17),
    RED_POTION("red_potion", 18),
    BLUE_SHIELD("blue_shield", 19),
    WOOD_SHIELD("wood_shield", 20),
    SWORD("sword", 21),
    TENT("tent", 22, true),
    SKY_SWORD("sky_sword", 23),
    GOLD_SWORD("gold_sword", 24);

    String name;
    int index;
    boolean collision;

    ObjectMap(String name, int index) {
        this.name = name;
        this.index = index;
    }

    ObjectMap(String name, int index, boolean collision) {
        this.name = name;
        this.index = index;
        this.collision = collision;
    }

    public int getIndex() {
        return index;
    }

    public String getFileName() {
        return String.format("%03d", index) + ".png";
    }

    public boolean hasCollision() {
        return collision;
    }
}
