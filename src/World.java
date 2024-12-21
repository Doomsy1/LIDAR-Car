/*
 * World.java
 * Ario Barin Ostovary & Kevin Dang
 * 
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class World {
    private static final int WIDTH;
    private static final int HEIGHT;
    
    private LiCar licar;
    
    // Mask and background
    private static final String MASK_PATH = "mask.png";
    private static final String BG_PATH = "background.png";
    private static final Image mask;
    private static final Image bg;

    // Load mask and background
    static {
        try {
            mask = ImageIO.read(new File(MASK_PATH));
            bg = ImageIO.read(new File(BG_PATH));

            if (mask == null) {
                throw new IllegalStateException("Failed to load " + MASK_PATH);
            }
            if (bg == null) {
                throw new IllegalStateException("Failed to load " + BG_PATH);
            }

            WIDTH = bg.getWidth(null);
            HEIGHT = bg.getHeight(null);
    
            if (mask.getWidth(null) != WIDTH || mask.getHeight(null) != HEIGHT) {
                throw new IllegalStateException("Mask and background dimensions do not match");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading images: " + e.getMessage(), e);
        }
    }

    public static final Color AIR = new Color(255, 174, 201);
    
    public World() {

        this.licar = new LiCar(WIDTH / 2, HEIGHT / 2, 0);

        this.licar.setMovementKeys(KeyEvent.VK_Q, KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_S);

    }

    public void update(boolean[] keysPressed) {
        licar.update(keysPressed);
    }

    public static boolean isAir(int x, int y) {
        BufferedImage bufferedMask = (BufferedImage) mask;
        int pixel = bufferedMask.getRGB(x, y);

        // Check if the pixel is out of bounds
        if (x < 0 || x >= mask.getWidth(null) || y < 0 || y >= mask.getHeight(null)) {
            return false;
        }

        // Check if the pixel is clear
        return pixel == AIR.getRGB();
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public void drawStatic(Graphics g, boolean showMap, boolean showReadingsOnMap, boolean showRaysOnMap) {
        // Create a buffered image to draw on
        BufferedImage buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();

        // Enable antialiasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the background and car
        g2d.drawImage(bg, 0, 0, null);
        licar.drawCar(g2d);
        
        // Draw optional overlays
        if (showRaysOnMap) {
            licar.drawRays(g2d);
        }
        if (showReadingsOnMap) {
            licar.drawReadings(g2d);
        }

        // Scale and draw the final image
        g.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
        
        // Clean up graphics resources
        g2d.dispose();
    }

    public void drawDynamic(Graphics g, boolean showReadingsOnMap, boolean showRaysOnMap, boolean showMap) {
        // Dynamic drawing of the world with car and lidar readings
        BufferedImage buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(bg, 0, 0, null);
        licar.drawCar(g2d);

        // Draw optional overlays
        if (showRaysOnMap) {
            licar.drawRays(g2d);
        }
        if (showReadingsOnMap) {
            licar.drawReadings(g2d);
        }

        // rotate the image to have the car facing up and in the center
        DirectedPoint carCenter = licar.getPosition();
        AffineTransform transform = new AffineTransform();
        transform.rotate(carCenter.getAngle().getRadians(), carCenter.getX(), carCenter.getY());
        
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        g.drawImage(op.filter(buffer, null), 0, 0, null);
    }
}


