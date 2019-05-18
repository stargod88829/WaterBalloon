package game;

import java.util.Vector;
import javafx.scene.image.Image;
//import javafx.scene.image.WritableImage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Player{
	public static final int CENTER=0;
	public static final int UP=1;
	public static final int DOWN=2;
	public static final int LEFT=3;
	public static final int RIGHT=4;
//	public enum Nav{UP, DOWN, LEFT, RIGHT};

	public int x;
	public int y;
	public Image meImg;

//	private Canvas drawingCanvas;
	private int absX;
	private int absY;
	public int[] obsStatus= new int[5];
	public int[] bombStatus= new int[5];

	Player(int x, int y, Image meImg){
		this.x= x;
		this.y= y;
		this.meImg= meImg;
//		this.drawingCanvas= drawingCanvas;
	}

//	public int getX() { return x; }
//	public int getY() { return y; }

	public void move(int nav){
		switch (nav){
			case UP:
				y--;
				break;
			case DOWN:
				y++;
				break;
			case LEFT:
				x--;
				break;
			case RIGHT:
				x++;
				break;
			default:
				break;

		}
	}
	public void checkAround(){
		obsStatus[CENTER]= GameController.tileVec.get(x+17*y).getObs();
		obsStatus[UP]= GameController.tileVec.get(x+17*(y-1)).getObs();
		obsStatus[DOWN]= GameController.tileVec.get(x+17*(y+1)).getObs();
		obsStatus[LEFT]= GameController.tileVec.get(x-1+17*y).getObs();
		obsStatus[RIGHT]= GameController.tileVec.get(x+1+17*y).getObs();

		bombStatus[CENTER]= GameController.tileVec.get(x+17*y).getBombStatus();
		bombStatus[UP]= GameController.tileVec.get(x+17*(y-1)).getBombStatus();
		bombStatus[DOWN]= GameController.tileVec.get(x+17*(y+1)).getBombStatus();
		bombStatus[LEFT]= GameController.tileVec.get(x-1+17*y).getBombStatus();
		bombStatus[RIGHT]= GameController.tileVec.get(x+1+17*y).getBombStatus();
	}

	public void drawMe(Canvas drawingCanvas){
		GraphicsContext gb = drawingCanvas.getGraphicsContext2D();
		gb.drawImage(meImg, GameController.canvasXOffset+40*x, GameController.canvasYOffset+40*y, 40, 40);
	}
}
