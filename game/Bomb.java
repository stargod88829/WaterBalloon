package game;

import java.util.Vector;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.Timer;
import java.util.TimerTask;


public class Bomb extends GameController{
	private int x;
	private int y;
	private Player player;
	private Canvas drawingCanvas;
	private long timeLeft= 3500;
	private Vector<Tile> tileVec;

	public static Image pic= new Image("image/waterball/waterball.png");

	Bomb(Player player, Canvas drawingCanvas){
		this.player= player;
		tileVec= GameController.tileVec;
		this.drawingCanvas= drawingCanvas;
	}
//	Bomb(int xPos, int yPos, Canvas drawingCanvas){
//		x= xPos; y= yPos;
//		tileVec= GameController.getTileVec();
//		this.drawingCanvas= drawingCanvas;
//	}

	public void putBomb(int xPos, int yPos){
		Timer timer= new Timer(true);

		x= xPos;
		y= yPos;
		TimerTask task= new TimerTask(){
			@Override
			public void run(){
				System.out.println("BOOM! @("+x+","+y+")");
				//System.out.println(GameController.readFile("ObstacleData.txt"));
				if(tileVec.get((y-1)*17+x).getObs() == 1 ||
						tileVec.get((y-1)*17+x).getObs() == 2 ||
						tileVec.get((y-1)*17+x).getObs() == 3){
					tileVec.get((y-1)*17+x).setObs(-1);
					tileVec.get((y-1)*17+x).setBombStatus(0);
				}//up
				if(tileVec.get((y+1)*17+x).getObs() == 1 ||
						tileVec.get((y+1)*17+x).getObs() == 2 ||
						tileVec.get((y+1)*17+x).getObs() == 3){
					tileVec.get((y+1)*17+x).setObs(-1);
					tileVec.get((y+1)*17+x).setBombStatus(0);
				}//down
				if(tileVec.get(y*17+(x-1)).getObs() == 1 ||
						tileVec.get(y*17+(x-1)).getObs() == 2 ||
						tileVec.get(y*17+(x-1)).getObs() == 3){
					tileVec.get(y*17+(x-1)).setObs(-1);
					tileVec.get(y*17+(x-1)).setBombStatus(0);
				}//left
				if(tileVec.get(y*17+(x+1)).getObs() == 1 ||
						tileVec.get(y*17+(x+1)).getObs() == 2 ||
						tileVec.get(y*17+(x+1)).getObs() == 3){
					tileVec.get(y*17+(x+1)).setObs(-1);
					tileVec.get(y*17+(x+1)).setBombStatus(0);
				}//right
				if(tileVec.get(y*17+x).getBombStatus() == 1 ||
					tileVec.get(y*17+x).getObs() == 4){
					tileVec.get(y*17+x).setObs(-1);
					tileVec.get(y*17+x).setBombStatus(0);
				}//center
				renderTiles();
				if(tileVec.get(player.y*17+player.x).getObs() != 4){
					player.drawMe(drawingCanvas);
				}
				//drawMe();
				//GameController.reachRenderTiles();
			}
		};
		timer.schedule(task, timeLeft);
	}

//	public Vector<Tile> getTileVec(){
//		return tileVec;
//	}
	public void renderTiles(){

		GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
		if(tileVec.size() < 15*17){
			gc.drawImage(new Image("image/Error.png"),
				(drawingCanvas.getWidth()-48)/2, (drawingCanvas.getHeight()-48)/2, 48, 48);
			return;
		}

		for(int i= 0; i < 15; i++){
			for(int j= 0; j < 17; j++){
				gc.drawImage(tileVec.get(i*17+j).getBack(), GameController.canvasXOffset+40*j,
					GameController.canvasYOffset+40*i, 40, 40);
				gc.drawImage(tileVec.get(i*17+j).getFront(), GameController.canvasXOffset+40*j,
					GameController.canvasYOffset+40*i, 40, 40);
			}
		}//Draw Obs and Background

		for(int i= 0; i < 15; i++){
			for(int j= 0; j < 17; j++){
				if(tileVec.get(i*17+j).getBombStatus() == 1)
					if(tileVec.get(i*17+j).getObs() != 4)//Bush
						gc.drawImage(pic, GameController.canvasXOffset+40*j,
							GameController.canvasYOffset+40*i, 40, 40);
			}
		}//Draw Bomb
	}

//	public void drawMe(){
//		GraphicsContext gb = drawingCanvas.getGraphicsContext2D();
//		gb.drawImage(player.meImg, GameController.canvasXOffset+40*player.x,
//			GameController.canvasYOffset+40*player.y, 40, 40);
//	}


	// public void drawBomb(){
	// 	GraphicsContext gb = drawingCanvas.getGraphicsContext2D();
	// 	gb.drawImage(pic, GameController.canvasXOffset+40*GameController.meX,
	// 		GameController.canvasYOffset+40*GameController.meY, 40, 40);
	// }
	// public static GraphicsContext getGC(){
	//
	// 	return tileVec;
	// }

}
