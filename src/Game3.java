/* Game3.java
 * This example add 3 things.
 * 1. How to load and display an image.
 * 2. Mouse interactions.
 * 3. Although we can have multiple windows in Swing, it is usually
 *    easiest to just have one, and keep track of what screen we are on. 
 **/
 
 
 import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.Timer;

public class Game3 extends JFrame{
	GamePanel game= new GamePanel();
		
    public Game3() {
		super("Move the Box");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(game);
		pack();
		setVisible(true);
    }
    
    public static void main(String[] arguments) {
		new Game3();		
    }
}

class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener{

	public static final int INTRO=0, GAME=1, END=2;

	private int screen = INTRO;
	private boolean []keys;
	private boolean backgroundOn = true;

	private ArrayList<Point> points = new ArrayList<Point>();

	Timer timer;
	private Image back;
	private Car car = new Car(400, 300, 20, 40);
	
	public GamePanel(){

		back = new ImageIcon("background.png").getImage();
		keys = new boolean[KeyEvent.KEY_LAST+1];


		setPreferredSize(new Dimension(800, 600));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		addMouseListener(this);
		timer = new Timer(20, this);
		timer.start();

	}

	public void move(){

		if (keys[KeyEvent.VK_SPACE]){
			backgroundOn = true;
		}
		else{
			backgroundOn = false;
		}

		int x = 0;
		int y = 0;
		int carX = car.getX();
		int carY = car.getY();

		if(keys[KeyEvent.VK_LEFT] && Mask.clear(carX-5, carY)){
			x-=5;
		}
		if(keys[KeyEvent.VK_RIGHT] && Mask.clear(carX+5, carY)){
			x+=5;
		}
		if(keys[KeyEvent.VK_UP] && Mask.clear(carX, carY-5)){
			y-=5;
		}
		if(keys[KeyEvent.VK_DOWN] && Mask.clear(carX, carY+5)){
			y+=5;
		}

		car.move(x,y);
		// updateData();
			
	}

	public void updateData(){
		int distance = car.readLidar();

		if (distance == -1){
			return;
		}
		// System.out.println(distance);

		car.rotateLidar();

		int xDistance = (int)(distance * Math.cos(Math.toRadians(car.getLidarBearing())));
		int yDistance = (int)(distance * Math.sin(Math.toRadians(car.getLidarBearing())));
		Point point = new Point(car.getX()+xDistance, car.getY()+yDistance);
		points.add(point);
		

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		move();  // Handles car movement
		car.rotateLidar();  // Ensure lidar always rotates
		updateData();  // Update lidar data
		repaint();  // Refresh the display
	}

	
	@Override
	public void keyReleased(KeyEvent ke){
		int key = ke.getKeyCode();
		keys[key] = false;
	}	
	
	@Override
	public void keyPressed(KeyEvent ke){
		int key = ke.getKeyCode();
		keys[key] = true;
	}
	
	@Override
	public void keyTyped(KeyEvent ke){}
	@Override
	public void	mouseClicked(MouseEvent e){}

	@Override
	public void	mouseEntered(MouseEvent e){}

	@Override
	public void	mouseExited(MouseEvent e){}

	@Override
	public void	mousePressed(MouseEvent e){

	}

	@Override
	public void	mouseReleased(MouseEvent e){}

	@Override
	public void paint(Graphics g){

		if (backgroundOn){
		g.drawImage(back, 0, 0, null);
		}
		else{
			g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);
		}



		car.draw(g);

		for(Point point: points){
			// System.out.println(point.getX() + " " + point.getY());
			g.setColor(Color.WHITE);
			g.fillOval((int)point.getX(), (int)point.getY(), 5, 5);
		}
	}

}

class Point{

	private int x, y;

	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	
}

class Car{
	private int x, y;
	private int sizeX, sizeY;
	private Lidar lidar = new Lidar(x+sizeX/2, y+sizeY/2);

	public Car(int x, int y, int sizeX, int sizeY){
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public void move(int dx, int dy){
		x += dx;
		y += dy;
		lidar.updatePosition(x, y);
	}

	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}

	public void rotateLidar(){
		lidar.rotate(6);
	}
	public int readLidar(){
		return lidar.read();
	}
	public int getLidarBearing(){
		return lidar.getBearing();
	}

	public void draw(Graphics g){
		int lidarPointerX = (int)(lidar.getX() + 10 * Math.cos(Math.toRadians(lidar.getBearing())));
		int lidarPointerY = (int)(lidar.getY() + 10 * Math.sin(Math.toRadians(lidar.getBearing())));

		g.setColor(Color.BLUE);
		g.drawRect(x-sizeX/2, y-sizeY/2, sizeX, sizeY);
		g.setColor(Color.RED);
		g.drawLine(lidar.getX(), lidar.getY(), lidarPointerX, lidarPointerY);
		
		lidar.draw(g);
	}

}
