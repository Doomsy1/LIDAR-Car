/*
 * Particle.java
 * Ario Barin Ostovary
 * Class for a particle in the particle filter
 * Particles are used to represent a possible position and a possible map
 */

import java.util.List;

public class Particle {
    private final DirectedPoint pose;
    private final OccupancyGrid occupancyGrid;

    private double weight;

    private static final double POSITION_NOISE = 2.5;
    private static final double ANGLE_NOISE = 0.1; 

    public Particle(DirectedPoint position, OccupancyGrid occupancyGrid) {
        this.pose = position.copy();
        this.occupancyGrid = occupancyGrid;
        this.weight = 0.0;
    }

    public double getWeight() {
        return weight;
    }

    public OccupancyGrid getOccupancyGrid() {
        return occupancyGrid;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public DirectedPoint getPose() {
        return pose;
    }

    public DirectedPoint getGridPose() {
        int x = occupancyGrid.worldToGridX(pose.getX());
        int y = occupancyGrid.worldToGridY(pose.getY());
        return new DirectedPoint(x, y, pose.getAngle());
    }

    public void updatePose(double speed, double rotation) {
        pose.move(speed);

        // offset the position by a gaussian noise
        double dx = Util.randomGaussian(POSITION_NOISE);
        double dy = Util.randomGaussian(POSITION_NOISE);
        pose.move(dx, dy);

        double deltaAngle = rotation + Util.randomGaussian(ANGLE_NOISE);
        pose.rotate(deltaAngle);
    }

    public void updateWeight(List<MyVector> lidarReadings) {
        double newLogWeight = 0.0;

        for (MyVector reading : lidarReadings) {
            double expectedReading = simulateLidarReading(reading.getDirection());
            double error = reading.getMagnitude() - expectedReading;
            double currentLogWeight = -((error * error) / (2 * 10 * 10));
            newLogWeight += currentLogWeight;
        }

        this.weight = Math.exp(newLogWeight);
    }

    private double simulateLidarReading(Angle direction) {
        DirectedPoint sensor = pose.copy();
        sensor.rotate(direction);
        sensor.move(Lidar.MAX_DISTANCE);

        int startX = occupancyGrid.worldToGridX(pose.getX());
        int startY = occupancyGrid.worldToGridY(pose.getY());
        int endX = occupancyGrid.worldToGridX(sensor.getX());
        int endY = occupancyGrid.worldToGridY(sensor.getY());

        List<MyPoint> cells = RayCaster.getCellsAlongRay(startX, startY, endX, endY);

        for (MyPoint cell : cells) {
            if (cell.getX() < 0 || cell.getY() < 0 || cell.getX() >= occupancyGrid.getWidth() || cell.getY() >= occupancyGrid.getHeight()) {
                continue; // might be "break;"
            }

            if (occupancyGrid.isOccupiedCell((int) cell.getX(), (int) cell.getY())) {
                double obstacleX = occupancyGrid.gridToWorldX(cell.getX());
                double obstacleY = occupancyGrid.gridToWorldY(cell.getY());

                double dx = obstacleX - pose.getX();
                double dy = obstacleY - pose.getY();

                return Math.sqrt(dx * dx + dy * dy);
            }
        }

        return Lidar.MAX_DISTANCE;
    }

    public Particle copy() {
        Particle newParticle = new Particle(pose.copy(), occupancyGrid);
        newParticle.setWeight(weight);
        return newParticle;
    }
}