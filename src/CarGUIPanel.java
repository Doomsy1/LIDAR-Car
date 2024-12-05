/* 
 * CarGUIPanel.java
 * Ario Barin Ostovary & Kevin Dang
 * 
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class CarGUIPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private BufferedImage mapImage;
    private HashTable<Point, ArrayList<EmotionalEntry>> emotionTable;

    private static final int WIDTH = 800, HEIGHT = 600;
    private static final int FPS = 50;
    private static final String MAP_FILE = "windsor.png";
    private static final String DATA_FILE = "creeper.txt";
    private static final int RADIUS = 10;

    private int mouseX = 0, mouseY = 0;
    private boolean mousePressed = false;
    private Timer timer;

    public CarGUIPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Load the map image
        try {
            mapImage = ImageIO.read(new File(MAP_FILE));
        } catch (Exception e) {
            System.out.println(e);
        }

        // Load the data
        emotionTable = loadData(DATA_FILE);

        // Add mouse listeners
        addMouseListener(this);
        addMouseMotionListener(this);

        // Start the timer
        timer = new Timer(1000 / FPS, this);
        timer.start();
    }

    private HashTable<Point, ArrayList<EmotionalEntry>> loadData(String filename) {
        HashTable<Point, ArrayList<EmotionalEntry>> eTable = new HashTable<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            // Read lines and add to the table (x y lh hs eb)
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");

                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);

                // Check if the point is within the bounds of the map
                if (x < 0 || x > WIDTH - 1 || y < 0 || y > HEIGHT - 1) {
                    continue;
                }

                // Create a point and add the emotional entry to the table
                Point point = new Point(x, y);

                int lh = Integer.parseInt(parts[2]);
                int hs = Integer.parseInt(parts[3]);
                int eb = Integer.parseInt(parts[4]);
                EmotionalEntry entry = new EmotionalEntry(lh, hs, eb);

                // If the point is not in the table, add it
                if (!eTable.contains(point)) {
                    eTable.add(point, new ArrayList<>());
                }

                // Add the emotional entry to the point
                eTable.get(point).add(entry);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return eTable;
    }

    private boolean isInRadius(int x, int y, int ex, int ey) {
        // Check if the point is within the radius
        return Math.hypot(x - ex, y - ey) <= RADIUS;
    }

    private int mapEmotion(int emotion) {
        // Map the emotion value to a color value between 0 and 255
        return (int) ((emotion + 100) * 255.0 / 200.0);
    }

    private Color getEntryColor(ArrayList<EmotionalEntry> entries) {
        // Get the average emotion values
        int count = entries.size();
        int totalLh = 0, totalHs = 0, totalEb = 0;

        // Sum the emotion values
        for (EmotionalEntry entry : entries) {
            totalLh += entry.getLh();
            totalHs += entry.getHs();
            totalEb += entry.getEb();
        }

        // Calculate the average emotion values
        int avgLh = totalLh / count;
        int avgHs = totalHs / count;
        int avgEb = totalEb / count;

        // Map the average emotion values to a color
        int red = mapEmotion(avgLh);
        int green = mapEmotion(avgHs);
        int blue = mapEmotion(avgEb);

        return new Color(red, green, blue);
    }

    private void drawEmotions(Graphics g, int x, int y) {
        // Calculate the bounds of the radius
        int top = y - RADIUS;
        int bottom = y + RADIUS;
        int left = x - RADIUS;
        int right = x + RADIUS;

        // Draw the emotions
        for (int ex = left; ex <= right; ex++) {
            for (int ey = top; ey <= bottom; ey++) {
                // Check if the point is within the radius
                if (!isInRadius(x, y, ex, ey)) {
                    continue; // Skip if not in radius
                }

                // Get the emotional entries for the point
                ArrayList<EmotionalEntry> entries = emotionTable.get(new Point(ex, ey));
                if (entries == null) {
                    continue; // Skip if no entries
                }

                // Get the color of the entries
                Color color = getEntryColor(entries);
                g.setColor(color);
                g.fillRect(ex, ey, 1, 1);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Set the mouse position and mark the mouse as pressed - repaint the panel
        mouseX = e.getX();
        mouseY = e.getY();
        mousePressed = true;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Set the mouse position and mark the mouse as not pressed - repaint the panel
        mousePressed = false;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Set the mouse position - repaint the panel
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        // Draw the map image
        g.drawImage(mapImage, 0, 0, null);

        // Draw the emotions if the mouse is pressed
        if (mousePressed) {
            drawEmotions(g, mouseX, mouseY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
