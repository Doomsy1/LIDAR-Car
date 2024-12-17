/*
 * Mask.java
 * Kevin Dang
 * 
 */

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

class Mask {
	private static Image mask; // Mask image
	private static Image bg; // Background image

	static {
		try {
			mask = ImageIO.read(new File("mask.png"));
			bg = ImageIO.read(new File("background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final Color AIR = new Color(255, 174, 201);

	public static boolean clear(int x, int y) {
		BufferedImage bufferedMask = (BufferedImage) mask;
		int pixel = bufferedMask.getRGB(x, y);

		// Check if the pixel is out of bounds
		if (x < 0 || x >= mask.getWidth(null) || y < 0 || y >= mask.getHeight(null)) {
			return false;
		}

		// Check if the pixel is clear
		return pixel == AIR.getRGB();
	}

	public static void draw(Graphics g) {
		g.drawImage(bg, 0, 0, null);
	}

	public static int getWidth() {
		return CarGUIPanel.BASE_WIDTH;
	}

	public static int getHeight() {
		return CarGUIPanel.BASE_HEIGHT;
	}
}