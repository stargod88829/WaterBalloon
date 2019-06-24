package game;

import javafx.scene.image.Image;

public class Tile {
	private int x;
	private int y;
	private int obs;
	private int bombStat;
	private int itemStat;
	private Image back;
	private Image front;
	private Image itemPic;

	Tile(){x=0; y=0;}
	Tile(int num){x= num; y=num; back= GameController.getTile(x,y);}
	Tile(int x, int y){this.x= x; this.y= y; back= GameController.getTile(this.x,this.y);}

	public Image getBack(){return back;}
	public void setBack(Image bg){back= bg;}

	public int getObs(){return obs;}
	//0: Frame, 1: RedBlock, 2: OrangeBlock
	//3: Box, 4: Bushes, 5: Non-Destroyable
	//-1: path7
	public void setObs(int num){obs= num; front= GameController.getObstacle(obs);}
	public Image getFront(){return front;}
	public void setFront(Image thing){front= thing;}

	public int getBombStatus(){return bombStat;}
	//0: No Bomb, 1: Bomb Set, 9: Wall
	//Explosion:
	// -1: Up, -2: Down, -3:Left, -4: Right, -5: Center
	public void setBombStatus(int num){bombStat= num;}

	public int getItemStatus(){return itemStat;}
	//0: Nothing, 1: Speed, 2: Power, 3: Quantity, 4:Nothing
	public void setItemStatus(int num){itemStat= num; itemPic= GameController.getItemPic(num);}
	public Image getItemPic(){return itemPic;}
}
