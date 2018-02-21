package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class Speech {
	int xpos;
	int ypos;
	int speechState = -1;
	ArrayList <String> messages = new ArrayList<String>();
	/*
	 * cycles through each message given and draws it in a speech bubble at the x,y position on the screen
	 */
	public Speech(int x, int y){
		xpos = x;
		ypos = y;
	}
	public void addMessage(String msg){
		messages.add(msg);
	}
	public void update(){

		speechState++;

	}
	public void Draw(Graphics g){
		if(speechState>=0&&speechState<messages.size()){
			//System.out.println("drawing speech");
			g.drawImage(GamePanel.currentBattle.loser.portrait, xpos-40, ypos,40, 40, null);
			g.drawImage(GamePanel.speechBubble, xpos, ypos,200, 40, null);
			Font font = new Font("Iwona Heavy",Font.BOLD,12);
			g.setFont(font);
			g.setColor(Color.black);
			//System.out.println("messages.size() = "+messages.size());
			if(speechState<messages.size()){
				g.drawString(messages.get(speechState), xpos+10, ypos+20);
				//System.out.println(messages.get(speechState));
			}
//			else{
//				System.out.println("ending the battle");
//				GamePanel.levels.get(GamePanel.level).Units.remove(GamePanel.currentBattle.loser);
//				GamePanel.battle=false;
//				GamePanel.currentBattle=null;
//			}
		}
	}
}
