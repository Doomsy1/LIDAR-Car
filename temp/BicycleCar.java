/*
 * BicycleCar.java
 * Ario Barin Ostovary
 *
 */

import java.awt.Color;
import java.awt.Graphics;

public class BicycleCar {
    private final DirectedPoint frontTire, backTire;

    private int moveForward, moveBackward, rotateAntiClockwise, rotateClockwise;

    private final double maxSpeed;
    private final double maxAcceleration;
    private final double friction;

    private double speed;
    private double acceleration;

    private final double maxRotationSpeed;
    private final double maxAngularAcceleration;
    private final double maxSteeringAngle;
    private final double rotationFriction;

    private double steeringAngle;
    private double rotationSpeed;
    private double rotationAcceleration;
    private final double straightenFactor;

    private final double length;
    private final double width;
    private final double tireLength;
    private final double tireWidth;

    private Angle heading;

    private static final class Constants {
        static final double MAX_SPEED = 25;
        static final double MAX_ACCELERATION = 0.5;
        static final double MAX_ROTATION_SPEED = Math.toRadians(5);
        static final double MAX_ANGULAR_ACCELERATION = Math.toRadians(2);
        static final double MAX_STEERING_ANGLE = Math.toRadians(15);
        static final double STRAIGHTEN_FACTOR = 0.5;
        static final double LENGTH = 100;
        static final double WIDTH = 60;
        static final double TIRE_LENGTH = 15;
        static final double TIRE_WIDTH = 7;
    }

    public BicycleCar(int x, int y) {
        speed = 0;
        acceleration = 0;

        this.maxSpeed = Constants.MAX_SPEED;
        this.maxAcceleration = Constants.MAX_ACCELERATION;
        this.friction = maxSpeed / (maxSpeed + maxAcceleration);

        this.maxRotationSpeed = Constants.MAX_ROTATION_SPEED;
        this.maxAngularAcceleration = Constants.MAX_ANGULAR_ACCELERATION;
        this.maxSteeringAngle = Constants.MAX_STEERING_ANGLE;
        this.rotationFriction = maxRotationSpeed / (maxRotationSpeed + maxAngularAcceleration);

        this.steeringAngle = 0;
        this.rotationSpeed = 0;
        this.rotationAcceleration = 0;
        this.straightenFactor = Constants.STRAIGHTEN_FACTOR;

        this.length = Constants.LENGTH;
        this.width = Constants.WIDTH;
        this.tireLength = Constants.TIRE_LENGTH;
        this.tireWidth = Constants.TIRE_WIDTH;

        // Start facing right, as Tank did (0 angle means facing right)
        this.heading = new Angle(0);

        frontTire = new DirectedPoint(x, y, heading);
        backTire = frontTire.copy();
        frontTire.move(length / 2, heading);
        backTire.move(-length / 2, heading);
    }

    public DirectedPoint getPosition() {
        double midX = (frontTire.getX() + backTire.getX()) / 2.0;
        double midY = (frontTire.getY() + backTire.getY()) / 2.0;
        return new DirectedPoint(midX, midY, heading.getAngle());
    }

    public int getX() {
        return (int) ((frontTire.getX() + backTire.getX()) / 2);
    }

    public int getY() {
        return (int) ((frontTire.getY() + backTire.getY()) / 2);
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
        } else {
            steeringAngle *= straightenFactor;
        }

        rotationSpeed += rotationAcceleration;
        rotationSpeed *= rotationFriction;

        steeringAngle += rotationSpeed;
        steeringAngle = Math.max(Math.min(steeringAngle, maxSteeringAngle), -maxSteeringAngle);

        if (Math.abs(steeringAngle) > 0 && Math.abs(speed) > 0) {
            double turningRadius = length / Math.tan(steeringAngle);
            double angularVelocity = speed / turningRadius;
            heading.rotate(angularVelocity);
        }

        backTire.setAngle(heading.getAngle());
        frontTire.setAngle(heading.getAngle() + steeringAngle);

        double dx = speed * heading.getCos();
        double dy = speed * heading.getSin();

        backTire.move(dx, dy);
        frontTire.setXY(
                backTire.getX() + length * heading.getCos(),
                backTire.getY() + length * heading.getSin());
    }

    private void drawTire(Graphics g, DirectedPoint tire, Color color) {
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
        Angle perpendicular = frontTire.getAngle().copy();
        perpendicular.rotate(Math.PI / 2);

        DirectedPoint frontLeftTire = frontTire.copy();
        frontLeftTire.move(width / 2, perpendicular);

        DirectedPoint frontRightTire = frontTire.copy();
        frontRightTire.move(-width / 2, perpendicular);

        g.setColor(Color.ORANGE);
        g.drawLine((int) frontLeftTire.getX(), (int) frontLeftTire.getY(),
                (int) frontRightTire.getX(), (int) frontRightTire.getY());

        drawTire(g, frontLeftTire, Color.BLUE);
        drawTire(g, frontRightTire, Color.BLUE);

        perpendicular = backTire.getAngle().copy();
        perpendicular.rotate(Math.PI / 2);

        DirectedPoint backLeftTire = backTire.copy();
        backLeftTire.move(width / 2, perpendicular);

        DirectedPoint backRightTire = backTire.copy();
        backRightTire.move(-width / 2, perpendicular);

        g.setColor(Color.ORANGE);
        g.drawLine((int) backLeftTire.getX(), (int) backLeftTire.getY(),
                (int) backRightTire.getX(), (int) backRightTire.getY());

        drawTire(g, backLeftTire, Color.RED);
        drawTire(g, backRightTire, Color.RED);

        g.setColor(Color.GREEN);
        g.drawLine((int) frontTire.getX(), (int) frontTire.getY(),
                (int) backTire.getX(), (int) backTire.getY());

        g.setColor(Color.BLACK);
        DirectedPoint headingPoint = frontTire.copy();
        headingPoint.move(20, frontTire.getAngle());

        g.drawLine((int) frontTire.getX(), (int) frontTire.getY(),
                (int) headingPoint.getX(), (int) headingPoint.getY());

        double arrowAngle = Math.PI / 6;
        double arrowLength = 10;

        DirectedPoint arrowLeft = headingPoint.copy();
        DirectedPoint arrowRight = headingPoint.copy();

        arrowLeft.move(arrowLength, new Angle(frontTire.getAngle().getAngle() + Math.PI - arrowAngle));
        arrowRight.move(arrowLength, new Angle(frontTire.getAngle().getAngle() + Math.PI + arrowAngle));

        g.drawLine((int) headingPoint.getX(), (int) headingPoint.getY(),
                (int) arrowLeft.getX(), (int) arrowLeft.getY());
        g.drawLine((int) headingPoint.getX(), (int) headingPoint.getY(),
                (int) arrowRight.getX(), (int) arrowRight.getY());
    }
}
