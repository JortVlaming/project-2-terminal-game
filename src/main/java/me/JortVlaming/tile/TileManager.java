package me.JortVlaming.tile;

import me.JortVlaming.game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TileManager {
    GamePanel gp;
    Tile[] tiles;

    int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        mapTileNum = new int[gp.getMaxWorldCol()][gp.getMaxWorldRow()];

        loadTiles();
    }

    void loadTiles() {
        List<TileMap> tileMap = Arrays.asList(TileMap.values());

        tiles = new Tile[tileMap.size()];

        Collections.sort(tileMap);

        for (int i = 0; i < tileMap.size(); i++) {
            int index = tileMap.get(i).getIndex();
            if (index >= tiles.length) {
                System.err.println("Error: Index " + index + " is out of bounds for the tiles array.");
                continue;
            }
            tiles[index] = new Tile();
            try {
                InputStream is = getClass().getResourceAsStream("/tiles/" + tileMap.get(i).getFileName());
                if (is == null) {
                    System.out.println("Failed to load tile " + 1);
                    if (i != 0) {
                        tiles[index] = tiles[0];
                    }
                    continue;
                }
                tiles[index].image = ImageIO.read(is);
                tiles[index].collision = tileMap.get(i).hasCollision();
                System.out.println("Loaded tile " + index + " as " + tileMap.get(i).name());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == null) {
                tiles[i] = new Tile(); // assign a default tile
                System.out.println("Filled empty tile at index " + i + " with a default tile.");
            }
        }
    }

    public void loadMap(String map) {
        try {
            InputStream is = getClass().getResourceAsStream("/worlds/" + map + ".txt");

            if (is == null) {
                System.out.println("Map " + map + " does not exist!");
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0, row = 0;

            while (row < gp.getMaxWorldRow()) {
                String line = br.readLine();

                if (line == null) {
                    System.out.println("Reached end of map file before filling the map. Filling remaining rows with default tiles.");
                    for (int fillRow = row; fillRow < gp.getMaxWorldRow(); fillRow++) {
                        Arrays.fill(mapTileNum[fillRow], 0);
                    }
                    break;
                }

                String[] nums = line.split(" ");

                while (col < gp.getMaxWorldCol()) {
                    int num = 0; // Default to tile index 0
                    if (col < nums.length) {
                        try {
                            num = Integer.parseInt(nums[col]);
                            if (num >= tiles.length || num < 0) {
                                System.err.println("Invalid tile index " + num + " at position (" + col + ", " + row + "). Defaulting to 0.");
                                num = 0;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format at column " + col + ", row " + row + ". Defaulting to 0.");
                            num = 0;
                        }
                    }

                    mapTileNum[col][row] = num;
                    col++;
                }

                if (col == gp.getMaxWorldCol()) {
                    col = 0;
                    row++;
                }
            }

            br.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2D) {
        int worldCol = 0, worldRow = 0;

        while (worldCol < gp.getMaxWorldCol() && worldRow < gp.getMaxWorldRow()) {
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.getTileSize();
            int worldY = worldRow * gp.getTileSize();
            int screenX = worldX - gp.getPlayer().worldX + gp.getPlayer().screenX;
            int screenY = worldY - gp.getPlayer().worldY + gp.getPlayer().screenY;

            if (worldX + gp.getTileSize() > gp.getPlayer().worldX - gp.getPlayer().screenX &&
                worldX - gp.getTileSize() < gp.getPlayer().worldX + gp.getPlayer().screenX &&
                worldY + gp.getTileSize() > gp.getPlayer().worldY - gp.getPlayer().screenY &&
                worldY - gp.getTileSize() < gp.getPlayer().worldY + gp.getPlayer().screenY) {
                g2D.drawImage(tiles[tileNum].image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
            }
            worldCol++;

            if (worldCol == gp.getMaxWorldCol()) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
