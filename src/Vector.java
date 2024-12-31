/*
 * Vector.java
 * Ario Barin Ostovary
 */

public class Vector {
    private Angle direction;
    private double magnitude;

    public Vector(Angle direction, double magnitude) {
        this.direction = direction;
        this.magnitude = magnitude;
    }

    public Vector(double direction, double magnitude) {
        this.direction = new Angle(direction);
        this.magnitude = magnitude;
    }

    public Angle getDirection() {
        return direction;
    }

    public double getDirectionRadians() {
        return direction.getRadians();
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setDirection(Angle direction) {
        this.direction = direction;
    }

    public void setDirection(double direction) {
        this.direction = new Angle(direction);
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public double getX() {
        return direction.getCos() * magnitude;
    }

    public double getY() {
        return direction.getSin() * magnitude;
    }

    public Vector add(Vector other) {
        double x = direction.getCos() * magnitude + other.getDirection().getCos() * other.getMagnitude();
        double y = direction.getSin() * magnitude + other.getDirection().getSin() * other.getMagnitude();
        return new Vector(new Angle(Math.atan2(y, x)), Math.sqrt(x * x + y * y));
    }

    public Vector subtract(Vector other) {
        return add(other.scale(-1));
    }

    public Vector scale(double scalar) {
        return new Vector(direction, magnitude * scalar);
    }
}
