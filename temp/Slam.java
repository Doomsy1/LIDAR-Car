public class Slam {
    private final LiCar car;
    private final Lidar lidar;
    private final Map map; // occupancy grid
    private static final double START_WIDTH = 100;
    private static final double START_HEIGHT = 100;

    public Slam(LiCar car, Lidar lidar) {
        this.car = car;
        this.lidar = lidar;

        this.map = new Map(START_WIDTH, START_HEIGHT);
    }

    public void update() {
    }
}
