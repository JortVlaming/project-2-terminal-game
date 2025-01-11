package me.JortVlaming.game;

import me.JortVlaming.entity.Entity;
import me.JortVlaming.monster.HostileEntity;
import me.JortVlaming.object.ObjectMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUI {
    GamePanel gp;

    Font font;
    Font font_16;
    Font font_24;
    Font font_64;

    BasicStroke bStroke_0 = new BasicStroke(0f);
    BasicStroke bStroke_2 = new BasicStroke(2.0f);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final List<Message> messages = new ArrayList<>();

    private Dialogue currentDialogue;

    private final String fontFileName = "MinecraftRegular-Bmg3";

    // Heart full, heart half, heart empty
    BufferedImage heart_f, heart_h, heart_e;

    public GUI(GamePanel gp) {
        this.gp = gp;

        InputStream is = getClass().getResourceAsStream("/fonts/" + fontFileName + ".ttf");
        if (is == null) {
            font = new Font("Arial", Font.PLAIN, 32);
            font_16 = font.deriveFont(16f);
            font_24 = font.deriveFont(24f);
            font_64 = font.deriveFont(64f);
            System.err.println("Failed to load font " + fontFileName + ". Using Arial as fallback.");
            return;
        }
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(Font.PLAIN, 32);
            font_16 = font.deriveFont(16f);
            font_24 = font.deriveFont(24f);
            font_64 = font.deriveFont(64f);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        heart_f = Util.scaleImage(gp.objectManager.getObjectImage(ObjectMap.FULL_HEART), gp.tileSize, gp.tileSize);
        heart_h = Util.scaleImage(gp.objectManager.getObjectImage(ObjectMap.HALF_HEART), gp.tileSize, gp.tileSize);
        heart_e = Util.scaleImage(gp.objectManager.getObjectImage(ObjectMap.EMPTY_HEART), gp.tileSize, gp.tileSize);
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
        g2D.setStroke(bStroke_0);

        if (gp.currentState == GameState.PAUSED) {
            drawPlayerLife(g2D);
            drawTextCentered(g2D, "PAUSED", 250);
        }

        if (gp.currentState == GameState.PLAYING) {
            drawPlayerLife(g2D);
            drawHealthBars(g2D);
            drawGUIMessages(g2D);
        }

        if (gp.currentState == GameState.DIALOGUE) {
            drawPlayerLife(g2D);
            drawDialogueBox(g2D);
        }

        if (gp.currentState == GameState.TITLE_SCREEN) {
            drawTitleScreen(g2D);
        }

        if (gp.currentState == GameState.CHARACTER_SCREEN) {
            drawHealthBars(g2D);
            drawPlayerStats(g2D);
            drawPlayerLife(g2D);
        }

        if (GamePanel.DEBUG) {
            g2D.setColor(Color.WHITE);
            g2D.setBackground(Color.GRAY);
            int height = g2D.getFontMetrics().getHeight();
            g2D.drawString("E: " + gp.entityManager.entitiesDrawnCount + " / " + gp.entityManager.entitiesUpdatedCount + " / " + gp.entityManager.averageEntityActionLockTimer, 5, 15+height);
            g2D.drawString("X: " + gp.getPlayer().worldX + " Y: " + gp.getPlayer().worldY, 5, 15+height+5+height);
            g2D.drawString("FPS: " + gp.FPS, 5, 15+height+5+height+5+height);
        }
    }

    private void drawPlayerLife(Graphics2D g2D) {

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;

        int i = 0;

        // draw empty life canisters to render over later
        while (i < gp.player.maxLife/2) {
            g2D.drawImage(heart_e, x, y, null);
            i++;
            x += gp.tileSize;
        }

        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        // draw current life over empty canisters
        while (i < gp.player.life) {
            g2D.drawImage(heart_h, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2D.drawImage(heart_f, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }
    }

    public int upgradeHover = 0;

    private void drawPlayerStats(Graphics2D g2D) {
        int x = gp.getWidth() - gp.getTileSize()*5;
        int y = gp.getTileSize() * 4;

        g2D.setColor(new Color(0,0,0,0.6f));
        g2D.fillRoundRect(x, y, gp.getTileSize()*4, gp.getTileSize()*4-10, 10, 10);

        g2D.setColor(new Color(1,1,1,1f));
        float thickness = 5f;
        Stroke old = g2D.getStroke();
        g2D.setStroke(new BasicStroke(thickness));
        g2D.drawRoundRect(x+10, y+10, gp.getTileSize()*4-20, gp.getTileSize()*4-30, 10, 10);
        g2D.setStroke(old);

        int textX = gp.getWidth() - gp.getTileSize()*2;

        g2D.drawString("Level", x+20, y+40);
        g2D.drawString(String.valueOf(gp.getPlayer().level), textX+20, y+40);

        g2D.drawString("Life", x+20, y+70);
        g2D.drawString(gp.getPlayer().life + "/" + gp.getPlayer().maxLife, textX-20, y+70);

        g2D.drawString("Strength", x+20, y+100);
        g2D.drawString(String.valueOf(gp.getPlayer().strength), textX+20, y+100);

        g2D.drawString("Dexterity", x+20, y+130);
        g2D.drawString(String.valueOf(gp.getPlayer().dexterity), textX+20, y+130);

        g2D.drawString("EXP", x+20, y+160);
        g2D.drawString(gp.getPlayer().exp + "/" + gp.getPlayer().nextLevelExp, textX-20, y+160);

        g2D.drawString("Coins", x+20, y+190);
        g2D.drawString(String.valueOf(gp.getPlayer().coin), textX+20, y+190);

        g2D.setFont(font_24);
        g2D.drawString("Power Points", x+20, y+220);
        g2D.setFont(font);
        g2D.drawString(String.valueOf(gp.getPlayer().powerPoints), textX+20, y+220);

        if (gp.getPlayer().powerPoints > 0) {
            g2D.drawString("<", textX+gp.getTileSize()+10, y+70+(30*upgradeHover));
        }
    }

    private void drawTitleScreen(Graphics2D g2D) {
        g2D.setColor(Color.BLACK);
        FontMetrics metrics = g2D.getFontMetrics(font);
        int textWidth = metrics.stringWidth("Press space to start...");
        int textHeight = metrics.getHeight();
        g2D.fillRoundRect(gp.getWidth() / 2 - textWidth / 2 - 10, gp.getHeight()-(100+textHeight), textWidth + 20, textHeight + 10, 10, 10);

        g2D.setColor(Color.WHITE);

        g2D.drawString("Press space to start...", gp.getWidth() / 2 - textWidth / 2, gp.getHeight() - 100);

        g2D.setFont(font_64);
        g2D.setStroke(bStroke_2);

        FontMetrics metrics_64 = g2D.getFontMetrics(font_64);
        textWidth = metrics_64.stringWidth("Blue Boy Adventure");
        textHeight = metrics_64.getHeight();

        g2D.drawString("Blue Boy Adventure", gp.getWidth()/2 - textWidth/2, textHeight*2);
    }

    private void drawHealthBars(Graphics2D g2D) {
        for (Entity entity : gp.getEntityManager().activeNPCEntities) {
            if (!(entity instanceof HostileEntity)) continue;

            HostileEntity e = (HostileEntity) entity;

            if (e.life == e.maxLife || e.dying) continue;

            int width = gp.getTileSize() / e.maxLife * e.life;
            int height = 10;

            int healthBarX = Util.worldXToScreenX((int) e.worldX);
            int healthBarY = Util.worldYToScreenY((int) e.worldY) - e.healthBarOffsetY;

            g2D.setColor(Color.GRAY);
            g2D.fillRect(healthBarX-2 , healthBarY-2, gp.getTileSize()+4, height+4);
            g2D.setColor(Color.BLACK);
            g2D.fillRect(healthBarX, healthBarY, gp.getTileSize(), height);
            g2D.setColor(Color.RED);
            g2D.fillRect(healthBarX, healthBarY, width, height);
        }
    }

    private void drawGUIMessages(Graphics2D g2D) {
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
