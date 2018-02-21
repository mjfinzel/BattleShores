package Game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Tile {
	BufferedImage[][] texture;
	int xpos;
	int ypos;
	int tileX=0;
	int tileY=0;
	int arrowX=0;
	int tileID = 0;
	int actionType = 0;
	int collisionType = 0;//0= land, 1 = water, 2 = can only be flown over, 3 = impassible
	public boolean hasArrow = false;
	String name = "null";
	Animation water;
	Animation lava;
	Animation beach;
	Animation river;
	BufferedImage[][] beaches;
	BufferedImage[][] actionTypeImg;
	
	public Tile(Point pos,BufferedImage[][] sprites,BufferedImage[][]Beaches,BufferedImage[][]action,int ID){
		
			
		actionTypeImg=action;
		//System.out.println("xpos: "+pos.x+", ypos: "+pos.y);
		if(pos!=null&&sprites!=null){
			texture = sprites;
			beaches=Beaches;
			xpos=pos.x;
			ypos=pos.y;
		}
		
		tileID=ID;
		if(tileID==0){//grass
			tileX=0;
			name = "Grass";
		}
		else if(tileID==1){//water
			tileX=1;
			water = new Animation(texture, 2, GamePanel.randomNumber(500, 1000), tileX, 0, 0, true, 0, 0);
			water.updatePosition(this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32));
			name = "Water";
		}
		else if(tileID==2){//
			tileX=2;
			name = "Dirt";
		}
		else if(tileID==3){//
			tileX=3;
			name = "Flat Sand";
		}
		else if(tileID==4){//
			tileX=3;
			tileY=1;
			name = "Sand Dunes";
		}
		else if(tileID==5){//
			tileX=4;
			name = "Flat Snow";
		}
		else if(tileID==6){//
			tileX=5;
			name = "Brick Floor";
		}
		else if(tileID==7){//
			tileX=5;
			tileY=1;
			this.collisionType=3;
			name = "Brick Corner Wall";
		}
		else if(tileID==8){//
			tileX=5;
			tileY=2;
			name = "Vertical Upper Wall";
			this.collisionType=3;
		}
		else if(tileID==9){//
			tileX=5;
			tileY=3;
			name = "Horizontal Upper Wall";
			this.collisionType=3;
		}
		else if(tileID==10){//
			tileX=5;
			tileY=4;
			name = "Front of Brick Wall";
			this.collisionType=3;
		}
		else if(tileID==11){//
			tileX=5;
			tileY=5;
			name = "Front Corner Brick";
			this.collisionType=3;
		}
		else if(tileID==12){//
			tileX=6;
			lava = new Animation(GamePanel.animatedTiles, 36, GamePanel.randomNumber(200, 250),  1, 0, 0, true, 0, 0);
			lava.updatePosition(this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32));
			name = "Lava";
		}
		else if(tileID==13){//
			tileX=0;
			tileY=17;
			name = "Mountain";
		}
		else if(tileID==14){
			tileX=8;
			tileY=0;
			name = "Trees";
		}
		else if(tileID==15){//
			tileX=0;
			tileY=0;
			beach = new Animation(beaches, 2, GamePanel.randomNumber(500, 1000),  tileX, 0, 0, true, 0, 0);
			beach.updatePosition(this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32));
			name = "Beach";
		}
		else if(tileID==16){
			tileX=4;
			tileY=2;
			name = "River";
			this.collisionType=3;
		}
		else if(tileID==17){
			tileX=4;
			tileY=8;
			name = "Bridge";
		}
	}
	public void updatePos(){
		if(water!=null)
		water.updatePosition(this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32));
		if(lava!=null)
			lava.updatePosition(this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32));
		if(beach!=null)
			beach.updatePosition(this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32));
		if(river!=null)
			river.updatePosition(this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32));
		//System.out.println("updated");
	}
	
	public void Draw(Graphics g){
		//System.out.println("xpos: "+xpos+", ypos: "+ypos);
		if(tileID!=1&&tileID!=12&&tileID!=15){//not water
			g.drawImage(texture[tileX][tileY], this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32),32, 32, null);
		}
		else if(tileID==1){//water
			water.Draw(g);
		}
		else if(tileID==12){//lava
			lava.Draw(g);
		}
		else if(tileID==15){//beach
			beach.Draw(g);
		}
		if(river!=null){
			river.Draw(g);
		}
		if(this.actionType==1){
			g.drawImage(actionTypeImg[0][0], this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32),32, 32, null);
		}
		else if(this.actionType==2){
			g.drawImage(actionTypeImg[1][0], this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32),32, 32, null);
		}
		else if(this.actionType==3){
			g.drawImage(actionTypeImg[2][0], this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32),32, 32, null);
		}
		else if(this.actionType==4){
			g.drawImage(actionTypeImg[3][0], this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32),32, 32, null);
		}
		if(this.hasArrow==true){
			g.drawImage(GamePanel.arrows[arrowX][0], this.xpos+(GamePanel.mapY*32), this.ypos+(GamePanel.mapX*32),32, 32, null);
		}
	}
}
