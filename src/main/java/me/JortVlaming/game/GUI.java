package me.JortVlaming.game;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUI {
    GamePanel gp;

    Font font;
    Font font_16;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final List<Message> messages = new ArrayList<>();

    private Dialogue currentDialogue;

    private final String fontFileName = "Minercraftory";

    public GUI(GamePanel gp) {
        this.gp = gp;

        InputStream is = getClass().getResourceAsStream("/fonts/" + fontFileName + ".ttf");
        if (is == null) {
            font = new Font("Arial", Font.PLAIN, 32);
            font_16 = font.deriveFont(16f);
            System.err.println("Failed to load font " + fontFileName + ". Using Arial as fallback.");
            return;
        }
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(Font.PLAIN, 18);
            font_16 = font.deriveFont(16f);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        if (gp.currentState == GameState.PLAYING) {
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
        } else if (gp.currentState == GameState.DIALOGUE) {
            drawDialogueBox(g2D);
        }
    }

    private void drawDialogueBox(Graphics2D g2D) {
        int x = gp.getTileSize() * 2;
        int y = gp.getHeight() - gp.getTileSize() * 5;
        int width = gp.getWidth() - (gp.getTileSize() * 4);
        int height = gp.getTileSize() * 4;

        g2D.setColor(new Color(0, 0, 0, 210) );
        g2D.fillRoundRect(x, y, width, height, 10, 10);
        g2D.setColor(Color.WHITE);
        //g2D.drawString(currentDialogue.author, x + 10, y + 35);

        drawDialogueSubBox(g2D, x, y, width, height);

        g2D.setColor(Color.WHITE);
        int textY = y + 40;
        int textHeight = g2D.getFontMetrics(font).getHeight();
        for (String line : currentDialogue.getCurrentMessage().text.split("\n")) {
            g2D.drawString(line, x + 15, textY);
            textY += textHeight;
        }

        g2D.setFont(font_16);
        g2D.drawString("Press space to " + (currentDialogue.hasNextMessage() ? "Continue" : "Close") + "...", x + 15, y + height - 25);
        g2D.setFont(font);
    }

    private void drawDialogueSubBox(Graphics2D g2D, int x, int y, int width, int height) {
        Color c = Color.WHITE;
        g2D.setColor(c);
        g2D.drawRoundRect(x+10, y+10, width-20, height-20, 10, 10);
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

    public Dialogue getCurrentDialogue() {
        return currentDialogue;
    }

    public void setCurrentDialogue(Dialogue currentDialogue) {
        this.currentDialogue = currentDialogue;
    }

    public void clearDialogue() {
        this.currentDialogue = null;
    }

    public boolean hasDialogue() {
        return this.currentDialogue != null;
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

    public static class Dialogue {
        public String author;
        List<NPCDialogMessage> messages = new ArrayList<>();
        int currentMessageIndex = 0;

        public Dialogue(String author) {
            this.author = author;
        }

        public Dialogue addMessage(String text) {
            messages.add(new NPCDialogMessage(text));
            return this;
        }

        public NPCDialogMessage getCurrentMessage() {
            return messages.get(currentMessageIndex);
        }

        public void nextMessage() {
            currentMessageIndex++;
        }

        public boolean hasNextMessage() {
            return currentMessageIndex < messages.size() - 1;
        }

        public static class NPCDialogMessage extends Message {
            public NPCDialogMessage(String text) {
                super(text, -1);
            }
        }
    }
}
