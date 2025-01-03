/*
 * Particle.java
 * Ario Barin Ostovary
 * Class for a particle in the particle filter
 * Particles are used to represent a possible position and a possible map
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Particle {
    private final DirectedPoint pose;
    private final OccupancyGrid occupancyGrid;

    private double weight;

    private static final int STARTING_WIDTH = 100;
    private static final int STARTING_HEIGHT = 100;
    private static final int RESOLUTION = 1;

    private static final double POSITION_NOISE = 0.05;
    private static final double ANGLE_NOISE = 0.001;

    private static final double SENSOR_NOISE = 1.0;
    private static final double OCCUPIED_THRESHOLD = 0.7;

    public Particle(DirectedPoint position) {
        this.pose = position;
        this.occupancyGrid = new OccupancyGrid(STARTING_WIDTH, STARTING_HEIGHT, RESOLUTION);
        this.weight = 1.0;
    }

    public Particle(Particle particle) {
        this.pose = particle.getPose().copy();
        this.occupancyGrid = particle.getOccupancyGrid().copy();
        this.weight = particle.getWeight();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public DirectedPoint getPose() {
        return pose;
    }

    public OccupancyGrid getOccupancyGrid() {
        return occupancyGrid;
    }

    public void updatePose(double speed, double angle) {
        pose.move(speed);

        // offset the position by a gaussian noise
        double dx = Util.gaussianNoise(POSITION_NOISE);
        double dy = Util.gaussianNoise(POSITION_NOISE);
        pose.move(dx, dy);

        double noisyAngle = angle + Util.gaussianNoise(ANGLE_NOISE);
        pose.rotate(noisyAngle);
    }

    public void updateWeight(List<Vector> lidarReadings) {
        double totalWeight = 1.0;

        for (Vector reading : lidarReadings) {
            Vector expectedReading = simulateLidarReading(reading.getDirection());
            double error = Math.abs(reading.getMagnitude() - expectedReading.getMagnitude());
            double probability = Math.exp(-(error * error) / (2 * SENSOR_NOISE * SENSOR_NOISE));
            totalWeight *= probability;
        }

        this.weight = totalWeight;
    }

    private Vector simulateLidarReading(Angle direction) {
        double step = 0.5;
        double distance = 0;

        DirectedPoint sensor = pose.copy();
        sensor.rotate(direction);

        while (distance < Lidar.MAX_DISTANCE) {
            distance += step;
            sensor.move(step);

            if (occupancyGrid.getProbability((int) Math.round(sensor.getX()),
                    (int) Math.round(sensor.getY())) > OCCUPIED_THRESHOLD) {
                break;
            }
        }

        return new Vector(direction, distance);
    }

    public void updateOccupancyGrid(List<Vector> readings) {
        for (Vector reading : readings) {
            DirectedPoint rayEnd = pose.copy();
            rayEnd.rotate(reading.getDirection());
            rayEnd.move(reading.getMagnitude());

            List<MyPoint> freeCells = RayCaster.getCellsAlongRay(pose.getPoint(), rayEnd.getPoint());
            for (MyPoint cell : freeCells) {
                occupancyGrid.decreaseProbability(cell.getX(), cell.getY());
            }

            if (reading.getMagnitude() < Lidar.MAX_DISTANCE) {
                occupancyGrid.increaseProbability(rayEnd.getX(), rayEnd.getY());
            }
        }
    }

    public void draw(Graphics g, OccupancyGrid grid, Color color) {
        int panelWidth = g.getClipBounds().width;
        int panelHeight = g.getClipBounds().height;

        int worldWidth = grid.getWorldWidth();
        int worldHeight = grid.getWorldHeight();

        double widthScale = panelWidth / worldWidth;
        double heightScale = panelHeight / worldHeight;

        int radius = 5;

        // draw the particle
        g.setColor(color);
        int x = (int) Math.round(pose.getX() * widthScale);
        int y = (int) Math.round(pose.getY() * heightScale);
        g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

        // draw the particle's ray
        DirectedPoint end = pose.copy();
        end.move(10);
        int endX = (int) Math.round(end.getX() * widthScale);
        int endY = (int) Math.round(end.getY() * heightScale);
        g.drawLine(x, y, endX, endY);
    }
}