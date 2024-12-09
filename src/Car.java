/*
 * Car.java
 * Ario Barin Ostovary
 * 
 */

import java.awt.Color;
import java.awt.Graphics;

public class Car {
    private final Vector frontTire, backTire;

    // Movement keys
    private int moveForward, moveBackward, rotateAntiClockwise, rotateClockwise;

    // Movement constants
    private final double maxSpeed; // in pixels per second
    private final double maxAcceleration; // in pixels per second squared
    private final double friction; // to keep the car within maxSpeed

    // Movement variables
    private double speed; // in pixels per second
    private double acceleration; // in pixels per second squared

    // Rotation constants
    private final double maxRotationSpeed; // in radians per frame
    private final double maxAngularAcceleration; // in radians per frame squared
    private final double maxSteeringAngle; // max angle of rotation in radians
    private final double rotationFriction; // constant to keep the car within maxRotationSpeed

    // Rotation variables
    private double steeringAngle; // in radians
    private double rotationSpeed; // in radians per frame
    private double rotationAcceleration; // in radians per frame squared
    private final double straightenFactor; // constant to straighten the car

    // Dimensions
    private final double length; // in pixels
    private final double width; // in pixels
    private final double tireLength; // in pixels
    private final double tireWidth; // in pixels

    // Heading
    private double heading; // in radians

    public Car(int x, int y) {
        speed = 0;
        acceleration = 0;

        
        // Movement constants
        this.maxSpeed = 25;
        this.maxAcceleration = 0.5;
        this.friction = maxSpeed / (maxSpeed + maxAcceleration);
        
        // Rotation constants
        this.maxRotationSpeed = Math.toRadians(5);
        this.maxAngularAcceleration = Math.toRadians(2);
        this.maxSteeringAngle = Math.toRadians(15);
        this.rotationFriction = maxRotationSpeed / (maxRotationSpeed + maxAngularAcceleration);
        
        // Rotation variables
        this.steeringAngle = 0;
        this.rotationSpeed = 0;
        this.rotationAcceleration = 0;
        this.straightenFactor = 0.5;
        
        // Dimensions
        this.length = 40;
        this.width = 100;
        this.tireLength = 15;
        this.tireWidth = 7;


        this.heading = -Math.PI / 2;

        frontTire = new Vector(x, y, heading);
        backTire = frontTire.copy();
        frontTire.move(length / 2, heading);
        backTire.move(-length / 2, heading);
    }

    public int getX() {
        // Return the middle of front tire and back tire
        return (int) ((frontTire.getX() + backTire.getX()) / 2);
    }

    public int getY() {
        // Return the middle of front tire and back tire
        return (int) ((frontTire.getY() + backTire.getY()) / 2);
    }

    public void setMovementKeys(int moveForward, int moveBackward, int rotateAntiClockwise, int rotateClockwise) {
        this.moveForward = moveForward;
        this.moveBackward = moveBackward;
        this.rotateAntiClockwise = rotateAntiClockwise;
        this.rotateClockwise = rotateClockwise;
    }

    public void update(boolean[] keys) {
        // Handle acceleration
        acceleration = 0;
        if (keys[moveForward] && keys[moveBackward]) {} 
        else if (keys[moveForward]) { acceleration = maxAcceleration; } 
        else if (keys[moveBackward]) { acceleration = -maxAcceleration; }

        // Update speed with acceleration and apply friction
        speed += acceleration;
        speed *= friction;


        // Handle steering
        rotationAcceleration = 0;
        if (keys[rotateAntiClockwise] && keys[rotateClockwise]) {}
        else if (keys[rotateAntiClockwise]) {
            rotationAcceleration = -maxAngularAcceleration;
        } else if (keys[rotateClockwise]) {
            rotationAcceleration = maxAngularAcceleration;
        } else {
            // accelerate towards zero rotation speed
            steeringAngle *= straightenFactor;
        }


        // Update rotationSpeed with acceleration and apply friction
        rotationSpeed += rotationAcceleration;
        rotationSpeed *= rotationFriction;

        // Update steeringAngle with rotationSpeed and apply straighten factor
        steeringAngle += rotationSpeed;
        
        // Clamp steeringAngle to maxRotation
        steeringAngle = Math.max(Math.min(steeringAngle, maxSteeringAngle), -maxSteeringAngle);
        

        // Update heading based on steering angle and speed
        if (Math.abs(steeringAngle) > 0 && Math.abs(speed) > 0) { // Avoid division by zero
            double turningRadius = length / Math.tan(steeringAngle);
            double angularVelocity = speed / turningRadius;
            heading += angularVelocity;
        }

        // Normalize heading to [0, 360)
        heading = (heading + 2 * Math.PI) % (2 * Math.PI);

        // Update tire angles based on new heading
        backTire.setAngle(heading);
        frontTire.setAngle(heading + steeringAngle);

        // Calculate new positions
        double dx = speed * Math.cos(heading);
        double dy = speed * Math.sin(heading);

        // Update back tire position
        backTire.moveXY(dx, dy);

        // Update front tire position based on length and heading
        frontTire.setXY(
            backTire.getX() + length * Math.cos(heading),
            backTire.getY() + length * Math.sin(heading));

        System.out.println("Speed: " + speed + " Acceleration: " + acceleration + " RotationSpeed: " + rotationSpeed + " RotationAcceleration: " + rotationAcceleration);
    }

