/*
 * Tank.java
 * Ario Barin Ostovary
 * 
 */

import java.awt.Color;
import java.awt.Graphics;

public class Tank {
    // Tank position
    private final DirectedPoint position;

    // Movement keys
    private int moveForward, moveBackward, rotateAntiClockwise, rotateClockwise;

    // Movement Constants
    private final double maxSpeed;
    private final double maxAcceleration;
    private final double friction;

    // Movement variables
    private double speed;
    private double acceleration;

    // Rotation Constants
    private final double maxRotationSpeed;
    private final double maxAngularAcceleration;
    private final double rotationFriction;

    // Rotation variables
    private double rotationSpeed;
    private double rotationAcceleration;

    // Tank dimensions
    private final double tankLength;
    private final double tankWidth;

    private final double driveTireLength;
    private final double driveTireWidth;

    private final double secondaryTireLength;
    private final double secondaryTireWidth;

    public Tank(int x, int y) {
        position = new DirectedPoint(x, y, 0);

        // Movement Constants
        this.maxSpeed = 5;
        this.maxAcceleration = 0.5;
        this.friction = maxSpeed / (maxSpeed + maxAcceleration);

        // Movement variables
        speed = 0;
        acceleration = 0;

        // Rotation Constants
        this.maxRotationSpeed = Math.toRadians(1.5);
        this.maxAngularAcceleration = Math.toRadians(0.4);
        this.rotationFriction = maxRotationSpeed / (maxRotationSpeed + maxAngularAcceleration);

        // Rotation variables
        rotationSpeed = 0;
        rotationAcceleration = 0;

        // Tank dimensions
        this.tankLength = 150;
        this.tankWidth = 50;

        this.driveTireLength = 10;
        this.driveTireWidth = 5;
        
        this.secondaryTireLength = 10;
        this.secondaryTireWidth = 5;
    }

    public DirectedPoint getPosition() {
        return position;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }

    public void setMovementKeys(int moveForward, int moveBackward, int rotateAntiClockwise, int rotateClockwise) {
        this.moveForward = moveForward;
        this.moveBackward = moveBackward;
        this.rotateAntiClockwise = rotateAntiClockwise;
        this.rotateClockwise = rotateClockwise;
    }

    public void update(boolean[] keys) {
        // Update acceleration
        acceleration = 0;
        if (keys[moveForward] && keys[moveBackward]) {
        } else if (keys[moveForward]) {
            acceleration = maxAcceleration;
        } else if (keys[moveBackward]) {
            acceleration = -maxAcceleration;
        }

        // Update speed based on acceleration
        speed += acceleration;
        speed *= friction;

        // Update rotation acceleration
        rotationAcceleration = 0;
        if (keys[rotateAntiClockwise] && keys[rotateClockwise]) {
        } else if (keys[rotateAntiClockwise]) {
            rotationAcceleration = -maxAngularAcceleration;
        } else if (keys[rotateClockwise]) {
            rotationAcceleration = maxAngularAcceleration;
        }

        // Update rotation speed based on rotation acceleration
        rotationSpeed += rotationAcceleration;
        rotationSpeed *= rotationFriction;

        // Update position based on speed and rotation speed
        position.rotate(rotationSpeed);
        position.move(speed);
    }

    public void drawTire(Graphics g, DirectedPoint tire, Color color) {
        g.setColor(color);
        Angle angle = tire.getAngle();
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        // Calculate the points of the tire
        xPoints[0] = (int) (tire.getX() - (driveTireLength / 2) * angle.getCos() - (driveTireWidth / 2) * angle.getSin());
        yPoints[0] = (int) (tire.getY() - (driveTireLength / 2) * angle.getSin() + (driveTireWidth / 2) * angle.getCos());

        xPoints[1] = (int) (tire.getX() + (driveTireLength / 2) * angle.getCos() - (driveTireWidth / 2) * angle.getSin());
        yPoints[1] = (int) (tire.getY() + (driveTireLength / 2) * angle.getSin() + (driveTireWidth / 2) * angle.getCos());

        xPoints[2] = (int) (tire.getX() + (driveTireLength / 2) * angle.getCos() + (driveTireWidth / 2) * angle.getSin());
        yPoints[2] = (int) (tire.getY() + (driveTireLength / 2) * angle.getSin() - (driveTireWidth / 2) * angle.getCos());

        xPoints[3] = (int) (tire.getX() - (driveTireLength / 2) * angle.getCos() + (driveTireWidth / 2) * angle.getSin());
        yPoints[3] = (int) (tire.getY() - (driveTireLength / 2) * angle.getSin() - (driveTireWidth / 2) * angle.getCos());

        g.fillPolygon(xPoints, yPoints, 4);
    }

    public void draw(Graphics g) {
        Angle perpendicularAngle = position.getAngle();
        perpendicularAngle.rotate(Math.PI / 2);

        DirectedPoint middleLeft = position.copy();
        middleLeft.move(tankWidth / 2, perpendicularAngle);

        DirectedPoint middleRight = position.copy();
        middleRight.move(-tankWidth / 2, perpendicularAngle);

        drawTire(g, middleLeft, Color.RED);
        drawTire(g, middleRight, Color.RED);

        DirectedPoint frontLeft = middleLeft.copy();
        frontLeft.move(tankLength / 3);

        DirectedPoint frontRight = middleRight.copy();
        frontRight.move(tankLength / 3);

        drawTire(g, frontLeft, Color.GREEN);
        drawTire(g, frontRight, Color.GREEN);

        DirectedPoint backLeft = middleLeft.copy();
        backLeft.move(-tankLength / 3);

        DirectedPoint backRight = middleRight.copy();
        backRight.move(-tankLength / 3);

        drawTire(g, backLeft, Color.BLUE);
        drawTire(g, backRight, Color.BLUE);
    }
}