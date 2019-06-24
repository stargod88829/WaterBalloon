package game;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {
	@FXML private Canvas drawingCanvas;
//	@FXML private Button obsTestBtn;
//	@FXML private Button kbTestBtn;
//	@FXML private Label fpsLabel;
//	@FXML private Slider fpsSlider;
//	@FXML private Label bombRangeLabel;
//	@FXML private Slider bombRangeSlider;
//	@FXML private Label volumeLabel;
//	@FXML private Slider volumeSlider;
	@FXML private Label cdTimerLabel;
	@FXML private Label counterLabel;
	@FXML private Label lifeLeftLabel;

	private Timeline cdTimer;
	private int timeLeft=180;

	@FXML private ImageView speedStat;
	@FXML private ImageView powerStat;
	@FXML private ImageView quantStat;

	@FXML private Button settingsBtn;

	private Timer aniTimer=new Timer(true);
	private CanvasRepaint<Player> task;
	// @FXML private Button upBtn;
	// @FXML private Button downBtn;
	// @FXML private Button leftBtn;
	// @FXML private Button rightBtn;

//	public final BooleanProperty[] isPressed = new BooleanProperty[5];
//	public final BooleanBinding[][] bothPressed = new BooleanBinding[5][5];

	public static final int X_BLOCKS=17;
	public static final int Y_BLOCKS=15;

	public static boolean inObsTest= true;
	public static boolean inKBTest= true;
//	public static final Set<KeyCode> pressed = new HashSet<KeyCode>();
	public static final ArrayList<KeyCode> pressed = new ArrayList<KeyCode>();


	public static int meX=1;
	public static int meY=1;
	private Image p1Img= new Image("image/ME.png");
	private Player p1=new Player(1,1, p1Img);

	private static Image tileSet= new Image("image/Dungeon_Tileset.png");
	private static PixelReader backReader = tileSet.getPixelReader();
	public static int canvasXOffset= 160;
	public static int canvasYOffset= 20;
	public static int fps= 10;

	private String mapDataStr;
	private String obstacleDataStr;
	private String bombsDataStr;
	public static Vector<Tile> tileVec= new Vector<>();

	File bgmFile = new File("audio/BGM/TheAdventureBegins8-bitRemix.wav");
	//@https://opengameart.org/content/the-adventure-begins-8-bit-remix
	private Media bgm;
	public static MediaPlayer bgmPlayer;

	public static double volume= 1;
	public static double fxVolume= 1;

	public void initialize() {
		GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0,0,drawingCanvas.getWidth(),drawingCanvas.getHeight());

		speedStat.setImage(Item.SPEED[p1.speed]);
		powerStat.setImage(Item.POWER[p1.power]);
		quantStat.setImage(Item.QUANT[p1.quant]);

		settingsBtn.setFocusTraversable(false);

		p1.catchCanvas(drawingCanvas);
		p1.catchImageViews(speedStat, powerStat, quantStat);
		drawingCanvas.getParent().addEventFilter(KeyEvent.KEY_PRESSED,
				event -> {
					keyPressed(event);
//					event.consume();
				}
		);
		drawingCanvas.getParent().addEventFilter(KeyEvent.KEY_RELEASED,
				event -> {
					keyReleased(event);
//					event.consume();
				}
		);

		System.out.println(drawingCanvas.getParent().toString());

		loadDataFile("game/MapData.txt", "game/ObstacleData.txt", "game/BombsData.txt");
		renderTiles();

		p1.drawMe();
		task = new CanvasRepaint<>(drawingCanvas) {
			@Override
			public void repaint(GraphicsContext gc, Player player) {
				renderTiles();
				player.drawMe();
			}
		};

		aniTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				task.requestRepaint(p1);
			}
		},0 , (int)(1./fps*1000));

