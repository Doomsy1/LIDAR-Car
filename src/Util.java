/*
 * Util.java
 * Ario Barin Ostovary
 * Class for utility functions
 */

public class Util {
    private static final java.util.Random random = new java.util.Random();

    public static double gaussianNoise(double standardDeviation) {
        return random.nextGaussian() * standardDeviation;
    }

    public static double randomDouble(double start, double end) {
        return start + (end - start) * random.nextDouble();
    }
}