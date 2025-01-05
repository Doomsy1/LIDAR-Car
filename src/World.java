/* 
 * World.java
 * Ario Barin Ostovary
 * Class for the world, contains the tank and the map
 * Contains the drawing of the world, the tank, and the lidar
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.ImageIO;

public class World {
    public static final int WORLD_WIDTH, WORLD_HEIGHT;

    private static final Image mask;
    private static final BufferedImage bufferedMask;
    private static final Image bg;

    static {
        try {
            mask = ImageIO.read(new File("mask.png"));
            bufferedMask = (BufferedImage) mask;
            bg = ImageIO.read(new File("background.png"));

            if (mask.getWidth(null) != bg.getWidth(null) || mask.getHeight(null) != bg.getHeight(null)) {
                throw new RuntimeException("Mask and background images must have the same dimensions");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load required images", e);
        }

        WORLD_WIDTH = mask.getWidth(null);
        WORLD_HEIGHT = mask.getHeight(null);
    }

    public static final Color AIR = new Color(255, 174, 201);

    private final LiCar licar;

    public World() {
        this.licar = new LiCar(WORLD_WIDTH * 1/5, WORLD_HEIGHT * 1/4, 0,
                KeyEvent.VK_W, KeyEvent.VK_S,
                KeyEvent.VK_A, KeyEvent.VK_D);
    }

    public static boolean isAir(int x, int y) {
        if (x < 0 || x >= WORLD_WIDTH || y < 0 || y >= WORLD_HEIGHT) {
            return false;
        }

        return bufferedMask.getRGB(x, y) == AIR.getRGB();
    }

    public static int getWidth() {
        return WORLD_WIDTH;
    }

    public static int getHeight() {
        return WORLD_HEIGHT;
    }

    public static void drawMask(Graphics g) {
        g.drawImage(mask, 0, 0, null);
    }

    public static void drawBackground(Graphics g) {
        g.drawImage(bg, 0, 0, null);
    }

    private void drawView(
            boolean centerCar,
            Graphics2D g2d,
            Color backgroundColor,
            Consumer<Graphics2D> drawOperations) {
        // Get dimensions from the graphics context
        int viewportWidth = g2d.getClipBounds().width;
        int viewportHeight = g2d.getClipBounds().height;

        // Initialize BufferedImage
        BufferedImage view = new BufferedImage(viewportWidth, viewportHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = view.createGraphics();

        // Calculate scaling factors
        double scaleX = viewportWidth / (double) WORLD_WIDTH;
        double scaleY = viewportHeight / (double) WORLD_HEIGHT;
        double scale = Math.min(scaleX, scaleY);

        // Fill background
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, viewportWidth, viewportHeight);

        // Center the view in the viewport
        int centerX = viewportWidth / 2;
        int centerY = viewportHeight / 2;

        // Apply transformations
        graphics.translate(centerX, centerY);
        graphics.scale(scale, scale);

        // Only apply car-following transformations if this is not the fixed world view
        if (centerCar) {
            DirectedPoint tankPosition = licar.getActualPosition();
            double tankX = tankPosition.getX();
            double tankY = tankPosition.getY();
            double tankAngle = tankPosition.getAngle().getRadians();

            graphics.rotate(-tankAngle - Math.PI / 2);
            graphics.translate(-tankX, -tankY);
        } else {
            // For fixed world view, just center the world
            graphics.translate(-WORLD_WIDTH / 2, -WORLD_HEIGHT / 2);
        }

        // Execute unique drawing operations
        drawOperations.accept(graphics);

        // Dispose temporary Graphics2D
        graphics.dispose();

        // Draw the buffered image onto the main Graphics2D context
        g2d.drawImage(view, 0, 0, null);
    }

    public void update(boolean[] keys) {
        licar.update(keys);
    }

    public void drawSimulation(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            drawView(
                    true,
                    g2d,
                    Color.WHITE,
                    graphics -> {
                        drawBackground(graphics);
                        licar.draw(graphics, false, true);
                    });
        } finally {
            g2d.dispose();
        }
    }

    public void drawLidar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            drawView(
                    true,
                    g2d,
                    Color.BLACK,
                    graphics -> {
                        licar.draw(graphics, false, true);
                    });
        } finally {
            g2d.dispose();
        }
    }

    public void drawWorld(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            drawView(
                    false,
                    g2d,
                    Color.WHITE,
                    graphics -> {
                        drawBackground(graphics);
                        licar.draw(graphics, false, true);
                    });
        } finally {
            g2d.dispose();
        }
    }

    public void drawOccupancyGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        licar.drawOccupancyGrid(g2d);
        g2d.dispose();
    }
}