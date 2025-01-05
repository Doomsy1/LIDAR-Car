/*
 * LiCar.java
 * Ario Barin Ostovary & Kevin Dang
 * Class combining the tank and lidar - uses particlefilter to create a map of the world
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class LiCar {
    private final Lidar lidar;
    private final Tank tank;
    private List<MyVector> lidarReadings;

    private final ParticleFilter particleFilter;

    public LiCar(int x, int y, double angle,
            int moveForward, int moveBackward,
            int rotateAntiClockwise, int rotateClockwise) {
        tank = new Tank(x, y, angle, moveForward, moveBackward, rotateAntiClockwise, rotateClockwise);
        lidar = new Lidar();
        lidarReadings = new ArrayList<>();

        particleFilter = new ParticleFilter(new OccupancyGrid("background.png", Color.BLACK, Color.WHITE, 1));
    }

    public DirectedPoint getActualPosition() {
        return tank.getPosition();
    }

    public DirectedPoint getEstimatedPosition() {
        return particleFilter.getEstimatedPosition();
    }

    public void update(boolean[] keysPressed) {
        tank.update(keysPressed);
        
        // Scan lidar
        lidarReadings = lidar.scan(tank.getPosition());

        // Update particle filter
        double speed = tank.getSpeed();
        double angle = tank.getRotationSpeed();
        particleFilter.update(speed, angle, lidarReadings);
    }

    private void drawCar(Graphics g) {
        DirectedPoint tankPosition = tank.getPosition();
        lidar.draw(g, tankPosition);
        tank.draw(g);
    }

    private void drawEstimatedPosition(Graphics g) {
        int width = 800;
        int height = 600;
        int x = (int) Math.round(getEstimatedPosition().getX() + width / 2);
        int y = (int) Math.round(getEstimatedPosition().getY() + height / 2);
        g.setColor(Color.RED);
        int radius = 10;
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        DirectedPoint end = getEstimatedPosition().copy();
        end.move(radius * 3);
        int endX = (int) Math.round(end.getX() + width / 2);
        int endY = (int) Math.round(end.getY() + height / 2);
        g.drawLine(x, y, endX, endY);
    }

    private void drawRays(Graphics g) {
        DirectedPoint tankPosition = tank.getPosition();
        g.setColor(Color.RED);
        for (MyVector v : lidarReadings) {
            DirectedPoint rayEnd = tankPosition.copy();
            rayEnd.rotate(v.getDirection());
            rayEnd.move(v.getMagnitude());
            g.drawLine((int) tankPosition.getX(), (int) tankPosition.getY(), (int) rayEnd.getX(), (int) rayEnd.getY());
        }
    }

    private void drawReadings(Graphics g) {
        DirectedPoint tankPosition = tank.getPosition();
        int radius = 3;
        g.setColor(Color.GREEN);
        for (MyVector v : lidarReadings) {
            DirectedPoint rayEnd = tankPosition.copy();
            rayEnd.rotate(v.getDirection());
            rayEnd.move(v.getMagnitude());
            g.drawOval((int) rayEnd.getX() - radius, (int) rayEnd.getY() - radius, radius * 2, radius * 2);
        }
    }

    public void draw(Graphics g, boolean drawRays, boolean drawReadings) {
        drawCar(g);
        drawEstimatedPosition(g);

        if (drawRays) {
            drawRays(g);
        }
        if (drawReadings) {
            drawReadings(g);
        }
    }

    public void drawOccupancyGrid(Graphics g) {
        particleFilter.draw(g);
    }
}