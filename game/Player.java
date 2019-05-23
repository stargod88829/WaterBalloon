package game;

import java.io.File;
import java.util.Vector;
import javafx.scene.image.Image;
//import javafx.scene.image.WritableImage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

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
	public int power=2;
	public Image meImg;
	private static Image foxChar_L= new Image("image/fox-run-alt.png");
	private static Image foxChar_R= new Image("image/fox-run.png");
//	private static Image leaves_L= new Image("image/Leaves_180x180_14.png");
//	private static Image leaves_R= new Image("image/Leaves_180x180_14_alter.png");
	private static Image bushAlt= new Image("image/bush_tile_alter.png");
	private static PixelReader frameReader;
//	private static PixelReader charFrameReader_R = foxChar_R.getPixelReader();
	private Vector<Image> charFrames;
	private Vector<Image> charFrames_L= new Vector<>();
	private Vector<Image> charFrames_R= new Vector<>();
//	private Vector<Image> leafFrames_L= new Vector<>();
//	private Vector<Image> leafFrames_R= new Vector<>();
	private int frameCount;
	private int maxFrame;
	private int frameInterval;

	private Canvas drawingCanvas;
	public double abs_x;
	public double abs_y;
	private double dx;
	private double dy;
	public int[] obsStatus= new int[5];
	public int[] bombStatus= new int[5];
	public Vector<Bomb> bombVector= new Vector<Bomb>(5);
	private int bombCount= 0;
	private int maxBombCount;

	File fxFile = new File("audio/FX/sfx_movement_footsteps5.wav");
	private Media fx;
	private MediaPlayer fxPlayer;

	File deathFxFile = new File("audio/FX/sfx_lowhealth_alarmloop5.wav");
	private Media deathFx;
	private MediaPlayer deathFxPlayer;

	Player(int x, int y, Image meImg){
		this.x= x;
		this.y= y;
		abs_x= GameController.canvasXOffset+x*40;
		abs_y= GameController.canvasYOffset+y*40;
		this.meImg= meImg;
		dx = 0;
		dy = 0;
//		this.drawingCanvas= drawingCanvas;
//		moveTimer = new Timer(true);
		maxBombCount = 5;

		maxFrame= 6;
		frameCount= 0;
		frameInterval= 1;
		for (int i=1; i<=6; i++){
			frameReader = foxChar_L.getPixelReader();
			charFrames_L.add(getFrame(i,1,24));
			frameReader = foxChar_R.getPixelReader();
			charFrames_R.add(getFrame(i,1,24));
		}
//		for (int i=1; i<=14; i++){
//			frameReader= leaves_L.getPixelReader();
//			leafFrames_L.add(getFrame(i,1,180));
//			frameReader= leaves_R.getPixelReader();
//			leafFrames_R.add(getFrame(i,1,180));
//		}
		charFrames= charFrames_R;

		fx= new Media(fxFile.toURI().toString());
		fxPlayer= new MediaPlayer(fx);
		fxPlayer.setVolume(GameController.volume);

		deathFx= new Media(deathFxFile.toURI().toString());
		deathFxPlayer= new MediaPlayer(deathFx);
		deathFxPlayer.setVolume(GameController.volume);

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
//				renderTiles();
//				if(obsStatus[Player.CENTER]==-1)
//					drawMe();
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
//				renderTiles();
//				if(obsStatus[Player.DOWN]==-1)
//					drawMe();
			}
