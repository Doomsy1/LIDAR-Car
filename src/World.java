/*
 * World.java
 * Ario Barin Ostovary & Kevin Dang
 * 
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class World {
    private static final int WIDTH = 800, HEIGHT = 600;

    private LiCar licar;

    // Mask and background
    private static Image mask;
    private static Image bg;

    // Load mask and background
    static {
        try {
            mask = ImageIO.read(new File("mask.png"));
            bg = ImageIO.read(new File("background.png")); 
        } catch (IOException e) {
            System.err.println("Error loading images: " + e.getMessage());
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

    public void drawStatic(Graphics g, boolean showMap, boolean showLidarOnMap, boolean showRaysOnMap) {
        // Static drawing of the full world with car placed on the map

        // scale the image to the size of the window
        Image scaledImage = bg.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        g.drawImage(scaledImage, 0, 0, null);

        // draw the car on the map
        licar.draw(g, carPosition, showRaysOnMap, showLidarOnMap);
    }
}


