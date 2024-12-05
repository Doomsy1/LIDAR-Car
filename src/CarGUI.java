/*
 * CarGUI.java
 * Ario Barin Ostovary & Kevin Dang
 * .
 */

import javax.swing.*;
import java.util.*;

public class CarGUI extends JFrame {
    private CarGUIPanel hashAssign2Panel = new CarGUIPanel();

    public CarGUI() {
        super("Car GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(hashAssign2Panel);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new CarGUI();
    }
}

