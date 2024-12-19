
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class World {
    private static final int WIDTH = 800, HEIGHT = 600;

    private LiCar licar;
    private DirectedPoint carPosition;

    public World() {
        this.licar = new LiCar(this);
        this.carPosition = new DirectedPoint(WIDTH / 2, HEIGHT / 2, 0);

        this.licar.setMovementKeys(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);
    }

    public double readLidar(Angle lidarBearing) {
        DirectedPoint sensor = carPosition.copy();
        sensor.rotate(lidarBearing);
    }

    public void update(boolean[] keysPressed) {
        licar.update(keysPressed);
        double speed = licar.getSpeed();
        double rotationSpeed = licar.getRotationSpeed();
        carPosition.move(speed);
        carPosition.rotate(rotationSpeed);
    }

    // Static drawing of the full world
    public void drawStatic(Graphics g, boolean showMap, boolean showScansOnMap, boolean showRaysOnMap) {
        if (showMap) {
            Mask.draw(g);
        }

        licar.draw(g, carPosition, showRaysOnMap, showScansOnMap);

        if (showScansOnMap) {
            licar.drawLidarView(g, carPosition, showRaysOnMap);
        }
    }

    // Dynamic drawing of the car and its lidar
    public void drawDynamic(Graphics g, boolean showLidar, boolean showRays, boolean showScans) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);

        g2d.setClip(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        g2d.translate(g.getClipBounds().width / 2, g.getClipBounds().height / 2);
        g2d.scale(1, 1);
        g2d.rotate(-carPosition.getAngle().getAngle());

        licar.draw(g2d, carPosition, showRays, showScans);
    }
}
