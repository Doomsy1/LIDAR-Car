/*
 * Car.java
 * Ario Barin Ostovary & Kevin Dang
 * .
 */

import java.awt.Color;
import java.awt.Graphics;

public class Car {
    private final Vector frontTire, backTire;
    private int moveForward;
    private int moveBackward;
    private int rotateAntiClockwise;
    private int rotateClockwise;

    private double speed; // in pixels per frame
    private double acceleration; // in pixels per frame per frame

    private final double maxSpeed;
    private final double maxAcceleration;
    
    private final double LENGTH; // in pixels

    private static final double MAX_ANGLE = 30; // car can have its front tires at most 30 degrees off from the back tires

    public Car(int x, int y, double maxSpeed, double maxAcceleration, double length) {
        speed = 0;
        acceleration = 0;

        frontTire = new Vector(x, y - length / 2, 90);
        backTire = new Vector(x, y + length / 2, 90);

        this.maxSpeed = maxSpeed;
        this.maxAcceleration = maxAcceleration;
        LENGTH = length;
    }

    public void setMovementKeys(int moveForward, int moveBackward, int rotateAntiClockwise, int rotateClockwise) {
        this.moveForward = moveForward;
        this.moveBackward = moveBackward;
        this.rotateAntiClockwise = rotateAntiClockwise;
        this.rotateClockwise = rotateClockwise;
    }
    
    public void update() {
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);

        System.out.println(frontTire.getX() + " " + frontTire.getY());

        Vector frontTireEnd = frontTire.copy();
        frontTireEnd.move(LENGTH/2);

        Vector backTireEnd = backTire.copy();
        backTireEnd.move(LENGTH/2);

        g.drawLine((int) frontTire.getX(), (int) frontTire.getY(),
                (int) frontTireEnd.getX(), (int) frontTireEnd.getY());
        g.drawLine((int) backTire.getX(), (int) backTire.getY(),
                (int) backTireEnd.getX(), (int) backTireEnd.getY());
    }
}
