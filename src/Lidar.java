/*
 * Lidar.java
 * Kevin Dang
 * Class for the lidar, can read the distance to the nearest object in a certain direction
 */

import java.awt.*;

public class Lidar {
    private final Angle bearing;

    // tick = 1.8 degrees
    private static final double TICK = Math.PI / 180 * 1.8;

    private final double resolution = 50;

    public static final double MIN_DISTANCE = 1;
    public static final double MAX_DISTANCE = 300;

    private final double lidarRadius = 10;
    private final double readingNoise = 1.5;
    private final double bearingNoise = 0.005;

    public Lidar() {
        bearing = new Angle(0);
    }

    public Angle getBearing() {
        return bearing;
    }

    public void update() {
        bearing.rotate(Math.PI * 2 / resolution + Util.gaussianNoise(bearingNoise));
        // bearing.rotate(Math.PI * 2 / resolution + 0.01);
        // bearing.rotate(Math.PI * 2 / resolution);
    }

    public Vector randomizeReading(double reading) {
        return new Vector(bearing.copy(), reading + Util.gaussianNoise(readingNoise));
    }

    public Vector read(DirectedPoint lidarPosition) {
        DirectedPoint beam = lidarPosition.copy();
        beam.rotate(bearing); // Make the beam face in the direction of the lidar

        double step = 0.5;
        double distance = 0;

        while (distance < MAX_DISTANCE) {
            distance += step;
            beam.move(step);
            if (!World.isAir((int) beam.getX(), (int) beam.getY())) {
                if (distance > MIN_DISTANCE) {
                    return randomizeReading(distance);
                }
                return new Vector(bearing.copy(), MIN_DISTANCE);
            }
        }
        return new Vector(bearing.copy(), MAX_DISTANCE);
    }

    public void draw(Graphics g, DirectedPoint lidarPosition) {
        g.setColor(new Color(10, 10, 10, 32));
        g.drawOval((int) (lidarPosition.getX() - lidarRadius / 2), (int) (lidarPosition.getY() - lidarRadius / 2),
                (int) lidarRadius, (int) lidarRadius);

        // Draw where the lidar is pointing
        DirectedPoint sensor = lidarPosition.copy();
        sensor.rotate(bearing);
        sensor.move(MAX_DISTANCE);
        g.drawLine((int) lidarPosition.getX(), (int) lidarPosition.getY(), (int) sensor.getX(), (int) sensor.getY());
    }
}