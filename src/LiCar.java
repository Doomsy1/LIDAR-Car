/*
 * LiCar.java
 * Ario Barin Ostovary & Kevin Dang
 * 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class LiCar {
    // Parts of the car
    private final Lidar lidar; // Lidar range finder
    private final Tank tank; // Tank that the lidar is mounted on

    // Parameters
    private static final int lidarScansPerFrame = 50;

    // Data
    private final ArrayList<Vector> lidarReadings;

    public LiCar(double x, double y, double angle) {
        this.lidar = new Lidar();
        this.tank = new Tank(x, y, angle);
        this.lidarReadings = new ArrayList<>();
    }

    public void setMovementKeys(int leftForward, int leftBackward, int rightForward, int rightBackward) {
        this.tank.setMovementKeys(leftForward, leftBackward, rightForward, rightBackward);
    }

    public void update(boolean[] keysPressed) {
        tank.update(keysPressed);
        lidarReadings.clear();
        for (int i = 0; i < lidarScansPerFrame; i++) {
            lidar.update();
            double reading = lidar.read(tank.getPosition());
            if (reading != -1) {
                lidarReadings.add(new Vector(lidar.getBearing(), reading));
            }
        }
    }
}

