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

    // Toggle fields
    private boolean showMap = false;
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
        // if the key is pressed, set the corresponding index in the keysPressed array
        // to true
        if (e.getKeyCode() < keysPressed.length) {
            keysPressed[e.getKeyCode()] = true;
        }

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_Q) {
            showMap = !showMap;
        }
        if (code == KeyEvent.VK_E) {
            showLidarOnMap = !showLidarOnMap;
        }
        if (code == KeyEvent.VK_R) {
            showRaysOnMap = !showRaysOnMap;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // if the key is released, set the corresponding index in the keysPressed array
        // to false
        if (e.getKeyCode() < keysPressed.length) {
            keysPressed[e.getKeyCode()] = false;
        }
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
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Left View
        // set clip to the left half of the screen
        g2d.setClip(0, 0, getWidth() / 2, getHeight());
        world.drawStatic(g2d, showMap, showLidarOnMap, showRaysOnMap);

        // Right View
        // set clip to the right half of the screen
        g2d.setClip(getWidth() / 2, 0, getWidth() / 2, getHeight());
        world.drawDynamic(g2d, showLidarOnMap, showRaysOnMap, true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        step();
        repaint();
    }
}
