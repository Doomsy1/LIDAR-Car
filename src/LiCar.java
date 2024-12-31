/*
 * LiCar.java
 * Ario Barin Ostovary & Kevin Dang
 * 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class LiCar {
    private final Lidar lidar;
    private final Tank tank;
    private final int lidarScansPerFrame;
    private final ArrayList<Point> lidarReadings;

    public LiCar(int x, int y) {
        tank = new Tank(x, y);
        lidar = new Lidar();
        lidarScansPerFrame = 50;
        lidarReadings = new ArrayList<>();
    }

    public void update(boolean[] keysPressed) {
        tank.update(keysPressed);


        DirectedPoint lidarPosition = tank.getPosition();
        lidarReadings.clear();

        for (int i = 0; i < lidarScansPerFrame; i++) {
            lidar.update();
            Vector reading = lidar.read(lidarPosition);
            if (reading != null) {
                DirectedPoint p = lidarPosition.copy();
                p.rotate(reading.getDirection());
                p.move(reading.getMagnitude());
                lidarReadings.add(p.getPoint());
            }
        }
    }

    public void setMovementKeys(int moveForward, int moveBackward, int rotateAntiClockwise, int rotateClockwise) {
        tank.setMovementKeys(moveForward, moveBackward, rotateAntiClockwise, rotateClockwise);
    }

    public DirectedPoint getPosition() {
        return tank.getPosition();
    }

    private void drawCar(Graphics g) {
        DirectedPoint tankPosition = tank.getPosition();
        lidar.draw(g, tankPosition);
        tank.draw(g);
    }

    private void drawRays(Graphics g) {
        DirectedPoint tankPosition = tank.getPosition();
        g.setColor(Color.RED);
        for (Point p : lidarReadings) {
            g.drawLine((int) tankPosition.getX(), (int) tankPosition.getY(), (int) p.getX(), (int) p.getY());
        }
    }

    private void drawReadings(Graphics g) {
        DirectedPoint tankPosition = tank.getPosition();
        int radius = 3;
        g.setColor(Color.GREEN);
        for (Point p : lidarReadings) {
            g.drawOval((int) p.getX() - radius, (int) p.getY() - radius, radius * 2, radius * 2);
        }
    }

    public void draw(Graphics g, boolean drawRays, boolean drawReadings) {
        drawCar(g);

        if (drawRays) {
            drawRays(g);
        }
        if (drawReadings) {
            drawReadings(g);
        }
    }
}
