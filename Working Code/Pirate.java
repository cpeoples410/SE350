import java.awt.Point;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Pirate implements Observer, Runnable {
	
	Point shipLocation;
	Point pirateLocation;
	SailStrategy sailStrategy;
	OceanMap map;
	Random rand = new Random();
	int x = 0;
	protected Thread pirateThread;
	Boolean done = false;
	Image spriteImage = new Image("pirateship.png",50,50,true, true);
	ImageView spriteImageView = new ImageView(spriteImage);
	
	public Pirate(OceanMap oceanMap) {
		
		this.map = oceanMap;
		this.sailStrategy = new VerticalSail();		//default sail strategy
		while(x != -1) {
    			pirateLocation= new Point(rand.nextInt(10),rand.nextInt(10));
    			if(oceanMap.islands[(int) pirateLocation.getX()][(int) pirateLocation.getY()]) {
    				x = 0;
    			}
    			else x = x -1;
    		}
	}
	
	public void start() {
		pirateThread = new Thread(this);
		pirateThread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
		if (pirateThread == null) {
			return;
		}
		pirateThread.stop();
		done = true;
	}
	
	@Override
	public void run() {
	
		while(!done) {
			sailStrategy.sail(this);
			draw();
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public void addToPane(AnchorPane root) {
		root.getChildren().add(spriteImageView);
	}
	
	public void draw(){
		
		//method to redraw the pirate as it moves
		int x = this.pirateLocation.x;
		int y = this.pirateLocation.y;
		spriteImageView.setX(x*50);
		spriteImageView.setY(y*50);
	}

	public void setSailStrategy(SailStrategy sail) {
		this.sailStrategy = sail;
	}
	
	public Point getPirateLocation() {
		return pirateLocation;
	}
	
	public Point getShipLocation() {
		return shipLocation;
	}
	
	public void setX(int x) {
		pirateLocation.x = x;
	}
	
	public void setY(int y) {
		pirateLocation.y = y;
	}

	@Override
	public void update(Observable ship, Object obj) {
		if(ship instanceof Ship) {
			shipLocation = ((Ship)ship).getShipLocation();
		}
	}
	
}
