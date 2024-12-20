/*
 * DirectedPoint.java
 * Ario Barin Ostovary
 * 
 */
public class DirectedPoint {
    private final Point point;
    private final Angle angle;

    public DirectedPoint(double x, double y, double angle) {
        this.point = new Point(x, y);
        this.angle = new Angle(angle);
    }

    public DirectedPoint(Point point, double angle) {
        this.point = point;
        this.angle = new Angle(angle);
    }

    public DirectedPoint(double x, double y, Angle angle) {
        this.point = new Point(x, y);
        this.angle = angle;
    }

    public DirectedPoint(Point point, Angle angle) {
        this.point = point;
        this.angle = angle;
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public Point getPoint() {
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

    public double distance(DirectedPoint other) {
        return point.distance(other.getPoint());
    }

    public DirectedPoint copy() {
        return new DirectedPoint(point.copy(), angle.getAngle());
    }

    @Override
    public int hashCode() {
        return (int) (point.getX() + point.getY() + angle.getAngle());
    }
}