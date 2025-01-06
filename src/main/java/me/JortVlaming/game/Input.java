package me.JortVlaming.game;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    private int NUM_KEYS = 256;
    private boolean[] keys = new boolean[NUM_KEYS];
    private boolean[] keysLast = new boolean[NUM_KEYS];

    private int NUM_BUTTONS = MouseInfo.getNumberOfButtons();
    private boolean[] buttons = new boolean[NUM_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    private int mouseX, mouseY;
    private int scroll;
    private int scale;

    public Input(int scale)
    {
        mouseX = 0;
        mouseY = 0;
        scroll = 0;
        this.scale = scale;
    }

    public void update()
    {
        scroll = 0;

        System.arraycopy(keys, 0, keysLast, 0, NUM_KEYS);

        System.arraycopy(buttons, 0, buttonsLast, 0, NUM_BUTTONS);
    }

    private void expandKeysCheck(int code) {
        if (code > NUM_KEYS) {
            NUM_KEYS = code+1;

            keys = Arrays.copyOf(keys, NUM_KEYS);
            keysLast = Arrays.copyOf(keysLast, NUM_KEYS);

            System.out.println("Expanded keys array to " + NUM_KEYS);
        }
    }

    private void expandButtonsCheck(int btn) {
        if (btn > NUM_BUTTONS) {
            NUM_BUTTONS = btn+1;

            buttons = Arrays.copyOf(buttons, NUM_BUTTONS);
            buttonsLast = Arrays.copyOf(keysLast, NUM_BUTTONS);

            System.out.println("Expanded buttons array to " + NUM_BUTTONS);
        }
    }

    public boolean isKey(int code) {
        expandKeysCheck(code);
        return keys[code];
    }

    public boolean isKeyUp(int code) {
        expandKeysCheck(code);
        return !keys[code] && keysLast[code];
    }

    public boolean isKeyDown(int code) {
        expandKeysCheck(code);
        return keys[code] && !keysLast[code];
    }

    public boolean isButton(int button) {
        expandKeysCheck(button);
        return buttons[button];
    }

    public boolean isButtonUp(int button) {
        expandKeysCheck(button);
        return !buttons[button] && buttonsLast[button];
    }

    public boolean isButtonDown(int button) {
        expandKeysCheck(button);
        return buttons[button] && !buttonsLast[button];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        expandKeysCheck(code);
        keys[code] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        expandKeysCheck(code);
        keys[code] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        expandKeysCheck(e.getButton());
        buttons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        expandKeysCheck(e.getButton());
        buttons[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int)(e.getX() / scale);
        mouseY = (int)(e.getY() / scale);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int)(e.getX() / scale);
        mouseY = (int)(e.getY() / scale);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getScroll() {
        return scroll;
    }
}
