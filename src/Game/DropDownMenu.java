package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DropDownMenu {


	//ArrayList <String> menuButtons = new ArrayList<String>();
	int xpos = 0;
	int ypos = 0;
	public int currentButton = -1;
	public ArrayList <DropDownButton> buttons = new ArrayList<DropDownButton>();
	public DropDownMenu(Point pos){
		xpos=pos.x;
		ypos=pos.y;
		System.out.println("created dropdown menu");
		//not in battle, unit is selected, unit is capable of attacking another unit
		if(GamePanel.newUnCreatedUnit==null&&!GamePanel.battle&&GamePanel.currentBattle==null&&Controller.currentUnit!=-1&&GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).getAttackableUnits().size()>0&&!GamePanel.titleScreen){
			buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Attack"));
		}
		//not in battle, unit is selected
		if(GamePanel.newUnCreatedUnit==null&&!GamePanel.battle&&GamePanel.currentBattle==null&&Controller.currentUnit!=-1&&!GamePanel.titleScreen&&!GamePanel.itemMenuOpen){
			buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Items"));
			for(int i = 0; i<GamePanel.levels.get(GamePanel.level).chests.size();i++){
				if(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).xpos==GamePanel.levels.get(GamePanel.level).chests.get(i).xpos&&GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).ypos==GamePanel.levels.get(GamePanel.level).chests.get(i).ypos){
					GamePanel.currentChest=i;
					if(GamePanel.levels.get(GamePanel.level).chests.get(GamePanel.currentChest).opened==false){
						buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Open"));
					}
				}
			}
			buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Wait"));
		}

		//selecting a weapon
		if(GamePanel.newUnCreatedUnit==null&&Controller.mouseClickState==5&&!GamePanel.titleScreen){
			System.out.println("added a button");
			System.out.println(Controller.currentUnit);
			System.out.println(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit));

			for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items.length;i++){
				if(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i]!=null){
					if(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].isWeapon||GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].isWeapon){
						DropDownButton temp = new DropDownButton(xpos,ypos+(buttons.size())*30,GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].currentDurability+"/"+GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].durability+" "+GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].name);
						temp.itemIndex=i;
						buttons.add(temp);

					}
				}
			}
		}
		//selecting an item
		if(GamePanel.newUnCreatedUnit==null&&GamePanel.itemMenuOpen&&!GamePanel.itemSubMenuOpen){
			for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items.length;i++){
				if(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i]!=null){
					DropDownButton temp = new DropDownButton(xpos,ypos+(buttons.size())*30,GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].currentDurability+"/"+GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].durability+" "+GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].name);
					temp.itemIndex=i;
					buttons.add(temp);
					System.out.println("Added a new button with item index: "+buttons.get(buttons.size()-1).itemIndex);
					//buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].currentDurability+"/"+GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].durability+" "+GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[i].name));
					//buttons.get(buttons.size()-1).itemIndex=i;
				}
			}
		}
		//item submenu is open (equip/discard/etc)
		if(GamePanel.newUnCreatedUnit==null&&GamePanel.itemSubMenuOpen){
			if(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[GamePanel.selectedItem].isWeapon||GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[GamePanel.selectedItem].isSpell){

				buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Equip"));
			}
			else if(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).items[GamePanel.selectedItem].isPotion){
				buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Use"));
			}
			buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Discard"));
		}
		//not in battle and no unit is selected
		if(GamePanel.newUnCreatedUnit==null&&!GamePanel.battle&&GamePanel.currentBattle==null&&!GamePanel.titleScreen){
			if(Controller.currentUnit==-1){
				buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"End Turn"));
				buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Exit Game"));
				buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Items"));
			}
			buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Cancel"));
		}
		//title screen
		if(GamePanel.titleScreen){
			buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"New Game"));
			buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Load Game"));
			buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Level Editor"));
			buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Exit Game"));
		}
		if(GamePanel.newUnCreatedUnit!=null){
			buttons.add(new DropDownButton(xpos,ypos+(buttons.size())*30,"Create Unit"));
		}




	}
	public void Draw(Graphics g){
		//System.out.println("current button = "+currentButton);
		g.drawImage(GamePanel.menuBorder, xpos-3, ypos-3,186, (30*buttons.size())+6, null);
		for(int i = 0; i<buttons.size();i++){
			buttons.get(i).Draw(g);
			//System.out.println("drawing buttons");
		}
		boolean found = false;
		for(int i = 0; i<GamePanel.menu.buttons.size();i++){
			if(MouseInfo.getPointerInfo().getLocation().x>=GamePanel.menu.buttons.get(i).xpos&&MouseInfo.getPointerInfo().getLocation().x<=(GamePanel.menu.buttons.get(i).xpos+180)&&MouseInfo.getPointerInfo().getLocation().y>=GamePanel.menu.buttons.get(i).ypos&&MouseInfo.getPointerInfo().getLocation().y<(GamePanel.menu.buttons.get(i).ypos+30)){

				GamePanel.menu.currentButton = i;
				found = true;
			}
		}
		if(!found){
			GamePanel.menu.currentButton=-1;
		}

	}
}
