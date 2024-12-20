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

    // Viewing fields
    private boolean showMap = true;
    private boolean showLidarOnMap = true;
    private boolean showRaysOnMap = true;

    public CarGUIPanel() {
        keysPressed = new boolean[KeyEvent.KEY_LAST + 1];

        world = new World();

        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
        setFocusable(true);
        requestFocus();

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        timer = new Timer(1000 / FPS, this);
        timer.start();
    }

    public void step() {
        world.update(keysPressed);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO: there was an if statement here that checked the length of the keysPressed array (if it was less than the keycode, then it would not be pressed but i don't think that's necessary)
        keysPressed[e.getKeyCode()] = true;

        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_E -> showMap = !showMap;
            case KeyEvent.VK_R -> showLidarOnMap = !showLidarOnMap;
            case KeyEvent.VK_D -> showRaysOnMap = !showRaysOnMap;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO: another if statement here
        keysPressed[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        step();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Left View
        g2d.setClip(0, 0, getWidth() / 2, getHeight());
        world.drawStatic(g2d, showMap, showLidarOnMap, showRaysOnMap);

        // Right View
        g2d.setClip(getWidth() / 2, 0, getWidth() / 2, getHeight());
        world.drawDynamic(g2d, showLidarOnMap, showRaysOnMap, true);
    }
}

