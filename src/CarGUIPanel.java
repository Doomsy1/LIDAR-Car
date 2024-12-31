/* 
 * CarGUIPanel.java
 * Ario Barin Ostovary & Kevin Dang
 * 
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CarGUIPanel extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {
    public static final int BASE_WIDTH = 1200, BASE_HEIGHT = 800;
    private static final int FPS = 50;

    // Array of keys pressed
    private final boolean[] keysPressed;

    private final Timer timer;

    private final World world;

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
        // You can adjust these values to change the layout
        int leftWidth = getWidth() / 3;
        int middleWidth = getWidth() / 3;
        int rightWidth = getWidth() / 3;
        int height = getHeight();

        // Left view: simulation
        BufferedImage leftView = new BufferedImage(leftWidth, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = leftView.createGraphics();
        g2d.setClip(0, 0, leftWidth, height);  // Set clip region
        world.drawSimulation(g2d);
        g2d.dispose();

        // Middle view: lidar
        BufferedImage middleView = new BufferedImage(middleWidth, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dMiddle = middleView.createGraphics();
        g2dMiddle.setClip(0, 0, middleWidth, height);  // Set clip region
        world.drawLidar(g2dMiddle);
        g2dMiddle.dispose();

        // Right view: world
        BufferedImage worldView = new BufferedImage(rightWidth, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dWorld = worldView.createGraphics();
        g2dWorld.setClip(0, 0, rightWidth, height);  // Set clip region
        world.drawWorld(g2dWorld);
        g2dWorld.dispose();

        // Draw both buffered images to the panel
        g.drawImage(leftView, 0, 0, this);
        g.drawImage(middleView, leftWidth, 0, this);
        g.drawImage(worldView, leftWidth + middleWidth, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        step();
        repaint();
    }
}