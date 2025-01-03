/*
 * Tank.java
 * Ario Barin Ostovary
 * Class for the tank
 * Contains the position, movement, and drawing of the tank
 */

import java.awt.Color;
import java.awt.Graphics;

public class Tank {
    // Tank position
    private final DirectedPoint position;

    // Movement keys
    public final int moveForward;
    public final int moveBackward;
    public final int rotateAntiClockwise;
    public final int rotateClockwise;

    // Movement Constants
    private final double MAX_SPEED = 5;
    private final double MAX_ACCELERATION = 1;
    private final double FRICTION = MAX_SPEED / (MAX_SPEED + MAX_ACCELERATION);

    // Movement variables
    private double speed;
    private double acceleration;

    // Rotation Constants
    private final double MAX_ROTATION_SPEED = Math.toRadians(3);
    private final double MAX_ANGUALR_ACCELERATION = Math.toRadians(0.7);
    private final double ROTATION_FRICTION = MAX_ROTATION_SPEED / (MAX_ROTATION_SPEED + MAX_ANGUALR_ACCELERATION);

    // Rotation variables
    private double rotationSpeed;
    private double rotationAcceleration;

    // Tank dimensions
    private final double TANK_LENGTH = 50;
    private final double TANK_WIDTH = 20;

    private final double DRIVE_TIRE_LENGTH = 10;
    private final double DRIVE_TIRE_WIDTH = 5;

    private final double SECONDARY_TIRE_LENGTH = 8;
    private final double SECONDARY_TIRE_WIDTH = 4;

    // Noise constants
    private final double SPEED_NOISE = 0.05;
    private final double ROTATION_NOISE = 0.0005;

    public Tank(int x, int y, double angle, int moveForward, int moveBackward, int rotateAntiClockwise,
            int rotateClockwise) {
        this.moveForward = moveForward;
        this.moveBackward = moveBackward;
        this.rotateAntiClockwise = rotateAntiClockwise;
        this.rotateClockwise = rotateClockwise;

        position = new DirectedPoint(x, y, angle);

        // Movement variables
        speed = 0;
        acceleration = 0;

        // Rotation variables
        rotationSpeed = 0;
        rotationAcceleration = 0;
    }

    public DirectedPoint getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed + Util.gaussianNoise(SPEED_NOISE);
    }

    public double getRotationSpeed() {
        return rotationSpeed + Util.gaussianNoise(ROTATION_NOISE);
    }

    public void update(boolean[] keys) {
        // Update acceleration
        acceleration = 0;
        if (keys[moveForward] && keys[moveBackward]) {
        } else if (keys[moveForward]) {
            acceleration = MAX_ACCELERATION;
        } else if (keys[moveBackward]) {
            acceleration = -MAX_ACCELERATION;
        }

        // Update speed based on acceleration
        speed += acceleration;
        speed *= FRICTION;

        // Update rotation acceleration
        rotationAcceleration = 0;
        if (keys[rotateAntiClockwise] && keys[rotateClockwise]) {
        } else if (keys[rotateAntiClockwise]) {
            rotationAcceleration = -MAX_ANGUALR_ACCELERATION;
        } else if (keys[rotateClockwise]) {
            rotationAcceleration = MAX_ANGUALR_ACCELERATION;
        }

        // Update rotation speed based on rotation acceleration
        rotationSpeed += rotationAcceleration;
        rotationSpeed *= ROTATION_FRICTION;

        // Update position based on speed and rotation speed
        position.rotate(rotationSpeed);
        position.move(speed);
    }

    public void drawTire(Graphics g, DirectedPoint tire, Color color, boolean isDriveTire) {
        g.setColor(color);
        Angle angle = tire.getAngle();
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        // Use appropriate tire dimensions based on type
        double tireLength = isDriveTire ? DRIVE_TIRE_LENGTH : SECONDARY_TIRE_LENGTH;
        double tireWidth = isDriveTire ? DRIVE_TIRE_WIDTH : SECONDARY_TIRE_WIDTH;

        // Calculate the points of the tire
        xPoints[0] = (int) (tire.getX() - (tireLength / 2) * angle.getCos() - (tireWidth / 2) * angle.getSin());
        yPoints[0] = (int) (tire.getY() - (tireLength / 2) * angle.getSin() + (tireWidth / 2) * angle.getCos());

        xPoints[1] = (int) (tire.getX() + (tireLength / 2) * angle.getCos() - (tireWidth / 2) * angle.getSin());
        yPoints[1] = (int) (tire.getY() + (tireLength / 2) * angle.getSin() + (tireWidth / 2) * angle.getCos());

        xPoints[2] = (int) (tire.getX() + (tireLength / 2) * angle.getCos() + (tireWidth / 2) * angle.getSin());
        yPoints[2] = (int) (tire.getY() + (tireLength / 2) * angle.getSin() - (tireWidth / 2) * angle.getCos());

        xPoints[3] = (int) (tire.getX() - (tireLength / 2) * angle.getCos() + (tireWidth / 2) * angle.getSin());
        yPoints[3] = (int) (tire.getY() - (tireLength / 2) * angle.getSin() - (tireWidth / 2) * angle.getCos());

        g.fillPolygon(xPoints, yPoints, 4);
    }

    public void draw(Graphics g) {
        Angle perpendicularAngle = position.getAngle();
        perpendicularAngle.rotate(Math.PI / 2);

        DirectedPoint middleLeft = position.copy();
        middleLeft.move(TANK_WIDTH / 2, perpendicularAngle);

        DirectedPoint middleRight = position.copy();
        middleRight.move(-TANK_WIDTH / 2, perpendicularAngle);

        drawTire(g, middleLeft, Color.RED, true);
        drawTire(g, middleRight, Color.RED, true);

        DirectedPoint frontLeft = middleLeft.copy();
        frontLeft.move(TANK_LENGTH / 3);

        DirectedPoint frontRight = middleRight.copy();
        frontRight.move(TANK_LENGTH / 3);

        drawTire(g, frontLeft, Color.GREEN, false);
        drawTire(g, frontRight, Color.GREEN, false);

        DirectedPoint backLeft = middleLeft.copy();
        backLeft.move(-TANK_LENGTH / 3);

        DirectedPoint backRight = middleRight.copy();
        backRight.move(-TANK_LENGTH / 3);

        drawTire(g, backLeft, Color.BLUE, false);
        drawTire(g, backRight, Color.BLUE, false);
    }
}