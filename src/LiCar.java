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
    private ArrayList<Point> lidarReadings;

    public LiCar(int x, int y) {
        tank = new Tank(x, y);
        lidar = new Lidar();
        lidarScansPerFrame = 50;
        lidarReadings = new ArrayList<>();
    }

    public void update(boolean[] keysPressed) {
        tank.update(keysPressed);
        lidar.rotate(tank.getRotationSpeed());
        DirectedPoint tankPosition = tank.getPosition();
        lidarReadings.clear();
        for (int i = 0; i < lidarScansPerFrame; i++) {
            lidar.update();
            double reading = lidar.read((int) tankPosition.getX(), (int) tankPosition.getY());
            if (reading != -1) {
                DirectedPoint scanVector = new DirectedPoint(tankPosition.getX(), tankPosition.getY(),
                        lidar.getBearing());
                scanVector.move(reading, lidar.getBearing());
                lidarReadings.add(new Point(scanVector.getX(), scanVector.getY()));
            }
        }
    }

    public void setMovementKeys(int moveForward, int moveBackward, int rotateAntiClockwise, int rotateClockwise) {
        tank.setMovementKeys(moveForward, moveBackward, rotateAntiClockwise, rotateClockwise);
    }

    public DirectedPoint getPosition() {
        return tank.getPosition();
    }

    public void draw(Graphics g, boolean drawRays) {
        DirectedPoint tankPosition = tank.getPosition();
        lidar.draw(g, (int) tankPosition.getX(), (int) tankPosition.getY());
        tank.draw(g);
        for (Point p : lidarReadings) {
            if (drawRays) {
                g.setColor(Color.RED);
                g.drawLine((int) tankPosition.getX(), (int) tankPosition.getY(), (int) p.getX(), (int) p.getY());
            }
            g.setColor(Color.GREEN);
            g.drawOval((int) p.getX() - 5, (int) p.getY() - 5, 10, 10);
        }
    }

    public void drawLidarView(Graphics g, boolean drawRays) {
        DirectedPoint tankPosition = tank.getPosition();
        for (Point p : lidarReadings) {
            if (drawRays) {
                g.setColor(Color.RED);
                g.drawLine((int) tankPosition.getX(), (int) tankPosition.getY(), (int) p.getX(), (int) p.getY());
            }
            g.setColor(Color.GREEN);
            g.drawOval((int) p.getX() - 3, (int) p.getY() - 3, 6, 6);
        }
    }
}
