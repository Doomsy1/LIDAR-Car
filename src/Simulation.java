import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Simulation {
    private boolean 
    showWorld = false, 
    showPOV = false, 
    showReadings = true, 
    showSLAM = true;

    private final int
    showWorldToggle = KeyEvent.VK_1, 
    showPOVToggle = KeyEvent.VK_2, 
    showReadingsToggle = KeyEvent.VK_3, 
    showSLAMToggle = KeyEvent.VK_4;
    
    private final BufferedImage worldIcon, povIcon, lidarIcon, slamIcon;
    private final Button[] viewButtons;

    private final World world;

    public Simulation() {
        world = new World();
    }
}