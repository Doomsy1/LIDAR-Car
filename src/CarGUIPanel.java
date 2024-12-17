/* 
 * CarGUIPanel.java
 * Ario Barin Ostovary & Kevin Dang
 * 
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CarGUIPanel extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {
    public static final int BASE_WIDTH = 800, BASE_HEIGHT = 600;
    private static final int FPS = 50;

    // Array of keys pressed
    private final boolean[] keysPressed;

    private final Timer timer;

    private final LiCar licar;

    public CarGUIPanel() {
        keysPressed = new boolean[KeyEvent.KEY_LAST + 1];
        
        licar = new LiCar(BASE_WIDTH / 2, BASE_HEIGHT / 2);
        licar.setMovementKeys(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);
        
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
        licar.update(keysPressed);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // if the key is pressed, set the corresponding index in the keysPressed array to true
        if (e.getKeyCode() < keysPressed.length) {
            keysPressed[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // if the key is released, set the corresponding index in the keysPressed array to false
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
        int currentWidth = getWidth();
        int currentHeight = getHeight();

        double scaleX = currentWidth / (double) BASE_WIDTH;
        double scaleY = currentHeight / (double) BASE_HEIGHT;
        double scale = Math.min(scaleX, scaleY);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, currentWidth, currentHeight);

        DirectedPoint tankPosition = licar.getPosition();
        double tankX = tankPosition.getX();
        double tankY = tankPosition.getY();
        double tankAngle = tankPosition.getAngle().getAngle();

        int leftViewportWidth = currentWidth / 2;
        int rightViewportX = currentWidth / 2;
        int centerLeftX = leftViewportWidth / 2;
        int centerLeftY = currentHeight / 2;
        int centerRightX = rightViewportX + (leftViewportWidth / 2);
        int centerRightY = currentHeight / 2;

        AffineTransform originalTransform = g2d.getTransform();

        // Left view: simulation
        g2d.setClip(0, 0, leftViewportWidth, currentHeight);
        g2d.translate(centerLeftX, centerLeftY);
        g2d.scale(scale, scale);
        g2d.rotate(-tankAngle - Math.PI / 2);
        g2d.translate(-tankX, -tankY);
        Mask.draw(g2d);
        licar.draw(g2d, true);
        g2d.setTransform(originalTransform);

        // Right view: lidar
        g2d.setClip(rightViewportX, 0, leftViewportWidth, currentHeight);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(rightViewportX, 0, leftViewportWidth, currentHeight);
        g2d.translate(centerRightX, centerRightY);
        g2d.scale(scale, scale);
        g2d.rotate(-tankAngle - Math.PI / 2);
        g2d.translate(-tankX, -tankY);
        licar.drawLidarView(g2d, false);
        g2d.setTransform(originalTransform);

        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        step();
        repaint();
    }
}