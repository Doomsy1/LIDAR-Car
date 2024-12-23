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

    // Movement variables
    private double leftSpeed = 0;
    private double rightSpeed = 0;

    // Tank dimensions
    private final double tankLength = 150;
    private final double tankWidth = 50;

    private final double driveTireLength = 16;
    private final double driveTireWidth = 10;

    private final double secondaryTireLength = 10;
    private final double secondaryTireWidth = 6;

    // Distance between the left and right wheels
    private final double wheelBase = tankWidth; // Assuming tankWidth represents the distance between wheels

    public Tank(double x, double y, double angle) {
        position = new DirectedPoint(x, y, angle);
    }

    public void setMovementKeys(int leftForward, int leftBackward, int rightForward, int rightBackward) {
        this.leftForward = leftForward;
        this.leftBackward = leftBackward;
        this.rightForward = rightForward;
        this.rightBackward = rightBackward;
    }
    
    public DirectedPoint getPosition() {
        return position;
    }

    public void update(boolean[] keys) {
        // Left side
        if (keys[leftForward] && keys[leftBackward]) { 
            leftSpeed = 0;
        } 
        else if (keys[leftForward]) {
            leftSpeed = maxSpeed;
        } 
        else if (keys[leftBackward]) {
            leftSpeed = -maxSpeed;
        }
        else {
            leftSpeed = 0;
        }
        
        // Right side
        if (keys[rightForward] && keys[rightBackward]) { 
            rightSpeed = 0;
        } 
        else if (keys[rightForward]) {
            rightSpeed = maxSpeed;
        } 
        else if (keys[rightBackward]) {
            rightSpeed = -maxSpeed;
        }
        else {
            rightSpeed = 0;
        }

        // Calculate linear and angular velocities
        double linearVelocity = (leftSpeed + rightSpeed) / 2.0;
        double angularVelocity = (rightSpeed - leftSpeed) / wheelBase;

        // Update position based on current angle
        double deltaX = linearVelocity * Math.cos(position.getAngle().getRadians());
        double deltaY = linearVelocity * Math.sin(position.getAngle().getRadians());

        position.move(deltaX, deltaY);

        // Update orientation
        position.getAngle().rotate(angularVelocity);

        System.out.println("Tank position: " + position.getX() + ", " + position.getY());
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
