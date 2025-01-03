/*
 * DirectedPoint.java
 * Ario Barin Ostovary
 * Class for a point with an angle
 */

public class DirectedPoint {
    private final MyPoint point;
    private final Angle angle;

    public DirectedPoint(double x, double y, double angle) {
        this.point = new MyPoint(x, y);
        this.angle = new Angle(angle);
    }

    public DirectedPoint(MyPoint point, double angle) {
        this.point = point;
        this.angle = new Angle(angle);
    }

    public DirectedPoint(double x, double y, Angle angle) {
        this.point = new MyPoint(x, y);
        this.angle = angle;
    }

    public DirectedPoint(MyPoint point, Angle angle) {
        this.point = point;
        this.angle = angle;
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public MyPoint getPoint() {
        return point.copy();
    }

    public Angle getAngle() {
        return angle.copy();
    }

    public void setAngle(double angle) {
        this.angle.setAngle(angle);
    }

    public void move(double distance) {
        point.move(distance, angle);
    }

    public void setXY(double x, double y) {
        point.setX(x);
        point.setY(y);
    }

    public void move(double dx, double dy) {
        point.move(dx, dy);
    }

    public void move(double distance, Angle angle) {
        point.move(distance, angle);
    }

    public void rotate(double degrees) {
        angle.rotate(degrees);
    }

    public void rotate(Angle angle) {
        this.angle.rotate(angle);
    }

    public double distance(DirectedPoint other) {
        return point.distance(other.getPoint());
    }

    public DirectedPoint copy() {
        return new DirectedPoint(point.copy(), angle.copy());
    }

    @Override
    public int hashCode() {
        return (int) (point.getX() + point.getY() + angle.getRadians());
    }
}