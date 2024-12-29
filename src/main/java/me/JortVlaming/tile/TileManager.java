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

        mapTileNum = new int[gp.getMaxScreenCol()][gp.getMaxScreenRow()];

        loadTiles();
    }

    void loadTiles() {
        List<TileMap> tileMap = Arrays.asList(TileMap.values());

        tiles = new Tile[tileMap.size()];

        Collections.sort(tileMap);

        for (int i = 0; i < tileMap.size(); i++) {
            tiles[tileMap.get(i).getIndex()] = new Tile();
            try {
                InputStream is = getClass().getResourceAsStream("/tiles/" + tileMap.get(i).getFileName());
                if (is == null) {
                    System.out.println("Failed to load tile " + 1);
                    if (i != 0) {
                        tiles[tileMap.get(i).getIndex()] = tiles[0];
                    }
                    continue;
                }
                tiles[tileMap.get(i).getIndex()].image = ImageIO.read(is);
                tiles[tileMap.get(i).getIndex()].collision = tileMap.get(i).hasCollision();
                System.out.println("Loaded tile " + tileMap.get(i).getIndex() + " as " + tileMap.get(i).name());
            } catch (IOException e) {
                throw new RuntimeException(e);
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

            while (col < gp.getMaxScreenCol() && row < gp.getMaxScreenRow()) {
                String line = br.readLine();

                String[] nums = line.split(" ");
                while (col < gp.getMaxScreenCol()) {
                    int num = Integer.parseInt(nums[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.getMaxScreenCol()) {
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

    boolean destinedToFailDrawAll = false;

    public void drawAll(Graphics2D g2D) {
        if (destinedToFailDrawAll) return;
        int x = 0, y = 0;

        for (Tile t : tiles) {
            if (x > gp.getMaxScreenCol()) {
                x = 0;
                y++;
            }

            if (y > gp.getMaxScreenRow()) {
                System.out.println("Failed to render all tiles to the screen to a size constraint, will not draw again!");
                destinedToFailDrawAll = true;
                break;
            }

            g2D.drawImage(t.image, x*gp.getTileSize(), y*gp.getTileSize(), gp.getTileSize(), gp.getTileSize(), null);

            x++;
        }
    }

    public void draw(Graphics2D g2D) {
        int col = 0, row = 0;
        int x = 0, y = 0;

        while (col < gp.getMaxScreenCol() && row < gp.getMaxScreenRow()) {
            Tile toDraw = tiles[mapTileNum[col][row]];
            g2D.drawImage(toDraw.image, x, y, gp.getTileSize(), gp.getTileSize(), null);
            col++;
            x += gp.getTileSize();

            if (col == gp.getMaxScreenCol()) {
                col = 0;
                x = 0;
                row++;
                y += gp.getTileSize();
            }
        }
    }
}
