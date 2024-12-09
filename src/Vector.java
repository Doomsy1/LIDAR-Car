/*
 * Vector.java
 * Ario Barin Ostovary
 * 
 */

public class Vector {
    private double x, y;
    private double angle;

    public Vector(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle; // in radians
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getAngle() { return angle; }

    public void setAngle(double angle) { this.angle = angle % (2 * Math.PI); }

    public void move(double distance) {
        x += distance * Math.cos(angle);
        y += distance * Math.sin(angle);
    }

    public void moveXY(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void move(double distance, double angle) {
        x += distance * Math.cos(angle);
        y += distance * Math.sin(angle);
    }

    public void rotate(double degrees) {
        angle = (angle + degrees) % (2 * Math.PI);
    }

    public double angleDifference(Vector other) {
        return (angle - other.angle + Math.PI) % (2 * Math.PI) - Math.PI;
    }

    public double distance(Vector other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    public Vector copy() {
        return new Vector(x, y, angle);
    }
}
