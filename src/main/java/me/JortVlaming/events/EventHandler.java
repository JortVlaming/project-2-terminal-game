package me.JortVlaming.events;

import me.JortVlaming.game.GamePanel;
import me.JortVlaming.object.ObjectMap;
import me.JortVlaming.object.SuperObject;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class EventHandler {
    GamePanel gp;

    List<Rectangle2D> damagePitRectangles;

    public EventHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void loadEventsFromMap_csv(String map) {
        try {
            InputStream is = getClass().getResourceAsStream("/worlds/" + map + "/" + map + "_EventTiles.csv");

            if (is == null) {
                System.out.println("Map " + map + " does not exist!");
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0, row = 0;

            while (row < gp.getMaxWorldRow()) {
                String line = br.readLine();

                if (line == null) {
                    break;
                }

                String[] nums = line.split(",");

                while (col < gp.getMaxWorldCol()) {
                    int num = 0;
                    if (col < nums.length) {
                        try {
                            num = Integer.parseInt(nums[col]);
                            if (num == 1) {
                                Rectangle rect = new Rectangle();
                                rect.x = col*gp.getTileSize();
                                rect.y = row*gp.getTileSize();
                                rect.width = gp.getTileSize();
                                rect.height = gp.getTileSize();
                                continue;
                            }
                            col++;
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format at column " + col + ", row " + row + ". Defaulting to 0.");
                            num = 0;
                        }
                    }

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
}
