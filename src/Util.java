/*
 * Util.java
 * Ario Barin Ostovary
 * Class for utility functions
 */

import java.util.concurrent.ThreadLocalRandom;

public class Util {
    public static double randomGaussian(double standardDeviation) {
        return ThreadLocalRandom.current().nextGaussian() * standardDeviation;
    }

    public static double randomDouble(double start, double end) {
        return ThreadLocalRandom.current().nextDouble(start, end);
    }

    public static double logitToProb(double logit) {
        if (logit > 0) {
            // avoid overflow
            return 1.0 / (1.0 + Math.exp(-logit));
        } else {
            // avoid underflow
            return Math.exp(logit) / (1.0 + Math.exp(logit));
        }
    }

    public static double probToLogit(double prob) {
        if (prob <= 0.0 || prob >= 1.0) {
            throw new IllegalArgumentException("Probability must be between 0 and 1 exclusive");
        }
        return Math.log(prob / (1.0 - prob));
    }
}