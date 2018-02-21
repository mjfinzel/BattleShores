package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class EventMessage {
	long creationTime = System.currentTimeMillis();
	int duration = 3000;
	int timePerLetter = 130;
	int xpos=945;
	int ypos=540;
	Item item;
	int messageWidth;
	String message;
	String aOrAn = "";
	//used for messages like: "You found a sword!" that disappear after a few seconds
	public EventMessage(String msg, int itemID){
		
		message = msg;
		if(itemID>=0){
			item = new Item(itemID);
			if(isVowel(item.name.charAt(0))){
				aOrAn="a ";
			}
			else{
				aOrAn="an ";
			}
			message=message+aOrAn;
			messageWidth = (message.length()+aOrAn.length()+item.name.length())*8;
		}
		else{
			messageWidth = message.length()*8;
		}
		duration=timePerLetter*(messageWidth/8);
		xpos=xpos-(messageWidth/2);

	}
	public boolean isVowel(char c){
		if(item.name.charAt(0)=='a'||item.name.charAt(0)=='e'||item.name.charAt(0)=='i'||item.name.charAt(0)=='o'||item.name.charAt(0)=='u'||
				item.name.charAt(0)=='A'||item.name.charAt(0)=='E'||item.name.charAt(0)=='I'||item.name.charAt(0)=='O'||item.name.charAt(0)=='U'){
			return true;
		}
		return false;
	}
	public void update(){
		if(creationTime+duration<=System.currentTimeMillis()){
			GamePanel.eventMessages.remove(this);
		}

	}
	public void Draw(Graphics g){
		Font font = new Font("Iwona Heavy",Font.BOLD,14);
		g.setFont(font);
		//get metrics from font
		FontMetrics metrics = g.getFontMetrics(font);
		//get the advance of the message in this font
		int adv = 0;
		g.setColor(Color.BLACK);
		if(item.itemID==-1){
		messageWidth = metrics.stringWidth(message);
		}
		else{
			messageWidth=metrics.stringWidth(message)+metrics.stringWidth(item.name);
		}
		g.drawImage(GamePanel.button, xpos-10, ypos,(int)messageWidth+20, 30, null);
		//draw the message
		for(int i = 0; i<message.length();i++){
			g.drawString(message.charAt(i)+"", xpos+adv, ypos+20);
			adv=adv+(metrics.charWidth(message.charAt(i)));
		}
		if(item!=null){
			g.setColor(Color.blue);
			g.drawString(item.name+"!", xpos+(adv), ypos+20);
		}
	}
}
