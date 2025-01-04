package me.JortVlaming.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUI {
    GamePanel gp;

    Font font;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final List<Message> messages = new ArrayList<>();

    public GUI(GamePanel gp) {
        this.gp = gp;

        font = new Font("Arial", Font.PLAIN, 32);
    }

    public void showMessage(Message message) {
        messages.add(message);

        executorService.submit(() -> {
            try {
                for (int i = 0; i < message.timeAllowedToExist; i++) {
                    while (gp.currentState == GameState.PAUSED) {
                        Thread.sleep(1);
                    }
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messages.remove(message);
        });
    }

    public void draw(Graphics2D g2D) {
        g2D.setFont(font);
        g2D.setColor(Color.WHITE);

        if (gp.currentState == GameState.PAUSED) {
            drawTextCentered(g2D, "PAUSED", 250);
        }

        if (GamePanel.DEBUG) {
            g2D.setColor(Color.WHITE);
            g2D.setBackground(Color.GRAY);
            g2D.drawString("E: " + gp.entitiesDrawnCount + " / " + gp.entitiesUpdatedCount + " / " + gp.averageEntityActionLockTimer, 5, 35);
        }

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

    private void drawTextCentered(Graphics2D g2D, String text, int y) {
        FontMetrics metrics = g2D.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);

        int x = (gp.getWidth() - textWidth) / 2;

        g2D.drawString(text, x, y);
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
