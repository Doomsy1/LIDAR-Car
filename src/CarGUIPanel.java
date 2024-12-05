/* 
 * CarGUIPanel.java
 * Ario Barin Ostovary & Kevin Dang
 * 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CarGUIPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private static final int WIDTH = 800, HEIGHT = 600;
    private static final int FPS = 50;
    private Timer timer;

    public CarGUIPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // Start the timer
        timer = new Timer(1000 / FPS, this);
        timer.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void paintComponent(Graphics g) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
