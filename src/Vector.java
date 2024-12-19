/*
 * Vector.java
 * Ario Barin Ostovary
 * 
 */

public class Vector {
    private double direction;
    private double magnitude;

    public Vector(double direction, double magnitude) {
        this.direction = direction;
        this.magnitude = magnitude;
    }

    public double getDirection() {
        return direction;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public Vector add(Vector vector) {
        double x = this.magnitude * Math.cos(this.direction) + vector.magnitude * Math.cos(vector.direction);
        double y = this.magnitude * Math.sin(this.direction) + vector.magnitude * Math.sin(vector.direction);
        return new Vector(Math.atan2(y, x), Math.sqrt(x * x + y * y));
    }

    public Vector subtract(Vector vector) {
        double x = this.magnitude * Math.cos(this.direction) - vector.magnitude * Math.cos(vector.direction);
        double y = this.magnitude * Math.sin(this.direction) - vector.magnitude * Math.sin(vector.direction);
        return new Vector(Math.atan2(y, x), Math.sqrt(x * x + y * y));
    }
}
