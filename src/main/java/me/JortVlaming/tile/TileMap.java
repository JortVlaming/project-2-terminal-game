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
    TREE(16),
    WALL(32),
    WATER0(18),
    WATER1(19),
    WATER2(20),
    WATER3(21),
    WATER4(22),
    WATER5(23),
    WATER6(24),
    WATER7(25),
    WATER8(26),
    WATER9(27),
    WATER10(28),
    WATER11(29),
    WATER12(30),
    WATER13(31),
    STAIRS1(36),
    STAIRS2(37);

    private int index;

    TileMap(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getFileName() {
        return String.format("%03d", index) + ".png";
    }
}
