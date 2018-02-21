package Game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Cursor {
	static BufferedImage[][] cursorImg = Images.cut("/textures/Cursor.png",36,36);
	public static Point cursorPos = new Point(-2,-2);
	public static ArrayList<Point> arrows = new ArrayList<Point>();
	public static int movecount = 1;
	public static Animation cursor = new Animation(cursorImg, 2, 800, 0, cursorPos.x, cursorPos.y, true, 0, 0);
	public static void drawArrow(int x, int y, int movelimit){
		if(movecount<movelimit){
			arrows.add(new Point(x/32,y/32));
			GamePanel.levels.get(GamePanel.level).tileMap[x/32][y/32].hasArrow=true;
			movecount++;
		}
		

	}
	public void Draw(Graphics g){
		cursor.Draw(g);
	}
}
