/*
 * LiCar.java
 * Ario Barin Ostovary & Kevin Dang
 * Class combining the tank and lidar - uses particlefilter to create a map of the world
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class LiCar {
    private final Lidar lidar;
    private final Tank tank;
    private final int lidarScansPerFrame = 50;
    private final ArrayList<Vector> lidarReadings;

    private final ParticleFilter particleFilter;
    private final int numParticles = 2;

    public LiCar(int x, int y, double angle,
            int moveForward, int moveBackward,
            int rotateAntiClockwise, int rotateClockwise) {
        tank = new Tank(x, y, angle, moveForward, moveBackward, rotateAntiClockwise, rotateClockwise);
        lidar = new Lidar();
        lidarReadings = new ArrayList<>();

        particleFilter = new ParticleFilter(numParticles);
    }

    public DirectedPoint getActualPosition() {
        return tank.getPosition();
    }

    public void update(boolean[] keysPressed) {
        tank.update(keysPressed);

        double speed = tank.getSpeed();
        double angle = tank.getRotationSpeed();

        particleFilter.predict(speed, angle);

        lidarReadings.clear();

        // Scan lidar
        for (int i = 0; i < lidarScansPerFrame; i++) {
            lidar.update();
            Vector reading = lidar.read(tank.getPosition());
            if (reading != null) {
                lidarReadings.add(reading);
            }
        }

        particleFilter.updateWeights(lidarReadings);

        particleFilter.resample();

        particleFilter.updateGrids(lidarReadings);
    }

    private void drawCar(Graphics g) {
        DirectedPoint tankPosition = tank.getPosition();
        lidar.draw(g, tankPosition);
        tank.draw(g);
    }

    private void drawRays(Graphics g) {
        DirectedPoint tankPosition = tank.getPosition();
        g.setColor(Color.RED);
        for (Vector v : lidarReadings) {
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
        for (Vector v : lidarReadings) {
            DirectedPoint rayEnd = tankPosition.copy();
            rayEnd.rotate(v.getDirection());
            rayEnd.move(v.getMagnitude());
            g.drawOval((int) rayEnd.getX() - radius, (int) rayEnd.getY() - radius, radius * 2, radius * 2);
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

    public void drawOccupancyGrid(Graphics g) {
        particleFilter.draw(g);
    }
}