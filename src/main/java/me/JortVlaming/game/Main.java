package me.JortVlaming.game;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");

        GamePanel panel = new GamePanel();
        window.add(panel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        for (String s : args) {
            if (s.equalsIgnoreCase("--no-collisions")) {
                GamePanel.CHECK_COLLISION = false;
            }
            if (s.equalsIgnoreCase("--no-objects")) {
                GamePanel.DO_OBJECTS = false;
            }
            if (s.equalsIgnoreCase("--no-entities")) {
                GamePanel.DO_ENTITIES = false;
            }
            if (s.equalsIgnoreCase("--allow-debug")) {
                GamePanel.ALLOW_DEBUG = true;
            }
        }

        panel.startGameThread();
    }
}