//		fpsSlider.valueProperty().addListener(
//				(ov, oldValue, newValue) -> {
//					fps = newValue.intValue();
//					fpsLabel.setText("FPS: "+fps);
//					aniTimer.cancel();
//					aniTimer= new Timer(true);
//					aniTimer.schedule(new TimerTask() {
//						@Override
//						public void run() {
//							task.requestRedraw(p1);
//						}
//					},0,(int)(1./fps*1000));
//					System.out.println("Interval Updated= "+(int)(1./fps*1000));
//				}
//		);
//		fpsSlider.setValue(fps);
//
//		bombRangeSlider.valueProperty().addListener(
//				(ov, oldValue, newValue) -> {
//					int bombRange = newValue.intValue();
//					bombRangeLabel.setText("Bomb Range: "+bombRange);
//					p1.setPower(bombRange);
//					System.out.println("Bomb Range Updated: "+bombRange);
//				}
//		);
//
//		volumeSlider.valueProperty().addListener(
//				(ov, oldValue, newValue) -> {
//					int volPercent = newValue.intValue();
//					volumeLabel.setText("Media Volume: "+volPercent+"%");
//					volume= volPercent/100.0;
//
//					bgmPlayer.setVolume(0.6 * volume);
//					p1.resetFxPlayer();
//					p1.resetDeathFxPlayerPlayer();
//					System.out.println("Volume Updated: "+volume);
//				}
//		);
//		volumeSlider.setValue(volume*100);

		bgm= new Media(bgmFile.toURI().toString());
		bgmPlayer= new MediaPlayer(bgm);
		bgmPlayer.setVolume(0.6 * volume);
		bgmPlayer.setAutoPlay(true);
		bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		cdTimerLabel.setText(""+timeLeft);
		cdTimer = new Timeline();
		cdTimer.setCycleCount(Timeline.INDEFINITE);
		cdTimer.getKeyFrames().add(
			new KeyFrame(Duration.seconds(1),
					(e) -> {
						timeLeft--;
						cdTimerLabel.setText(""+timeLeft);
						if (timeLeft <= 0) {
							cdTimer.stop();
						}
					}
			)
		);
		cdTimer.playFromStart();
	}

//	ChangeListener<Boolean> boolHandler= new ChangeListener<Boolean>() {
//		@Override
//		public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//
//		}
//	};


//	@FXML
//	private void obsTestBtnPressed(ActionEvent e) {
//		System.out.println("Player1 @("+p1.x+","+p1.y+"), abs= ("+p1.abs_x+","+p1.abs_y+")");
//		if(!inObsTest){
//			p1.abs_x= canvasXOffset+p1.x*40;
//			p1.abs_y= canvasYOffset+p1.y*40;
//			obsTestBtn.setStyle("-fx-border-color: RED; ");
//			obsTestBtn.setText("Stop");
//			inObsTest= true;
//		}
//		else{
//			obsTestBtn.setStyle("-fx-border-color: LIGHTGREEN; ");
//			obsTestBtn.setText("StartObsTest");
//			inObsTest= false;
//		}
//	}
//	@FXML
//	private void kbTestBtnPressed(ActionEvent e) {
//		if(!inKBTest){
//			kbTestBtn.setStyle("-fx-border-color: RED; ");
//			kbTestBtn.setText("Stop");
//			inKBTest= true;
//		}
//		else{
//			kbTestBtn.setStyle("-fx-border-color: LIGHTGREEN; ");
//			kbTestBtn.setText("StartKBTest");
//			inKBTest= false;
//		}
//	}

//	@FXML
//	private void navBtnPressed(ActionEvent e) {
//		String eStr= e.getSource().toString();
//		if(eStr.contains("upBtn")){
//			System.out.println("\tUP");
//			if(tileVec.get(meX+17*(meY-1)).getObs()==-1)
//			{
////				meX=meX;
//				meY=meY-1;
//				//print the character
//				renderTiles();
//				p1.drawMe();
//			}
//			else if(tileVec.get(meX+17*(meY-1)).getObs()==4)
//			{
////				meX=meX;
//				meY=meY-1;
//				//print the character
//				renderTiles();
////				drawMe(drawingCanvas);
//			}
//		}
//		else if(eStr.contains("downBtn")){
//			System.out.println("\tDOWN");
//			if(tileVec.get(meX+17*(meY+1)).getObs()==-1)
//			{
////				meX=meX;
//				meY=meY+1;
//				//print the character
//				renderTiles();
//				p1.drawMe();
//			}
//			else if(tileVec.get(meX+17*(meY+1)).getObs()==4)
//			{
////				meX=meX;
//				meY=meY+1;
//				//print the character
//				renderTiles();
////				p1.drawMe(drawingCanvas);
//			}
//		}
//		else if(eStr.contains("leftBtn")){
//			System.out.println("\tLEFT");
//			if(tileVec.get(meX-1+17*meY).getObs()==-1)
//			{
//				meX=meX-1;
////				meY=meY;
//				//print the character
//				renderTiles();
//				p1.drawMe();
//			}
//			else if(tileVec.get(meX-1+17*meY).getObs()==4)
//			{
//				meX=meX-1;
////				meY=meY;
//				//print the character
//				renderTiles();
////				p1.drawMe(drawingCanvas);
//			}
//		}
//		else if(eStr.contains("rightBtn")){
//			System.out.println("\tRIGHT");
//			if(tileVec.get(meX+1+17*meY).getObs()==-1)
//			{
//				meX=meX+1;
////				meY=meY;
//				//print the character
//				renderTiles();
//				p1.drawMe();
//			}
//			else if(tileVec.get(meX+1+17*meY).getObs()==4)
//			{
//				meX=meX+1;
////				meY=meY;
//				//print the character
//				renderTiles();
////				p1.drawMe();
//			}
//		}
//	}

	@FXML
	private void settingsBtnPressed(ActionEvent e) throws Exception {
		Parent root =
				FXMLLoader.load(getClass().getResource("SettingsPage.fxml"));

		Scene scene = new Scene(root);
		Stage stage= new Stage();
		stage.setTitle("Settings");
		stage.setScene(scene);
		stage.show();
	}

