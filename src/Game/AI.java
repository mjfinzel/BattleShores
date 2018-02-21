package Game;

import java.awt.Point;
import java.util.ArrayList;

public class AI{
	static Unit unit;
	public static ArrayList <ArrayList<Point>> paths = new ArrayList<ArrayList<Point>>();
	public AI(){


	}
	public static void update(){
		boolean greyout = true;
		if(Controller.currentUnit!=-1&&GamePanel.levels.size()>GamePanel.level&&GamePanel.levels.get(GamePanel.level).Units.size()>Controller.currentUnit){
			if(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).greyedOut==false){
				unit = GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit);
				if(unit.AI_ID==0&&unit.greyedOut==false&&GamePanel.currentTurn==unit.team&&unit.team!=0){
					unit = GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit);
					System.out.println("checking for hostile units");
					//move the current unit to the closest enemy unit
					for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
						//System.out.println("in for loop");
						//if any of the units aren't on this unit's team and aren't this unit
						if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=unit.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=unit&&!GamePanel.battle){
							//System.out.println("detected hostile units, checking if they're in range");
							//System.out.println("enemy unit position is: "+GamePanel.levels.get(GamePanel.level).Units.get(j).xpos+","+GamePanel.levels.get(GamePanel.level).Units.get(j).ypos+" this unit is at: "+unit.xpos+","+unit.ypos);
							int temp = getShortestPathToPoint(new Point(GamePanel.levels.get(GamePanel.level).Units.get(j).xpos,GamePanel.levels.get(GamePanel.level).Units.get(j).ypos),true).size();
							//System.out.println("length of the shortest path to target is: "+temp+" tiles");
							//if any of these units are within the attack range of this unit
							if(temp>0&&getShortestPathToPoint(new Point(GamePanel.levels.get(GamePanel.level).Units.get(j).xpos,GamePanel.levels.get(GamePanel.level).Units.get(j).ypos),true).size()<=(unit.moveRange+unit.items[unit.currentWeapon()].maxWeaponRange)){
								//System.out.println("detected a hostile unit that is in range, starting battle, shortest path was length: "+getShortestPathToPoint(new Point(GamePanel.levels.get(GamePanel.level).Units.get(j).xpos,GamePanel.levels.get(GamePanel.level).Units.get(j).ypos),true).size());
								GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).movePath=getShortestPathToPoint(new Point(GamePanel.levels.get(GamePanel.level).Units.get(j).xpos,GamePanel.levels.get(GamePanel.level).Units.get(j).ypos),true);
								
								//draw the path that was moved over on the map
								for(int k = 0; k<GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).movePath.size();k++){
									//GamePanel.levels.get(GamePanel.level).tileMap[GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).movePath.get(k).y][GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).movePath.get(k).x].actionType=2;
									
								}
								//move to that unit
								GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).moveToTile();
								//attack that unit
								GamePanel.currentBattle = new Battle(unit,GamePanel.levels.get(GamePanel.level).Units.get(j));
								GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).greyOut(true);
								GamePanel.battle = true;

							}
						}
						else{
							greyout=false;
						}
					}
					unit.greyOut(greyout);
				}
				else if(unit.AI_ID==1&&unit.greyedOut==false&&GamePanel.currentTurn==unit.team&&unit.team!=0){
					
				}
				else if(unit.AI_ID==1&&unit.greyedOut==false&&GamePanel.currentTurn==unit.team&&unit.team!=0){
					
				}
			}
		}
	}
	public static ArrayList<Point> getShortestPathToPoint(Point destination, boolean findAdjacent){
		//System.out.println("destination = "+destination.x+","+destination.y);
		unit = GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit);
		paths.clear();
		//System.out.println("Cleared paths");
		Point temp = new Point(unit.ypos,unit.xpos);
		if(!findAdjacent){
			findAllPaths((new ArrayList<Point>()), temp,destination);
		}
		else{//find the shortest path to any tile adjacent to the destination
			Point destinationA = new Point(destination.x+1,destination.y);
			Point destinationB = new Point(destination.x-1,destination.y);
			Point destinationC = new Point(destination.x,destination.y+1);
			Point destinationD = new Point(destination.x,destination.y-1);
			findAllPaths((new ArrayList<Point>()), temp,destinationA);
			findAllPaths((new ArrayList<Point>()), temp,destinationB);
			findAllPaths((new ArrayList<Point>()), temp,destinationC);
			findAllPaths((new ArrayList<Point>()), temp,destinationD);
		}
		ArrayList <Point> shortestPath = new ArrayList<Point>();
		if(paths.size()>0){
			//System.out.println("there are "+paths.size()+" paths to the point");
			//System.out.println("These paths are: ");
			for(int i = 0; i<paths.size();i++){
				System.out.println(paths.get(i).toString());
			}
			shortestPath = paths.get(0);
			//System.out.println("initial shortest path = "+paths.get(0).toString());
			for(int i = 0; i<paths.size();i++){
				//System.out.println("checking if path is shorter than "+shortestPath.toString());
				if(paths.get(i).size()<shortestPath.size()){
					shortestPath=paths.get(i);
					//System.out.println("Set shortest path to: "+paths.get(i).toString());
				}
			}
		}
		else{
			//System.out.println("there is no path to the point");
		}


		return shortestPath;
	}
	public static void findAllPaths(ArrayList <Point>path, Point node, Point destination)
	{   
		unit = GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit);
		Point temp = new Point(node.y,node.x);
		path.add(temp);
		//System.out.println("path is visiting: "+node.x+","+node.y);
		if(path.size() > 0&&temp.x==destination.x&&temp.y==destination.y){//&&path.size()<=unit.moveRange
			//System.out.println("Added a path! This path is size: "+path.size());
			ArrayList <Point> temperary = new ArrayList <Point>();
			for(int i = 0; i<path.size();i++){
				temperary.add(path.get(i));
			}

			paths.add(temperary);
			//System.out.println("Paths: "+paths.toString());
			//System.out.println("Paths after adding: "+paths.toString());
			//System.out.println("number of paths:" +paths.size());
			//System.out.println("complete");
		}

		for(int i = 0; i<getPassableAdjacentTiles(temp).size();i++){
			if(path.size()<unit.moveRange) findAllPaths(path, getPassableAdjacentTiles(temp).get(i),destination);
		}
		path.remove(temp);

	}
	public static ArrayList<Point> getPassableAdjacentTiles(Point tile){
		unit = GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit);
		ArrayList<Point> adjacentTiles = new ArrayList<Point>();
		if(tile.x-1>0){
			boolean crashed = false;
			//loop through units
			for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
				//check if any units are in the same position as this tile and they're not on this units team
				if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=unit.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=unit&&tile.y==GamePanel.levels.get(GamePanel.level).Units.get(j).ypos&&(tile.x-1)==GamePanel.levels.get(GamePanel.level).Units.get(j).xpos){
					crashed = true;
				}
			}
			if(GamePanel.levels.get(GamePanel.level).tileMap[tile.y][(tile.x-1)].collisionType<unit.mobility&&!crashed)
				adjacentTiles.add(new Point(tile.y,(tile.x-1)));
		}
		if(tile.y-1>0){
			boolean crashed = false;
			//loop through units
			for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
				//check if any units are in the same position as this tile and they're not on this units team
				if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=unit.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=unit&&(tile.y-1)==GamePanel.levels.get(GamePanel.level).Units.get(j).ypos&&(tile.x)==GamePanel.levels.get(GamePanel.level).Units.get(j).xpos){
					crashed = true;
				}
			}
			if(GamePanel.levels.get(GamePanel.level).tileMap[(tile.y-1)][(tile.x)].collisionType<unit.mobility&&!crashed)
				adjacentTiles.add(new Point((tile.y-1),(tile.x)));
		}
		if(tile.x+1<GamePanel.levels.get(GamePanel.level).levelWidth){
			boolean crashed = false;
			//loop through units
			for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
				//check if any units are in the same position as this tile and they're not on this units team
				if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=unit.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=unit&&tile.y==GamePanel.levels.get(GamePanel.level).Units.get(j).ypos&&(tile.x+1)==GamePanel.levels.get(GamePanel.level).Units.get(j).xpos){
					crashed = true;
				}
			}
			if(GamePanel.levels.get(GamePanel.level).tileMap[tile.y][(tile.x+1)].collisionType<unit.mobility&&!crashed)
				adjacentTiles.add(new Point(tile.y,(tile.x+1)));
		}
		if(tile.y+1<GamePanel.levels.get(GamePanel.level).levelHeight){
			boolean crashed = false;
			//loop through units
			for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
				//check if any units are in the same position as this tile and they're not on this units team
				if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=unit.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=unit&&(tile.y+1)==GamePanel.levels.get(GamePanel.level).Units.get(j).ypos&&(tile.x)==GamePanel.levels.get(GamePanel.level).Units.get(j).xpos){
					crashed = true;
				}
			}
			if(GamePanel.levels.get(GamePanel.level).tileMap[(tile.y+1)][(tile.x)].collisionType<unit.mobility&&!crashed)
				adjacentTiles.add(new Point((tile.y+1),(tile.x)));
		}
		return adjacentTiles;
	}
}