    private void drawTire(Graphics g, Vector tire, Color color) {
        // Tire is the center of the tire
        // Draw a rectangle around the tire (top down view) - rotated by the tire's angle
        g.setColor(color);
        
        // Calculate the corners of the rectangle
        double angle = tire.getAngle();
        
        // Calculate the four corners of the rectangle
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        
        // Top left corner
        xPoints[0] = (int) (tire.getX() - (tireLength/2) * Math.cos(angle) - (tireWidth/2) * Math.sin(angle));
        yPoints[0] = (int) (tire.getY() - (tireLength/2) * Math.sin(angle) + (tireWidth/2) * Math.cos(angle));
        
        // Top right corner
        xPoints[1] = (int) (tire.getX() + (tireLength/2) * Math.cos(angle) - (tireWidth/2) * Math.sin(angle));
        yPoints[1] = (int) (tire.getY() + (tireLength/2) * Math.sin(angle) + (tireWidth/2) * Math.cos(angle));
        
        // Bottom right corner
        xPoints[2] = (int) (tire.getX() + (tireLength/2) * Math.cos(angle) + (tireWidth/2) * Math.sin(angle));
        yPoints[2] = (int) (tire.getY() + (tireLength/2) * Math.sin(angle) - (tireWidth/2) * Math.cos(angle));
        
        // Bottom left corner
        xPoints[3] = (int) (tire.getX() - (tireLength/2) * Math.cos(angle) + (tireWidth/2) * Math.sin(angle));
        yPoints[3] = (int) (tire.getY() - (tireLength/2) * Math.sin(angle) - (tireWidth/2) * Math.cos(angle));
        
        // Draw the filled polygon
        g.fillPolygon(xPoints, yPoints, 4);
    }

    public void draw(Graphics g) {
        double perpendicular;

        // Front tires
        perpendicular = frontTire.getAngle() + Math.PI / 2;
        
        // Front left tire
        Vector frontLeftTire = frontTire.copy();
        frontLeftTire.move(width / 2, perpendicular);
        
        // Front right tire
        Vector frontRightTire = frontTire.copy();
        frontRightTire.move(width / 2, perpendicular + Math.PI);
        
        // Axel between front tires
        g.setColor(Color.ORANGE);
        g.drawLine((int) frontLeftTire.getX(), (int) frontLeftTire.getY(), (int) frontRightTire.getX(), (int) frontRightTire.getY());
        
        // Draw front tires
        drawTire(g, frontLeftTire, Color.BLUE);
        drawTire(g, frontRightTire, Color.BLUE);
        

        // Back tires
        perpendicular = backTire.getAngle() + Math.PI / 2;

        // Back left tire
        Vector backLeftTire = backTire.copy();
        backLeftTire.move(width / 2, perpendicular);
        
        // Back right tire
        Vector backRightTire = backTire.copy();
        backRightTire.move(width / 2, perpendicular + Math.PI);
        
        // Axel between back tires
        g.setColor(Color.ORANGE);
        g.drawLine((int) backLeftTire.getX(), (int) backLeftTire.getY(), (int) backRightTire.getX(), (int) backRightTire.getY());
        
        // Draw back tires
        drawTire(g, backLeftTire, Color.RED);
        drawTire(g, backRightTire, Color.RED);


        // Draw the line between the front and back tires
        g.setColor(Color.GREEN);
        g.drawLine((int) frontTire.getX(), (int) frontTire.getY(), (int) backTire.getX(), (int) backTire.getY());

        // Draw the heading of the front tire as an arrow
        g.setColor(Color.BLACK);
        Vector headingPoint = frontTire.copy();
        headingPoint.move(length / 2);
        
        // // Draw main line
        // g.drawLine((int) frontTire.getX(), (int) frontTire.getY(), (int) headingPoint.getX(), (int) headingPoint.getY());
        
        // // Draw arrow head
        // Vector arrowLeft = headingPoint.copy();
        // Vector arrowRight = headingPoint.copy();
        // arrowLeft.move(10, frontTire.getAngle() + Math.PI / 4 * 3);
        // arrowRight.move(10, frontTire.getAngle() - Math.PI / 4 * 3);
        
        // g.drawLine((int) headingPoint.getX(), (int) headingPoint.getY(), (int) arrowLeft.getX(), (int) arrowLeft.getY());
        // g.drawLine((int) headingPoint.getX(), (int) headingPoint.getY(), (int) arrowRight.getX(), (int) arrowRight.getY());
    }
}
