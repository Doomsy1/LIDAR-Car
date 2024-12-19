public class Map {
    private int width;
    private int height;
    private boolean[][] grid;

    private static final double RESIZE_FACTOR = 1.5;

    public Map(double width, double height) {
        this.width = (int) width;
        this.height = (int) height;
        this.grid = new boolean[this.width][this.height];
    }

    public void resize(LiCar car) {
    }

    public void update(LiCar car, Lidar lidar) {
    }
}
