package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;
import java.util.Vector;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class GameController {
	@FXML private Canvas drawingCanvas;
	@FXML private Button obsTestBtn;
	@FXML private Button kbTestBtn;
	// @FXML private Button upBtn;
	// @FXML private Button downBtn;
	// @FXML private Button leftBtn;
	// @FXML private Button rightBtn;

//	public final BooleanProperty[] isPressed = new BooleanProperty[5];
//	public final BooleanBinding[][] bothPressed = new BooleanBinding[5][5];

	private boolean inObsTest= false;
	private boolean inKBTest= true;
	public static final Set<KeyCode> pressed = new HashSet<KeyCode>();


	public static int meX=1;
	public static int meY=1;
	private Image p1Img= new Image("image/ME.png");
	private Player p1=new Player(1,1, p1Img);

	private static Image tileSet= new Image("image/Dungeon_Tileset.png");
	private static PixelReader backReader = tileSet.getPixelReader();
	public static int canvasXOffset= 160;
	public static int canvasYOffset= 20;

	private String mapDataStr;
	private String obstacleDataStr;
	private String bombsDataStr;
	public static Vector<Tile> tileVec= new Vector<>();

	public void initialize() {

//		for(int i= Player.CENTER; i<= Player.RIGHT; i++) {
//			isPressed[i] = new SimpleBooleanProperty(false);
//		}
//		for(int i= Player.CENTER; i<= Player.RIGHT; i++){
//			for (int j= Player.CENTER; j<= Player.RIGHT; j++){
//				bothPressed[i][j]
//						= isPressed[i].and(isPressed[j]);
//			}
//		}
//		bothPressed[Player.UP][Player.LEFT].addListener(
//			(obs, werePressed, arePressed) -> {
//				System.out.println("UP + LEFT");
//				System.out.println(arePressed ?"true":"false");
//			}
//		);
//		bothPressed[Player.UP][Player.RIGHT].addListener(
//			(obs, werePressed, arePressed) -> {
//				System.out.println("UP + RIGHT");
//			}
//		);
//		bothPressed[Player.DOWN][Player.LEFT].addListener(
//			(obs, werePressed, arePressed) -> {
//				System.out.println("DOWN + LEFT");
//			}
//		);
//		bothPressed[Player.DOWN][Player.RIGHT].addListener(
//			(obs, werePressed, arePressed) -> {
//				System.out.println("DOWN + RIGHT");
//			}
//		);

		p1.catchCanvas(drawingCanvas);
		drawingCanvas.getParent().addEventFilter(KeyEvent.KEY_PRESSED,
				event -> {
					keyPressed(event);
					event.consume();
				}
		);
		drawingCanvas.getParent().addEventFilter(KeyEvent.KEY_RELEASED,
				event -> {
					keyReleased(event);
					event.consume();
				}
		);

		System.out.println(drawingCanvas.getParent().toString());

		loadDataFile("game/MapData.txt", "game/ObstacleData.txt", "game/BombsData.txt");
		renderTiles();

		p1.drawMe(drawingCanvas);
	}

//	ChangeListener<Boolean> boolHandler= new ChangeListener<Boolean>() {
//		@Override
//		public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//
//		}
//	};


	@FXML
	private void obsTestBtnPressed(ActionEvent e) {
		System.out.println("Player1 @("+p1.x+","+p1.y+")");
		if(!inObsTest){
			obsTestBtn.setStyle("-fx-border-color: RED; ");
			obsTestBtn.setText("Stop");
			inObsTest= true;
		}
		else{
			obsTestBtn.setStyle("-fx-border-color: LIGHTGREEN; ");
			obsTestBtn.setText("StartObsTest");
			inObsTest= false;
		}
	}
	@FXML
	private void kbTestBtnPressed(ActionEvent e) {
		if(!inKBTest){
			kbTestBtn.setStyle("-fx-border-color: RED; ");
			kbTestBtn.setText("Stop");
			inKBTest= true;
		}
		else{
			kbTestBtn.setStyle("-fx-border-color: LIGHTGREEN; ");
			kbTestBtn.setText("StartKBTest");
			inKBTest= false;
		}
	}

	@FXML
	private void navBtnPressed(ActionEvent e) {
		String eStr= e.getSource().toString();
		if(eStr.contains("upBtn")){
			System.out.println("\tUP");
			if(tileVec.get(meX+17*(meY-1)).getObs()==-1)
			{
//				meX=meX;
				meY=meY-1;
				//print the character
				renderTiles();
				p1.drawMe(drawingCanvas);
			}
			else if(tileVec.get(meX+17*(meY-1)).getObs()==4)
			{
//				meX=meX;
				meY=meY-1;
				//print the character
				renderTiles();
//				drawMe(drawingCanvas);
			}
		}
		else if(eStr.contains("downBtn")){
			System.out.println("\tDOWN");
			if(tileVec.get(meX+17*(meY+1)).getObs()==-1)
			{
//				meX=meX;
				meY=meY+1;
				//print the character
				renderTiles();
				p1.drawMe(drawingCanvas);
			}
			else if(tileVec.get(meX+17*(meY+1)).getObs()==4)
			{
//				meX=meX;
				meY=meY+1;
				//print the character
				renderTiles();
//				p1.drawMe(drawingCanvas);
			}
		}
		else if(eStr.contains("leftBtn")){
			System.out.println("\tLEFT");
			if(tileVec.get(meX-1+17*meY).getObs()==-1)
			{
				meX=meX-1;
//				meY=meY;
				//print the character
				renderTiles();
				p1.drawMe(drawingCanvas);
			}
			else if(tileVec.get(meX-1+17*meY).getObs()==4)
			{
				meX=meX-1;
//				meY=meY;
				//print the character
				renderTiles();
//				p1.drawMe(drawingCanvas);
			}
		}
		else if(eStr.contains("rightBtn")){
			System.out.println("\tRIGHT");
			if(tileVec.get(meX+1+17*meY).getObs()==-1)
			{
				meX=meX+1;
//				meY=meY;
				//print the character
				renderTiles();
				p1.drawMe(drawingCanvas);
			}
			else if(tileVec.get(meX+1+17*meY).getObs()==4)
			{
				meX=meX+1;
//				meY=meY;
				//print the character
				renderTiles();
//				p1.drawMe();
			}
		}
	}


	public void keyPressed(KeyEvent e) {
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
		}
		else{
			if(!pressed.contains(e.getCode())){
				pressed.add(e.getCode());
				System.out.println("ADD KEY");
			}
		}

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

	private void keyReleased(KeyEvent e) {
		if(!inKBTest)
			return;
		pressed.remove(e.getCode());

		p1.checkAround();
		if(pressed.isEmpty()) {
			p1.stopMoveTimer();
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

		// if(propID == 1 || propID == 2 || propID == 3){
		// 	tileVec.get(idY*17+idX).setObs(-1);
		// 	renderTiles(tileVec);
		// 	drawMe();
		// }
	}


	public void loadDataFile(String mapDataAddress, String obstacleDataAddress, String bombsDataAddress){
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
				tileVec.add(new Tile(mapScanner.nextInt(), mapScanner.nextInt()));
				tileVec.lastElement().setObs(obsScanner.nextInt());
				tileVec.lastElement().setBombStatus(bombScanner.nextInt());
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
			}
		}//Draw Obs and Background

		for(int i= 0; i < 15; i++){
			for(int j= 0; j < 17; j++){
				if(tileVec.get(i*17+j).getBombStatus() == 1)
					if(tileVec.get(i*17+j).getObs() != 4)//Bush
						gc.drawImage(Bomb.pic, GameController.canvasXOffset+40*j,
							GameController.canvasYOffset+40*i, 40, 40);
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

	public static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (
			Stream<String> stream = Files.lines( Paths.get(filePath) );
		){
        	stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

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
