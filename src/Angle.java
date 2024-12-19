/*
 * Angle.java
 * Ario Barin Ostovary
 * 
 */

public class Angle {
    private double angle; // in radians

    public Angle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public double getCos() {
        return Math.cos(angle);
    }

    public double getSin() {
        return Math.sin(angle);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    private double normalize(double angle) {
        // Normalize the angle to be between 0 and 2*pi
        return (angle + Math.PI * 2) % (Math.PI * 2);
    }

    public void rotate(double angle) {
        this.angle = normalize(this.angle + angle);
    }

    public void rotate(Angle other) {
        this.angle = normalize(this.angle + other.getAngle());
    }

    public Angle copy() {
        return new Angle(angle);
    }

    @Override
    public String toString() {
        return String.format("%.2f", angle);
    }
}