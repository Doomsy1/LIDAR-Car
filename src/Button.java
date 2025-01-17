import java.awt.*;
import java.awt.image.BufferedImage;

public class Button {
    private int x, y, width, height;
    private final Runnable onClick;
    private final BufferedImage icon;
    private boolean state;
    private boolean isHovered;

    private static final Color DISABLED_COLOR = new Color(128, 0, 0, 200);
    private static final Color ENABLED_COLOR = new Color(0, 128, 0, 200);
    private static final Color ENABLED_HOVER_COLOR = new Color(32, 64, 0, 200);
    private static final Color DISABLED_HOVER_COLOR = new Color(64, 32, 0, 200);

    public Button(int x, int y, int width, int height, BufferedImage icon, boolean initialState, Runnable onClick) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.icon = icon;
        this.state = initialState;
        this.onClick = onClick;
        this.isHovered = false;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (isHovered) {
            g.setColor(state ? ENABLED_HOVER_COLOR : DISABLED_HOVER_COLOR);
        } else {
            g.setColor(state ? ENABLED_COLOR : DISABLED_COLOR);
        }

        g.fillRect(x, y, width, height);
        Util.drawImage(g, icon, x, y, width, height);
    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
    }

    public void click() {
        onClick.run();
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
    }
}