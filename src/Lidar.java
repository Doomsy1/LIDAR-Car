/*
 * Lidar.java
 * Kevin Dang
 * 
 */

import java.awt.*;

public class Lidar {
    private final Angle bearing;
    public final double resolution;
    public final double minDistance;
    public final double maxDistance;
    public final double lidarRadius;
    public final double noise;


    public Lidar() {
        this.bearing = new Angle(0);
        this.resolution = 50;
        this.minDistance = 75;
        this.maxDistance = 200;
        this.lidarRadius = 10;
        this.noise = 0;
    }

    public Angle getBearing() {
        return bearing;
    }

    public void update() {
        bearing.rotate(Math.PI * 2 / resolution);
    }

    public void rotate(double angle) {
        bearing.rotate(angle);
    }

    public double randomizeReading(double reading) {
        return reading + new java.util.Random().nextGaussian() * noise;
    }

    public double read(int x, int y) {
        Point start = new Point(x, y);
        DirectedPoint beam = new DirectedPoint(x, y, bearing);
        while (start.distance(beam.getX(), beam.getY()) < maxDistance) {
            beam.move(1);
            if (beam.getX() < 0 || beam.getY() < 0 || beam.getX() > Mask.getWidth() || beam.getY() > Mask.getHeight()) {
                if (start.distance(beam.getX(), beam.getY()) > minDistance) {
                    return randomizeReading(start.distance(beam.getX(), beam.getY()));
                }
                return -1;
            }
            if (!Mask.clear((int) beam.getX(), (int) beam.getY())) {
                if (start.distance(beam.getX(), beam.getY()) > minDistance) {
                    return randomizeReading(start.distance(beam.getX(), beam.getY()));
                }
                return -1;
            }
        }
        return -1;
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(new Color(10, 10, 10, 32));
        g.drawOval((int) (x - lidarRadius / 2), (int) (y - lidarRadius / 2), (int) lidarRadius, (int) lidarRadius);

        // Draw where the lidar is pointing
        DirectedPoint sensor = new DirectedPoint(x, y, bearing);
        sensor.move(maxDistance, bearing);
        g.drawLine((int) x, (int) y, (int) sensor.getX(), (int) sensor.getY());
    }
}