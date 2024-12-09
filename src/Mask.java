import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;


class Mask{



	private static Image mask;
	private static Image bg;

	static {
		try {
			mask = ImageIO.read(new File("mask.png"));
			bg = ImageIO.read(new File("background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final Color AIR = new Color(255,174,201);

    public static boolean clear(int x, int y){

		BufferedImage bufferedMask = (BufferedImage) mask;
		int pixel = bufferedMask.getRGB(x, y);

		if(x<0 || x>= mask.getWidth(null) || y<0 || y>= mask.getHeight(null)){
			return false;
		}
		if(pixel != AIR.getRGB()){
			return false;
		}

		return true;

	}	

}