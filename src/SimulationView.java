import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class SimulationView extends View {
    private boolean 
    showWorld = false, 
    showPOV = false, 
    showReadings = true, 
    showSLAM = true;

    private final int
    showWorldToggle = KeyEvent.VK_1, 
    showPOVToggle = KeyEvent.VK_2, 
    showReadingsToggle = KeyEvent.VK_3, 
    showSLAMToggle = KeyEvent.VK_4;
    
    private static final BufferedImage worldIcon, povIcon, lidarIcon, slamIcon;
    private final int buttonWidth = 50, buttonHeight = 50;
    private final Button[] viewButtons;

    private final World world;

    static {
        worldIcon = Util.loadImage("assets/world_icon.png");
        povIcon = Util.loadImage("assets/local_icon.png");
        lidarIcon = Util.loadImage("assets/lidar_icon.png");
        slamIcon = Util.loadImage("assets/slam_icon.png");
    }

    public SimulationView(World world, CarGUIPanel panel) {
        super(panel);
        this.world = world;
        viewButtons = new Button[4];
        initializeButtons();
    }

    public void initializeButtons() {
        int iconWidth = 50;
        int iconHeight = 50;
        int totalWidth = iconWidth * 4;
        int xOffset = (panel.getWidth()) / 2 - totalWidth / 2;
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

    public void updateButtonPositions() {
        int totalWidth = buttonWidth * 4;
        int xOffset = (panel.getWidth()) / 2 - totalWidth / 2;
        int yOffset = 10;

        for (int i = 0; i < viewButtons.length; i++) {
            if (viewButtons[i] != null) {
                viewButtons[i].setPosition(xOffset + i * buttonWidth, yOffset);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Button button : viewButtons) {
            if (button.contains(e.getX(), e.getY())) {
                button.click();
                break;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Button button : viewButtons) {
            button.setHovered(button.contains(e.getX(), e.getY()));
        }
    }

    private void drawButtons(Graphics g) {
        for (Button button : viewButtons) {
            button.draw(g);
        }
    }

    @Override
    public void step(boolean[] keysDown, boolean[] keysPressed) {
        world.update(keysDown);

        if (keysPressed[showWorldToggle] && (showPOV || showReadings || showSLAM)) {
            showWorld = !showWorld;
            viewButtons[0].setState(showWorld);
        } else if (keysPressed[showPOVToggle] && (showWorld || showReadings || showSLAM)) {
            showPOV = !showPOV;
            viewButtons[1].setState(showPOV);
        } else if (keysPressed[showReadingsToggle] && (showWorld || showPOV || showSLAM)) {
            showReadings = !showReadings;
            viewButtons[2].setState(showReadings);
        } else if (keysPressed[showSLAMToggle] && (showWorld || showPOV || showReadings)) {
            showSLAM = !showSLAM;
            viewButtons[3].setState(showSLAM);
        }
    }

    @Override
    public void draw(Graphics g) {
        updateButtonPositions();
        
        int screenWidth = panel.getWidth();
        int screenHeight = panel.getHeight();

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

            g.drawImage(simView, viewX, viewY, panel);

            viewX += viewWidth;
        }

        // Lidar View
        if (showReadings) {
            BufferedImage lidarView = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2dLidar = lidarView.createGraphics();
            g2dLidar.setClip(0, 0, viewWidth, viewHeight);
            world.drawLidar(g2dLidar);
            g2dLidar.dispose();

            g.drawImage(lidarView, viewX, viewY, panel);

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

            g.drawImage(povView, viewX, viewY, panel);

            viewX += viewWidth;
        }

        if (showSLAM) {
            BufferedImage occupancyView = new BufferedImage(viewWidth, viewHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2dOccupancy = occupancyView.createGraphics();
            g2dOccupancy.setClip(0, 0, viewWidth, viewHeight);
            world.drawOccupancyGrid(g2dOccupancy);
            g2dOccupancy.dispose();

            g.drawImage(occupancyView, viewX, viewY, panel);
        }

        drawButtons(g);
    }
}