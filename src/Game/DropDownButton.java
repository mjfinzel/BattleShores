package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;

public class DropDownButton {
	public int xpos = -1;
	public int ypos = -1;
	public boolean isSelected = false;
	String description;
	int itemIndex = -1;
	public DropDownButton(int x, int y, String name){
		xpos=x;
		ypos=y;
		description = name;

	}
	public void pressButton(){

		if(description == "Attack"){
			//GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).getAttackableUnits();
			Controller.mouseClickState=4;
			GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).greyOut(true);
			Controller.moving=false;
			GamePanel.menuHidden = true;
			//GamePanel.battle = true;
		}
		else if(description == "Wait"){
			GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).greyOut(true);
			Controller.moving=false;
			Controller.currentUnit=-1;
			GamePanel.menuHidden = true;
			Controller.mouseClickState=0;
		}
		else if(description == "Create Unit"){
			System.out.println("Create Unit was clicked on!");
			//assign the proper stats to the new unit



			GamePanel.levels.get(GamePanel.level).Units.add(GamePanel.newUnCreatedUnit);
			GamePanel.newUnCreatedUnit=null;
			System.out.println("added a unit!");
		}
		else if(description == "Open"){
			
				GamePanel.levels.get(GamePanel.level).chests.get(GamePanel.currentChest).open(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit));
				GamePanel.chestActive=true;
			
		}
		else if(description == "Exit Game"){
			System.exit(0);
		}
		else if(description == "End Turn"){

			if(GamePanel.currentTurn<1){
				GamePanel.currentTurn++;
			}
			else{
				GamePanel.currentTurn=0;
			}
			for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.size();i++){
				GamePanel.levels.get(GamePanel.level).Units.get(i).greyOut(false);
				System.out.println("current turn set to: "+GamePanel.currentTurn);
			}
			Controller.mouseClickState=0;
		}
		else if(description == "Cancel"){
			if(Controller.currentUnit!=-1){
				GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).xpos = GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).posBeforeMove.x;
				GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).ypos = GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).posBeforeMove.y;
				GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).staticMapSpriteAnim.xpos = GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).posBeforeMove.x*32;
				GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).staticMapSpriteAnim.ypos = (GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).posBeforeMove.y*32)-20;
			}

			GamePanel.menuHidden=true;
			Controller.mouseClickState=0;

			if(GamePanel.itemSubMenuOpen){
				GamePanel.menuHidden=true;
				Controller.mouseClickState=0;
				GamePanel.itemMenuOpen=false;
				GamePanel.itemSubMenuOpen=false;
			}
		}
		else if(description == "Level Editor"){
			GamePanel.titleScreen=false;
			GamePanel.menuHidden = true;

		}
		else if(description == "New Game"){
			System.out.println("new game was pressed");
			GamePanel.titleScreen=false;
			GamePanel.menuHidden = true;
			Controller.loadLevel(GamePanel.levelQueue.get(0));
			System.out.println("new game success");
		}
		else if(description == "Items"){
			System.out.println("Clicked on the items button!");
			int oldMenuX=GamePanel.menu.xpos;
			int oldMenuY=GamePanel.menu.ypos;
			GamePanel.itemMenuOpen=true;
			GamePanel.menu=new DropDownMenu(new Point(oldMenuX,oldMenuY));
		}
		else if(description == "Use"){
			System.out.println("current unit: "+Controller.currentUnit);
			//System.out.println("selected item: "+itemIndex);
			GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[GamePanel.selectedItem].Use(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit));
			GamePanel.selectedItem=-1;
			GamePanel.itemMenuOpen=false;
			GamePanel.itemSubMenuOpen=false;
		}
		else if(description == "Discard"){
			GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[GamePanel.selectedItem]=null;
			GamePanel.menuHidden=true;
			Controller.mouseClickState=0;
			GamePanel.selectedItem=-1;
			GamePanel.itemMenuOpen=false;
			GamePanel.itemSubMenuOpen=false;
		}
		else if(itemIndex!=-1&&GamePanel.itemMenuOpen==false){
			if(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[itemIndex].name==this.description){
				GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).equipItem(itemIndex);
				Controller.mouseClickState=0;
				System.out.println("equiped an item");
			}
		}
		else if(GamePanel.itemMenuOpen==true){//if the item menu is open, create a new menu to equip, discard, etc
			GamePanel.selectedItem=itemIndex;
			int oldMenuX=GamePanel.menu.xpos;
			int oldMenuY=GamePanel.menu.ypos;
			GamePanel.itemMenuOpen=true;
			GamePanel.menu=new DropDownMenu(new Point(oldMenuX,oldMenuY));
			GamePanel.itemSubMenuOpen = true;

		}


	}
	public void Draw(Graphics g){
		Font font = new Font("Iwona Heavy",Font.BOLD,12);;
		g.setFont(font);
		g.setColor(Color.black);
		if(!GamePanel.battle){
			g.drawImage(GamePanel.button, xpos, ypos,180, 30, null);
			g.drawString(description, xpos+5, ypos+20);
		}
		//check if this is the current button
		if(MouseInfo.getPointerInfo().getLocation().x>=this.xpos&&MouseInfo.getPointerInfo().getLocation().x<=(this.xpos+180)&&MouseInfo.getPointerInfo().getLocation().y>=this.ypos&&MouseInfo.getPointerInfo().getLocation().y<(this.ypos+30)){
			for(int i = 0; i<GamePanel.menu.buttons.size();i++){
				if(GamePanel.menu.buttons.get(i)==this){
					GamePanel.menu.currentButton = i;
					g.drawImage(GamePanel.buttonHighLight, xpos, ypos,180, 30, null);
				}
			}

			//System.out.println("changed currentbutton to "+i);
		}
		else{
			GamePanel.menu.currentButton=-1;
		}

	}
}
