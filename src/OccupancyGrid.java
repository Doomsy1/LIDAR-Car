/* 
 * OccupancyGrid.java
 * Ario Barin Ostovary
 * Class for the occupancy grid, a grid that represents 
 * the probability of a cell being occupied by an object
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class OccupancyGrid {
    private double[][] grid;

    private int width;
    private int height;

    private final int cellSize;

    // center of the grid at the start
    private int centerX, centerY;

    private static final double GROW_FACTOR = 1.1;

    // log odds
    private static final double DEFAULT_LOG_ODDS = Math.log(0.5 / (1 - 0.5));
    private static final double LOG_ODDS_OCCUPIED = Math.log(0.99 / (1 - 0.99));
    private static final double LOG_ODDS_FREE = Math.log(0.01 / (1 - 0.01));

    // max and min log odds
    private static final double MAX_LOG_ODDS = Math.log(0.95 / (1 - 0.95));
    private static final double MIN_LOG_ODDS = Math.log(0.05 / (1 - 0.05));

    private BufferedImage occupancyImage;

    public OccupancyGrid(int width, int height, int cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;

        this.grid = new double[width][height];

        // initialize grid
        this.grid = filledGrid(width, height);

        // center of the grid at the start
        this.centerX = width / 2;
        this.centerY = height / 2;

        // create image
        this.occupancyImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        updateOccupancyImage();
    }

    public int getWorldWidth() {
        return width * cellSize;
    }

    public int getWorldHeight() {
        return height * cellSize;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    private int worldToGridX(double x) {
        return (int) Math.round(x / cellSize) + centerX;
    }

    private int worldToGridY(double y) {
        return (int) Math.round(y / cellSize) + centerY;
    }

    // helper function
    private double[][] filledGrid(int width, int height) {
        double[][] filledGrid = new double[width][height];
        for (int x = 0; x < width; x++) {
            Arrays.fill(filledGrid[x], DEFAULT_LOG_ODDS);
        }
        return filledGrid;
    }

    private void resizeGrid(int dx, int dy) {
        int x = worldToGridX(dx);
        int y = worldToGridY(dy);

        boolean resized = false;

        int newWidth = width;
        int newHeight = height;
        int newCenterX = centerX;
        int newCenterY = centerY;

        int newX = x;
        int newY = y;

        // check if the new position is within the grid (must resize if not)
        if (newX < 0 || newX >= newWidth) {
            newWidth = (int) (width * GROW_FACTOR);
            if (newX < 0) {
                newCenterX += (int) (width * GROW_FACTOR - width);
            }
            newX = (int) Math.round(newX / cellSize) + newCenterX;
            resized = true;
        }

        if (newY < 0 || newY >= newHeight) {
            newHeight = (int) (height * GROW_FACTOR);
            if (newY < 0) {
                newCenterY += (int) (height * GROW_FACTOR - height);
            }
            newY = (int) Math.round(newY / cellSize) + newCenterY;
            resized = true;
        }

        if (resized) {
            // new grid
            double[][] newGrid = filledGrid(newWidth, newHeight);

            int xOffset = newCenterX - centerX;
            int yOffset = newCenterY - centerY;

            // copy the previous grid into the new grid
            for (int i = 0; i < width; i++) {
                System.arraycopy(grid[i], 0, newGrid[i + xOffset], yOffset, height);
            }

            // update grid and dimensions
            grid = newGrid;
            width = newWidth;
            height = newHeight;
            centerX = newCenterX;
            centerY = newCenterY;

            // update image
            this.occupancyImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            updateOccupancyImage();
        }
    }

    public void increaseProbability(double x, double y) {
        increaseProbability((int) Math.round(x), (int) Math.round(y));
    }

    public void increaseProbability(int dx, int dy) {
        resizeGrid(dx, dy);

        int x = worldToGridX(dx);
        int y = worldToGridY(dy);

        // increase probability
        grid[x][y] += LOG_ODDS_OCCUPIED;
        grid[x][y] = Math.min(grid[x][y], MAX_LOG_ODDS);

        // update image
        updateCellInImage(x, y);
    }

    public void decreaseProbability(double x, double y) {
        decreaseProbability((int) Math.round(x), (int) Math.round(y));
    }

    public void decreaseProbability(int dx, int dy) {
        resizeGrid(dx, dy);

        int x = worldToGridX(dx);
        int y = worldToGridY(dy);

        // reduce probability
        grid[x][y] += LOG_ODDS_FREE;
        grid[x][y] = Math.max(grid[x][y], MIN_LOG_ODDS);

        // update image
        updateCellInImage(x, y);
    }

    private double getProbability(double logOdds) {
        return 1.0 - 1.0 / (1.0 + Math.exp(logOdds));
    }

    public double getProbability(int dx, int dy) {
        resizeGrid(dx, dy);

        int x = worldToGridX(dx);
        int y = worldToGridY(dy);

        return getProbability(grid[x][y]);
    }

    public void clearGrid() {
        grid = filledGrid(width, height);
    }

    public void saveGrid(String filename) {
        // TODO
    }

    public void loadGrid(String filename) {
        // TODO
    }

    public void saveImage(String filename) {
        // TODO
    }

    private Color getColor(int x, int y) {
        double prob = getProbability(grid[x][y]);
        if (prob > 0.7) {
            return Color.BLACK; // Occupied
        } else if (prob < 0.3) {
            return Color.WHITE; // Free
        } else {
            return new Color((int) (255 * prob), (int) (255 * prob), (int) (255 * prob));
        }
    }

    private void updateOccupancyImage() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                occupancyImage.setRGB(x, y, getColor(x, y).getRGB());
            }
        }
    }

    private void updateCellInImage(int x, int y) {
        occupancyImage.setRGB(x, y, getColor(x, y).getRGB());
    }

    public void draw(Graphics g) {
        int panelWidth = g.getClipBounds().width;
        int panelHeight = g.getClipBounds().height;

        g.drawImage(occupancyImage, 0, 0, panelWidth, panelHeight, null);
    }

    public OccupancyGrid copy() {
        // Create new grid with same dimensions and cell size
        OccupancyGrid copy = new OccupancyGrid(width, height, cellSize);

        // Copy center coordinates
        copy.centerX = this.centerX;
        copy.centerY = this.centerY;

        // Deep copy the grid array
        for (int x = 0; x < width; x++) {
            System.arraycopy(this.grid[x], 0, copy.grid[x], 0, height);
        }

        // Update the image to reflect the copied grid
        copy.updateOccupancyImage();

        return copy;
    }
}