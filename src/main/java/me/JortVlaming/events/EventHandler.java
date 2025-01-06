package me.JortVlaming.events;

import me.JortVlaming.game.GUI;
import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.Util;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    GamePanel gp;

    List<Rectangle2D> damagePitRectangles;
    List<Integer> pitsForDisposing = new ArrayList<>();

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        damagePitRectangles = new ArrayList<>();
    }

    public void handleDamagePit(int index) {
        if (pitsForDisposing.contains(index)) return;
        gp.getPlayer().takeDamage(1);
        gp.getGUI().showMessage(new GUI.Message("You fell into a pit", 2500));
        pitsForDisposing.add(index);
    }

    public void loadEventsFromMap_csv(String map) {
        damagePitRectangles = new ArrayList<>();
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
                            String s = nums[col].replace(" ", "");
                            num = Integer.parseInt(s);
                            if (num == 1) {
                                Rectangle rect = new Rectangle();
                                rect.x = col*gp.getTileSize();
                                rect.y = row*gp.getTileSize();
                                rect.width = gp.getTileSize();
                                rect.height = gp.getTileSize();
                                damagePitRectangles.add(rect);
                                System.out.println("Loaded damage pit at " + col*gp.getTileSize() + ", " + row*gp.getTileSize());
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format (" + nums[col] + ") at column " + col + ", row " + row + ". Defaulting to 0.");
                            num = 0;
                        }
                        col++;
                    }
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
        g2D.setColor(Color.red);

        for (Rectangle2D r : damagePitRectangles) {
            int x = (int) r.getX();
            int y = (int) r.getY();
            int width = (int) r.getWidth();
            int height = (int) r.getHeight();
            if (Util.isOnScreen(x, y, GamePanel.getInstance()))
                g2D.fillRect(x, y, width, height);
        }
    }

    public List<Rectangle2D> getDamagePitRectangles() {
        return damagePitRectangles;
    }

    public void disposeMarkedForDispose() {
        for (int i : pitsForDisposing) {
            damagePitRectangles.remove(i);
        }

        pitsForDisposing.clear();
    }
}