//	@FXML
	private void keyPressed(KeyEvent e) {
		//System.out.println("keyPressed()");
		if(!inKBTest)
			return;
//		System.out.println(""+e.getCode().toString());


//
		p1.checkAround();
		if(pressed.isEmpty()) {
			pressed.add(e.getCode());
			System.out.println("NEW KEY");
			p1.runMoveTimer();
//			task.requestRedraw(p1);
		}
		else{
			if(!pressed.contains(e.getCode())){
				pressed.add(e.getCode());
				System.out.println("ADD KEY");
//				task.requestRedraw(p1);
			}
		}
//		task.requestRedraw(p1);

//		switch (e.getCode()) {
//			case UP:
//			case KP_UP:
//			case W:
////				System.out.println("\tUP");
////				isPressed[Player.UP].set(true);
//				if( (p1.obsStatus[Player.UP]==-1 || p1.obsStatus[Player.UP]==4) && p1.bombStatus[Player.UP]==0)
//				{
//					p1.move(Player.UP);
//					//print the character
//					renderTiles();
//					if(p1.obsStatus[Player.UP]==-1)
//						p1.drawMe(drawingCanvas);
//				}
//				break;
//			case DOWN:
//			case KP_DOWN:
//			case S:
////				System.out.println("\tDOWN");
////				isPressed[Player.DOWN].set(true);
//				if( (p1.obsStatus[Player.DOWN]==-1 || p1.obsStatus[Player.DOWN]==4) && p1.bombStatus[Player.DOWN]==0)
//				{
//					p1.move(Player.DOWN);
//					//print the character
//					renderTiles();
//					if(p1.obsStatus[Player.DOWN]==-1)
//						p1.drawMe(drawingCanvas);
//				}
//				break;
//			case LEFT:
//			case KP_LEFT:
//			case A:
////				System.out.println("\tLEFT");
////				isPressed[Player.LEFT].set(true);
//				if( (p1.obsStatus[Player.LEFT]==-1 || p1.obsStatus[Player.LEFT]==4) && p1.bombStatus[Player.LEFT]==0)
//				{
//					p1.move(Player.LEFT);
//					//print the character
//					renderTiles();
//					if(p1.obsStatus[Player.LEFT]==-1)
//						p1.drawMe(drawingCanvas);
//				}
//				break;
//			case RIGHT:
//			case KP_RIGHT:
//			case D:
////				System.out.println("\tRIGHT");
////				isPressed[Player.RIGHT].set(true);
//				if( (p1.obsStatus[Player.RIGHT]==-1 || p1.obsStatus[Player.RIGHT]==4) && p1.bombStatus[Player.RIGHT]==0)
//				{
//					p1.move(Player.RIGHT);
//					//print the character
//					renderTiles();
//					if(p1.obsStatus[Player.RIGHT]==-1)
//						p1.drawMe(drawingCanvas);
//				}
//				break;
//			case SPACE:
////				System.out.println("\tSPACE");
//				tileVec.get(17*p1.y+p1.x).setBombStatus(1);
//				renderTiles();
//				if(p1.obsStatus[Player.CENTER]==-1)
//					p1.drawMe(drawingCanvas);
//
//				Bomb bomb= new Bomb(p1,drawingCanvas);
//				bomb.putBomb(p1.x, p1.y);
////				tileVec= bomb.getTileVec();
//				//model.getBombs().add(bomb1);
//				//renderTiles(tileVec);
//				//bomb1= null;
//				break;
//			default:
//				break;
//		}
//		renderTiles();
//		if(p1.obsStatus[Player.CENTER]==-1)
//			p1.drawMe(drawingCanvas);
	}

