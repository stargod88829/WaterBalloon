package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.Timer;
import java.util.TimerTask;


public class Bomb{
	private int x;
	private int y;
	private Player player;
	private Canvas drawingCanvas;
	private long timeLeft= 3500;
	private long timeLeft2= 500;
	private Vector<Tile> tileVec;

	public static Image pic= new Image("image/waterball/waterball.png");
	public static Image up= new Image("image/waterball/up.png");
	public static Image down= new Image("image/waterball/down.png");
	public static Image left= new Image("image/waterball/left.png");
	public static Image right= new Image("image/waterball/right.png");
	public static Image center= new Image("image/waterball/center.png");


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

	public void putBomb(int xPos, int yPos) {
		Timer timer = new Timer(true);
		x = xPos;
		y = yPos;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				detonate(1);
			}
		};
		timer.schedule(task, timeLeft);
//				renderTiles();
		System.out.println("Bomb set");
	}

	public void detonate(){
		int tempObsStat;
		Vector<Integer> destroyedTileVec= new Vector<>();
		System.out.println("BOOM! @("+x+","+y+")");

		//UP
		for(int i=1; i<= player.power; i++){
			System.out.println("BombUp");
			tempObsStat = tileVec.get((y - i )*17+x).getObs();
			System.out.println("\tObsStat= "+tempObsStat);
			if(tempObsStat == 1 || tempObsStat == 2 || tempObsStat == 3 || tempObsStat == -1 ){

				tileVec.get((y-i)*17+x).setObs(-1);
				tileVec.get((y-i)*17+x).setBombStatus(-1);
				destroyedTileVec.add((y-i)*17+x);
			}
			if(tempObsStat != -1)
				break;
		}
		//DOWN
		for(int i=1; i<= player.power; i++){
			System.out.println("BombDown");
			tempObsStat = tileVec.get((y + i )*17+x).getObs();
			System.out.println("\tObsStat= "+tempObsStat);
			if(tempObsStat == 1 || tempObsStat == 2 || tempObsStat == 3 || tempObsStat == -1 ){

				tileVec.get((y+i)*17+x).setObs(-1);
				tileVec.get((y+i)*17+x).setBombStatus(-1);
				destroyedTileVec.add((y+i)*17+x);
			}
			if(tempObsStat != -1)
				break;
		}

		//LEFT
		for(int i=1; i<= player.power; i++){
			System.out.println("BombLeft:");
			tempObsStat = tileVec.get(y*17+(x - i)).getObs();
			System.out.println("\tObsStat= "+tempObsStat);
			if(tempObsStat == 1 || tempObsStat == 2 || tempObsStat == 3 || tempObsStat == -1 ){

				tileVec.get(y*17+(x-i)).setObs(-1);
				tileVec.get(y*17+(x-i)).setBombStatus(-1);
				destroyedTileVec.add(y*17+(x-i));
			}
			if(tempObsStat != -1)
				break;
		}

		//RIGHT
		for(int i=1; i<= player.power; i++){
			System.out.println("BombRight:");
			tempObsStat = tileVec.get(y*17+(x + i)).getObs();
			System.out.println("\tObsStat= "+tempObsStat);
			if(tempObsStat == 1 || tempObsStat == 2 || tempObsStat == 3 || tempObsStat == -1 ){

				tileVec.get(y*17+(x+i)).setObs(-1);
				tileVec.get(y*17+(x+i)).setBombStatus(-1);
				destroyedTileVec.add(y*17+(x+i));
			}
			if(tempObsStat != -1)
				break;
		}

		//CENTER
		tempObsStat= tileVec.get(y*17+x).getObs();
		if(tileVec.get(y*17+x).getBombStatus() == 1 ||
				tempObsStat == 4 || tempObsStat  == -1 ) {
			tileVec.get(y*17+x).setObs(-1);
			tileVec.get(y*17+x).setBombStatus(-5);
			destroyedTileVec.add(y*17+x);
		}

		Timer timeru= new Timer(true);
		TimerTask tasku= new TimerTask(){
			@Override
			public void run() {
				for(int pos:destroyedTileVec)
					tileVec.get(pos).setBombStatus(0);
				System.out.println("\t!!IN Tasku (UP)!!");
			}
		};
		timeru.schedule(tasku, timeLeft2);
		player.setBombCount(player.getBombCount()-1);
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
				if(tileVec.get(i*17+j).getObs() != 4){//Bush
					if(tileVec.get(i*17+j).getBombStatus() == 1)
						gc.drawImage(pic, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);

					if (tileVec.get(i*17+j).getBombStatus() == -1)
						gc.drawImage(up, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);

					if (tileVec.get(i*17+j).getBombStatus() == -2)
						gc.drawImage(down, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);

					if (tileVec.get(i*17+j).getBombStatus() == -3)
						gc.drawImage(left, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);

					if (tileVec.get(i*17+j).getBombStatus() == -4)
						gc.drawImage(right, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);

					if (tileVec.get(i*17+j).getBombStatus() == -5)
						gc.drawImage(center, GameController.canvasXOffset+40*j,
								GameController.canvasYOffset+40*i, 40, 40);
				}
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
