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
    private static final int FPS = 100;

    // Array of keys currently pressed
    private final boolean[] keysPressed;
    private final Timer timer;
    private final World world;

    private int currentFPS = 0;
    private int frameCount = 0;
    private long lastFPSCheck = 0;

    private static final int MENU = 0, SIM = 1, REAL = 2, SETTINGS = 3;
    private int currentView = SIM;

    private boolean showWorld = false, showPOV = false, showReadings = true, showSLAM = true;
    private final int showWorldToggle, showPOVToggle, showReadingsToggle, showSLAMToggle;

    private final BufferedImage worldIcon, povIcon, lidarIcon, slamIcon;
    private final Button[] viewButtons;

    public CarGUIPanel() {
        keysPressed = new boolean[KeyEvent.KEY_LAST + 1];
        timer = new Timer(1000 / FPS, this);
        world = new World();

        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
        setFocusable(true);
        requestFocus();

        // Initialize toggle keys
        showWorldToggle = KeyEvent.VK_1;
        showPOVToggle = KeyEvent.VK_2;
        showReadingsToggle = KeyEvent.VK_3;
        showSLAMToggle = KeyEvent.VK_4;

        // Load icons
        worldIcon = Util.loadImage("assets/world_icon.png");
        povIcon = Util.loadImage("assets/local_icon.png");
        lidarIcon = Util.loadImage("assets/lidar_icon.png");
        slamIcon = Util.loadImage("assets/slam_icon.png");

        // Initialize buttons array
        viewButtons = new Button[4];

        // Add listeners
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        // Add component listener for resize events
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateButtonPositions();
                repaint();
            }
        });

        timer.start();
    }

    private void initializeButtons() {
        int iconWidth = 50;
        int iconHeight = 50;
        int totalWidth = iconWidth * 4;
        int xOffset = (getWidth()) / 2 - totalWidth / 2;
        int yOffset = 10;

        viewButtons[0] = new Button(xOffset, yOffset, iconWidth, iconHeight, worldIcon, showWorld,
                () -> {
                    if (showPOV || showReadings || showSLAM) {
                        showWorld = !showWorld;
                        viewButtons[0].setState(showWorld);
                    }
                });

        viewButtons[1] = new Button(xOffset + iconWidth, yOffset, iconWidth, iconHeight, povIcon, showPOV,
                () -> {
                    if (showWorld || showReadings || showSLAM) {
                        showPOV = !showPOV;
                        viewButtons[1].setState(showPOV);
                    }
                });

        viewButtons[2] = new Button(xOffset + 2 * iconWidth, yOffset, iconWidth, iconHeight, lidarIcon, showReadings,
                () -> {
                    if (showWorld || showPOV || showSLAM) {
                        showReadings = !showReadings;
                        viewButtons[2].setState(showReadings);
                    }
                });

        viewButtons[3] = new Button(xOffset + 3 * iconWidth, yOffset, iconWidth, iconHeight, slamIcon, showSLAM,
                () -> {
                    if (showWorld || showPOV || showReadings) {
                        showSLAM = !showSLAM;
                        viewButtons[3].setState(showSLAM);
                    }
                });
    }

    private void updateButtonPositions() {
        int iconWidth = 50;
        int iconHeight = 50;
        int totalWidth = iconWidth * 4;
        int xOffset = (getWidth() - totalWidth) / 2;
        int yOffset = 10;

        // Instead of creating new buttons, just update positions
        for (int i = 0; i < viewButtons.length; i++) {
            if (viewButtons[i] != null) {
                viewButtons[i].setPosition(xOffset + i * iconWidth, yOffset);
            }
        }
    }

    public void stepMenu() {

    }

    public void stepSimulation() {
        frameCount++;
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastFPSCheck >= 1000) { // Update FPS every second
            currentFPS = frameCount;
            frameCount = 0;
            lastFPSCheck = currentTime;
        }
        System.out.println("FPS: " + currentFPS);
        world.update(keysPressed);
    }

    public void stepReal() {

    }

    public void stepSettings() {

    }

    public void step() {
        switch (currentView) {
            case MENU -> stepMenu();
            case SIM -> stepSimulation();
            case REAL -> stepReal();
            case SETTINGS -> stepSettings();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < keysPressed.length) {
            keysPressed[e.getKeyCode()] = true;
        }

        if (e.getKeyCode() == showWorldToggle && (showPOV || showReadings || showSLAM)) {
            showWorld = !showWorld;
            viewButtons[0].setState(showWorld);
        } else if (e.getKeyCode() == showPOVToggle && (showWorld || showReadings || showSLAM)) {
            showPOV = !showPOV;
            viewButtons[1].setState(showPOV);
        } else if (e.getKeyCode() == showReadingsToggle && (showWorld || showPOV || showSLAM)) {
            showReadings = !showReadings;
            viewButtons[2].setState(showReadings);
        } else if (e.getKeyCode() == showSLAMToggle && (showWorld || showPOV || showReadings)) {
            showSLAM = !showSLAM;
            viewButtons[3].setState(showSLAM);
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
        if (currentView == MENU) {
            currentView = SIM;
            return;
        }

        for (Button button : viewButtons) {
            if (button.contains(e.getX(), e.getY())) {
                button.click();
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Reset hover states when mouse leaves the panel
        for (Button button : viewButtons) {
            button.setHovered(false);
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Button button : viewButtons) {
            button.setHovered(button.contains(e.getX(), e.getY()));
        }
        repaint(); // Ensure the panel repaints to show hover effects
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawIcons(Graphics g) {
        if (viewButtons[0] == null) {
            initializeButtons();
        }
        for (Button button : viewButtons) {
            button.draw(g);
        }
    }

    private void drawSimulation(Graphics g) {
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        int viewX = 0;
        int viewY = 0;

        int viewWidth = screenWidth;
        int viewHeight = screenHeight;

        // count number of views
        int numViews = 0;
        if (showWorld)
            numViews++;
        if (showPOV)
            numViews++;
        if (showReadings)
            numViews++;
        if (showSLAM)
            numViews++;

        if (numViews == 0) {
            System.out.println("No views to show, how...");
            return;
        }

        if (numViews == 4) {
            viewWidth = screenWidth / 2;
            viewHeight = screenHeight / 2;
        } else {
            viewWidth = screenWidth / numViews;
            viewHeight = screenHeight;
        }

        // Simulation View
        if (showWorld) {
            BufferedImage simView = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = simView.createGraphics();
            g2d.setClip(0, 0, viewWidth, viewHeight);
            world.drawSimulation(g2d);
            g2d.dispose();

            g.drawImage(simView, viewX, viewY, this);

            viewX += viewWidth;
        }

        // Lidar View
        if (showReadings) {
            BufferedImage lidarView = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2dLidar = lidarView.createGraphics();
            g2dLidar.setClip(0, 0, viewWidth, viewHeight);
            world.drawLidar(g2dLidar);
            g2dLidar.dispose();

            g.drawImage(lidarView, viewX, viewY, this);

            if (numViews == 4) {
                viewX = 0;
                viewY += viewHeight;
            } else {
                viewX += viewWidth;
            }
        }

        // POV View
        if (showPOV) {
            BufferedImage povView = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2dPOV = povView.createGraphics();
            g2dPOV.setClip(0, 0, viewWidth, viewHeight);
            world.drawPOV(g2dPOV);
            g2dPOV.dispose();

            g.drawImage(povView, viewX, viewY, this);

            viewX += viewWidth;
        }

        if (showSLAM) {
            BufferedImage occupancyView = new BufferedImage(viewWidth, viewHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2dOccupancy = occupancyView.createGraphics();
            g2dOccupancy.setClip(0, 0, viewWidth, viewHeight);
            world.drawOccupancyGrid(g2dOccupancy);
            g2dOccupancy.dispose();

            g.drawImage(occupancyView, viewX, viewY, this);
        }

        drawIcons(g);

        // Draw FPS counter
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("FPS: " + currentFPS, 10, 20);
    }

    private void drawReal(Graphics g) {

    }

    private void drawSettings(Graphics g) {

    }

    @Override
    public void paint(Graphics g) {
        switch (currentView) {
            case MENU -> drawMenu(g);
            case SIM -> drawSimulation(g);
            case REAL -> drawReal(g);
            case SETTINGS -> drawSettings(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        step();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Initialize or update button positions
        if (viewButtons[0] == null) {
            initializeButtons();
        } else {
            updateButtonPositions();
        }
    }
}