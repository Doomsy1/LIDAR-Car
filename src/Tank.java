/*
 * Tank.java
 * Ario Barin Ostovary
 * 
 */

import java.awt.Color;
import java.awt.Graphics;

public class Tank {
    private final DirectedPoint position;
    private int moveForward, moveBackward, rotateAntiClockwise, rotateClockwise;
    private final double maxSpeed;
    private final double maxAcceleration;
    private final double friction;
    private double speed;
    private double acceleration;
    private final double maxRotationSpeed;
    private final double maxAngularAcceleration;
    private final double rotationFriction;
    private double rotationSpeed;
    private double rotationAcceleration;
    private final double length;
    private final double width;
    private final double tireLength;
    private final double tireWidth;

    public Tank(int x, int y) {
        position = new DirectedPoint(x, y, 0);
        this.maxSpeed = 5;
        this.maxAcceleration = 0.5;
        this.friction = maxSpeed / (maxSpeed + maxAcceleration);
        speed = 0;
        acceleration = 0;
        this.maxRotationSpeed = Math.toRadians(1.5);
        this.maxAngularAcceleration = Math.toRadians(0.4);
        this.rotationFriction = maxRotationSpeed / (maxRotationSpeed + maxAngularAcceleration);
        rotationSpeed = 0;
        rotationAcceleration = 0;
        this.length = 150;
        this.width = 50;
        this.tireLength = 10;
        this.tireWidth = 5;
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
        acceleration = 0;
        if (keys[moveForward] && keys[moveBackward]) {
        } else if (keys[moveForward]) {
            acceleration = maxAcceleration;
        } else if (keys[moveBackward]) {
            acceleration = -maxAcceleration;
        }

        speed += acceleration;
        speed *= friction;

        rotationAcceleration = 0;
        if (keys[rotateAntiClockwise] && keys[rotateClockwise]) {
        } else if (keys[rotateAntiClockwise]) {
            rotationAcceleration = -maxAngularAcceleration;
        } else if (keys[rotateClockwise]) {
            rotationAcceleration = maxAngularAcceleration;
        }

        rotationSpeed += rotationAcceleration;
        rotationSpeed *= rotationFriction;

        position.rotate(rotationSpeed);
        position.move(speed);
    }

    public void drawTire(Graphics g, DirectedPoint tire, Color color) {
        g.setColor(color);
        Angle angle = tire.getAngle();
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

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
        middleLeft.move(width / 2, perpendicularAngle);

        DirectedPoint middleRight = position.copy();
        middleRight.move(-width / 2, perpendicularAngle);

        drawTire(g, middleLeft, Color.RED);
        drawTire(g, middleRight, Color.RED);

        DirectedPoint frontLeft = middleLeft.copy();
        frontLeft.move(length / 3);

        DirectedPoint frontRight = middleRight.copy();
        frontRight.move(length / 3);

        drawTire(g, frontLeft, Color.GREEN);
        drawTire(g, frontRight, Color.GREEN);

        DirectedPoint backLeft = middleLeft.copy();
        backLeft.move(-length / 3);

        DirectedPoint backRight = middleRight.copy();
        backRight.move(-length / 3);

        drawTire(g, backLeft, Color.BLUE);
        drawTire(g, backRight, Color.BLUE);
    }
}