package game;

import java.util.Vector;
import javafx.scene.image.Image;
//import javafx.scene.image.WritableImage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
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
	private Boolean[] navFlag= new Boolean[5];

	private Timer moveTimer;
	private TimerTask moveTask;

	public int x;
	public int y;
	public Image meImg;

	private Canvas drawingCanvas;
	private double incX;
	private double incY;
	public int[] obsStatus= new int[5];
	public int[] bombStatus= new int[5];
	public Vector<Bomb> bombVector= new Vector<>();;

	Player(int x, int y, Image meImg){
		this.x= x;
		this.y= y;
		this.meImg= meImg;
		incX= 0;
		incY= 0;
//		this.drawingCanvas= drawingCanvas;
//		moveTimer = new Timer(true);
	}

//	public int getX() { return x; }
//	public int getY() { return y; }

	public void runMoveTimer(){
//		moveTimer.cancel();
		moveTask= new TimerTask() {
			@Override
			public void run() {
				move();
			}
		};
		moveTimer = new Timer(true);
		moveTimer.schedule(moveTask,30,100);
		System.out.println("Move Timer Running");
	}
	public void stopMoveTimer(){
//		moveTimer.cancel();
		moveTask.cancel();
//		moveTimer.purge();
		System.out.println("Move Timer Stopped");
	}
	public void move(){


		if(GameController.pressed.contains(KeyCode.UP)){//UP
//			System.out.println("UP");
			checkAround();
			if( (obsStatus[Player.UP]==-1 || obsStatus[Player.UP]==4) && bombStatus[Player.UP]==0)
			{
				move(Player.UP);
				System.out.println("MOVED UP");
				//print the character
				renderTiles();
				if(obsStatus[Player.CENTER]==-1)
					drawMe();
			}
//			navFlag[UP]=true;
		}

		if(GameController.pressed.contains(KeyCode.DOWN)){//DOWN
			checkAround();
			if( (obsStatus[Player.DOWN]==-1 || obsStatus[Player.DOWN]==4) && bombStatus[Player.DOWN]==0)
			{
				move(Player.DOWN);
				System.out.println("MOVED DOWN");
				//print the character
				renderTiles();
				if(obsStatus[Player.DOWN]==-1)
					drawMe();
			}
//			navFlag[DOWN]=true;
		}

		if(GameController.pressed.contains(KeyCode.LEFT)){//LEFT
			checkAround();
			if( (obsStatus[Player.LEFT]==-1 || obsStatus[Player.LEFT]==4) && bombStatus[Player.LEFT]==0)
			{
				move(Player.LEFT);
				System.out.println("MOVED LEFT");
				//print the character
				renderTiles();
				if(obsStatus[Player.LEFT]==-1)
					drawMe();
			}
//			navFlag[LEFT]=true;
		}

		if(GameController.pressed.contains(KeyCode.RIGHT)){//RIGHT
			checkAround();
			if( (obsStatus[Player.RIGHT]==-1 || obsStatus[Player.RIGHT]==4) && bombStatus[Player.RIGHT]==0)
			{
				move(Player.RIGHT);
				System.out.println("MOVED RIGHT");
				//print the character
				renderTiles();
				if(obsStatus[Player.RIGHT]==-1)
					drawMe();
			}
//			navFlag[RIGHT]=true;
		}

		if(GameController.pressed.contains(KeyCode.SPACE)){//SPACE
			checkAround();
			if(bombVector.size() < 5){
				System.out.println("\tIN SPACE "+bombVector.size());

				GameController.tileVec.get(17*y+x).setBombStatus(1);
				renderTiles();
				if(obsStatus[Player.CENTER]==-1)
					drawMe(drawingCanvas);

//				Bomb bomb= new Bomb(this,drawingCanvas);
				bombVector.add(new Bomb(this,drawingCanvas));
				bombVector.lastElement().putBomb(x, y);
			}
//
//				tileVec= bomb.getTileVec();
			//model.getBombs().add(bomb1);
			//renderTiles(tileVec);
			//bomb1= null;

//			navFlag[RIGHT]=true;
		}
	}
	public void move(int nav){
		switch (nav){
			case UP:
//				incY--;
//				if(incY==-20){
//					y--;
//				}
//				if(incY<=-40)
//					incY=0;
				y--;
				break;
			case DOWN:
//				incY++;
//				if(incY==20){
//					y++;
//				}
//				if(incY>=40)
//					incY=0;
				y++;
				break;
			case LEFT:
//				incX--;
//				if(incX==-20){
//					x--;
//				}
//				if(incX<=-40)
//					incX=0;
				x--;
				break;
			case RIGHT:
//				incX++;
//				if(incX==20){
//					x++;
//				}
//				if(incX>=40)
//					incX=0;
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

	public void drawMe(){
		checkAround();
		if(bombStatus[CENTER]<= -1 && bombStatus[CENTER]>= -5){
			x=1; y=1;
		}
		GraphicsContext gb = drawingCanvas.getGraphicsContext2D();
		gb.drawImage(meImg, GameController.canvasXOffset+40*x+incX, GameController.canvasYOffset+40*y+incY, 40, 40);
	}
	public void catchCanvas(Canvas drawingCanvas){
		this.drawingCanvas= drawingCanvas;
	}

	public void drawMe(Canvas drawingCanvas){
		checkAround();
		if(bombStatus[CENTER]<= -1 && bombStatus[CENTER]>= -5){
			x=1; y=1;
		}
		GraphicsContext gb = drawingCanvas.getGraphicsContext2D();
		gb.drawImage(meImg, GameController.canvasXOffset+40*x, GameController.canvasYOffset+40*y, 40, 40);
	}

	public void renderTiles(){

		GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
		if(GameController.tileVec.size() < 15*17){
			gc.drawImage(new Image("image/Error.png"),
					(drawingCanvas.getWidth()-48)/2, (drawingCanvas.getHeight()-48)/2, 48, 48);
			return;
		}

		for(int i= 0; i < 15; i++){
			for(int j= 0; j < 17; j++){
				gc.drawImage(GameController.tileVec.get(i*17+j).getBack(), GameController.canvasXOffset+40*j,
						GameController.canvasYOffset+40*i, 40, 40);
				gc.drawImage(GameController.tileVec.get(i*17+j).getFront(), GameController.canvasXOffset+40*j,
						GameController.canvasYOffset+40*i, 40, 40);
			}
		}//Draw Obs and Background

		for(int i= 0; i < 15; i++){
			for(int j= 0; j < 17; j++){
				if(GameController.tileVec.get(i*17+j).getObs() != 4){//Bush
					if(GameController.tileVec.get(i*17+j).getBombStatus() == 1)
						gc.drawImage(Bomb.pic, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);

					if (GameController.tileVec.get(i*17+j).getBombStatus() == -1)
						gc.drawImage(Bomb.up, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);

					if (GameController.tileVec.get(i*17+j).getBombStatus() == -2)
						gc.drawImage(Bomb.down, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);

					if (GameController.tileVec.get(i*17+j).getBombStatus() == -3)
						gc.drawImage(Bomb.left, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);

					if (GameController.tileVec.get(i*17+j).getBombStatus() == -4)
						gc.drawImage(Bomb.right, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);

					if (GameController.tileVec.get(i*17+j).getBombStatus() == -5)
						gc.drawImage(Bomb.center, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);
				}
			}
		}//Draw Bomb
	}
}
