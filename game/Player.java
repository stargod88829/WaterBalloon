package game;

import java.util.Vector;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Player{
	public int x;
	public int y;
	public Image meImg;

	private Canvas drawingCanvas;
	private int absX;
	private int absY;

	Player(int x, int y, Image meImg){
		this.x= x;
		this.y= y;
		this.meImg= meImg;
	}

//	public int getX() { return x; }
//	public int getY() { return y; }
}
