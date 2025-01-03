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
    private static final int FPS = 60;

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
        // if the key is pressed, set the corresponding index in the keysPressed array
        // to true
        if (e.getKeyCode() < keysPressed.length) {
            keysPressed[e.getKeyCode()] = true;
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
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        // Simulation View
        int simX = 0;
        int simY = 0;
        int simWidth = screenWidth / 2;
        int simHeight = screenHeight / 2;

        BufferedImage simView = new BufferedImage(simWidth, simHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = simView.createGraphics();
        g2d.setClip(0, 0, simWidth, simHeight); // Set clip region
        world.drawSimulation(g2d);
        g2d.dispose();

        g.drawImage(simView, simX, simY, this);

        // Lidar View
        int lidarX = screenWidth / 2;
        int lidarY = 0;
        int lidarWidth = screenWidth / 2;
        int lidarHeight = screenHeight / 2;

        BufferedImage lidarView = new BufferedImage(lidarWidth, lidarHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dLidar = lidarView.createGraphics();
        g2dLidar.setClip(0, 0, lidarWidth, lidarHeight); // Set clip region
        world.drawLidar(g2dLidar);
        g2dLidar.dispose();

        g.drawImage(lidarView, lidarX, lidarY, this);

        // World View
        int worldX = 0;
        int worldY = screenHeight / 2;
        int worldWidth = screenWidth / 2;
        int worldHeight = screenHeight / 2;

        BufferedImage worldView = new BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dWorld = worldView.createGraphics();
        g2dWorld.setClip(0, 0, worldWidth, worldHeight); // Set clip region
        world.drawWorld(g2dWorld);
        g2dWorld.dispose();

        g.drawImage(worldView, worldX, worldY, this);

        // Occupancy Grid View
        int occupancyX = screenWidth / 2;
        int occupancyY = screenHeight / 2;
        int occupancyWidth = screenWidth / 2;
        int occupancyHeight = screenHeight / 2;

        BufferedImage occupancyView = new BufferedImage(occupancyWidth, occupancyHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dOccupancy = occupancyView.createGraphics();
        g2dOccupancy.setClip(0, 0, occupancyWidth, occupancyHeight); // Set clip region
        world.drawOccupancyGrid(g2dOccupancy);
        g2dOccupancy.dispose();

        g.drawImage(occupancyView, occupancyX, occupancyY, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        step();
        repaint();
    }
}