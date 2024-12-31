/*
 * Lidar.java
 * Kevin Dang
 * 
 */

import java.awt.*;

public class Lidar {
    private final Angle bearing;
    private final double resolution;
    private final double minDistance;
    private final double maxDistance;
    private final double lidarRadius;
    private final double noise;


    public Lidar() {
        this.bearing = new Angle(0);
        this.resolution = 50;
        this.minDistance = 75;
        this.maxDistance = 200;
        this.lidarRadius = 10;
        this.noise = 1;
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

    public Vector randomizeReading(double reading) {
        return new Vector(bearing, reading + new java.util.Random().nextGaussian() * noise);
    }

    public Vector read(DirectedPoint lidarPosition) {
        Point start = new Point(lidarPosition.getX(), lidarPosition.getY());

        DirectedPoint beam = lidarPosition.copy();
        beam.rotate(bearing.getRadians());
        
        while (start.distance(beam) < maxDistance) {
            beam.move(1);
            if (!World.isAir((int) beam.getX(), (int) beam.getY())) {
                if (start.distance(beam) > minDistance) {
                    return randomizeReading(start.distance(beam));
                }
                return null;
            }
        }
        return null;
    }

    public void draw(Graphics g, DirectedPoint lidarPosition) {
        g.setColor(new Color(10, 10, 10, 32));
        g.drawOval((int) (lidarPosition.getX() - lidarRadius / 2), (int) (lidarPosition.getY() - lidarRadius / 2), (int) lidarRadius, (int) lidarRadius);

        // Draw where the lidar is pointing
        DirectedPoint sensor = lidarPosition.copy();
        sensor.rotate(bearing);
        sensor.move(maxDistance);
        g.drawLine((int) lidarPosition.getX(), (int) lidarPosition.getY(), (int) sensor.getX(), (int) sensor.getY());
    }
}