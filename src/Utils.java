import java.util.Random;

public class Utils {
    private static final Random random = new Random();

    public static double randomGaussian(double mean, double std) {
        return random.nextGaussian() * std + mean;
    }

    public static double randomUniform(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }
}
