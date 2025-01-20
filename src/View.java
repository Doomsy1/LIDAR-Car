import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class View {
    protected CarGUIPanel panel;

    public View(CarGUIPanel panel) {
        this.panel = panel;
    }

    public abstract void step(boolean[] keysDown, boolean[] keysPressed);

    public abstract void draw(Graphics g);  

    public abstract void keyPressed(KeyEvent e);

    public abstract void keyReleased(KeyEvent e);

    public abstract void mousePressed(MouseEvent e);

    public abstract void mouseReleased(MouseEvent e);

    public abstract void mouseMoved(MouseEvent e);
}
