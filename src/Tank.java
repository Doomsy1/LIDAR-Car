/*
 * Tank.java
 * Ario Barin Ostovary
 * 
 */

import java.awt.Color;
import java.awt.Graphics;

public class Tank {
    // Position
    private final DirectedPoint position; // Center of the tank, facing forward

    // Movement keys
    private int leftForward, leftBackward, rightForward, rightBackward;

    // Movement Constants
    private final double maxSpeed = 5;
    private final double maxAcceleration = 0.5;
    private final double friction = maxSpeed / (maxSpeed + maxAcceleration);

    // Movement variables
    private double leftSpeed = 0;
    private double rightSpeed = 0;
    private double leftAcceleration = 0;
    private double rightAcceleration = 0;

    // Tank dimensions
    private final double tankLength = 150;
    private final double tankWidth = 50;

    private final double driveTireLength = 16;
    private final double driveTireWidth = 10;

    private final double secondaryTireLength = 10;
    private final double secondaryTireWidth = 6;

    public Tank(double x, double y, double angle) {
        position = new DirectedPoint(x, y, angle);
    }

    public void setMovementKeys(int leftForward, int leftBackward, int rightForward, int rightBackward) {
        this.leftForward = leftForward;
        this.leftBackward = leftBackward;
        this.rightForward = rightForward;
        this.rightBackward = rightBackward;
    }

    public void update(boolean[] keys) {
        // Update acceleration
        leftAcceleration = 0;
        rightAcceleration = 0;

        // Left side
        if (keys[leftForward] && keys[leftBackward]) {} 
        else if (keys[leftForward]) {
            leftAcceleration = maxAcceleration;
        } 
        else if (keys[leftBackward]) {
            leftAcceleration = -maxAcceleration;
        }
        
        // Right side
        if (keys[rightForward] && keys[rightBackward]) {} 
        else if (keys[rightForward]) {
            rightAcceleration = maxAcceleration;
        } 
        else if (keys[rightBackward]) {
            rightAcceleration = -maxAcceleration;
        }

        // Update speed
        leftSpeed += leftAcceleration;
        rightSpeed += rightAcceleration;

        // Apply friction
        leftSpeed *= friction;
        rightSpeed *= friction;
        
        // TODO: check if this is correct
        // Update position
        double wheelBase = tankWidth; // Distance between the two wheels
        double deltaTheta = (rightSpeed - leftSpeed) / wheelBase;

        double distance = (leftSpeed + rightSpeed) / 2.0;

        position.getAngle().rotate(deltaTheta);

        double dx = distance * position.getAngle().getCos();
        double dy = distance * position.getAngle().getSin();

        position.move(dx, dy);
    }

    public void drawTire(Graphics g, DirectedPoint tire, Color color, double length, double width) {
        g.setColor(color);
        Angle angle = tire.getAngle();
        
        double[][] cornerOffsets = {
            {-0.5, -0.5}, // top left
            { 0.5, -0.5}, // top right
            { 0.5,  0.5}, // bottom right
            {-0.5,  0.5}  // bottom left
        };

        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        for (int i = 0; i < 4; i++) {
            double lMult = cornerOffsets[i][0] * length;
            double wMult = cornerOffsets[i][1] * width;

            xPoints[i] = (int) (tire.getX() + 
                lMult * angle.getCos() + 
                wMult * angle.getSin());
            yPoints[i] = (int) (tire.getY() + 
                lMult * angle.getSin() - 
                wMult * angle.getCos());
        }

        g.fillPolygon(xPoints, yPoints, 4);
    }

    public void draw(Graphics g) {
        Angle perpendicularAngle = position.getAngle();
        perpendicularAngle.rotate(Math.PI / 2);

        DirectedPoint middleLeft = position.copy();
        middleLeft.move(tankWidth / 2, perpendicularAngle);

        DirectedPoint middleRight = position.copy();
        middleRight.move(-tankWidth / 2, perpendicularAngle);

        drawTire(g, middleLeft, Color.RED, driveTireLength, driveTireWidth);
        drawTire(g, middleRight, Color.RED, driveTireLength, driveTireWidth);

        DirectedPoint frontLeft = middleLeft.copy();
        frontLeft.move(tankLength / 3);

        DirectedPoint frontRight = middleRight.copy();
        frontRight.move(tankLength / 3);

        drawTire(g, frontLeft, Color.GREEN, secondaryTireLength, secondaryTireWidth);
        drawTire(g, frontRight, Color.GREEN, secondaryTireLength, secondaryTireWidth);

        DirectedPoint backLeft = middleLeft.copy();
        backLeft.move(-tankLength / 3);

        DirectedPoint backRight = middleRight.copy();
        backRight.move(-tankLength / 3);

        drawTire(g, backLeft, Color.BLUE, secondaryTireLength, secondaryTireWidth);
        drawTire(g, backRight, Color.BLUE, secondaryTireLength, secondaryTireWidth);
    }
}


