/*
 * RayCaster.java
 * Ario Barin Ostovary
 * Class for casting rays from a point to a point
 */

import java.util.ArrayList;
import java.util.List;

public class RayCaster {
    // get the cells along the ray from start to end
    public static List<MyPoint> getCellsAlongRay(MyPoint start, MyPoint end) {
        List<MyPoint> cells = new ArrayList<>();

        int x0 = (int) Math.round(start.getX());
        int y0 = (int) Math.round(start.getY());
        int x1 = (int) Math.round(end.getX());
        int y1 = (int) Math.round(end.getY());

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;

        while (true) {
            cells.add(new MyPoint(x0, y0));

            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }

            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }

        return cells;
    }
}