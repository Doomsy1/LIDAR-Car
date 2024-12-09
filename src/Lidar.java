import java.awt.*;



class Lidar{


    private int bearing = 0;
    private int x;
    private int y;

    public static final int RESOLUTION = 60;
    public static final int MAX_DISTANCE = 200;
    public static final int RPM = 60;
    private final int LIDAR_RADIUS = 10;

    public Lidar(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void rotate(int degrees){
        bearing += degrees;
        bearing = bearing % 360;
    }   

    public int read(){
        for (int curr = 0; curr < MAX_DISTANCE ; curr+=1){
            int pointX = (int)(this.x + curr * Math.cos(Math.toRadians(bearing)));
            int pointY = (int)(this.y + curr * Math.sin(Math.toRadians(bearing)));
            if(!Mask.clear(pointX, pointY)){
                return curr;
            }
        }
        return -1;
    }

    public void updatePosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g){
        g.setColor(Color.RED);
        g.drawOval(x-LIDAR_RADIUS/2, y-LIDAR_RADIUS/2, LIDAR_RADIUS, LIDAR_RADIUS);
    }

    public int getBearing(){
        return bearing;
    }

}