//			navFlag[DOWN]=true;
		}

		if(GameController.pressed.contains(KeyCode.LEFT)){//LEFT
			checkAround();

			charFrames= charFrames_L;


			charFrames= charFrames_L;
			if( (obsStatus[Player.LEFT]==-1 || obsStatus[Player.LEFT]==4) && bombStatus[Player.LEFT]==0)
			{
				move(Player.LEFT);
				System.out.println("MOVED LEFT");
				//print the character
//				renderTiles();
//				if(obsStatus[Player.LEFT]==-1)
//					drawMe();
			}
//			navFlag[LEFT]=true;
		}

		if(GameController.pressed.contains(KeyCode.RIGHT)){//RIGHT
			checkAround();

			charFrames= charFrames_R;


			if( (obsStatus[Player.RIGHT]==-1 || obsStatus[Player.RIGHT]==4) && bombStatus[Player.RIGHT]==0)
			{
				move(Player.RIGHT);
				System.out.println("MOVED RIGHT");
				//print the character
//				renderTiles();
//				if(obsStatus[Player.RIGHT]==-1)
//					drawMe();
			}
//			navFlag[RIGHT]=true;
		}

		if(GameController.pressed.contains(KeyCode.SPACE)){//SPACE
			checkAround();
//			if(bombCount < 5){

				if (GameController.tileVec.get(y*17+x).getBombStatus() ==0 ) {

//					bombCount++;
					System.out.println("\tIN SPACE "+bombCount);

					GameController.tileVec.get(17*y+x).setBombStatus(1);
//					renderTiles();
//					if(obsStatus[Player.CENTER]==-1)
//						drawMe(drawingCanvas);

					Bomb bomb= new Bomb(this,drawingCanvas);

//                    bombVector.add(new Bomb(this,drawingCanvas));
//                    bombVector.elementAt(bombCount).putBomb(x, y);
                    bomb.putBomb(x,y);

//                }
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
				if(GameController.inObsTest){
					dy=-1;
					if((abs_x-GameController.canvasXOffset)%40==0)
						abs_y-=10;
					changePosition();
				}
				else{
					y--;
				}

				break;
			case DOWN:
//				incY++;
//				if(incY==20){
//					y++;
//				}
//				if(incY>=40)
//					incY=0;
				if(GameController.inObsTest){
					dy=1;
					if((abs_x-GameController.canvasXOffset)%40==0)
						abs_y+=10;
					changePosition();
				}
				else{
					y++;
				}

				break;
			case LEFT:
//				incX--;
//				if(incX==-20){
//					x--;
//				}
//				if(incX<=-40)
//					incX=0;
				if(GameController.inObsTest){
					dx=-1;
					if((abs_y-GameController.canvasYOffset)%40==0)
						abs_x-=10;
					changePosition();
				}
				else{
					x--;
				}


				break;
			case RIGHT:
//				incX++;
//				if(incX==20){
//					x++;
//				}
//				if(incX>=40)
//					incX=0;
				if(GameController.inObsTest){
					dx=1;
					if((abs_y-GameController.canvasYOffset)%40==0)
						abs_x+=10;
					changePosition();
				}
				else {
					x++;
				}

				break;
			default:
				break;

		}

		frameCount++;
		if(frameCount%2==0){
//			fxPlayer= new MediaPlayer(fx);
			fxPlayer.stop();
			fxPlayer.play();
		}
		if(frameCount>frameInterval*(maxFrame-1))
			frameCount=0;


	}

	private void changePosition() {
		if((abs_x- GameController.canvasXOffset)%40==0 && (abs_y-GameController.canvasYOffset)%40==0){
			x=(int)((abs_x+20-GameController.canvasXOffset)/40);
			y=(int)((abs_y+20-GameController.canvasYOffset)/40);
		}

		System.out.println("abs_x= "+abs_x+", x= "+x);
		System.out.println("abs_y= "+abs_y+", y= "+y);
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
		GraphicsContext gb = drawingCanvas.getGraphicsContext2D();
		checkAround();
		if(obsStatus[CENTER]==-1){
			if(GameController.inObsTest){
				if(bombStatus[CENTER]<= -1 && bombStatus[CENTER]>= -5){
					x=1; y=1;
					abs_x= GameController.canvasXOffset+x*40;
					abs_y= GameController.canvasYOffset+y*40;
				}
				gb.drawImage(charFrames.elementAt(frameCount/frameInterval), abs_x, abs_y, 40, 40);
			}
			else{

				if(bombStatus[CENTER]<= -1 && bombStatus[CENTER]>= -5){
					x=1; y=1;
				}
				gb.drawImage(charFrames.elementAt(frameCount/frameInterval), GameController.canvasXOffset+40*x, GameController.canvasYOffset+40*y, 40, 40);
			}
		}
		else if(obsStatus[CENTER]== 4){

			if(bombStatus[CENTER]<= -1 && bombStatus[CENTER]>= -5){
				x=1; y=1;
				abs_x= GameController.canvasXOffset+x*40;
				abs_y= GameController.canvasYOffset+y*40;
			}
			gb.drawImage(bushAlt, GameController.canvasXOffset+40*x, GameController.canvasYOffset+40*y, 40, 40);

		}


	}
	public void catchCanvas(Canvas drawingCanvas){
		this.drawingCanvas= drawingCanvas;
		bombVector.clear();
		for(int i=0; i< maxBombCount; i++){
			bombVector.add(new Bomb(this, drawingCanvas));
		}
	}

	public void drawMe(Canvas drawingCanvas){
		GraphicsContext gb = drawingCanvas.getGraphicsContext2D();
		checkAround();
		if(GameController.inObsTest){
			if(bombStatus[CENTER]<= -1 && bombStatus[CENTER]>= -5){
				x=1; y=1;
				abs_x= GameController.canvasXOffset+x*40;
				abs_y= GameController.canvasYOffset+y*40;

			}
			gb.drawImage(charFrames.elementAt(frameCount/frameInterval), abs_x, abs_y, 40, 40);
		}
		else{

			if(bombStatus[CENTER]<= -1 && bombStatus[CENTER]>= -5){
				x=1; y=1;
			}
			gb.drawImage(charFrames.elementAt(frameCount/frameInterval), GameController.canvasXOffset+40*x, GameController.canvasYOffset+40*y, 40, 40);
		}
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

	public int getBombCount() {
		return bombCount;
	}
	public void setBombCount(int bombCount){
		this.bombCount= bombCount;
	}

	public void dXYReset(){
		dx =0; dy =0;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public static WritableImage getFrame(int idX, int idY, int size){
		//WritableImage rtnTile = new WritableImage(charFrameReader_L, 16, 16, 16, 16);
		return new WritableImage(frameReader, size*(idX-1), size*(idY-1), size, size);
	}

	public void resetFxPlayer(){
		fxPlayer.setVolume(GameController.volume);
	}
	public void resetDeathFxPlayerPlayer(){
		deathFxPlayer.setVolume(GameController.volume);
	}
}
