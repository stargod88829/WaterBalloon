package game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.security.SecureRandom;
import java.util.Vector;

public class Item extends Tile{

	private int x;
	private int y;
	private Player player;
	private Vector<Tile> tileVec;



	public static final Image[] SPEED = {
			new Image("image/Error.png"),
			new Image("image/Skills/Speed_1.png"),
			new Image("image/Skills/Speed_2.png"),
			new Image("image/Skills/Speed_3.png"),
			new Image("image/Skills/Speed_4.png"),
			new Image("image/Skills/Speed_5.png"),
			new Image("image/Skills/Speed_6.png"),
			new Image("image/Skills/Speed_7.png")
	};
	public static final Image[] POWER = {
			new Image("image/Error.png"),
			new Image("image/Skills/Power_1.png"),
			new Image("image/Skills/Power_2.png"),
			new Image("image/Skills/Power_3.png"),
			new Image("image/Skills/Power_4.png"),
			new Image("image/Skills/Power_5.png")
	};
	public static final Image[] QUANT= {
			new Image("image/Error.png"),
			new Image("image/Skills/Quant_1.png"),
			new Image("image/Skills/Quant_2.png"),
			new Image("image/Skills/Quant_3.png"),
			new Image("image/Skills/Quant_4.png"),
			new Image("image/Skills/Quant_5.png")
	};


	Item(Player player){
		this.player= player;
		tileVec= GameController.tileVec;
	}



//	public static Image Item4Img= new Image("image/PURPLEPOTION.jpg");
	
//	drawItem1();
//	drawItem2();
//	drawItem3();
//	drawItem4();

	public void generateItem(int xPos, int yPos) {
		x = xPos;
		y = yPos;
		SecureRandom rand= new SecureRandom();
		System.out.println("Item #"+3+" @ ("+x+","+y+")");
	}

//
//	if(abs_x==Item1X && abs_y==Item1Y)
//		power++;
//	if(abs_x==Item3X && abs_y==Item3Y)
//		power++;
//
//	if(abs_x==Item2X && abs_y==Item2Y)
//		power--;
//	if(abs_x==Item4X && abs_y==Item4Y)
//		power--;
}