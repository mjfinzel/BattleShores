package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.util.ArrayList;

public class ModifiableValue {
	int xpos=-1;
	int ypos=-1;
	int value=-1;
	int valueLowerLimit=0;
	int valueUpperLimit=0;
	String name = "";

	//if the value is a string
	String stringValue = "";
	int stringIndex = 0;
	ArrayList <String> possibleNames = new ArrayList<String>();
	public ModifiableValue(int x, int y, String valueName){
		this.xpos=x;
		this.ypos=y;
		this.name=valueName;
		if(valueName.equals("Current Name of This Unit")){
			possibleNames.add("Bandit Soldier");
			possibleNames.add("Tough Guy");
			possibleNames.add("Big Dude");
			possibleNames.add("Jeb");
			possibleNames.add("William");
			possibleNames.add("Tythus");
		}
		else if(valueName.equals("AI Used By This Unit")){
			possibleNames.add("None");
			possibleNames.add("Aggressive");
		}
		else if(valueName.equals("Character Class of this Unit")){
			possibleNames.add("Soldier");
			possibleNames.add("Archer");
		}
	}
	public boolean mouseOnTopButton(){
		//System.out.println("checking if mouse is on top button");
		if(value!=-1){
			if(MouseInfo.getPointerInfo().getLocation().x>=xpos&&MouseInfo.getPointerInfo().getLocation().y>=ypos){
				if(MouseInfo.getPointerInfo().getLocation().x<=xpos+40&&MouseInfo.getPointerInfo().getLocation().y<=ypos+15){
					return true;
				}
			}
		}
		else{
			if(MouseInfo.getPointerInfo().getLocation().x>=xpos&&MouseInfo.getPointerInfo().getLocation().y>=ypos){
				if(MouseInfo.getPointerInfo().getLocation().x<=xpos+200&&MouseInfo.getPointerInfo().getLocation().y<=ypos+15){
					return true;
				}
			}
		}
		return false;
	}
	public boolean mouseOnBottomButton(){
		//System.out.println("checking if mouse is on bottom button");
		if(stringValue==""){
			if(MouseInfo.getPointerInfo().getLocation().x>=xpos&&MouseInfo.getPointerInfo().getLocation().y>=ypos+47){
				if(MouseInfo.getPointerInfo().getLocation().x<=xpos+40&&MouseInfo.getPointerInfo().getLocation().y<=ypos+62){
					return true;
				}
			}
		}
		else{
			if(MouseInfo.getPointerInfo().getLocation().x>=xpos&&MouseInfo.getPointerInfo().getLocation().y>=ypos+47){
				if(MouseInfo.getPointerInfo().getLocation().x<=xpos+200&&MouseInfo.getPointerInfo().getLocation().y<=ypos+62){
					return true;
				}
			}
		}
		return false;
	}
	public void pressBottomButton(){
		if(value!=-1){
			if(value>valueLowerLimit){
				value--;
			}
			else{
				value=valueUpperLimit;
			}
		}
		else{//string
			if(stringIndex>0)
				stringIndex--;
			else
				stringIndex = possibleNames.size()-1;
			stringValue = possibleNames.get(stringIndex);

		}
	}
	public void pressTopButton(){
		if(value!=-1){
			//determine which button this is
			//if this is a button between 12 and 17
			//if the total value of buttons 12 through 17 added up is less than the power limit
			//increase the limit
			if(value<valueUpperLimit){
				value++;
			}
			else{
				value=valueLowerLimit;
			}
		}
		else{//string
			if(stringIndex<possibleNames.size()-1)
				stringIndex++;
			else
				stringIndex = 0;
			stringValue = possibleNames.get(stringIndex);
		}
	}
	public void Draw(Graphics g){
		Font font = new Font("Iwona Heavy",Font.BOLD,16);
		g.setFont(font);
		if(value!=-1&&stringValue==""){
			g.drawImage(GamePanel.modifiableValueArt, xpos, ypos,40, 62, null);
			if(mouseOnBottomButton()){//highlight the bottom button
				g.drawImage(GamePanel.buttonHighLight, xpos, ypos+47,40, 15, null);
			}
			if(mouseOnTopButton()){//highlight the top button
				g.drawImage(GamePanel.buttonHighLight, xpos, ypos,40, 15, null);
			}
		}
		else if(value==-1&&stringValue!=""){
			g.drawImage(GamePanel.modifiableStringValueArt, xpos, ypos,200, 62, null);
			if(mouseOnBottomButton()){//highlight the bottom button
				g.drawImage(GamePanel.buttonHighLight, xpos, ypos+47,200, 15, null);
			}
			if(mouseOnTopButton()){//highlight the top button
				g.drawImage(GamePanel.buttonHighLight, xpos, ypos,200, 15, null);
			}
		}

		if(value!=-1&&stringValue==""){
			if(value==valueUpperLimit){
				g.setColor(Color.BLUE);
			}
			else{
				g.setColor(Color.BLACK);
			}
			g.drawString(value+"", xpos+5, ypos+36);
			g.setColor(Color.BLACK);
			g.drawString(name, xpos+45, ypos+36);
		}
		else if(value==-1&&stringValue!=""){
			if(value==valueUpperLimit){
				g.setColor(Color.BLUE);
			}
			else{
				g.setColor(Color.BLACK);
			}
			g.drawString(stringValue, xpos+5, ypos+36);
			g.setColor(Color.BLACK);
			g.drawString(name, xpos+205, ypos+36);
		}
		g.setColor(Color.BLACK);
	}
}
