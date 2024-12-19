/*
 * CarGUIPanel.java
 * Ario Barin Ostovary & Kevin Dang
 * 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CarGUIPanel extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {
    public static final int BASE_WIDTH = 800, BASE_HEIGHT = 600;
    private static final int FPS = 50;

    // Array of keys pressed
    private final boolean[] keysPressed;

    private final Timer timer;

    private final World world;
}
