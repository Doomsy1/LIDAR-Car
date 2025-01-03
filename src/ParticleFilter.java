/*
 * ParticleFilter.java
 * Ario Barin Ostovary
 * Class for the particle filter
 * Uses particles to represent a possible position and a possible map
 * Uses the particles to estimate the position and map
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ParticleFilter {
    private List<Particle> particles;
    private final int numParticles;

    private static final double INITIAL_POSITION_STDDEV = 5.0;
    private static final double INITIAL_BEARING_STDDEV = 0.05;

    public ParticleFilter(int numParticles) {
        // Initialize particle filter with new occupancy grids

        this.numParticles = numParticles;
        this.particles = new ArrayList<>();

        for (int i = 0; i < numParticles; i++) {
            // startimg with a pose of 0, 0, 0
            DirectedPoint noisyPose = new DirectedPoint(0, 0, 0);
            particles.add(new Particle(noisyPose));
        }
    }

    public void predict(double speed, double rotation) {
        for (Particle particle : particles) {
            particle.updatePose(speed, rotation);
        }
    }

    public void updateWeights(List<Vector> lidarReadings) {
        double totalWeight = 0;

        for (Particle particle : particles) {
            particle.updateWeight(lidarReadings);
            totalWeight += particle.getWeight();
        }

        // normalize weights
        for (Particle particle : particles) {
            particle.setWeight(particle.getWeight() / totalWeight);
        }
    }

    public void resample() {
        List<Particle> newParticles = new ArrayList<>();

        double step = 1.0 / numParticles;
        double r = Util.randomDouble(0, step);
        double cumulativeWeight = 0;
        int index = 0;

        for (int i = 0; i < numParticles; i++) {
            double u = r + i * step;

            while (u > cumulativeWeight && index < numParticles - 1) {
                cumulativeWeight += particles.get(index).getWeight();
                index++;
            }

            Particle selectedParticle = particles.get(index);
            newParticles.add(new Particle(selectedParticle));
        }

        particles = newParticles;
    }

    public DirectedPoint estimatePose() {
        double sumX = 0.0;
        double sumY = 0.0;
        double sumSin = 0.0;
        double sumCos = 0.0;

        for (Particle particle : particles) {
            sumX += particle.getPose().getX();
            sumY += particle.getPose().getY();
            sumSin += Math.sin(particle.getPose().getAngle().getRadians());
            sumCos += Math.cos(particle.getPose().getAngle().getRadians());
        }

        double avgX = sumX / numParticles;
        double avgY = sumY / numParticles;
        double avgAngle = Math.atan2(sumSin, sumCos);

        return new DirectedPoint(avgX, avgY, avgAngle);
    }

    public OccupancyGrid getBestOccupancyGrid() {
        Particle bestParticle = particles.get(0);
        for (Particle particle : particles) {
            if (particle.getWeight() > bestParticle.getWeight()) {
                bestParticle = particle;
            }
        }
        return bestParticle.getOccupancyGrid();
    }

    public void updateGrids(List<Vector> lidarReadings) {
        for (Particle particle : particles) {
            particle.updateOccupancyGrid(lidarReadings);
        }
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void draw(Graphics g) {
        // draw the best grid
        OccupancyGrid bestGrid = getBestOccupancyGrid();
        bestGrid.draw(g);

        // draw the particles positions overlayed on the best grid
        for (Particle particle : particles) {
            particle.draw(g, bestGrid, Color.RED);
        }
    }
}
