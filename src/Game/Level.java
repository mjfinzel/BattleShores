package Game;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level {
	Tile[][] tileMap = new Tile[40][60];
	BufferedImage[][] tiles = Images.cut("/tiles/Tiles.png",32,32);
	BufferedImage[][] beaches = Images.cut("/tiles/WaterBeaches.png",32,32);
	public int levelCompletionCriteria = -1;
	ArrayList<LockedChest> chests = new ArrayList<LockedChest>();
	ArrayList<Unit> Units = new ArrayList<Unit>();
	int levelWidth;
	int levelHeight;
	public Level(int ID, int width, int height){
		levelWidth = width;
		levelHeight = height;
		ArrayList <Integer> williamItems = new ArrayList<Integer>();
		williamItems.add(0);
		williamItems.add(1);
		ArrayList <Integer> jebItems = new ArrayList<Integer>();
		jebItems.add(0);
		jebItems.add(1);
		ArrayList <Integer> banditSoldierItems = new ArrayList<Integer>();
		banditSoldierItems.add(0);
		ArrayList <Integer> tythusItems = new ArrayList<Integer>();
		tythusItems.add(2);
		//blue

		//Units.add(new Unit(8, 8, 0, 0,"William",williamItems));
		//Units.add(new Unit(9, 9, 0, 0,"Jeb",jebItems));
		//Units.add(new Unit(9, 10, 0, 1,"Tythus",tythusItems));
		//red
		//Units.add(new Unit(14, 16, 1, 0,"Bandit Soldier",banditSoldierItems));
		//Units.add(new Unit(17, 18, 1, 0,"Bandit Soldier",banditSoldierItems));
		//Units.add(new Unit(16, 20, 1, 0,"Bandit Soldier",banditSoldierItems));
		//Units.add(new Unit(16, 17, 1, 0,"Bandit Soldier",banditSoldierItems));
		//Units.add(new Unit(19, 11, 1, 0,"Bandit Soldier",banditSoldierItems));
		//Units.add(new Unit(19, 12, 1, 0,"Bandit Soldier",banditSoldierItems));
		//Units.add(new Unit(16, 18, 1, 0,"Tough Guy",banditSoldierItems));
		if(ID==0){
			//add tiles to the map
			for(int i = GamePanel.mapY; i<GamePanel.mapY+height;i++){
				for(int j = GamePanel.mapX;j<GamePanel.mapX+width;j++){

					Point pos = new Point(j*32,i*32);
					//System.out.println(pos.x+","+pos.y);
					tileMap[i][j]=new Tile(pos,tiles,beaches,GamePanel.actionImg,0);
					if(GamePanel.randomNumber(1, 10)<=1){
						//tileMap[i][j]=new Tile(pos,tiles,beaches,GamePanel.actionImg,7);
					}
				}
			}
		}
	}
	public void update(){

		for(int i = 0; i<levelHeight;i++){
			for(int j = 0;j<levelWidth;j++){
				tileMap[i][j].updatePos();

				int top = -1;
				int northwest = -1;
				int bottom = -1;
				int southwest = -1;
				int left = -1;
				int northeast = -1;
				int right = -1;
				int southeast = -1;
				if(i-1>=0){
					top=tileMap[i-1][j].tileID;
				}
				if(i+1<levelHeight){
					bottom=tileMap[i+1][j].tileID;
				}
				if(j-1>=0){
					left=tileMap[i][j-1].tileID;
				}
				if(j+1<levelWidth){
					right=tileMap[i][j+1].tileID;
				}
				if(i+1<levelHeight&&j+1<levelWidth){
					southeast=tileMap[i+1][j+1].tileID;
				}
				if(i-1>0&&j-1>0){
					northwest=tileMap[i-1][j-1].tileID;
				}
				if(i-1>0&&j+1<levelWidth){
					northeast=tileMap[i-1][j+1].tileID;
				}
				if(i+1<levelHeight&&j-1>0){
					southwest=tileMap[i+1][j-1].tileID;
				}
				if(tileMap[i][j].tileID==2){//dirt
					if(top==2&&bottom==2&&left==2&&right==2&&northwest!=0&&northeast!=0&&southwest!=0&&southeast!=0){
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=0;
					}
					else if(top==2&&bottom==2&&left==2&&right==2&&northwest!=2&&northeast!=0&&southwest!=0&&southeast!=0){
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=1;
					}
					else if(top==2&&bottom==2&&left==2&&right==2&&northwest!=0&&northeast!=0&&southwest!=0&&southeast!=2){
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=2;
					}
					else if(top==2&&bottom==2&&left==2&&right==2&&northwest!=0&&northeast!=2&&southwest!=0&&southeast!=0){
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=3;
					}
					else if(top==2&&bottom==2&&left==2&&right==2&&northwest!=0&&northeast!=0&&southwest!=2&&southeast!=0){
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=4;
					}
					else if(top!=2&&bottom==2&&left==2&&right==2&&(northwest!=2||northeast!=2)&&southwest!=0&&southeast!=0){//top
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=16;
					}
					else if(top==2&&bottom!=2&&left==2&&right==2&&northwest!=0&&northeast!=0&&(southwest!=2||southeast!=2)){//bottom
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=15;
					}
					else if(top==2&&bottom==2&&left==2&&right!=2&&northwest!=0&&southwest!=0&&(northeast!=2||southeast!=2)){//right
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=14;
					}
					else if(top==2&&bottom==2&&left!=2&&right==2&&(northwest!=2||southwest!=2)&&southeast!=0&&northeast!=0){//left
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=13;
					}
					else if(top==2&&bottom!=2&&left!=2&&right==2&&southwest!=2){ //bottom left
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=9;
					}
					else if(top!=2&&bottom==2&&left==2&&right!=2&&northeast!=2){ //top right
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=10;
					}
					else if(top==2&&bottom!=2&&left==2&&right!=2&&southeast!=2){ //bottom right
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=11;
					}
					else if(top!=2&&bottom==2&&left!=2&&right==2&&northwest!=2){ //top left
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=12;
					}
					else if(top!=2&&bottom!=2&&left!=2&&right!=2){ //top left
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=17;
					}
				}
				//mountain
				else if(tileMap[i][j].tileID==13){
					if(top!=13&&bottom!=13&&left!=13&&right!=13&&northwest!=13&&northeast!=13&&southwest!=13&&southeast!=13){
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=17;
					}
					else if(top!=13&&bottom==13&&left!=13&&right==13&&southeast==13){
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=1;
					}
					else if(top!=13&&bottom==13&&left==13&&right!=13&&southwest==13){
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=10;
					}
					else if(top==13&&bottom!=13&&left!=13&&right==13){
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=3;
					}
					else if(top==13&&bottom!=13&&left==13&&right!=13){
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=12;
					}
					else if(top==13&&bottom==13&&left!=13&&right==13){//grass on the left, northwest and southwest, mountain everywhere else
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=2;
					}
					else if(top==13&&bottom==13&&left==13&&right!=13){//grass on the right, northeast and southeast, mountain everywhere else
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=11;
					}
					else if(top!=13&&bottom==13&&left==13&&right==13){
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=4;
					}
					else if(top==13&&bottom==13&&left==13&&right==13){
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=5;
					}
					else if(top==13&&bottom!=13&&left==13&&right==13){
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=6;
					}

				}
				//trees
				else if(tileMap[i][j].tileID==14){
					if(top!=14&&bottom!=14&&left!=14&&right!=14&&northwest!=14&&northeast!=14&&southwest!=14&&southeast!=14){
						tileMap[i][j].tileX=8;
						tileMap[i][j].tileY=17;
					}
					else if(top!=14&&bottom==14&&left!=14&&right==14&&southeast==14){
						tileMap[i][j].tileX=8;
						tileMap[i][j].tileY=1;
					}
					else if(top!=14&&bottom==14&&left==14&&right!=14&&southwest==14){
						tileMap[i][j].tileX=8;
						tileMap[i][j].tileY=10;
					}
					else if(top==14&&bottom!=14&&left!=14&&right==14){
						tileMap[i][j].tileX=8;
						tileMap[i][j].tileY=3;
					}
					else if(top==14&&bottom!=14&&left==14&&right!=14){
						tileMap[i][j].tileX=8;
						tileMap[i][j].tileY=12;
					}
					else if(top==14&&bottom==14&&left!=14&&right==14){//grass on the left, northwest and southwest, trees everywhere else
						tileMap[i][j].tileX=8;
						tileMap[i][j].tileY=2;
					}
					else if(top==14&&bottom==14&&left==14&&right!=14){//grass on the right, northeast and southeast, trees everywhere else
						tileMap[i][j].tileX=8;
						tileMap[i][j].tileY=11;
					}
					else if(top!=14&&bottom==14&&left==14&&right==14){
						tileMap[i][j].tileX=8;
						tileMap[i][j].tileY=4;
					}
					else if(top==14&&bottom==14&&left==14&&right==14){
						tileMap[i][j].tileX=8;
						tileMap[i][j].tileY=5;
					}
					else if(top==14&&bottom!=14&&left==14&&right==14){
						tileMap[i][j].tileX=8;
						tileMap[i][j].tileY=6;
					}

				}
				else if(tileMap[i][j].tileID==15){
					if(top==15&&bottom==15&&left==1&&right!=15){
						tileMap[i][j].tileX=0;
						tileMap[i][j].tileY=0;
						tileMap[i][j].beach = new Animation(beaches, 2, GamePanel.randomNumber(500, 1000), tileMap[i][j].tileX, 0, 0, true, 0, 0);
						tileMap[i][j].updatePos();
					}
					else if(top==1&&bottom!=15&&left==15&&right==15){
						tileMap[i][j].tileX=1;
						tileMap[i][j].tileY=0;
						tileMap[i][j].beach = new Animation(beaches, 2, GamePanel.randomNumber(500, 1000),  tileMap[i][j].tileX, 0, 0, true, 0, 0);
						tileMap[i][j].updatePos();
					}
					else if(top==15&&bottom==15&&left!=1&&right==1){
						tileMap[i][j].tileX=2;
						tileMap[i][j].tileY=0;
						tileMap[i][j].beach = new Animation(beaches, 2, GamePanel.randomNumber(500, 1000),  tileMap[i][j].tileX, 0, 0, true, 0, 0);
						tileMap[i][j].updatePos();
					}
					else if(top!=1&&bottom==1&&left==15&&right==15){
						tileMap[i][j].tileX=3;
						tileMap[i][j].tileY=0;
						tileMap[i][j].beach = new Animation(beaches, 2, GamePanel.randomNumber(500, 1000),  tileMap[i][j].tileX, 0, 0, true, 0, 0);
						tileMap[i][j].updatePos();
					}
					//corners
					//top right
					else if(top!=1&&bottom==15&&left==15&&right!=1){
						tileMap[i][j].tileX=7;
						tileMap[i][j].tileY=0;
						tileMap[i][j].beach = new Animation(beaches, 2, GamePanel.randomNumber(500, 1000),  tileMap[i][j].tileX, 0, 0, true, 0, 0);
						tileMap[i][j].updatePos();
					}
					//top left
					else if(top!=1&&bottom==15&&left!=1&&right==15){
						tileMap[i][j].tileX=6;
						tileMap[i][j].tileY=0;
						tileMap[i][j].beach = new Animation(beaches, 2, GamePanel.randomNumber(500, 1000),  tileMap[i][j].tileX, 0, 0, true, 0, 0);
						tileMap[i][j].updatePos();
					}
					//bottom right
					else if(top==15&&bottom!=1&&left==15&&right!=1){
						tileMap[i][j].tileX=4;
						tileMap[i][j].tileY=0;
						tileMap[i][j].beach = new Animation(beaches, 2, GamePanel.randomNumber(500, 1000),  tileMap[i][j].tileX, 0, 0, true, 0, 0);
						tileMap[i][j].updatePos();
					}
					//bottom left
					else if(top==15&&bottom!=1&&left!=1&&right==15){
						tileMap[i][j].tileX=5;
						tileMap[i][j].tileY=0;
						tileMap[i][j].beach = new Animation(beaches, 2, GamePanel.randomNumber(500, 1000),  tileMap[i][j].tileX, 0, 0, true, 0, 0);
						tileMap[i][j].updatePos();
					}
				}
				else if(tileMap[i][j].tileID==16){//river
					if(top==16&&bottom==16&&left!=16&&right!=16){//vertical
						tileMap[i][j].tileX=4;
						tileMap[i][j].tileY=2;
						if(tileMap[i][j].river==null)
							tileMap[i][j].river = new Animation(GamePanel.animatedTiles, 16, 200,  2, 0, 0, true, 0, 0);
						tileMap[i][j].updatePos();
					}
					else if(top!=16&&bottom!=16&&left==16&&right==16){//horizontal
						tileMap[i][j].tileX=4;
						tileMap[i][j].tileY=3;
						if(tileMap[i][j].river==null)
							tileMap[i][j].river = new Animation(GamePanel.animatedTiles, 16, 200,  3, 0, 0, true, 0, 0);
						tileMap[i][j].updatePos();
					}
					else if(top!=16&&bottom==16&&left!=16&&right==16){
						tileMap[i][j].tileX=4;
						tileMap[i][j].tileY=4;
					}
					else if(top==16&&bottom!=16&&left!=16&&right==16){
						tileMap[i][j].tileX=4;
						tileMap[i][j].tileY=5;
					}
					else if((top==16||top==17)&&bottom!=16&&left==16&&right!=16){//
						tileMap[i][j].tileX=4;
						tileMap[i][j].tileY=6;
					}
					else if(top!=16&&bottom==16&&left==16&&right!=16){
						tileMap[i][j].tileX=4;
						tileMap[i][j].tileY=7;
					}
				}
				else if(tileMap[i][j].tileID==17){//bridge
					if(((top==1&&bottom==1)||(top==16&&bottom==16))&&left==0&&right==0){
						tileMap[i][j].tileX=4;
						tileMap[i][j].tileY=8;
					}
					else if(top==0&&bottom==0&&((left==1&&right==1)||(left==16&&right==16))){
						tileMap[i][j].tileX=4;
						tileMap[i][j].tileY=9;
					}
				}
				//				if(tileMap[i][j].hasArrow){
				//					boolean topA = false,bottomA = false,leftA = false,rightA = false;
				//					if(i-1>=0){
				//						topA=tileMap[i-1][j].hasArrow;
				//					}
				//					if(i+1<40){
				//						bottomA=tileMap[i+1][j].hasArrow;
				//					}
				//					if(j-1>=0){
				//						leftA=tileMap[i][j-1].hasArrow;
				//					}
				//					if(j+1<60){
				//						rightA=tileMap[i][j+1].hasArrow;
				//					}
				//					//arrows
				//					if(!topA&&bottomA&&!leftA&&!rightA){
				//						tileMap[i][j].arrowX=6;
				//					}
				//					else if(!topA&&!bottomA&&leftA&&!rightA){
				//						tileMap[i][j].arrowX=7;
				//					}
				//					else if(topA&&!bottomA&&!leftA&&!rightA){
				//						tileMap[i][j].arrowX=8;
				//					}
				//					else if(!topA&&!bottomA&&!leftA&&rightA){
				//						tileMap[i][j].arrowX=9;
				//					}
				//					//straight segments
				//					else if(topA&&bottomA&&!leftA&&!rightA){
				//						tileMap[i][j].arrowX=0;
				//					}
				//					else if(!topA&&!bottomA&&leftA&&rightA){
				//						tileMap[i][j].arrowX=1;
				//					}
				//					//corners
				//					else if(topA&&!bottomA&&leftA&&!rightA){
				//						tileMap[i][j].arrowX=3;
				//					}
				//					else if(!topA&&bottomA&&leftA&&!rightA){
				//						tileMap[i][j].arrowX=2;
				//					}
				//					else if(topA&&!bottomA&&!leftA&&rightA){
				//						tileMap[i][j].arrowX=4;
				//					}
				//					else if(!topA&&bottomA&&!leftA&&rightA){
				//						tileMap[i][j].arrowX=5;
				//					}
				//				}
			}
		}
		if(Controller.currentUnit!=-1&&Controller.currentUnit<Units.size()){
			Unit temp = Units.get(Controller.currentUnit);
			//System.out.println("--------------------------------------");
			for(int i = 0; i<temp.movePath.size();i++){
				//not the first or the last position in the path
				if(i>0&&i<temp.movePath.size()-1){
					Point prev = temp.movePath.get(i-1);
					Point next = temp.movePath.get(i+1);
					//System.out.println("prev = "+prev.x+","+prev.y+" next = "+next.x+","+next.y);
					//previous is to the left and next is to the right
					if(prev.y==temp.movePath.get(i).y&&next.y==temp.movePath.get(i).y){
						tileMap[temp.movePath.get(i).y][temp.movePath.get(i).x].arrowX=1;//good
					}
					//previous is above and next is below
					if(prev.x==temp.movePath.get(i).x&&next.x==temp.movePath.get(i).x){
						tileMap[temp.movePath.get(i).y][temp.movePath.get(i).x].arrowX=0;//good
					}
					//left and above
					if((prev.y<temp.movePath.get(i).y&&next.x<temp.movePath.get(i).x)||(next.y<temp.movePath.get(i).y&&prev.x<temp.movePath.get(i).x)){
						tileMap[temp.movePath.get(i).y][temp.movePath.get(i).x].arrowX=3;
					}
					//right and above
					if((prev.y>temp.movePath.get(i).y&&next.x<temp.movePath.get(i).x)||(next.y>temp.movePath.get(i).y&&prev.x<temp.movePath.get(i).x)){
						tileMap[temp.movePath.get(i).y][temp.movePath.get(i).x].arrowX=2;
					}
					//left and below
					if((prev.y<temp.movePath.get(i).y&&next.x>temp.movePath.get(i).x)||(next.y<temp.movePath.get(i).y&&prev.x>temp.movePath.get(i).x)){
						tileMap[temp.movePath.get(i).y][temp.movePath.get(i).x].arrowX=4;
					}
					//right and below
					if((prev.y>temp.movePath.get(i).y&&next.x>temp.movePath.get(i).x)||(next.y>temp.movePath.get(i).y&&prev.x>temp.movePath.get(i).x)){
						tileMap[temp.movePath.get(i).y][temp.movePath.get(i).x].arrowX=5;//good
					}
				}
				//create proper arrow heads
				else if(i==temp.movePath.size()-1&&temp.movePath.size()>1){
					Point prev = temp.movePath.get(i-1);
					if(prev!=null&&temp.movePath!=null){
						if(prev.y<temp.movePath.get(i).y){
							tileMap[temp.movePath.get(i).y][temp.movePath.get(i).x].arrowX=8;
						}
						if(prev.y>temp.movePath.get(i).y){
							tileMap[temp.movePath.get(i).y][temp.movePath.get(i).x].arrowX=6;
						}
						if(prev.x<temp.movePath.get(i).x){
							tileMap[temp.movePath.get(i).y][temp.movePath.get(i).x].arrowX=7;
						}
						if(prev.x>temp.movePath.get(i).x){
							tileMap[temp.movePath.get(i).y][temp.movePath.get(i).x].arrowX=9;
						}
					}
				}
				else if(i == 0){
					tileMap[temp.movePath.get(i).x][temp.movePath.get(i).y].arrowX=10;
				}
			}

		}
	}
}
