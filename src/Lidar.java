
import java.awt.Color;
import java.awt.Graphics;

/*
 * Lidar.java
 * Kevin Dang
 * 
 */

public class Lidar {
    // Angle of the lidar relative to the car
    private final Angle bearing;

    // Parameters
    public final double resolution = 50;
    public final double rotationPerFrame = Math.PI * 2 / resolution;

    // Lidar properties
    public final double minDistance = 25;
    public final double maxDistance = 200;
    public final double lidarRadius = 10;
    public final double noise = 0; // STD of pixel noise

    public Lidar() {
        this.bearing = new Angle(0);
    }

    public void update() {
        bearing.rotate(rotationPerFrame);
    }

    public double randomizeReading(double reading) {
        return Utils.randomGaussian(reading, noise);
    }

    public double read(DirectedPoint carPosition) {
        // gives the distance to the nearest object in the direction the lidar is pointing
        DirectedPoint beam = new DirectedPoint(carPosition.getX(), carPosition.getY(), bearing);
        beam.rotate(bearing);

        // Check if the sensor is out of bounds
        if (beam.getX() < 0 || beam.getX() >= World.getWidth() || beam.getY() < 0 || beam.getY() >= World.getHeight()) {
            return -1;
        }

        while (carPosition.distance(beam) < maxDistance) {
            if (World.isAir((int) beam.getX(), (int) beam.getY())) {
                return carPosition.distance(beam);
            }
            beam.move(1);
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

