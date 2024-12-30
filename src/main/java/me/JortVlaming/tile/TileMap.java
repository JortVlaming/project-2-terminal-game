package me.JortVlaming.tile;

public enum TileMap {
    VOID(0),
    EARTH(17),
    FLOOR1(34),
    GRASS0(1),
    GRASS1(2),
    HUT(33),
    ROAD0(3),
    ROAD1(4),
    ROAD2(5),
    ROAD3(6),
    ROAD4(7),
    ROAD5(8),
    ROAD6(9),
    ROAD7(10),
    ROAD8(11),
    ROAD9(12),
    ROAD10(13),
    ROAD11(14),
    ROAD12(15),
    TABLE(35),
    TREE(16, true),
    WALL(32, true),
    WATER0(18, true),
    WATER1(19, true),
    WATER2(20, true),
    WATER3(21, true),
    WATER4(22, true),
    WATER5(23, true),
    WATER6(24, true),
    WATER7(25, true),
    WATER8(26, true),
    WATER9(27, true),
    WATER10(28, true),
    WATER11(29, true),
    WATER12(30, true),
    WATER13(31, true),
    STAIRS1(36),
    STAIRS2(37);

    private int index;
    private boolean collision = false;

    TileMap(int index) {
        this.index = index;
    }

    TileMap(int index, boolean collision) {
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
