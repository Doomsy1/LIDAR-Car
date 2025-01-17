/*
 * Util.java
 * Ario Barin Ostovary
 * Class for utility functions
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;

public class Util {
    public static double randomGaussian(double standardDeviation) {
        return ThreadLocalRandom.current().nextGaussian() * standardDeviation;
    }

    public static double randomDouble(double start, double end) {
        return ThreadLocalRandom.current().nextDouble(start, end);
    }

    public static double logitToProb(double logit) {
        if (logit > 0) {
            // avoid overflow
            return 1.0 / (1.0 + Math.exp(-logit));
        } else {
            // avoid underflow
            return Math.exp(logit) / (1.0 + Math.exp(logit));
        }
    }

    public static double probToLogit(double prob) {
        if (prob <= 0.0 || prob >= 1.0) {
            throw new IllegalArgumentException("Probability must be between 0 and 1 exclusive");
        }
        return Math.log(prob / (1.0 - prob));
    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("Could not load image at path: " + path, e);
        }
    }

    public static void drawImage(Graphics g, BufferedImage img, int x, int y, int width, int height) {
        // Draw the image with nearest neighbor interpolation - no blurring
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.drawImage(img, x, y, width, height, null);
    }
}