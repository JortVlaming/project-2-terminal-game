package me.JortVlaming.game;

import me.JortVlaming.object.ObjectMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUI {
    GamePanel gp;

    Font font;
    Font bold;
    Font bold_80;
    BufferedImage keyImage;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final List<Message> messages = new ArrayList<>();

    public boolean gameFinished = false;

    public GUI(GamePanel gp) {
        this.gp = gp;

        font = new Font("Arial", Font.PLAIN, 32);
        bold = new Font("Arial", Font.BOLD, 32);
        bold_80 = new Font("Arial", Font.BOLD, 80);
        keyImage = gp.objectManager.getObjectImage(ObjectMap.KEY);
    }

    public void showMessage(Message message) {
        messages.add(message);

        executorService.submit(() -> {
            try {
                Thread.sleep(message.timeAllowedToExist);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messages.remove(message);
        });
    }

    public void draw(Graphics2D g2D) {
        g2D.setFont(font);

        if (gameFinished) {
            g2D.setFont(bold);
            FontMetrics metrics = g2D.getFontMetrics(bold);

            String text = "You found the treasure!";

            int textWidth = metrics.stringWidth(text);

            int x = gp.getWidth() / 2 - textWidth / 2;
            int y = gp.getHeight() / 2 - 150;

            g2D.setColor(Color.WHITE);
            g2D.drawString(text, x, y);

            g2D.setFont(bold_80);
            metrics = g2D.getFontMetrics(bold_80);

            text = "Congratulations!";

            textWidth = metrics.stringWidth(text);

            x = gp.getWidth() / 2 - textWidth / 2;
            y = gp.getHeight() - 150;

            g2D.setColor(Color.YELLOW);
            g2D.drawString(text, x, y);

            gp.update_player = false;

            executorService.submit(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                gp.running = false;
            });
        } else {
            g2D.setColor(Color.WHITE);
            g2D.drawImage(keyImage, 10, 23, gp.getTileSize() / gp.getScale() * 2, gp.getTileSize() / gp.getScale() * 2, null);
            g2D.drawString(("X " + gp.getPlayer().keys), 50, 50);

            if (!messages.isEmpty()) {
                FontMetrics metrics = g2D.getFontMetrics(font);
                int textHeight = metrics.getHeight();
                int i = 0;
                for (Message message : messages) {
                    int y = gp.getHeight() - ((i * (font.getSize() * 2))) - 50;
                    int textWidth = metrics.stringWidth(message.text);
                    int boxWidth = textWidth + 30;
                    int boxHeight = textHeight + 10;
                    int boxX = 5;
                    int boxY = y - textHeight - 5;
                    g2D.setColor(Color.BLACK);
                    g2D.fillRect(boxX, boxY, boxWidth, boxHeight);
                    g2D.setColor(Color.WHITE);
                    g2D.drawString(message.text, boxX + 15, boxY + (boxHeight - (boxHeight - textHeight) / 2 - metrics.getDescent()));
                    i++;
                }
            }
        }
    }

    public boolean hasNeedKeyMessage() {
        for (Message message : messages) {
            if (message instanceof NeedKeyMessage) {
                return true;
            }
        }
        return false;
    }

    public static class Message {
        public String text;
        public int timeAllowedToExist;

        public Message(String text, int timeAllowedToExist) {
            this.text = text;
            this.timeAllowedToExist = timeAllowedToExist;
        }
    }

    public static class NeedKeyMessage extends Message {
        public NeedKeyMessage(String text, int timeAllowedToExist) {
            super(text, timeAllowedToExist);
        }
    }
}
