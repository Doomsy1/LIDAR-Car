/*
 * Vector.java
 * Ario Barin Ostovary
 * Class for a vector in 2D space (direction and magnitude)
 */

public class MyVector {
    private Angle direction;
    private double magnitude;

    public MyVector(Angle direction, double magnitude) {
        this.direction = direction;
        this.magnitude = magnitude;
    }

    public MyVector(double direction, double magnitude) {
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

    public MyVector add(MyVector other) {
        double x = direction.getCos() * magnitude + other.getDirection().getCos() * other.getMagnitude();
        double y = direction.getSin() * magnitude + other.getDirection().getSin() * other.getMagnitude();
        return new MyVector(new Angle(Math.atan2(y, x)), Math.sqrt(x * x + y * y));
    }

    public MyVector subtract(MyVector other) {
        return add(other.scale(-1));
    }

    public MyVector scale(double scalar) {
        return new MyVector(direction, magnitude * scalar);
    }

    @Override
    public String toString() {
        return String.format("Vector(mag=%.2f, dir=%.2f)", getMagnitude(), getDirection().getRadians());
    }
}