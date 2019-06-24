package game;

import java.io.File;
import java.util.List;
import java.util.Vector;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class Player{
	public static final int CENTER=0;
	public static final int UP=1;
	public static final int DOWN=2;
	public static final int LEFT=3;
	public static final int RIGHT=4;
	public static final int LEFTUP=5;
	public static final int LEFTDOWN=6;
	public static final int RIGHTUP=7;
	public static final int RIGHTDOWN=8;
//	public enum Nav{UP, DOWN, LEFT, RIGHT};
//	private Boolean[] navFlag= new Boolean[5];

	private Timer moveTimer;
	private TimerTask moveTask;

	public int x;
	public int y;

	public int speed=2;
	public int power=1;
	public int quant=2;

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
//	private int frameInterval;

	private Canvas drawingCanvas;
	private ImageView speedStat;
	private ImageView powerStat;
	private ImageView quantStat;

	public double abs_x;
	public double abs_y;
	private double dx;
	private double dy;
	private double inc=5;
	public int nowDir=0;

	public int[] obsStatus= new int[9];
	public int[] bombStatus= new int[9];
	public Vector<Bomb> bombVector= new Vector<Bomb>(5);
	public int bombCount= 0;
	private int maxBombCount;

	File fxFile = new File("audio/FX/sfx_movement_footsteps5.wav");
	private Media fx;
	public static MediaPlayer fxPlayer;

	File itemFxFile = new File("audio/FX/sfx_coin_double5.wav");
	private Media itemFx;
	public static MediaPlayer itemFxPlayer;

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
//		maxBombCount = 5;

		maxFrame= 6;
		frameCount= 0;
//		frameInterval= 1;
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
		fxPlayer.setVolume(GameController.fxVolume);

		itemFx= new Media(itemFxFile.toURI().toString());
		itemFxPlayer= new MediaPlayer(itemFx);
		itemFxPlayer.setVolume(GameController.fxVolume);

		deathFx= new Media(deathFxFile.toURI().toString());
		deathFxPlayer= new MediaPlayer(deathFx);
		deathFxPlayer.setVolume(GameController.fxVolume);

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
			if( (obsStatus[Player.UP]==-1 || obsStatus[Player.UP]==4) && bombStatus[Player.UP]<=0)
			{
				move(Player.UP);
				System.out.println("MOVED UP");
				if( GameController.pressed.contains(KeyCode.LEFT) &&
						(obsStatus[Player.LEFTUP]==-1 || obsStatus[Player.LEFTUP]==4) && bombStatus[Player.LEFTUP]<=0) {
					move(Player.LEFT);
				}


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

//		checkAround();
//		KeyCode head=GameController.pressed.get(0);
//		if(GameController.pressed.contains(KeyCode.UP) && GameController.pressed.size()==1){
//			if((obsStatus[UP]==-1 || obsStatus[UP]==4) && bombStatus[UP]<=0) {
//				move(UP);
//				nowDir=UP;
//			}
//			else
//				nowDir=0;
//		}
//		if(GameController.pressed.contains(KeyCode.DOWN) && GameController.pressed.size()==1){
//			if((obsStatus[DOWN]==-1 || obsStatus[DOWN]==4) && bombStatus[DOWN]<=0) {
//				move(DOWN);
//				nowDir=DOWN;
//			}
//			else
//				nowDir=0;
//		}
//		if(GameController.pressed.contains(KeyCode.LEFT) && GameController.pressed.size()==1){
//			if((obsStatus[LEFT]==-1 || obsStatus[LEFT]==4) && bombStatus[LEFT]<=0) {
//				move(LEFT);
//				nowDir=LEFT;
//			}
//			else
//				nowDir=0;
//		}
//		if(GameController.pressed.contains(KeyCode.RIGHT) && GameController.pressed.size()==1){
//			if((obsStatus[RIGHT]==-1 || obsStatus[RIGHT]==4) && bombStatus[RIGHT]<=0) {
//				move(RIGHT);
//				nowDir=RIGHT;
//			}
//			else
//				nowDir=0;
//		}
//
//		if(GameController.pressed.contains(KeyCode.UP)
//				&& GameController.pressed.contains(KeyCode.LEFT)
//				&& GameController.pressed.size()==2){
//			if((obsStatus[UP]==-1 || obsStatus[UP]==4) && bombStatus[UP]<=0) {
//				if((obsStatus[LEFT]==-1 || obsStatus[LEFT]==4) && bombStatus[LEFT]<=0) {
//					if((obsStatus[LEFTUP]==-1 || obsStatus[LEFTUP]==4) && bombStatus[LEFTUP]<=0) {
//						move(LEFTUP);
//					}
//					else
//						move(nowDir);
//				}
//			}
//		}
//
//		if(head==KeyCode.UP){
//			if((obsStatus[UP]==-1 || obsStatus[UP]==4) && bombStatus[UP]<=0)
//			if(GameController.pressed.contains(KeyCode.LEFT)){
//				if((obsStatus[LEFT]==-1 || obsStatus[LEFT]==4) && bombStatus[LEFT]<=0)
//				if((obsStatus[LEFTUP]==-1 || obsStatus[LEFTUP]==4) && bombStatus[LEFTUP]<=0)
//				move(LEFTUP);
//				else
//					move(UP);
//				else
//					move(UP);
//			}
//			else if(GameController.pressed.contains(KeyCode.RIGHT)){
//				if((obsStatus[RIGHT]==-1 || obsStatus[RIGHT]==4) && bombStatus[RIGHT]<=0)
//				if((obsStatus[RIGHTUP]==-1 || obsStatus[RIGHTUP]==4) && bombStatus[RIGHTUP]<=0)
//				move(RIGHTUP);
//				else
//					move(UP);
//				else
//					move(UP);
//			}
//			else
//				move(UP);
//		}
//		else if(head==KeyCode.DOWN){
//			if((obsStatus[DOWN]==-1 || obsStatus[DOWN]==4) && bombStatus[DOWN]<=0)
//			if(GameController.pressed.contains(KeyCode.LEFT)){
//				if((obsStatus[LEFT]==-1 || obsStatus[LEFT]==4) && bombStatus[LEFT]<=0)
//				if((obsStatus[LEFTDOWN]==-1 || obsStatus[LEFTDOWN]==4) && bombStatus[LEFTDOWN]<=0)
//					move(LEFTDOWN);
//				else
//					move(DOWN);
//				else
//					move(DOWN);
//			}
//			else if(GameController.pressed.contains(KeyCode.RIGHT)){
//				if((obsStatus[RIGHT]==-1 || obsStatus[RIGHT]==4) && bombStatus[RIGHT]<=0)
//				if((obsStatus[RIGHTDOWN]==-1 || obsStatus[RIGHTDOWN]==4) && bombStatus[RIGHTDOWN]<=0)
//					move(RIGHTDOWN);
//				else
//					move(DOWN);
//				else
//					move(DOWN);
//			}
//			else
//				move(DOWN);
//		}
//		else if(head==KeyCode.LEFT){
//			if((obsStatus[LEFT]==-1 || obsStatus[LEFT]==4) && bombStatus[LEFT]<=0)
//			if(GameController.pressed.contains(KeyCode.UP)){
//				if((obsStatus[UP]==-1 || obsStatus[UP]==4) && bombStatus[UP]<=0)
//				if((obsStatus[LEFTUP]==-1 || obsStatus[LEFTUP]==4) && bombStatus[LEFTUP]<=0)
//					move(LEFTUP);
//				else
//					move(LEFT);
//				else
//					move(LEFT);
//			}
//			else if(GameController.pressed.contains(KeyCode.DOWN)){
//				if((obsStatus[DOWN]==-1 || obsStatus[DOWN]==4) && bombStatus[DOWN]<=0)
//				if((obsStatus[LEFTDOWN]==-1 || obsStatus[LEFTDOWN]==4) && bombStatus[LEFTDOWN]<=0)
//					move(LEFTDOWN);
//				else
//					move(LEFT);
//				else
//					move(LEFT);
//			}
//			else
//				move(LEFT);
//		}
//		else if(head==KeyCode.RIGHT){
//			if((obsStatus[RIGHT]==-1 || obsStatus[RIGHT]==4) && bombStatus[RIGHT]<=0)
//			if(GameController.pressed.contains(KeyCode.UP)){
//				if((obsStatus[UP]==-1 || obsStatus[UP]==4) && bombStatus[UP]<=0)
//				if((obsStatus[RIGHTUP]==-1 || obsStatus[RIGHTUP]==4) && bombStatus[RIGHTUP]<=0)
//					move(RIGHTUP);
//				else
//					move(RIGHT);
//				else
//					move(RIGHT);
//			}
//			else if(GameController.pressed.contains(KeyCode.DOWN)){
//				if((obsStatus[DOWN]==-1 || obsStatus[DOWN]==4) && bombStatus[DOWN]<=0)
//				if((obsStatus[RIGHTDOWN]==-1 || obsStatus[RIGHTDOWN]==4) && bombStatus[RIGHTDOWN]<=0)
//					move(RIGHTDOWN);
//				else
//					move(RIGHT);
//				else
//					move(RIGHT);
//			}
//			else
//				move(RIGHT);
//		}


//		if(GameController.pressed.contains(KeyCode.UP)){//UP
//
////			System.out.println("UP");
//			checkAround();
//			if( (obsStatus[Player.UP]==-1 || obsStatus[Player.UP]==4) && bombStatus[Player.UP]<=0)
//			{
//				move(Player.UP);
//				System.out.println("MOVED UP");
//			}
//
////			if( (obsStatus[UP]>=0 && obsStatus[UP]<=3)
////					|| obsStatus[UP]==5 || bombStatus[UP]==1)
////			{
////				System.out.println("UP Rejected");
////			}
////			else{
////				if(GameController.pressed.contains(KeyCode.LEFT))
////					if( (obsStatus[LEFTUP]>=0 && obsStatus[LEFTUP]<=3)
////							|| obsStatus[LEFTUP]==5 || bombStatus[LEFTUP]==1)
////					{
////						System.out.println("LEFTUP Rejected");
////					}
////					else
////						move(LEFT);
////
////				if(GameController.pressed.contains(KeyCode.RIGHT))
////					if( (obsStatus[RIGHTUP]>=0 && obsStatus[RIGHTUP]<=3)
////							|| obsStatus[RIGHTUP]==5 || bombStatus[RIGHTUP]==1)
////					{
////						System.out.println("RIGHTUP Rejected");
////					}
////					else
////						move(RIGHT);
////
////				move(UP);
////			}
////			navFlag[UP]=true;
//		}
//
//		else if(GameController.pressed.contains(KeyCode.DOWN)){//DOWN
//			checkAround();
//			if( (obsStatus[Player.DOWN]==-1 || obsStatus[Player.DOWN]==4) && bombStatus[Player.DOWN]<=0)
//			{
//				move(Player.DOWN);
//				System.out.println("MOVED DOWN");
//			}
//
////			if( (obsStatus[DOWN]>=0 && obsStatus[DOWN]<=3)
////					|| obsStatus[DOWN]==5 || bombStatus[DOWN]==1)
////			{
////				System.out.println("DOWN Rejected");
////			}
////			else{
////				if(GameController.pressed.contains(KeyCode.LEFT))
////					if( (obsStatus[LEFTDOWN]>=0 && obsStatus[LEFTDOWN]<=3)
////							|| obsStatus[LEFTDOWN]==5 || bombStatus[LEFTDOWN]==1)
////					{
////						System.out.println("LEFTDOWN Rejected");
////					}
////					else
////						move(LEFT);
////
////				if(GameController.pressed.contains(KeyCode.RIGHT))
////					if( (obsStatus[RIGHTDOWN]>=0 && obsStatus[RIGHTDOWN]<=3)
////							|| obsStatus[RIGHTDOWN]==5 || bombStatus[RIGHTDOWN]==1)
////					{
////						System.out.println("RIGHTDOWN Rejected");
////					}
////					else
////						move(RIGHT);
////
////				move(DOWN);
////			}
////			navFlag[DOWN]=true;
//		}
//
//		else if(GameController.pressed.contains(KeyCode.LEFT)){//LEFT
//			checkAround();
//
//			charFrames= charFrames_L;
//
//			if( (obsStatus[Player.LEFT]==-1 || obsStatus[Player.LEFT]==4) && bombStatus[Player.LEFT]<=0)
//			{
//				move(Player.LEFT);
//				System.out.println("MOVED LEFT");
//			}
//
////			if( (obsStatus[LEFT]>=0 && obsStatus[LEFT]<=3)
////					|| obsStatus[LEFT]==5 || bombStatus[LEFT]==1)
////			{
////				System.out.println("LEFT Rejected");
////			}
////			else{
////				if(GameController.pressed.contains(KeyCode.UP))
////					if( (obsStatus[LEFTUP]>=0 && obsStatus[LEFTUP]<=3)
////							|| obsStatus[LEFTUP]==5 || bombStatus[LEFTUP]==1)
////					{
////						System.out.println("LEFTUP Rejected");
////					}
////					else
////						move(UP);
////
////				if(GameController.pressed.contains(KeyCode.DOWN))
////					if( (obsStatus[LEFTDOWN]>=0 && obsStatus[LEFTDOWN]<=3)
////							|| obsStatus[LEFTDOWN]==5 || bombStatus[LEFTDOWN]==1)
////					{
////						System.out.println("LEFTDOWN Rejected");
////					}
////					else
////						move(DOWN);
////
////				move(LEFT);
////			}
////			navFlag[LEFT]=true;
//		}
//
//		else if(GameController.pressed.contains(KeyCode.RIGHT)){//RIGHT
//			checkAround();
//
//			charFrames= charFrames_R;
//
//
//			if( (obsStatus[Player.RIGHT]==-1 || obsStatus[Player.RIGHT]==4) && bombStatus[Player.RIGHT]<=0)
//			{
//				move(Player.RIGHT);
//				System.out.println("MOVED RIGHT");
//			}
//
////			if( (obsStatus[RIGHT]>=0 && obsStatus[RIGHT]<=3)
////					|| obsStatus[RIGHT]==5 || bombStatus[RIGHT]==1)
////			{
////				System.out.println("RIGHT Rejected");
////			}
////			else{
////				if(GameController.pressed.contains(KeyCode.UP))
////					if( (obsStatus[RIGHTUP]>=0 && obsStatus[RIGHTUP]<=3)
////							|| obsStatus[RIGHTUP]==5 || bombStatus[RIGHTUP]==1)
////					{
////						System.out.println("RIGHTUP Rejected");
////					}
////					else
////						move(UP);
////
////				if(GameController.pressed.contains(KeyCode.DOWN))
////					if( (obsStatus[RIGHTDOWN]>=0 && obsStatus[RIGHTDOWN]<=3)
////							|| obsStatus[RIGHTDOWN]==5 || bombStatus[RIGHTDOWN]==1)
////					{
////						System.out.println("RIGHTDOWN Rejected");
////					}
////					else
////						move(DOWN);
////
////				move(RIGHT);
////			}
////			navFlag[RIGHT]=true;
//		}

		if(GameController.pressed.contains(KeyCode.SPACE)){//SPACE
			checkAround();
			if(bombCount < quant){

				if (GameController.tileVec.get(y*17+x).getBombStatus() ==0 ) {


					System.out.println("\tIN SPACE "+bombCount);

					GameController.tileVec.get(17*y+x).setBombStatus(1);

					Bomb bomb= new Bomb(this);

//                    bombVector.add(new Bomb(this,drawingCanvas));
//                    bombVector.elementAt(bombCount).putBomb(x, y);
                    bomb.putBomb(x,y);
					bombCount++;

                }
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
				if(GameController.inObsTest){
					dy=-1;
					if(Math.abs(abs_x-GameController.canvasXOffset)%40==0)
						abs_y-=inc;
					changePosition();
				}
				else{
					y--;
				}

				break;
			case DOWN:
				if(GameController.inObsTest){
					dy=1;
					if(Math.abs(abs_x-GameController.canvasXOffset)%40==0)
						abs_y+=inc;
					changePosition();
				}
				else{
					y++;
				}

				break;
			case LEFT:
				if(GameController.inObsTest){
					dx=-1;
					if(Math.abs(abs_y-GameController.canvasYOffset)%40==0)
						abs_x-=inc;
					changePosition();
				}
				else{
					x--;
				}


				break;
			case RIGHT:
				if(GameController.inObsTest){
					dx=1;
					if(Math.abs(abs_y-GameController.canvasYOffset)%40==0)
						abs_x+=inc;
					changePosition();
				}
				else {
					x++;
				}

				break;

			case LEFTUP:
				if(GameController.inObsTest){
//					if(Math.abs(abs_y-GameController.canvasYOffset)%40==0)

					abs_x-=inc;
					abs_y-=inc;
					changePosition();
				}
				else {
					x--;
					y--;
				}
				break;
			case LEFTDOWN:
				if(GameController.inObsTest){
//					if(Math.abs(abs_y-GameController.canvasYOffset)%40==0)

					abs_x-=inc;
					abs_y+=inc;
					changePosition();
				}
				else {
					x--;
					y++;
				}
				break;
			case RIGHTUP:
				if(GameController.inObsTest){
//					if(Math.abs(abs_y-GameController.canvasYOffset)%40==0)

					abs_x+=inc;
					abs_y-=inc;
					changePosition();
				}
				else {
					x++;
					y--;
				}
				break;
			case RIGHTDOWN:
				if(GameController.inObsTest){
//					if(Math.abs(abs_y-GameController.canvasYOffset)%40==0)

					abs_x+=inc;
					abs_y+=inc;
					changePosition();
				}
				else {
					x++;
					y++;
				}
				break;

			default:
				break;

		}

		if(frameCount<(maxFrame-1))
			frameCount++;
		else
			frameCount=0;
		if(frameCount%2==0){
//			fxPlayer= new MediaPlayer(fx);
			fxPlayer.seek(Duration.ZERO);
			fxPlayer.play();
		}
//		if(frameCount>frameInterval*(maxFrame-1))
//			frameCount=0;


	}

	private void changePosition() {
		if(Math.abs(abs_x- GameController.canvasXOffset)%40==0 && Math.abs(abs_y-GameController.canvasYOffset)%40==0){
			x=(int)((abs_x+20-GameController.canvasXOffset)/40);
			y=(int)((abs_y+20-GameController.canvasYOffset)/40);
		}
//		switch (dir){
//			case UP:
//				x=(int)((abs_x+20-GameController.canvasXOffset)/40);
//				y=(int)((abs_y+40-GameController.canvasYOffset)/40);
//				break;
//			case DOWN:
//				x=(int)((abs_x+20-GameController.canvasXOffset)/40);
//				y=(int)((abs_y-GameController.canvasYOffset)/40);
//				break;
//			case LEFT:
//				x=(int)((abs_x+40-GameController.canvasXOffset)/40);
//				y=(int)((abs_y+20-GameController.canvasYOffset)/40);
//				break;
//			case RIGHT:
//				x=(int)((abs_x-GameController.canvasXOffset)/40);
//				y=(int)((abs_y+20-GameController.canvasYOffset)/40);
//				break;
//			default:
//				x=(int)((abs_x+20-GameController.canvasXOffset)/40);
//				y=(int)((abs_y+20-GameController.canvasYOffset)/40);
//				break;
//		}

		switch (GameController.tileVec.get(x+17*y).getItemStatus()){
			case 1:
				if(speed<7) {
					speed++;
					switch (speed){
						case 1:
							inc=4;
							break;
						case 2:
							inc=5;
							break;
						case 3:
							inc=8;
							break;
						case 4:
							inc=10;
							break;
						case 5:
							inc=20;
							break;
						default:
							break;
					}
					speedStat.setImage(Item.SPEED[speed]);
					itemFxPlayer.seek(Duration.ZERO);
					itemFxPlayer.play();
					System.out.println("\t\t###Speed up= "+speed);
				}
				GameController.tileVec.get(x+17*y).setItemStatus(0);
			case 2:
				if(power<5) {
					power++;
					powerStat.setImage(Item.POWER[power]);
					itemFxPlayer.seek(Duration.ZERO);
					itemFxPlayer.play();
					System.out.println("\t\t###Power up= "+power);
				}
				GameController.tileVec.get(x+17*y).setItemStatus(0);
				break;
			case 3:
				if(quant<5) {
					quant++;
					quantStat.setImage(Item.QUANT[quant]);
					itemFxPlayer.seek(Duration.ZERO);
					itemFxPlayer.play();
					System.out.println("\t\t###Quant up= "+quant);
				}
				GameController.tileVec.get(x+17*y).setItemStatus(0);
				break;
			default:
				GameController.tileVec.get(x+17*y).setItemStatus(0);
				break;
		}

		switch (GameController.tileVec.get(x+17*y).getBombStatus()){
			case -5:
			case -4:
			case -3:
			case -2:
			case -1:
				x=1; y=1;
				abs_x= GameController.canvasXOffset+x*40;
				abs_y= GameController.canvasYOffset+y*40;
				break;

			default:
				break;
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

		obsStatus[LEFTUP]= GameController.tileVec.get(x-1+17*(y-1)).getObs();
		obsStatus[LEFTDOWN]= GameController.tileVec.get(x-1+17*(y+1)).getObs();
		obsStatus[RIGHTUP]= GameController.tileVec.get(x+1+17*(y-1)).getObs();
		obsStatus[RIGHTDOWN]= GameController.tileVec.get(x+1+17*(y+1)).getObs();

		bombStatus[CENTER]= GameController.tileVec.get(x+17*y).getBombStatus();
		bombStatus[UP]= GameController.tileVec.get(x+17*(y-1)).getBombStatus();
		bombStatus[DOWN]= GameController.tileVec.get(x+17*(y+1)).getBombStatus();
		bombStatus[LEFT]= GameController.tileVec.get(x-1+17*y).getBombStatus();
		bombStatus[RIGHT]= GameController.tileVec.get(x+1+17*y).getBombStatus();

		bombStatus[LEFTUP]= GameController.tileVec.get(x-1+17*(y-1)).getBombStatus();
		bombStatus[LEFTDOWN]= GameController.tileVec.get(x-1+17*(y+1)).getBombStatus();
		bombStatus[RIGHTUP]= GameController.tileVec.get(x+1+17*(y-1)).getBombStatus();
		bombStatus[RIGHTDOWN]= GameController.tileVec.get(x+1+17*(y+1)).getBombStatus();
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
				gb.drawImage(charFrames.elementAt(frameCount), abs_x, abs_y, 40, 40);
			}
			else{

				if(bombStatus[CENTER]<= -1 && bombStatus[CENTER]>= -5){
					x=1; y=1;
				}
				gb.drawImage(charFrames.elementAt(frameCount), GameController.canvasXOffset+40*x, GameController.canvasYOffset+40*y, 40, 40);
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

	}
	public void catchImageViews(ImageView speedStat, ImageView powerStat, ImageView quantStat){
		this.speedStat= speedStat;
		this.powerStat= powerStat;
		this.quantStat= quantStat;
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

	private static WritableImage getFrame(int idX, int idY, int size){
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
