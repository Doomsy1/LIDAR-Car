/*  
 * Point.java
 * Ario Barin Ostovary
 * 
 */

public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void move(double distance, Angle angle) {
        x += distance * angle.getCos();
        y += distance * angle.getSin();
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public double distance(Point p) {
        return Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
    }

    public double distance(double x, double y) {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    public Point copy() {
        return new Point(x, y);
    }
}