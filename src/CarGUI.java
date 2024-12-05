/*
 * CarGUI.java
 * Ario Barin Ostovary & Kevin Dang
 * .
 */

import javax.swing.*;

public class CarGUI extends JFrame {
    private final CarGUIPanel panel = new CarGUIPanel();

    public CarGUI() {
        super("Car GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new CarGUI();
    }
}

