package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.SecureRandom;
import java.util.Vector;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;


public class Bomb extends Tile{
	private int x;
	private int y;
	private Player player;
	private long timeLeft= 3500;
	private long timeLeft2= 500;
	private Vector<Tile> tileVec;

	public static Image pic= new Image("image/waterball/waterball.png");
	public static Image up= new Image("image/waterball/up.png");
	public static Image down= new Image("image/waterball/down.png");
	public static Image left= new Image("image/waterball/left.png");
	public static Image right= new Image("image/waterball/right.png");
	public static Image center= new Image("image/waterball/center.png");


	File fxFile = new File("audio/FX/sfx_exp_shortest_soft3.wav");
	private Media fx;
	private MediaPlayer fxPlayer;


	Bomb(Player player){
		this.player= player;
		tileVec= GameController.tileVec;

		fx= new Media(fxFile.toURI().toString());
		fxPlayer= new MediaPlayer(fx);
		fxPlayer.setVolume(GameController.fxVolume);
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
				detonate();
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
		SecureRandom rand= new SecureRandom();

		//UP
		for(int i=1; i<= player.power; i++){
			System.out.println("BombUp");
			tempObsStat = tileVec.get((y - i )*17+x).getObs();
			System.out.println("\tObsStat= "+tempObsStat);
			if (tileVec.get((y-i)*17+x).getItemStatus()!= 0)
				tileVec.get((y-i)*17+x).setItemStatus(0);
			if(tempObsStat == 1 || tempObsStat == 2 || tempObsStat == 3 || tempObsStat == -1 ){

				tileVec.get((y-i)*17+x).setObs(-1);
				tileVec.get((y-i)*17+x).setBombStatus(-1);
				if(tempObsStat!=-1) {
					tileVec.get((y - i) * 17 + x).setItemStatus(rand.nextInt(5));
					player.countItem++;
				}
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
			if (tileVec.get((y+i)*17+x).getItemStatus()!= 0)
				tileVec.get((y+i)*17+x).setItemStatus(0);
			if(tempObsStat == 1 || tempObsStat == 2 || tempObsStat == 3 || tempObsStat == -1 ){

				tileVec.get((y+i)*17+x).setObs(-1);
				tileVec.get((y+i)*17+x).setBombStatus(-1);
				if(tempObsStat!=-1) {
					tileVec.get((y + i) * 17 + x).setItemStatus(rand.nextInt(5));
					player.countItem++;
				}
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
			if (tileVec.get(y*17+(x-i)).getItemStatus()!= 0)
				tileVec.get(y*17+(x-i)).setItemStatus(0);
			if(tempObsStat == 1 || tempObsStat == 2 || tempObsStat == 3 || tempObsStat == -1 ){

				tileVec.get(y*17+(x-i)).setObs(-1);
				tileVec.get(y*17+(x-i)).setBombStatus(-1);
				if(tempObsStat!=-1) {
					tileVec.get(y * 17 + (x - i)).setItemStatus(rand.nextInt(5));
					player.countItem++;
				}
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
			if (tileVec.get(y*17+(x+i)).getItemStatus()!= 0)
				tileVec.get(y*17+(x+i)).setItemStatus(0);
			if(tempObsStat == 1 || tempObsStat == 2 || tempObsStat == 3 || tempObsStat == -1 ){

				tileVec.get(y*17+(x+i)).setObs(-1);
				tileVec.get(y*17+(x+i)).setBombStatus(-1);
				if(tempObsStat!=-1) {
					tileVec.get(y * 17 + (x + i)).setItemStatus(rand.nextInt(5));
					player.countItem++;
				}
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
//		player.setBombCount(player.getBombCount()-1);
		player.bombCount--;

		fxPlayer.play();
	}


//	public Vector<Tile> getTileVec(){
//		return tileVec;
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
