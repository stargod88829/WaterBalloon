package game;

import java.util.*;

import javafx.scene.image.Image;
//import javafx.scene.image.WritableImage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.concurrent.TimeUnit;

public class Player extends GameController{
	public static final int CENTER=0;
	public static final int UP=1;
	public static final int DOWN=2;
	public static final int LEFT=3;
	public static final int RIGHT=4;
//	public enum Nav{UP, DOWN, LEFT, RIGHT};
	private Set<KeyCode> pressed;
	private Boolean[] navFlag= new Boolean[5];

	private Timer moveTimer;
	private TimerTask task;

	public int x;
	public int y;
	public Image meImg;

	private Canvas drawingCanvas;
	private int absX;
	private int absY;
	public int[] obsStatus= new int[5];
	public int[] bombStatus= new int[5];
	private Vector<Tile> tileVec;

	Player(int x, int y, Image meImg, Canvas drawingCanvas){
		this.x= x;
		this.y= y;
		this.meImg= meImg;
		this.drawingCanvas= drawingCanvas;
		tileVec= GameController.tileVec;
		moveTimer = new Timer(true);
		task= new TimerTask() {
			@Override
			public void run() {
				move();
			}
		};
	}

//	public int getX() { return x; }
//	public int getY() { return y; }
	public void move(){
		if(GameController.pressed.contains(KeyCode.UP)){//UP
//			System.out.println("UP");
			if( (obsStatus[Player.UP]==-1 || obsStatus[Player.UP]==4) && bombStatus[Player.UP]==0)
			{
				move(Player.UP);
				//print the character
				renderTiles();
				if(obsStatus[Player.UP]==-1)
					drawMe(drawingCanvas);
			}
			navFlag[UP]=true;
			if(GameController.pressed.contains(KeyCode.LEFT)){
//				System.out.println("UP + LEFT");
				navFlag[UP]=true;
			}
			if(GameController.pressed.contains(KeyCode.RIGHT)){
				System.out.println("UP + RIGHT");
			}
		}

		if(GameController.pressed.contains(KeyCode.DOWN)){//DOWN
			if(GameController.pressed.contains(KeyCode.LEFT)){
				System.out.println("DOWN + LEFT");
			}
			if(GameController.pressed.contains(KeyCode.RIGHT)){
				System.out.println("DOWN + RIGHT");
			}
		}


	}

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
	public void move(Set<KeyCode> pressed){

	}

	public void catchKeyPress(Set<KeyCode> pressed){

		this.pressed= new HashSet<KeyCode>(pressed);
		if(pressed.contains(KeyCode.UP)){//UP
			System.out.println("UP");
			if(pressed.contains(KeyCode.LEFT)){
				System.out.println("UP + LEFT");
			}
			if(pressed.contains(KeyCode.RIGHT)){
				System.out.println("UP + RIGHT");
			}
		}

		if(pressed.contains(KeyCode.DOWN)){//DOWN
			if(pressed.contains(KeyCode.LEFT)){
				System.out.println("DOWN + LEFT");
			}
			if(pressed.contains(KeyCode.RIGHT)){
				System.out.println("DOWN + RIGHT");
			}
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
