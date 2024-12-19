/*
 * LiCar.java
 * Ario Barin Ostovary & Kevin Dang
 * 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class LiCar {
    private Lidar lidar;
    private Tank tank;
    private int lidarScansPerFrame;
    private ArrayList<Vector> lidarReadings;
    private World world;

    public LiCar(World world) {
        tank = new Tank();
        lidar = new Lidar();
        lidarScansPerFrame = 50;
        lidarReadings = new ArrayList<>();
        this.world = world;
    }

    public void update(boolean[] keysPressed) {
        tank.update(keysPressed);
        lidar.rotate(tank.getRotationSpeed());
        lidarReadings.clear();
        for (int i = 0; i < lidarScansPerFrame; i++) {
            lidar.update();
            double reading = lidar.read();
            if (reading != -1) {
                
            }
        }
    }

    public void setMovementKeys(int moveForward, int moveBackward, int rotateAntiClockwise, int rotateClockwise) {
        tank.setMovementKeys(moveForward, moveBackward, rotateAntiClockwise, rotateClockwise);
    }

    public double getSpeed() {
        return tank.getSpeed();
    }

    public double getRotationSpeed() {
        return tank.getRotationSpeed();
    }

    public void draw(Graphics g, DirectedPoint tankPosition, boolean drawRays, boolean drawScans) {
        tank.draw(g, tankPosition);
        lidar.draw(g, (int) tankPosition.getX(), (int) tankPosition.getY());
        
        for (Point p : lidarReadings) {
            if (drawRays) {
                g.setColor(Color.RED);
                g.drawLine((int) tankPosition.getX(), (int) tankPosition.getY(), (int) p.getX(), (int) p.getY());
            }
            if (drawScans) {
                g.setColor(Color.GREEN);
                g.drawOval((int) p.getX() - 5, (int) p.getY() - 5, 10, 10);
            }
        }
    }

    public void drawLidarView(Graphics g, DirectedPoint tankPosition, boolean drawRays) {
        tank.draw(g, tankPosition);
        for (Point p : lidarReadings) {
            if (drawRays) {
                g.setColor(Color.RED);
                g.drawLine((int) tankPosition.getX(), (int) tankPosition.getY(), (int) p.getX(), (int) p.getY());
            }
            g.setColor(Color.GREEN);
            g.drawOval((int) p.getX() - 3, (int) p.getY() - 3, 6, 6);
        }
    }

    // Draws the static map with optional car, lidar readings, and rays
    public void drawStatic(Graphics g, DirectedPoint tankPosition, boolean showLidar, boolean showRays) {
        // Draw tank at its actual position
        tank.draw(g, tankPosition);

        if (showLidar) {
            // draw lidar sensor
            lidar.draw(g, (int) tankPosition.getX(), (int) tankPosition.getY());
            for (Point p : lidarReadings) {
                if (showRays) {
                    g.setColor(Color.RED);
                    g.drawLine((int) tankPosition.getX(), (int) tankPosition.getY(), (int) p.getX(), (int) p.getY());
                }
                g.setColor(Color.GREEN);
                g.drawOval((int) p.getX() - 3, (int) p.getY() - 3, 6, 6);
            }
        }
    }

    public void drawMapReadings(Graphics g, DirectedPoint tankPosition, boolean showRays) {
        if (showRays) {
            for (Point p : lidarReadings) {
                g.setColor(Color.RED);
                g.drawLine((int) tankPosition.getX(), (int) tankPosition.getY(), (int) p.getX(), (int) p.getY());
            }
        }

        for (Point p : lidarReadings) {
            g.setColor(Color.GREEN);
            g.drawOval((int) p.getX() - 3, (int) p.getY() - 3, 6, 6);
        }
    }
}
