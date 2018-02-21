package Game;

import java.awt.Graphics;
import java.util.ArrayList;

public class LockedChest {
	int xpos;
	int ypos;
	Item loot;//the item contained inside the chest
	Unit occupant;
	public ArrayList<LockMechanism> mechanisms = new ArrayList<LockMechanism>();
	boolean mechanismIsVisible = false;
	boolean opened = false;
	public LockedChest(int x, int y, int item, int difficulty){
		xpos=x;
		ypos=y;
		loot = new Item(item);
		int numberOfMechanisms = GamePanel.randomNumber(1, difficulty);
		for(int i = numberOfMechanisms; i>0;i--){
			//System.out.println("adding a new lock mechanism: "+i);
			mechanisms.add(new LockMechanism(800,115+(mechanisms.size()*60),GamePanel.randomNumber(10,30),GamePanel.randomNumber(5,15),GamePanel.randomNumber(1,1),GamePanel.randomNumber(1,3),difficulty));
			//System.out.println("added a lock mechanism: "+i);
		}
	}
	public void open(Unit user){
		GamePanel.chestActive=true;
		mechanismIsVisible=true;
		occupant=user;
	}
	public void dropLoot(Unit reciever){

		if(reciever.hasInventorySpace()){
			reciever.getItem(loot);
			GamePanel.eventMessages.add(new EventMessage("Found ",loot.itemID));
			//set this chest to an opened state
			this.opened=true;
			GamePanel.chestActive=false;
			this.mechanismIsVisible=false;
			GamePanel.currentChest=-1;
			reciever.greyOut(true);
		}
		else{
			//inform the player that they had no room for the item
			GamePanel.eventMessages.add(new EventMessage("You found an item but couldn't carry it! Left it on the ground!",-1));
		}
		Controller.mouseClickState=0;
	}
	public void Draw(Graphics g){
		if(!opened){
			g.drawImage(GamePanel.chestImages[0][0], this.xpos*32, this.ypos*32,32, 32, null);
		}
		else{
			g.drawImage(GamePanel.chestImages[1][0], this.xpos*32, this.ypos*32,32, 32, null);
		}
		if(this.mechanismIsVisible){
			int successfulMechs = 0;
			if(GamePanel.levels.get(GamePanel.level).chests.get(GamePanel.currentChest)==this){

				for(int i = 0; i<mechanisms.size();i++){
					//if one of the mechanisms was failed(red)
					if(mechanisms.get(i).selectorLocation==2){
						//set the mechanisms to be not visible
						this.mechanismIsVisible=false;
						GamePanel.chestActive=false;
						GamePanel.currentChest=-1;
						//grey out the occupant
						occupant.greyOut(true);
						Controller.mouseClickState=0;
						mechanisms.get(i).selectorLocation=0;
						mechanisms.get(i).interrupted=false;
						System.out.println("removed mechanisms from display! There were "+mechanisms.size()+" mechanisms and mechanism: "+i+" was red.");
					}
					//if all of the mechanisms are opened succesfully
					else if(mechanisms.get(i).selectorLocation==1&&mechanisms.get(i).interrupted){
						successfulMechs++;
						if(successfulMechs==mechanisms.size()&&mechanisms.size()>0&&!opened){
							dropLoot(occupant);
						}
					}
					mechanisms.get(i).Draw(g);
				}
			}
			

		}
	}
}
