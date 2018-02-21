package Game;

import java.awt.Graphics;
import java.awt.MouseInfo;

public class LockMechanism {
	int xpos = 0;
	int ypos = 0;
	int greenBars;
	int redBars;
	int greenLength;
	int redLength;
	int selectorX = 0;
	int selectorSpeed = 1;
	int selectorLocation = -1;//the colorID of the bar at the selector's location
	boolean increasing = true;
	boolean interrupted = false;//if the mechanism has been interupted yet
	boolean visible = false;
	DropDownButton interruptButton;
	int[] bar = new int[100];//grey = 0, green = 1; red = 2
	int recentUpdates=0;
	public LockMechanism(int x, int y,int greenL, int redL, int greenCount, int redCount, int speed){
		greenBars=greenCount;
		redBars=redCount;
		greenLength=greenL;
		redLength=redL;
		selectorSpeed = speed;
		xpos = x;
		ypos = y;
		selectorX=x+3;
		interruptButton = new DropDownButton(xpos+325,ypos+10,"Interrupt");
		//initialize the bar to be entirely grey
		for(int i = 0; i<100;i++){
			bar[i]=0;
		}
		//for the number of green bars
		for(int i = 0; i<greenBars;i++){
			//generate a random position for this bar and check if it's a valid position, if it isn't then retry
			boolean notFoundRoom = true;
			int pos=0;
			int tries = 0;
			while(notFoundRoom&&tries<200){//while it hasn't found room
				tries++;
				notFoundRoom=false;
				//generate a random position
				pos = GamePanel.randomNumber(0, 99-greenLength);
				for(int j = pos; j<(pos+greenLength);j++){

					if(bar[j]!=0){//if any of these positions are not grey
						notFoundRoom=true;
					}
				}
			}
			for(int j = pos; j<pos+greenLength;j++){
				bar[j]=1;
			}
		}

		//for the number of red bars
		for(int i = 0; i<redBars;i++){
			//generate a random position for this bar and check if it's a valid position, if it isn't then retry
			boolean notFoundRoom = true;
			int pos=0;
			int tries = 0;
			while(notFoundRoom&&tries<200){//while it hasn't found room
				tries++;
				notFoundRoom=false;
				//generate a random position
				pos = GamePanel.randomNumber(0, 99-redLength);
				for(int j = pos; j<pos+redLength;j++){
					if(bar[j]!=0){//if any of these positions are not grey
						notFoundRoom=true;
					}
				}
			}
			for(int j = pos; j<pos+redLength;j++){
				bar[j]=2;
			}
		}
	}
	public boolean mouseOnInterruptButton(){
		if(MouseInfo.getPointerInfo().getLocation().x>=interruptButton.xpos&&MouseInfo.getPointerInfo().getLocation().y>=interruptButton.ypos){
			if(MouseInfo.getPointerInfo().getLocation().x<=interruptButton.xpos+180&&MouseInfo.getPointerInfo().getLocation().y<=interruptButton.ypos+30){
				return true;
			}
		}
		return false;
	}
	public void interrupt(){
		
		//loop through every bar position
		for(int i = 0; i<100; i++){
			if(selectorX>=(i*3)+10+xpos&&selectorX-xpos<(i*3)+xpos+13){
				this.selectorLocation=bar[i];
			}
		}
		if(this.selectorLocation==1){//green
			this.interrupted=true;
		}
		if(this.selectorLocation==2){//red
			this.interrupted=true;
		}

	}
	public void update(){	
		recentUpdates++;
		if(recentUpdates>=selectorSpeed){
			recentUpdates=0;
			if(!interrupted){
				if(increasing){
					this.selectorX+=1;
				}
				else{
					this.selectorX-=1;
				}
			}

			if(selectorX<=xpos+3||selectorX>=xpos+303){
				if(increasing){
					this.increasing=false;
				}
				else{
					this.increasing=true;
				}
			}
		}
		
	}
	public void Draw(Graphics g){
		this.interruptButton.Draw(g);
		//if the mouse is over the interrupt button
		if(mouseOnInterruptButton()){
			g.drawImage(GamePanel.buttonHighLight, interruptButton.xpos, interruptButton.ypos,180, 30, null);
		}

		g.drawImage(GamePanel.button, xpos,ypos,320,50,null);
		for(int i = 0; i<100;i++){
			g.drawImage(GamePanel.miniGameBars[bar[i]][0], xpos+10+(i*3),ypos+10,3,30,null);
		}

		g.drawImage(GamePanel.miniGameSelector, selectorX,ypos+3,15,44,null);

	}
}