//	@FXML
	private void keyReleased(KeyEvent e) {
		if(!inKBTest)
			return;
		pressed.remove(e.getCode());

		p1.checkAround();
		if(pressed.isEmpty()) {
			p1.stopMoveTimer();
			p1.nowDir=0;
			System.out.println("NO KEYS");
		}
//		switch (e.getCode()) {
//			case UP:
//			case KP_UP:
//			case W:
////				System.out.println("\tUP");
////				isPressed[Player.UP].set(false);
//				break;
//			case DOWN:
//			case KP_DOWN:
//			case S:
////				System.out.println("\tDOWN");
////				isPressed[Player.DOWN].set(false);
//				break;
//			case LEFT:
//			case KP_LEFT:
//			case A:
////				System.out.println("\tLEFT");
////				isPressed[Player.LEFT].set(false);
//				break;
//			case RIGHT:
//			case KP_RIGHT:
//			case D:
////				System.out.println("\tRIGHT");
////				isPressed[Player.RIGHT].set(false);
//				break;
//			default:
//				break;
//		}
//		renderTiles();
//		if(p1.obsStatus[Player.CENTER]==-1){
//			p1.catchCanvas(drawingCanvas);
//			p1.drawMe();
//		}
	}

	@FXML
	private void canvasMousePressed(MouseEvent e) {
		if(!inObsTest)
			return;

		int idX, idY, propID, bombID;
		idX= (int)(e.getX()-canvasXOffset)/40;
		idY= (int)(e.getY()-canvasYOffset)/40;
		if(idX < 0 || idX > 16 || idY <0 || idY > 15)
			return;
		propID= tileVec.get(idY*17+idX).getObs();
		bombID= tileVec.get(idY*17+idX).getBombStatus();
		System.out.println("ObsId= "+propID+", BombStat= "+bombID);
		System.out.println("("+e.getX()+","+e.getY()+")");

		if(pressed.contains(KeyCode.CONTROL))
			if(propID == 1 || propID == 2 || propID == 3){
				tileVec.get(idY*17+idX).setObs(-1);
				renderTiles();
				p1.drawMe();
			}
	}


	private void loadDataFile(String mapDataAddress, String obstacleDataAddress, String bombsDataAddress){
		mapDataStr= readFile(mapDataAddress);
		obstacleDataStr= readFile(obstacleDataAddress);
		bombsDataStr= readFile(bombsDataAddress);

		mapDataStr= mapDataStr.replaceAll("[)][()]|[,]", " ");
		mapDataStr= mapDataStr.replaceAll("[()]", " ");

		// System.out.println(bombsDataStr);

		Scanner mapScanner= new Scanner(mapDataStr);
		Scanner obsScanner= new Scanner(obstacleDataStr);
		Scanner bombScanner= new Scanner(bombsDataStr);

		tileVec.clear();
//		obsScanner.nextInt();

		for(int i=0; i<15; i++){
			for(int j=0; j<17; j++){
				int temp;
				tileVec.add(new Tile(mapScanner.nextInt(), mapScanner.nextInt()));
				tileVec.lastElement().setObs(obsScanner.nextInt());
				tileVec.lastElement().setBombStatus(temp= bombScanner.nextInt());
				tileVec.lastElement().setItemStatus(temp);
			}
		}
	}

	public void renderTiles(){
		GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
		if(tileVec.size() < 15*17){
			gc.drawImage(new Image("image/Error.png"),
				(drawingCanvas.getWidth()-48)/2, (drawingCanvas.getHeight()-48)/2, 48, 48);
			return;
		}

		for(int i= 0; i < 15; i++){
			for(int j= 0; j < 17; j++){
				gc.drawImage(tileVec.get(i*17+j).getBack(), canvasXOffset+40*j, canvasYOffset+40*i, 40, 40);
				gc.drawImage(tileVec.get(i*17+j).getFront(), canvasXOffset+40*j, canvasYOffset+40*i, 40, 40);
				gc.drawImage(tileVec.get(i*17+j).getItemPic(), canvasXOffset+40*j, canvasYOffset+40*i, 40, 40);

			}
		}//Draw Obs and Background
		//Draw Item

		for(int i= 0; i < 15; i++){
			for(int j= 0; j < 17; j++){
				if(tileVec.get(i*17+j).getObs() != 4){//Bush
					if(tileVec.get(i*17+j).getBombStatus() == 1)
						gc.drawImage(Bomb.pic, canvasXOffset+40*j,
								canvasYOffset+40*i, 40, 40);

					if (tileVec.get(i*17+j).getBombStatus() == -1)
						gc.drawImage(Bomb.up, canvasXOffset+40*j,
								canvasYOffset+40*i, 40, 40);

					if (tileVec.get(i*17+j).getBombStatus() == -2)
						gc.drawImage(Bomb.down, canvasXOffset+40*j,
								canvasYOffset+40*i, 40, 40);

					if (tileVec.get(i*17+j).getBombStatus() == -3)
						gc.drawImage(Bomb.left, canvasXOffset+40*j,
								canvasYOffset+40*i, 40, 40);

					if (tileVec.get(i*17+j).getBombStatus() == -4)
						gc.drawImage(Bomb.right, canvasXOffset+40*j,
								canvasYOffset+40*i, 40, 40);

					if (tileVec.get(i*17+j).getBombStatus() == -5)
						gc.drawImage(Bomb.center, canvasXOffset+40*j,
								canvasYOffset+40*i, 40, 40);
				}
			}
		}//Draw Bomb


	}

	public static WritableImage getTile(int idX, int idY){
		WritableImage rtnTile = new WritableImage(backReader, 16*(idX-1), 16*(idY-1), 16, 16);
		//WritableImage rtnTile = new WritableImage(backReader, 16, 16, 16, 16);
		return rtnTile;
	}

	public static Image getObstacle(int id){
		Image rtnImg;
		switch (id) {
			case 0:
			case -1:
				rtnImg= new Image("image/blank.png");
				return rtnImg;
			case 1:
				rtnImg= new Image("image/redBlock.jpg");
				return rtnImg;
			case 2:
				rtnImg= new Image("image/orangeBlock.jpg");
				return rtnImg;
			case 3:
				rtnImg= new Image("image/Box.png");
				return rtnImg;
			case 4:
				rtnImg= new Image("image/bush_tile.png");
				return rtnImg;
			case 5:
				rtnImg= new Image("image/Wall.png");
				return rtnImg;
			case -2:
				rtnImg= new Image("image/waterball/waterball.png");
				return rtnImg;
			default:
				rtnImg= new Image("image/Error.png");
				return rtnImg;
		}
	}

	public static Image getItemPic(int id){
		Image rtnImg;
		switch (id) {
			case 0:
			case -1:
			case 9:
			case 4:
				rtnImg= new Image("image/blank.png");
				return rtnImg;
			case 1:
				rtnImg= new Image("image/SHOES.png");
				return rtnImg;
			case 2:
				rtnImg= new Image("image/POTION.png");
				return rtnImg;
			case 3:
				rtnImg= new Image("image/ADDBALLOON.png");
				return rtnImg;
			default:
				rtnImg= new Image("image/Error.png");
				return rtnImg;
		}
	}

	private static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try ( Stream<String> stream = Files.lines( Paths.get(filePath)) ){
        	stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }



//    public boolean isInObsTest(){
//		return inObsTest;
//    }

//	public void drawMe(){
//		GraphicsContext gb = drawingCanvas.getGraphicsContext2D();
//		gb.drawImage(p1.meImg, canvasXOffset+40*p1.x, canvasYOffset+40*p1.y, 40, 40);
//	}
//	public static Vector<Tile> getTileVec(){
//		return tileVec;
//	}
//	public Canvas getCanvas(){
//		return drawingCanvas;
//	}
	//private static GraphicsContext gbAlt = drawingCanvas.getGraphicsContext2D();
	// public static GraphicsContext getGC(){
	//
	// 	return gbAlt;
	// }
}//end
