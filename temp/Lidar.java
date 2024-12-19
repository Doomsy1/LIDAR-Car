/*
 * Lidar.java
 * Kevin Dang
 * 
 */

import java.awt.*;

public class Lidar {
    private final Angle bearing;
    public final double resolution;
    public final double rotationPerFrame;
    public final double minDistance;
    public final double maxDistance;
    public final double lidarRadius;
    public final double noise;


    public Lidar() {
        this.bearing = new Angle(0);
        this.resolution = 50;
        this.rotationPerFrame = Math.PI * 2 / (resolution);
        
        this.minDistance = 25;
        this.maxDistance = 200;
        this.lidarRadius = 10;
        this.noise = 0;
    }

    public Angle getBearing() {
        return bearing;
    }

    public void update() {
        bearing.rotate(rotationPerFrame);
    }

    public void rotate(double angle) {
        bearing.rotate(angle);
    }

    public double randomizeReading(double reading) {
        return reading + new java.util.Random().nextGaussian() * noise;
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