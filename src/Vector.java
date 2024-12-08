/*
 * A vector class that represents a point in 2D space and its angle - in degrees.
 */

public class Vector {
    private double x, y;
    private double angle;

    public Vector(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle; // in degrees
    }

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public void move(double distance) {
        x += distance * Math.cos(Math.toRadians(angle));
        y += distance * Math.sin(Math.toRadians(angle));
    }

    public void rotate(double degrees) {
        angle += degrees;
    }

    public double angleDifference(Vector other) {
        return angle - other.angle;
    }

    public double distance(Vector other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    public Vector copy() {
        return new Vector(x, y, angle);
        
    }
}
