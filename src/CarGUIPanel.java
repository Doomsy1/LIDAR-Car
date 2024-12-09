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
    public static final int WIDTH = 800, HEIGHT = 600;
    private static final int FPS = 60;
    private final boolean[] keysPressed;

    private final Timer timer;

    private final Car car;

    public CarGUIPanel() {
        keysPressed = new boolean[KeyEvent.KEY_LAST + 1];

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        car = new Car(WIDTH / 2, HEIGHT / 2);
        car.setMovementKeys(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);

        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        // Start the timer
        timer = new Timer(1000 / FPS, this);
        timer.start();
    }

    public void step() {
        car.update(keysPressed);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < keysPressed.length) {
            keysPressed[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < keysPressed.length) {
            keysPressed[e.getKeyCode()] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not used
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        car.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        step();
        repaint();
    }
}
