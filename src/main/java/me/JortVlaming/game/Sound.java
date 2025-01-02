package me.JortVlaming.game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL;

public Sound() {
    soundURL = new URL[Clips.values().length];

    for (Clips clip : Clips.values()) {
        soundURL[clip.index] = getClass().getResource("/sound/" + clip.filename + ".wav");
    }
}

    public void setFile(int i) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            audioInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public enum Clips {
        COIN("coin", 0),
        SPEAK("speak", 1),
        UNLOCK("unlock", 2),
        BLOCKED("blocked", 3),
        HITMONSTER("hitmonster", 4),
        MERCHANT("Merchant", 5),
        GAMEOVER("gameover", 6),
        CHIPWALL("chipwall", 7),
        BURNING("burning", 8),
        DOOROPEN("dooropen", 9),
        RECEIVEDAMAGE("receivedamage", 10),
        FANFARE("fanfare", 11),
        FINALBATTLE("FinalBattle", 12),
        POWERUP("powerup", 13),
        CUTTREE("cuttree", 14),
        STAIRS("stairs", 15),
        PARRY("parry", 16),
        LEVELUP("levelup", 17),
        CURSOR("cursor", 18),
        BLUEBOYADVENTURE("BlueBoyAdventure", 19),
        SLEEP("sleep", 20),
        DUNGEON("Dungeon", 21);

        public final String filename;
        public final int index;
        Clips(String filename, int index) {
            this.filename = filename;
            this.index = index;
        }
    }
}
