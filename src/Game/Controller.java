package Game;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JPanel;



public class Controller extends JPanel implements KeyListener,MouseListener,MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int xpos=0;
	public static int ypos=0;
	public static int westEdge = -2;
	public static int eastEdge = -2;
	public static int northEdge = -2;
	public static int southEdge = -2;
	public static String text = "";
	public static boolean saving = false;
	public static boolean loading = false;
	public static boolean moving = false;
	public static int mouseClickState = 0;//1=mouse dragging, 2=no mouse draggin, 3=pick menu option, 4 menu dissapear
	static int currentTileID = 0;
	public static int currentUnit = -1;//unit that is currently selected
	public static int hoveredUnit = -1;//unit the mouse is hovering over
	public static Point lastValidMousePosition = new Point(0,0);
	//public static Unit currentUnit;
	public static String currentTileName = "No tiles selected";
	private JPanel gamePanel;
	long starttime = 0;
	private static boolean[] keyboardState = new boolean[525];
	public static boolean mouseDragging = false;

	public Controller(){
		this.setDoubleBuffered(true);
		westEdge = -2;
		eastEdge = (GamePanel.levels.get(GamePanel.level).levelWidth*32)-34;
		northEdge = -2;
		southEdge = (GamePanel.levels.get(GamePanel.level).levelHeight*32)-34;
	}
	public static boolean keyboardKeyState(int key)
	{
		return keyboardState[key];
	}
	public void setGamePanel(JPanel panelRef) {
		gamePanel = panelRef;
		gamePanel.addKeyListener(this);
		gamePanel.addMouseListener(this);
		gamePanel.addMouseMotionListener(this);
	}
	public void updateAll(){
		if (gamePanel != null)
			gamePanel.getParent().repaint();
	}
	public static void updateMovePath(){
		if(moving&&GamePanel.levels.get(GamePanel.level).tileMap[MouseInfo.getPointerInfo().getLocation().y/32][MouseInfo.getPointerInfo().getLocation().x/32].actionType==1){

			int overlapPoint = 99999;
			//System.out.println("begining check, path size = "+GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.size());
			//loop through the path and find the index of the first occurance of this point
			for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.size();i++){
				if(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.get(i).equals(new Point(MouseInfo.getPointerInfo().getLocation().x/32,MouseInfo.getPointerInfo().getLocation().y/32))){
					overlapPoint = i;
					//System.out.println("discovered overlap point at "+i+"!");
					break;
				}
			}
			//remove all the points after the collision in the path and add this point
			while(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.size()>overlapPoint){

				int i = GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.size()-1;
				GamePanel.levels.get(GamePanel.level).tileMap[GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.get(i).y][GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.get(i).x].hasArrow=false;


				GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.remove(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.size()-1);
				//System.out.println("removed a point!");
			}
			//add tiles to the path
			GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.add(new Point(MouseInfo.getPointerInfo().getLocation().x/32,MouseInfo.getPointerInfo().getLocation().y/32));

			//add arrows to the map
			if(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.size()>1)
				GamePanel.levels.get(GamePanel.level).tileMap[MouseInfo.getPointerInfo().getLocation().y/32][MouseInfo.getPointerInfo().getLocation().x/32].hasArrow=true;
			//System.out.println("Added a point to the path! size = "+GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.size());
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {

		keyboardState[e.getKeyCode()] = true;
		//updateAll();
		if(keyboardKeyState(KeyEvent.VK_UP)&&!saving&&!loading){
			if(Cursor.cursorPos.y>northEdge){
				Cursor.cursorPos.y-=32;
				if(moving){
					//Cursor.drawArrow(Cursor.cursorPos.y+2, Cursor.cursorPos.x+2, 5);
				}
			}
			else if(Cursor.cursorPos.y==northEdge){

				if(GamePanel.mapX<0){
					//System.out.println("exiting top edge");
					GamePanel.mapX+=1;
					GamePanel.levels.get(GamePanel.level).update();
					//System.out.println("up");
					if(moving){
						//Cursor.drawArrow((Cursor.cursorPos.y+2)-GamePanel.mapX*32,((Cursor.cursorPos.x)+2)-GamePanel.mapY*32, currentUnit.moveRange);
					}
				}
			}
			else{
				System.out.println(Cursor.cursorPos.y);
			}
		}
		else if (keyboardKeyState(KeyEvent.VK_DOWN)&&!saving&&!loading){
			if(Cursor.cursorPos.y<southEdge){
				Cursor.cursorPos.y+=32;
				if(moving){
					//Cursor.drawArrow(Cursor.cursorPos.y+2, Cursor.cursorPos.x+2, 5);
				}
			}
			else if(Cursor.cursorPos.y==southEdge){

				if(GamePanel.mapX>(-20)){
					//System.out.println("exiting bottom edge");
					GamePanel.mapX-=1;
					GamePanel.levels.get(GamePanel.level).update();
					if(moving){
						//Cursor.drawArrow((Cursor.cursorPos.y+2)-GamePanel.mapX*32,((Cursor.cursorPos.x)+2)-GamePanel.mapY*32, currentUnit.moveRange);
					}
					//System.out.println("down");
				}
			}
		}
		else if (keyboardKeyState(KeyEvent.VK_RIGHT)&&!saving&&!loading){
			if(Cursor.cursorPos.x<eastEdge){
				Cursor.cursorPos.x+=32;
				if(moving){
					//Cursor.drawArrow(Cursor.cursorPos.y+2, Cursor.cursorPos.x+2, 5);
				}
			}
			else if(Cursor.cursorPos.x==eastEdge){
				if(GamePanel.mapY>(-40)){
					//System.out.println("exiting bottom edge");
					GamePanel.mapY-=1;
					GamePanel.levels.get(GamePanel.level).update();
					if(moving){
						//Cursor.drawArrow((Cursor.cursorPos.y+2)-GamePanel.mapX*32,((Cursor.cursorPos.x)+2)-GamePanel.mapY*32, currentUnit.moveRange);
					}
					//System.out.println("right");
				}
			}
		}
		else if (keyboardKeyState(KeyEvent.VK_LEFT)&&!saving&&!loading){
			if(Cursor.cursorPos.x>westEdge){
				Cursor.cursorPos.x-=32;
				if(moving){
					//Cursor.drawArrow(Cursor.cursorPos.y+2, Cursor.cursorPos.x+2, 5);
				}
			}
			else if(Cursor.cursorPos.x==westEdge){
				if(GamePanel.mapY<0){
					//System.out.println("exiting bottom edge");
					GamePanel.mapY+=1;
					GamePanel.levels.get(GamePanel.level).update();
					if(moving){
						//Cursor.drawArrow((Cursor.cursorPos.y+2)-GamePanel.mapX*32,((Cursor.cursorPos.x)+2)-GamePanel.mapY*32, currentUnit.moveRange);
					}
					//System.out.println("left");
				}
			}
		}
		else if ((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {//ctrl-s to save the level
			loading=false;
			saving=true;
		}
		else if ((e.getKeyCode() == KeyEvent.VK_U) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {//ctrl-u to create a new unit


			GamePanel.newUnCreatedUnit = new Unit();
			GamePanel.newUnCreatedUnit.setPosition((Cursor.cursorPos.x+2)/32, (Cursor.cursorPos.y+2)/32);

			//add a button for confirming that you are done creating the unit
			int menuXpos=1150;
			int menuYpos=880;
			GamePanel.menuHidden = false;
			GamePanel.menu = new DropDownMenu(new Point(menuXpos,menuYpos));
			lastValidMousePosition = new Point(menuXpos,menuYpos);

			//Unit temp = new Unit((Cursor.cursorPos.x+2)/32, (Cursor.cursorPos.y+2)/32, newUnitTeam, newUnitType, newUnitName, newUnitItems);
			Point newUnitWindowPos = new Point(500,400);
			//set the new unit's unit type/class
			int newUnitType = 0;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x, newUnitWindowPos.y-165, "Character Class of this Unit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).stringValue="Choose a unit class";
			//set the unit's team
			int newUnitTeam = 1;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+650, newUnitWindowPos.y+400, "This unit's Team"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=0;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=1;
			//set the unit's name
			String newUnitName = "Bandit Soldier";
			//set the unit's current level
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x, newUnitWindowPos.y-100, "Current Name of This Unit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).stringValue="Choose This Unit's Name";//start at level 1 by default
			//set the unit's AI
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+450, newUnitWindowPos.y-100, "AI Used By This Unit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).stringValue="Choose an AI Type";
			//set the unit's initial items
			ArrayList<Integer> newUnitItems = new ArrayList<Integer>();
			newUnitItems.add(0);
			//set the unit's initial stats
			int newUnitStr = 0;
			//set the unit's level scaling
			int chanceToGetStrength = 50;
			int chanceToGetArmour = 50;
			int chanceToGetMagic = 50;
			int chanceToGetSpeed = 50;
			int chanceToGetStamina = 80;
			int chanceToGetResistance = 50;
			//set the unit's current level
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+400, newUnitWindowPos.y+400, "Current Level of This Unit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=1;//start at level 1 by default
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=30;//maximum level is 30
			//set the unit's total power
			GamePanel.newUnCreatedUnit.powerLimit = 100;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x, newUnitWindowPos.y+400, "Total Non Health Stat Limit For This Unit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=GamePanel.newUnCreatedUnit.powerLimit;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=150;
			//set the unit's stat limits
			//let the player increase strength, armour, etc but the total of all limits should be less than the unit's total power
			GamePanel.newUnCreatedUnit.strengthLimit = 20;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x, newUnitWindowPos.y, "% Chance To Gain Strength"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=chanceToGetStrength;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=100;
			GamePanel.newUnCreatedUnit.magicLimit = 20;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x, newUnitWindowPos.y+65, "% Chance To Gain Magic"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=chanceToGetMagic;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=100;
			GamePanel.newUnCreatedUnit.armourLimit = 20;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x, newUnitWindowPos.y+130, "% Chance To Gain Armour"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=chanceToGetArmour;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=100;
			GamePanel.newUnCreatedUnit.speedLimit = 20;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x, newUnitWindowPos.y+195, "% Chance To Gain Speed"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=chanceToGetSpeed;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=100;
			GamePanel.newUnCreatedUnit.staminaLimit = 50;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x, newUnitWindowPos.y+260, "% Chance To Gain Stamina"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=chanceToGetStamina;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=100;
			GamePanel.newUnCreatedUnit.resistanceLimit = 20;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x, newUnitWindowPos.y+325, "% Chance To Gain Resistance"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=chanceToGetResistance;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=100;

			//add the modifiable values for the limits
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+300, newUnitWindowPos.y, " Strength Limit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=GamePanel.newUnCreatedUnit.strengthLimit;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.powerLimit-GamePanel.newUnCreatedUnit.magicLimit-GamePanel.newUnCreatedUnit.speedLimit-GamePanel.newUnCreatedUnit.armourLimit-GamePanel.newUnCreatedUnit.resistanceLimit;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+300, newUnitWindowPos.y+65, " Magic Limit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=GamePanel.newUnCreatedUnit.magicLimit;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.powerLimit-GamePanel.newUnCreatedUnit.strengthLimit-GamePanel.newUnCreatedUnit.speedLimit-GamePanel.newUnCreatedUnit.armourLimit-GamePanel.newUnCreatedUnit.resistanceLimit;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+300, newUnitWindowPos.y+130, " Speed Limit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=GamePanel.newUnCreatedUnit.speedLimit;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.powerLimit-GamePanel.newUnCreatedUnit.magicLimit-GamePanel.newUnCreatedUnit.strengthLimit-GamePanel.newUnCreatedUnit.armourLimit-GamePanel.newUnCreatedUnit.resistanceLimit;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+300, newUnitWindowPos.y+195, " Armour Limit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=GamePanel.newUnCreatedUnit.armourLimit;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.powerLimit-GamePanel.newUnCreatedUnit.magicLimit-GamePanel.newUnCreatedUnit.speedLimit-GamePanel.newUnCreatedUnit.strengthLimit-GamePanel.newUnCreatedUnit.resistanceLimit;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+300, newUnitWindowPos.y+260, " Stamina Limit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=GamePanel.newUnCreatedUnit.staminaLimit;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=100;;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+300, newUnitWindowPos.y+325, " Resistance Limit"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=GamePanel.newUnCreatedUnit.resistanceLimit;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.powerLimit-GamePanel.newUnCreatedUnit.magicLimit-GamePanel.newUnCreatedUnit.speedLimit-GamePanel.newUnCreatedUnit.armourLimit-GamePanel.newUnCreatedUnit.strengthLimit;

			//add starting stats for the unit
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+600, newUnitWindowPos.y, " Starting Strength"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=0;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.strengthLimit;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+600, newUnitWindowPos.y+65, " Starting Magic"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=0;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.magicLimit;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+600, newUnitWindowPos.y+130, " Starting Speed"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=0;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.speedLimit;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+600, newUnitWindowPos.y+195, " Starting Armour"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=0;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.armourLimit;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+600, newUnitWindowPos.y+260, " Starting Stamina"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=0;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.staminaLimit;
			GamePanel.modifiableValues.add(new ModifiableValue(newUnitWindowPos.x+600, newUnitWindowPos.y+325, " Starting Resistance"));
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).value=0;
			GamePanel.modifiableValues.get(GamePanel.modifiableValues.size()-1).valueUpperLimit=GamePanel.newUnCreatedUnit.resistanceLimit;


			//Unit temp = new Unit((Cursor.cursorPos.x+2)/32, (Cursor.cursorPos.y+2)/32, newUnitTeam, newUnitType, newUnitName, newUnitItems);
			//			temp.chanceToGetStrength=chanceToGetStrength;
			//			temp.chanceToGetArmour=chanceToGetArmour;
			//			temp.chanceToGetMagic=chanceToGetMagic;
			//			temp.chanceToGetSpeed=chanceToGetSpeed;
			//			temp.chanceToGetStamina=chanceToGetStamina;
			//			temp.chanceToGetResistance=chanceToGetResistance;
			//GamePanel.levels.get(GamePanel.level).Units.add(temp);
		}
		else if ((e.getKeyCode() == KeyEvent.VK_L) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {//ctrl-l to load the level
			saving=false;
			loading=true;
		}
		else if (keyboardKeyState(KeyEvent.VK_ESCAPE)&&(saving||loading)){
			saving = false;
			loading=false;
			text = "";
		}

		Cursor.cursor.updatePosition(Cursor.cursorPos.x, Cursor.cursorPos.y);
		if (keyboardKeyState(KeyEvent.VK_D)&&!saving&&!loading){
			Point temp = new Point((Cursor.cursorPos.x+2-GamePanel.mapY*32),(Cursor.cursorPos.y+2)-GamePanel.mapX*32);
			GamePanel.levels.get(GamePanel.level).tileMap[(Cursor.cursorPos.y+2)/32-GamePanel.mapX][((Cursor.cursorPos.x)+2)/32-GamePanel.mapY]=new Tile(temp,GamePanel.levels.get(GamePanel.level).tiles,GamePanel.levels.get(GamePanel.level).beaches,GamePanel.actionImg,currentTileID);
		}
	}
	public void addText(char c){

		if(Character.isLetter(c)||Character.isDigit(c)){
			text+=c;
		}
		else{
			System.out.println("Tried to add symbols to file name!");
		}


	}
	@Override
	public void keyReleased(KeyEvent e) {
		keyboardState[e.getKeyCode()] = false;
		//updateAll();
	}
	public static void loadLevel(String name){
		String map_path = System.getenv("APPDATA")+File.separator+"BattleShores_Saves"+File.separator+name+File.separator+name+"_map.txt";
		String units_path = System.getenv("APPDATA")+File.separator+"BattleShores_Saves"+File.separator+name+File.separator+name+"_units.txt";

		Path mapPath = Paths.get(map_path);
		Path unitsPath = Paths.get(units_path);

		InputStream map = null;
		InputStream unitsData = null;
		
		//if the file paths don't exist
		if(Files.notExists(mapPath)){
			System.out.println("files didn't exist, creating files");
			map = Controller.class.getResourceAsStream(("/"+name+"/"+name+"_map.txt")); 
			unitsData = Controller.class.getResourceAsStream(("/"+name+"/"+name+"_units.txt")); 

		}
		else{
			//    InputStream in = new FileInputStream(theFile);  
			try {
				map = new FileInputStream((System.getenv("APPDATA")+"/BattleShores_Saves/"+name+"/"+name+"_map.txt"));
				//unitsData = new FileInputStream((System.getenv("APPDATA")+"/BattleShores_Saves/"+name+"/"+name+"_units.txt"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			//unitsData = Controller.class.getResourceAsStream(("/"+name+"/"+name+"_units.txt")); 
		}
		int currentChar;
		ArrayList<String> lines = new ArrayList<String>();
		String line1 = "";

		BufferedReader reader = null;
		try{
			//load the level's tile arrangement
			reader = new BufferedReader(new InputStreamReader(map, "UTF-8"));
			//reader = new BufferedReader(new FileReader(file1));
			for(int i = 0; i<GamePanel.levels.get(GamePanel.level).levelHeight;i++){
				String line = reader.readLine();
				for(int j = 0;j<GamePanel.levels.get(GamePanel.level).levelWidth;j++){
					int start = 0;
					int end = -1;
					String temp = "";
					//System.out.println(line);
					char[] lineChars = line.toCharArray();
					//loop through every character in the line
					for(int k = 0; k<lineChars.length;k++){
						if(lineChars[k]==')'){
							end++;
						}
						if(end==j){
							//find the start of this tile's data
							for(int p = k; p>0;p--){
								if(lineChars[p]==','){
									start = p+1;
									break;
								}
							}
							//index of this tile
							temp = line.substring(start, k);
							//System.out.println("temp= "+temp+" start= "+start+" k= "+k);
							GamePanel.levels.get(GamePanel.level).tileMap[i][j]=new Tile(new Point(j*32,i*32),GamePanel.levels.get(0).tiles,GamePanel.levels.get(GamePanel.level).beaches,GamePanel.actionImg,Integer.valueOf(temp));
							break;
						}
					}

				}

			}
			try{reader.close();}catch(Exception ex){}

			loadUnits(name);
		}
		catch(Exception ex){
			System.out.println("Failed to write to (or create) game map file: "+ name);
			ex.printStackTrace();
		}
		finally{
			try{reader.close();}catch(Exception ex){}
		}
	}
	public <T>void saveVariable(T var, String variableName, String fileName){

	}
	public static void saveUnits(String name){
		Writer writer = null;
		//loop through every unit to save it's data


		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getenv("APPDATA")+File.separator+"BattleShores_Saves"+File.separator+name+File.separator+name+"_units.txt"), "utf-8"));
			for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.size();i++){
				Unit unit = GamePanel.levels.get(GamePanel.level).Units.get(i);
				writer.write("{\n");
				writer.write("xpos="+unit.xpos+"\n");
				writer.write("ypos="+unit.ypos+"\n");
				writer.write("team="+unit.team+"\n");
				writer.write("attackRange="+unit.attackRange+"\n");
				writer.write("unitType="+unit.unitType+"\n");
				writer.write("mobility="+unit.mobility+"\n");
				writer.write("baseUnitHP="+unit.baseUnitHP+"\n");
				writer.write("currentHealth="+(int)unit.currentHealth+"\n");
				writer.write("maxHealth="+(int)unit.maxHealth+"\n");
				writer.write("incomingXP="+unit.incomingXP+"\n");
				writer.write("incomingHealth="+(int)unit.incomingHealth+"\n");
				writer.write("unitName="+unit.unitName+"\n");
				writer.write("currentXP="+unit.currentXP+"\n");
				writer.write("Stamina="+unit.Stamina+"\n");
				writer.write("Strength="+unit.Strength+"\n");
				writer.write("Magic="+unit.Magic+"\n");
				writer.write("Speed="+unit.Speed+"\n");
				writer.write("moveRange="+unit.moveRange+"\n");
				writer.write("armour="+unit.armour+"\n");
				writer.write("resistance="+unit.resistance+"\n");
				writer.write("currentLevel="+unit.currentLevel+"\n");
				writer.write("powerLimit="+unit.powerLimit+"\n");
				writer.write("levelLimit="+unit.levelLimit+"\n");
				writer.write("chanceToGetStrength="+unit.chanceToGetStrength+"\n");
				writer.write("chanceToGetArmour="+unit.chanceToGetArmour+"\n");
				writer.write("chanceToGetMagic="+unit.chanceToGetMagic+"\n");
				writer.write("chanceToGetSpeed="+unit.chanceToGetSpeed+"\n");
				writer.write("chanceToGetStamina="+unit.chanceToGetStamina+"\n");
				writer.write("chanceToGetResistance="+unit.chanceToGetResistance+"\n");
				writer.write("strengthLimit="+unit.strengthLimit+"\n");
				writer.write("armourLimit="+unit.armourLimit+"\n");
				writer.write("magicLimit="+unit.magicLimit+"\n");
				writer.write("speedLimit="+unit.speedLimit+"\n");
				writer.write("staminaLimit="+unit.staminaLimit+"\n");
				writer.write("resistanceLimit="+unit.resistanceLimit+"\n");
				writer.write("startingStrength="+unit.startingStrength+"\n");
				writer.write("startingArmour="+unit.startingArmour+"\n");
				writer.write("startingMagic="+unit.startingMagic+"\n");
				writer.write("startingSpeed="+unit.startingSpeed+"\n");
				writer.write("startingStamina="+unit.startingStamina+"\n");
				writer.write("startingResistance="+unit.startingResistance+"\n");
				writer.write("itemIDs=[");
				for(int j = 0; j<unit.items.length;j++){
					if(unit.items[j]!=null){
						writer.write(unit.items[j].itemID+"");
					}
					else{
						writer.write("null");
					}
					if(j!=unit.items.length-1){
						writer.write(",");
					}
				}
				writer.write("]\n");
				writer.write("itemDurabilities=[");
				for(int j = 0; j<unit.items.length;j++){
					if(unit.items[j]!=null){
						writer.write(unit.items[j].currentDurability+"");
					}
					else{
						writer.write("null");
					}
					if(j!=unit.items.length-1){
						writer.write(",");
					}
				}
				writer.write("]\n");
				writer.write("axeLevel="+unit.axeLevel+"\n");
				writer.write("swordLevel="+unit.swordLevel+"\n");
				writer.write("spearLevel="+unit.spearLevel+"\n");
				writer.write("lifeMagicLevel="+unit.lifeMagicLevel+"\n");
				writer.write("bloodMagicLevel="+unit.bloodMagicLevel+"\n");
				writer.write("assistMagicLevel="+unit.assistMagicLevel+"\n");
				writer.write("bowLevel="+unit.bowLevel+"\n");
				writer.write("AI_ID="+unit.AI_ID+"\n");
				writer.write("greyedOut="+unit.greyedOut+"\n");
				writer.write("isBoss="+unit.isBoss+"\n");
				writer.write("}\n");
			}
		} catch (IOException ex) {
			// report
			ex.printStackTrace();  
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}


	}
	public static void loadUnits(String name){
		
		String units_path = System.getenv("APPDATA")+File.separator+"BattleShores_Saves"+File.separator+name+File.separator+name+"_units.txt";

		Path unitsPath = Paths.get(units_path);

		InputStream unitsData = null;
		
		//if the file paths don't exist
		if(Files.notExists(unitsPath)){
			System.out.println("files didn't exist, creating files");
			unitsData = Controller.class.getResourceAsStream(("/"+name+"/"+name+"_units.txt")); 
		}
		else{
			//    InputStream in = new FileInputStream(theFile);  
			try {
				unitsData = new FileInputStream((System.getenv("APPDATA")+"/BattleShores_Saves/"+name+"/"+name+"_units.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		}
		
		System.out.println("loading units");
		File file = new File(System.getenv("APPDATA")+File.separator+"BattleShores_Saves"+File.separator+name+File.separator+name+"_units.txt");
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(unitsData, "UTF-8"))){
			Unit newUnit=new Unit();
			String line;
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				char[] lineChars = line.toCharArray();
				char currentChar = 'a';
				int nonStringLength = 0;
				while(currentChar!='='&&nonStringLength<line.length()-1){
					currentChar=lineChars[nonStringLength];
					nonStringLength++;
				}
				if(line.contains("xpos")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.xpos=Integer.valueOf(value);
				}
				else if(line.contains("ypos")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.ypos=Integer.valueOf(value);
				}
				else if(line.contains("team")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.team=Integer.valueOf(value);
				}
				else if(line.contains("attackRange")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.attackRange=Integer.valueOf(value);
				}
				else if(line.contains("unitType")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.unitType=Integer.valueOf(value);
				}
				else if(line.contains("mobility")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.mobility=Integer.valueOf(value);
				}
				else if(line.contains("baseUnitHP")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.baseUnitHP=Integer.valueOf(value);
				}
				else if(line.contains("currentHealth")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.currentHealth=Integer.valueOf(value);
				}
				else if(line.contains("maxHealth")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.maxHealth=Integer.valueOf(value);
				}
				else if(line.contains("incomingXP")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.incomingXP=Integer.valueOf(value);
				}
				else if(line.contains("incomingHealth")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.incomingHealth=Integer.valueOf(value);
				}
				else if(line.contains("unitName")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.unitName=value;
				}
				else if(line.contains("currentXP")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.currentXP=Integer.valueOf(value);
				}
				else if(line.contains("Stamina")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.Stamina=Integer.valueOf(value);
				}
				else if(line.contains("Strength")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.Strength=Integer.valueOf(value);
				}
				else if(line.contains("Magic")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.Magic=Integer.valueOf(value);
				}
				else if(line.contains("Speed")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.Speed=Integer.valueOf(value);
				}
				else if(line.contains("moveRange")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.moveRange=Integer.valueOf(value);
				}
				else if(line.contains("armour")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.armour=Integer.valueOf(value);
				}
				else if(line.contains("resistance")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.resistance=Integer.valueOf(value);
				}
				else if(line.contains("currentLevel")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.currentLevel=Integer.valueOf(value);
				}
				else if(line.contains("powerLimit")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.powerLimit=Integer.valueOf(value);
				}
				else if(line.contains("levelLimit")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.levelLimit=Integer.valueOf(value);
				}
				else if(line.contains("chanceToGetStrength")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.chanceToGetStrength=Integer.valueOf(value);
				}
				else if(line.contains("chanceToGetArmour")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.chanceToGetArmour=Integer.valueOf(value);
				}
				else if(line.contains("chanceToGetMagic")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.chanceToGetMagic=Integer.valueOf(value);
				}
				else if(line.contains("chanceToGetSpeed")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.chanceToGetSpeed=Integer.valueOf(value);
				}
				else if(line.contains("chanceToGetStamina")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.chanceToGetStamina=Integer.valueOf(value);
				}
				else if(line.contains("chanceToGetResistance")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.chanceToGetResistance=Integer.valueOf(value);
				}
				else if(line.contains("strengthLimit")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.strengthLimit=Integer.valueOf(value);
				}
				else if(line.contains("armourLimit")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.armourLimit=Integer.valueOf(value);
				}
				else if(line.contains("magicLimit")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.magicLimit=Integer.valueOf(value);
				}
				else if(line.contains("speedLimit")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.speedLimit=Integer.valueOf(value);
				}
				else if(line.contains("staminaLimit")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.staminaLimit=Integer.valueOf(value);
				}
				else if(line.contains("resistanceLimit")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.resistanceLimit=Integer.valueOf(value);
				}
				else if(line.contains("startingStrength")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.startingStrength=Integer.valueOf(value);
				}
				else if(line.contains("startingArmour")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.startingArmour=Integer.valueOf(value);
				}
				else if(line.contains("startingMagic")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.startingMagic=Integer.valueOf(value);
				}
				else if(line.contains("startingSpeed")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.startingSpeed=Integer.valueOf(value);
				}
				else if(line.contains("startingStamina")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.startingStamina=Integer.valueOf(value);
				}
				else if(line.contains("startingResistance")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.startingResistance=Integer.valueOf(value);
				}
				else if(line.contains("itemIDs")){
					String value = line.substring(nonStringLength, line.length());
					//loop through every character in the string's value
					for(int j = 0; j<value.length();j++){
						if(value.charAt(j)=='['||value.charAt(j)==','){
							int startCharIndex=0;
							int curCharIndex = 1;
							int itemIndex =0;
							boolean foundAll = false;
							while(curCharIndex<value.length()&&value.charAt(curCharIndex)!=']'){
								String curValue="";
								while(value.charAt(curCharIndex)!=','&&value.charAt(curCharIndex)!=']'){
									curValue=curValue+value.charAt(curCharIndex);
									curCharIndex++;
								}
								if(curValue!=null&&newUnit.items[itemIndex]!=null){
									//newUnit.items[itemIndex].itemID=Integer.valueOf(curValue);
									newUnit.items[itemIndex] = new Item(Integer.valueOf(curValue));
								}
								itemIndex++;
								curCharIndex++;
								startCharIndex=curCharIndex;
							}
						}
					}
				}
				else if(line.contains("itemDurabilities")){
					String value = line.substring(nonStringLength, line.length());
					//loop through every character in the string's value
					for(int j = 0; j<value.length();j++){
						if(value.charAt(j)=='['||value.charAt(j)==','){
							int startCharIndex=0;
							int curCharIndex = 1;
							int itemIndex =0;
							boolean foundAll = false;
							while(curCharIndex<value.length()&&value.charAt(curCharIndex)!=']'){
								String curValue="";
								while(value.charAt(curCharIndex)!=','&&value.charAt(curCharIndex)!=']'){
									curValue=curValue+value.charAt(curCharIndex);
									curCharIndex++;
								}
								if(curValue!=null&&newUnit.items[itemIndex]!=null){
									newUnit.items[itemIndex].currentDurability=Integer.valueOf(curValue);
								}
								itemIndex++;
								curCharIndex++;
								startCharIndex=curCharIndex;
							}
						}
					}
				}
				else if(line.contains("swordLevel")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.swordLevel=Integer.valueOf(value);
				}
				else if(line.contains("spearLevel")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.spearLevel=Integer.valueOf(value);
				}
				else if(line.contains("lifeMagicLevel")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.lifeMagicLevel=Integer.valueOf(value);
				}
				else if(line.contains("bloodMagicLevel")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.bloodMagicLevel=Integer.valueOf(value);
				}
				else if(line.contains("assistMagicLevel")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.assistMagicLevel=Integer.valueOf(value);
				}
				else if(line.contains("bowLevel")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.bowLevel=Integer.valueOf(value);
				}
				else if(line.contains("AI_ID")){
					String value = line.substring(nonStringLength, line.length());
					newUnit.AI_ID=Integer.valueOf(value);
				}
				else if(line.contains("greyedOut")){
					String value = line.substring(nonStringLength, line.length());
					if(value=="true"){
						newUnit.greyedOut=true;
					}
					else if(value=="false"){
						newUnit.greyedOut=false;
					}
				}
				else if(line.contains("isBoss")){
					String value = line.substring(nonStringLength, line.length());
					if(value=="true"){
						newUnit.isBoss=true;
					}
					else if(value=="false"){
						newUnit.isBoss=false;
					}
				}
				//assign the proper portrait and speeches to this unit
				if(newUnit.unitName.equals("Bandit Soldier")){
					ArrayList<String> banditMessages = new ArrayList<String>();
					banditMessages.add("This.. is.. unfortunate....");
					banditMessages.add("You cannot win...");
					banditMessages.add("You're too late..");
					banditMessages.add("There is nothing you can do...");
					banditMessages.add("I'm... sorry....");
					newUnit.portrait=GamePanel.portraits[0][0];
					newUnit.deathSpeech.addMessage(banditMessages.get(GamePanel.randomNumber(0, banditMessages.size()-1)));
				}
				else if(newUnit.unitName.equals("Tough Guy")){
					newUnit.isBoss=true;
					newUnit.deathSpeech.addMessage("Heh..");
					newUnit.portrait=GamePanel.portraits[0][0];
				}
				else if(newUnit.unitName.equals("Big Dude")){
					newUnit.isBoss=true;
					newUnit.deathSpeech.addMessage("I was weak..");
					newUnit.portrait=GamePanel.portraits[0][0];
				}
				else if(newUnit.unitName.equals("Jeb")){
					newUnit.deathSpeech.addMessage("I failed... I'm..sorry...");
					newUnit.portrait=GamePanel.portraits[1][0];
				}
				else if(newUnit.unitName.equals("William")){
					newUnit.deathSpeech.addMessage("I can't see, it's so dark...");
					newUnit.portrait=GamePanel.portraits[2][0];
				}
				else if(newUnit.unitName.equals("Tythus")){
					newUnit.deathSpeech.addMessage("Oof...");
					newUnit.portrait=GamePanel.portraits[3][0];
				}
				newUnit.init();
			}
			GamePanel.levels.get(GamePanel.level).Units.add(newUnit);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("loaded units");
		for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.size();i++){
			System.out.println("unit xpos: "+GamePanel.levels.get(GamePanel.level).Units.get(i).xpos);
		}
	}
	public static void saveLevel(String name){
		String folderName = System.getenv("APPDATA")+File.separator+"BattleShores_Saves";
		Path saveDirectory = Paths.get(folderName);
		//create an empty folder to store save files in if none exist
		if(Files.notExists(saveDirectory)){
			String path = System.getenv("APPDATA")+File.separator+"BattleShores_Saves";
			//(use relative path for Unix systems)
			File dir = new File(path);
			//(works for both Windows and Linux)
			dir.mkdirs();
		}



		String path = System.getenv("APPDATA")+File.separator+"BattleShores_Saves"+File.separator+name;
		//(use relative path for Unix systems)
		File dir = new File(path);
		//(works for both Windows and Linux)
		dir.mkdirs(); 

		//create a text file inside the folder to store the level's map
		File lvl = new File((dir.getAbsolutePath())+"//"+name+"_map.txt");
		//create a text file inside the folder to store the level's units
		File unts = new File((dir.getAbsolutePath())+"//"+name+"_units.txt");

		Writer writer =null;
		try{
			//save the map data for the level
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lvl), "utf-8"));
			for(int i = 0; i<GamePanel.levels.get(GamePanel.level).levelHeight;i++){
				for(int j = 0;j<GamePanel.levels.get(GamePanel.level).levelWidth;j++){
					writer.write("("+GamePanel.levels.get(GamePanel.level).tileMap[i][j].xpos+","+GamePanel.levels.get(GamePanel.level).tileMap[i][j].ypos+","+GamePanel.levels.get(GamePanel.level).tileMap[i][j].tileID+")");
				}
				writer.write("\n");
			}
			try{writer.close();}catch(Exception ex){}
			saveUnits(name);
			//			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(unts), "utf-8"));
			//			ArrayList<Unit> temp = GamePanel.levels.get(GamePanel.level).Units;
			//			for(int i = 0; i<temp.size();i++){
			//				//store the location
			//				//Units.add(new Unit(17, 12, 1, 0,"Bandit Soldier",banditSoldierItems));
			//				writer.write("("+temp.get(i).xpos+","+temp.get(i).ypos+","+temp.get(i).team+","+temp.get(i).unitType+","+temp.get(i).unitName);
			//				//store the unit's items
			//				for(int j = 0; j<temp.get(i).items.length;j++){
			//					if(temp.get(i).items[j]!=null)
			//						writer.write(","+temp.get(i).items[j].itemID);
			//					else
			//						writer.write(",-1");
			//				}
			//				writer.write(")\n");
			//			}
			//confirm to the player that the level was saved
			System.out.println("Saved level as: "+name+" to filepath: "+lvl.getAbsolutePath());
		}
		catch(Exception ex){
			System.out.println("Failed to write to (or create) game map file: "+ name);
			ex.printStackTrace();
		}
		finally{
			try{writer.close();}catch(Exception ex){}
		}

	}
	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("key typed");
		if((saving||loading)&&!e.isActionKey()){
			addText(e.getKeyChar());
		}
		if (keyboardKeyState(KeyEvent.VK_BACK_SPACE)&&(saving||loading)){
			if(text.length()>=1)
				text = text.substring(0, text.length()-1);
		}
		//change tiles
		if (keyboardKeyState(KeyEvent.VK_ENTER)&&(saving||loading)){
			if(saving)//saveing the level
				saveLevel(text);
			else//loading the level
				loadLevel(text);
			text = "";
			saving=false;
			loading=false;
		}	
		if (keyboardKeyState(KeyEvent.VK_W)&&!saving&&!loading){
			if(currentTileID<17)
				currentTileID++;
			else
				currentTileID=0;
			Point temp = new Point(-20,-20);
			currentTileName=new Tile(temp,GamePanel.levels.get(GamePanel.level).tiles,GamePanel.levels.get(GamePanel.level).beaches,GamePanel.actionImg, currentTileID).name;
		}
		if (keyboardKeyState(KeyEvent.VK_U)&&!saving&&!loading){//place a new unit
			//open up a menu to allow the player to set the unit's stats
			//Units.add(new Unit(15, 12, 1, 0,"Bandit",banditSoldierItems));

		}
		if (keyboardKeyState(KeyEvent.VK_C)&&!saving&&!loading){
			if(moving){
				//currentUnit.xpos=Cursor.arrows.get(Cursor.arrows.size()-1).y;
				//currentUnit.ypos=Cursor.arrows.get(Cursor.arrows.size()-1).x;
				//moving = false;
			}
			else{
				for(int i = 0;i<GamePanel.levels.get(GamePanel.level).Units.size();i++){
					if(GamePanel.levels.get(GamePanel.level).Units.get(i).xpos*32==Cursor.cursorPos.x+2&&GamePanel.levels.get(GamePanel.level).Units.get(i).ypos*32==Cursor.cursorPos.y+2){
						//moving = true;
						//currentUnit = GamePanel.levels.get(GamePanel.level).Units.get(i);
						//GamePanel.levels.get(GamePanel.level).Units.get(i).showMoveGrid();
					}
				}
			}

		}
		if (keyboardKeyState(KeyEvent.VK_8)){
			System.out.println("ending level");
			GamePanel.endLevel();
		}
		if (keyboardKeyState(KeyEvent.VK_7)){

		}
		if (keyboardKeyState(KeyEvent.VK_6)){
			Controller.loadUnits("units");
		}
		if (keyboardKeyState(KeyEvent.VK_S)&&!saving&&!loading){
			if(currentTileID>0)
				currentTileID--;
			else
				currentTileID=17;
			Point temp = new Point(-20,-20);
			currentTileName=new Tile(temp,GamePanel.levels.get(GamePanel.level).tiles,GamePanel.levels.get(GamePanel.level).beaches,GamePanel.actionImg, currentTileID).name;
		}
		if (keyboardKeyState(KeyEvent.VK_E)&&!saving&&!loading){
			currentTileID=GamePanel.levels.get(GamePanel.level).tileMap[(((Cursor.cursorPos.y+2)/32+GamePanel.mapY))][(Cursor.cursorPos.x+2)/32].tileID;
			currentTileName=GamePanel.levels.get(GamePanel.level).tileMap[((Cursor.cursorPos.y+2)/32+GamePanel.mapY)][((Cursor.cursorPos.x+2)/32)].name;
		}

	}
	@Override
	public void mouseClicked(MouseEvent e) {

		boolean done = false;
		//check if the mouse is on any lock interrupt buttons
		if(GamePanel.levels.get(GamePanel.level).chests.size()>0){

			if(GamePanel.currentChest!=-1){
				//loop through every mechanism in the current chest
				for(int j = 0; j<GamePanel.levels.get(GamePanel.level).chests.get(GamePanel.currentChest).mechanisms.size();j++){

					//if the mouse was clicked on this mechanism's button
					if(GamePanel.levels.get(GamePanel.level).chests.get(GamePanel.currentChest).mechanisms.get(j).mouseOnInterruptButton()){

						GamePanel.levels.get(GamePanel.level).chests.get(GamePanel.currentChest).mechanisms.get(j).interrupt();
						System.out.println("interrupted!");
					}
				}
			}


		}

		if(GamePanel.currentBattle!=null){
			System.out.println("step 1");
			if(mouseClickState==0&&GamePanel.currentBattle.loser!=null){
				System.out.println("step 2");
				if(GamePanel.currentBattle.loser.deathSpeech!=null){
					System.out.println("step 3");
					if(GamePanel.currentBattle.loser.deathSpeech.speechState>=0){
						System.out.println("step 4");
						if(GamePanel.currentBattle.loser.deathSpeech.speechState>=GamePanel.currentBattle.loser.deathSpeech.messages.size()-1){
							System.out.println("ending the battle");
							boolean wasBoss = GamePanel.currentBattle.loser.isBoss;

							GamePanel.currentBattle.winner.gainXP(GamePanel.currentBattle.calculateXP());

						}
						if(GamePanel.battle)
							GamePanel.currentBattle.loser.deathSpeech.update();


					}
				}
			}
		}
		if(!GamePanel.chestActive&&GamePanel.newUnCreatedUnit==null&&!done&&mouseClickState == 0&&!GamePanel.titleScreen&&GamePanel.currentTurn==0&&!GamePanel.battle&&GamePanel.currentBattle==null){//click on a unit
			currentUnit=-1;
			ArrayList<Unit> units = GamePanel.levels.get(GamePanel.level).Units;
			System.out.println("entered click state 0");
			if(moving==false){
				System.out.println("moving is false");
				//check if the mouse is on any units
				for(int i = 0; i<units.size();i++){
					if(e.getX()>=(units.get(i).xpos*32)&&e.getY()<=(units.get(i).ypos*32)+32&&e.getX()<(units.get(i).xpos*32)+32&&e.getY()>=(units.get(i).ypos*32)){		
						if(GamePanel.levels.get(GamePanel.level).Units.get(i).team==GamePanel.currentTurn&&GamePanel.levels.get(GamePanel.level).Units.get(i).greyedOut==false){
							mouseClickState++;
							moving = true;
							currentUnit = i;
							//GamePanel.levels.get(GamePanel.level).Units.get(i).showMoveGrid();
							ArrayList<Tile> temp = new ArrayList<Tile>();
							GamePanel.levels.get(GamePanel.level).Units.get(i).findAllAttackablePositions(temp, GamePanel.levels.get(GamePanel.level).tileMap[GamePanel.levels.get(GamePanel.level).Units.get(i).ypos][GamePanel.levels.get(GamePanel.level).Units.get(i).xpos]);
							GamePanel.levels.get(GamePanel.level).Units.get(i).findAllPaths(temp, GamePanel.levels.get(GamePanel.level).tileMap[GamePanel.levels.get(GamePanel.level).Units.get(i).ypos][GamePanel.levels.get(GamePanel.level).Units.get(i).xpos]);

							//System.out.println("clicked on unit!");
							break;
						}
						else if(GamePanel.levels.get(GamePanel.level).Units.get(i).team!=GamePanel.currentTurn){
							ArrayList<Tile> temp = new ArrayList<Tile>();
							currentUnit = i;
							mouseClickState++;
							GamePanel.levels.get(GamePanel.level).Units.get(i).findAllAttackablePositions(temp, GamePanel.levels.get(GamePanel.level).tileMap[GamePanel.levels.get(GamePanel.level).Units.get(i).ypos][GamePanel.levels.get(GamePanel.level).Units.get(i).xpos]);
							GamePanel.levels.get(GamePanel.level).Units.get(i).findAllPaths(temp, GamePanel.levels.get(GamePanel.level).tileMap[GamePanel.levels.get(GamePanel.level).Units.get(i).ypos][GamePanel.levels.get(GamePanel.level).Units.get(i).xpos]);

						}
					}
				}
				//if the mouse is not on any units
				if(currentUnit ==-1&&GamePanel.menuHidden){
					int menuXpos=MouseInfo.getPointerInfo().getLocation().x;
					int menuYpos=MouseInfo.getPointerInfo().getLocation().y;
					GamePanel.menuHidden = false;
					GamePanel.menu = new DropDownMenu(new Point(menuXpos,menuYpos));
					lastValidMousePosition = new Point(menuXpos,menuYpos);
					System.out.println("clicked when no unit was selected");
					mouseClickState = 6;
				}
			}
		}
		else if(mouseClickState == 1){//click where you want the current unit to move
			mouseClickState++;
			if(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).team==GamePanel.currentTurn){//if it is the selected unit's turn
				moving = false;
				GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).moveToTile();
				for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.size();i++){
					GamePanel.levels.get(GamePanel.level).tileMap[GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.get(i).y][GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.get(i).x].hasArrow=false;
				}
				GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).movePath.clear();
				int menuXpos=(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).xpos*32)+32;
				int menuYpos=(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).ypos*32)+32;
				GamePanel.menuHidden = false;
				GamePanel.menu = new DropDownMenu(new Point(menuXpos,menuYpos));
				Robot robot = null;
				try {
					robot = new Robot();
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
				robot.mouseMove(menuXpos,menuYpos);
				lastValidMousePosition = new Point(menuXpos,menuYpos);
			}
			else{
				mouseClickState=0;
				GamePanel.removeMoveGrids();
			}
		}
		else if(mouseClickState == 2){//click on the button the mouse is currently over (wait, items, attack, cancel)
			if(GamePanel.menuHidden==false){
				//System.out.println("checking menu");
				//loop through each button in the menu
				for(int i = 0; i<GamePanel.menu.buttons.size();i++){
					//System.out.println("checking button number: "+i);
					//check if the mouse position is inside any of the buttons
					if(MouseInfo.getPointerInfo().getLocation().x>=GamePanel.menu.buttons.get(i).xpos&&MouseInfo.getPointerInfo().getLocation().x<=(GamePanel.menu.buttons.get(i).xpos+180)&&MouseInfo.getPointerInfo().getLocation().y>=GamePanel.menu.buttons.get(i).ypos&&MouseInfo.getPointerInfo().getLocation().y<(GamePanel.menu.buttons.get(i).ypos+30)){
						GamePanel.menu.currentButton = i;
						//System.out.println("changed currentbutton to "+i);
					}
				}


			}
			//check if the mouse is on any lock interrupt buttons
			if(GamePanel.levels.get(GamePanel.level).chests.size()>0){
				System.out.println("chest size greater than 0");
				//loop through every chest
				for(int i = 0; i<GamePanel.levels.get(GamePanel.level).chests.size();i++){
					//loop through every mechanism in the chest
					for(int j = 0; j<GamePanel.levels.get(GamePanel.level).chests.get(i).mechanisms.size();j++){
						//if the mouse was clicked on this mechanism's button
						if(GamePanel.levels.get(GamePanel.level).chests.get(i).mechanisms.get(j).mouseOnInterruptButton()){
							GamePanel.levels.get(GamePanel.level).chests.get(i).mechanisms.get(j).interrupt();
						}
					}
				}
			}
			if(GamePanel.menu.currentButton!=-1)
				GamePanel.menu.buttons.get(GamePanel.menu.currentButton).pressButton();
			//			GamePanel.menu.xpos=(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).xpos*32)+32;
			//			GamePanel.menu.ypos=(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).ypos*32)+32;
			if(!GamePanel.itemMenuOpen){
				//mouseClickState=3;
				GamePanel.menu.currentButton=-1;
				GamePanel.menuHidden = true;
			}
			else{
				mouseClickState=7;
			}
			GamePanel.removeMoveGrids();
		}
		if(mouseClickState == 3){
			mouseClickState = 0;
		}
		if(mouseClickState == 4){//click on the unit you wish to attack
			//loop through all units
			for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.size();i++){
				//if the mouse is on any units
				if(e.getX()>=(GamePanel.levels.get(GamePanel.level).Units.get(i).xpos*32)&&e.getY()<=(GamePanel.levels.get(GamePanel.level).Units.get(i).ypos*32)+32&&e.getX()<(GamePanel.levels.get(GamePanel.level).Units.get(i).xpos*32)+32&&e.getY()>=(GamePanel.levels.get(GamePanel.level).Units.get(i).ypos*32)){
					//if the unit the mouse is on is an attackable enemy unit
					for(int f = 0; f<GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).getAttackableUnits().size();f++ ){
						if(i==GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).getAttackableUnits().get(f)){
							GamePanel.currentBattle = new Battle(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit),GamePanel.levels.get(GamePanel.level).Units.get(i));

							System.out.println("started a battle");
							mouseClickState=5;
							break;
						}
					}
				}
			}
		}
		else if(mouseClickState ==5){//a unit has been selected to attack
			GamePanel.removeMoveGrids();
			//select an item to attack with
			if(GamePanel.menu.currentButton!=-1){
				GamePanel.menu.buttons.get(GamePanel.menu.currentButton).pressButton();
				GamePanel.battle = true;
				GamePanel.menuHidden=true;
				GamePanel.menu=null;
				mouseClickState=0;
			}
			else{
				int menuXpos=(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).xpos*32)+32;
				int menuYpos=(GamePanel.levels.get(GamePanel.level).Units.get(currentUnit).ypos*32)+32;
				GamePanel.menu = new DropDownMenu(new Point(menuXpos,menuYpos));
				System.out.println("created new dropdown menu");
				GamePanel.menuHidden = false;
				GamePanel.menu.currentButton=-1;
				lastValidMousePosition = new Point(menuXpos,menuYpos);

			}


		}
		else if(mouseClickState == 6||GamePanel.titleScreen){//mouse is not on any units, menu is open

			if(GamePanel.menu.currentButton!=-1){
				GamePanel.menu.buttons.get(GamePanel.menu.currentButton).pressButton();
				GamePanel.menu.currentButton=-1;
				GamePanel.menuHidden = true;
			}
		}
		else if(mouseClickState==7){//select an item from the item menu
			if(GamePanel.menu.currentButton!=-1){
				System.out.println("pressing button");
				GamePanel.menu.buttons.get(GamePanel.menu.currentButton).pressButton();
			}
		}

		System.out.println("mouse click state = "+mouseClickState);

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseDragged(MouseEvent e) {

	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(GamePanel.newUnCreatedUnit!=null&&GamePanel.modifiableValues!=null){//if the player is in the process of making a new unit
			if(GamePanel.menu.currentButton!=-1){
				GamePanel.menu.buttons.get(GamePanel.menu.currentButton).pressButton();
				GamePanel.menuHidden=true;
				GamePanel.menu=null;
			}
			System.out.println("new unit window is open");
			//loop through every modifiable value
			//
			if(GamePanel.newUnCreatedUnit!=null&&GamePanel.modifiableValues!=null){

				for(int i = 0; i<GamePanel.modifiableValues.size();i++){
					//if the player pushes one of the modifiableValue's buttons
					if(GamePanel.modifiableValues.get(i).mouseOnBottomButton()){//if the mouse is over the bottom button on one of the modifiable values
						if(GamePanel.modifiableValues.get(i).value>0&&i>=12&&i<=17){
							//strength limit
							if(i==12){
								if(GamePanel.modifiableValues.get(12).valueUpperLimit>0){								
									GamePanel.modifiableValues.get(12).valueUpperLimit--;
									GamePanel.newUnCreatedUnit.strengthLimit=GamePanel.modifiableValues.get(12).value;

								}
							}
							//magic limit
							if(i==13){
								if(GamePanel.modifiableValues.get(13).valueUpperLimit>0){								GamePanel.modifiableValues.get(13).valueUpperLimit--;
								GamePanel.newUnCreatedUnit.magicLimit=GamePanel.modifiableValues.get(13).value;
								}
							}
							//speed limit
							if(i==14){
								if(GamePanel.modifiableValues.get(14).valueUpperLimit>0){								GamePanel.modifiableValues.get(14).valueUpperLimit--;
								GamePanel.newUnCreatedUnit.speedLimit=GamePanel.modifiableValues.get(14).value;
								}
							}
							//armour limit
							if(i==15){
								if(GamePanel.modifiableValues.get(15).valueUpperLimit>0){								GamePanel.modifiableValues.get(15).valueUpperLimit--;
								GamePanel.newUnCreatedUnit.armourLimit=GamePanel.modifiableValues.get(15).value;
								}
							}
							//stamina limit
							if(i==16){
								if(GamePanel.modifiableValues.get(16).valueUpperLimit>0){
									GamePanel.modifiableValues.get(16).valueUpperLimit--;
									GamePanel.newUnCreatedUnit.staminaLimit=GamePanel.modifiableValues.get(16).value;
								}
							}

							//resistance limit
							if(i==17){
								if(GamePanel.modifiableValues.get(16).valueUpperLimit>0){								GamePanel.modifiableValues.get(17).valueUpperLimit--;
								GamePanel.newUnCreatedUnit.resistanceLimit=GamePanel.modifiableValues.get(17).value;
								}
							}
						}

						//press the button
						GamePanel.modifiableValues.get(i).pressBottomButton();

					}
					else if(GamePanel.modifiableValues.get(i).mouseOnTopButton()){//if the mouse is over the top button on one of the modifiable values

						//increase limits if this is a limit button

						//strength limit
						if(i==12){
							if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){

								GamePanel.modifiableValues.get(12).valueUpperLimit++;
								GamePanel.newUnCreatedUnit.strengthLimit=GamePanel.modifiableValues.get(12).value;

							}
						}
						//magic limit
						if(i==13){
							if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){
								GamePanel.modifiableValues.get(13).valueUpperLimit++;
								GamePanel.newUnCreatedUnit.magicLimit=GamePanel.modifiableValues.get(13).value;
							}
						}
						//speed limit
						if(i==14){
							if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){
								GamePanel.modifiableValues.get(14).valueUpperLimit++;
								GamePanel.newUnCreatedUnit.speedLimit=GamePanel.modifiableValues.get(14).value;
							}
						}
						//armour limit
						if(i==15){
							if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){
								GamePanel.modifiableValues.get(15).valueUpperLimit++;
								GamePanel.newUnCreatedUnit.armourLimit=GamePanel.modifiableValues.get(15).value;
							}
						}
						//stamina limit
						if(i==16){
							if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){
								GamePanel.modifiableValues.get(16).valueUpperLimit++;
								GamePanel.newUnCreatedUnit.staminaLimit=GamePanel.modifiableValues.get(16).value;
							}
						}

						//resistance limit
						if(i==17){
							if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){
								GamePanel.modifiableValues.get(17).valueUpperLimit++;
								GamePanel.newUnCreatedUnit.resistanceLimit=GamePanel.modifiableValues.get(17).value;
							}
						}
						//press the button
						GamePanel.modifiableValues.get(i).pressTopButton();
					}


				}

				//set the unit's class
				//if(i==0)
				GamePanel.newUnCreatedUnit.unitType=GamePanel.modifiableValues.get(0).stringIndex;
				//set the unit's team
				//if(i==1)
				GamePanel.newUnCreatedUnit.team=GamePanel.modifiableValues.get(1).value;
				//set the unit's name
				//if(i==2)
				GamePanel.newUnCreatedUnit.unitName=GamePanel.modifiableValues.get(2).stringValue;
				//set the unit's AI
				//if(i==3)
				GamePanel.newUnCreatedUnit.AI_ID=GamePanel.modifiableValues.get(3).stringIndex;
				//set the unit's level
				//if(i==4)
				GamePanel.newUnCreatedUnit.setCurrentLevel(GamePanel.modifiableValues.get(4).value);
				//total stat limit
				//if(i==5)
				GamePanel.newUnCreatedUnit.powerLimit=GamePanel.modifiableValues.get(5).value;
				//chance to get strength
				//if(i==6)
				GamePanel.newUnCreatedUnit.chanceToGetStrength=GamePanel.modifiableValues.get(6).value;
				//chance to get magic
				//if(i==7)
				GamePanel.newUnCreatedUnit.chanceToGetMagic=GamePanel.modifiableValues.get(7).value;
				//chance to get armour
				//if(i==8)
				GamePanel.newUnCreatedUnit.chanceToGetArmour=GamePanel.modifiableValues.get(8).value;
				//chance to get speed
				//if(i==9)
				GamePanel.newUnCreatedUnit.chanceToGetSpeed=GamePanel.modifiableValues.get(9).value;
				//chance to get stamina
				//if(i==10)
				GamePanel.newUnCreatedUnit.chanceToGetStamina=GamePanel.modifiableValues.get(10).value;
				//chance to get resistance
				//if(i==11)
				GamePanel.newUnCreatedUnit.chanceToGetResistance=GamePanel.modifiableValues.get(11).value;
				//strength limit
				//if(i==12){
				//if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){

				//GamePanel.modifiableValues.get(12).valueUpperLimit++;
				GamePanel.newUnCreatedUnit.strengthLimit=GamePanel.modifiableValues.get(12).value;

				//}
				//}
				//magic limit
				//if(i==13){
				//if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){
				//GamePanel.modifiableValues.get(13).valueUpperLimit++;
				GamePanel.newUnCreatedUnit.magicLimit=GamePanel.modifiableValues.get(13).value;
				//}
				//}
				//speed limit
				//if(i==14){
				//if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){
				//GamePanel.modifiableValues.get(14).valueUpperLimit++;
				GamePanel.newUnCreatedUnit.speedLimit=GamePanel.modifiableValues.get(14).value;
				//}
				//}
				//armour limit
				//if(i==15){
				//if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){
				//GamePanel.modifiableValues.get(15).valueUpperLimit++;
				GamePanel.newUnCreatedUnit.armourLimit=GamePanel.modifiableValues.get(15).value;
				//}
				//}
				//stamina limit
				//if(i==16){
				//if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){
				//GamePanel.modifiableValues.get(16).valueUpperLimit++;
				GamePanel.newUnCreatedUnit.staminaLimit=GamePanel.modifiableValues.get(16).value;
				//}
				//}

				//resistance limit
				//if(i==17){
				//if(GamePanel.newUnCreatedUnit.strengthLimit+GamePanel.newUnCreatedUnit.magicLimit+GamePanel.newUnCreatedUnit.speedLimit+GamePanel.newUnCreatedUnit.armourLimit+GamePanel.newUnCreatedUnit.resistanceLimit<GamePanel.newUnCreatedUnit.powerLimit){
				//GamePanel.modifiableValues.get(17).valueUpperLimit++;
				GamePanel.newUnCreatedUnit.resistanceLimit=GamePanel.modifiableValues.get(17).value;
				//}
				//}
				//starting strength
				//if(i==18)
				GamePanel.newUnCreatedUnit.startingStrength=GamePanel.modifiableValues.get(18).value;
				//starting magic
				//if(i==19)
				GamePanel.newUnCreatedUnit.startingMagic=GamePanel.modifiableValues.get(19).value;
				//starting speed
				//if(i==20)
				GamePanel.newUnCreatedUnit.startingSpeed=GamePanel.modifiableValues.get(20).value;
				//starting armour
				//if(i==21)
				GamePanel.newUnCreatedUnit.startingArmour=GamePanel.modifiableValues.get(21).value;
				//starting stamina
				//if(i==22)
				GamePanel.newUnCreatedUnit.startingStamina=GamePanel.modifiableValues.get(22).value;
				//starting resistance
				//if(i==23)
				GamePanel.newUnCreatedUnit.startingResistance=GamePanel.modifiableValues.get(23).value;
				GamePanel.newUnCreatedUnit.isBoss=false;
				if(GamePanel.newUnCreatedUnit.unitName.equals("Bandit Soldier")){
					ArrayList<String> banditMessages = new ArrayList<String>();
					banditMessages.add("GamePanel.newUnCreatedUnit.. is.. unfortunate....");
					banditMessages.add("You cannot win...");
					banditMessages.add("You're too late..");
					banditMessages.add("There is nothing you can do...");
					banditMessages.add("I'm... sorry....");
					GamePanel.newUnCreatedUnit.portrait=GamePanel.portraits[0][0];
					GamePanel.newUnCreatedUnit.deathSpeech.addMessage(banditMessages.get(GamePanel.randomNumber(0, banditMessages.size()-1)));
				}
				else if(GamePanel.newUnCreatedUnit.unitName.equals("Tough Guy")){
					GamePanel.newUnCreatedUnit.isBoss=true;
					GamePanel.newUnCreatedUnit.deathSpeech.addMessage("Heh..");
					GamePanel.newUnCreatedUnit.portrait=GamePanel.portraits[0][0];
				}
				else if(GamePanel.newUnCreatedUnit.unitName.equals("Big Dude")){
					GamePanel.newUnCreatedUnit.isBoss=true;
					GamePanel.newUnCreatedUnit.deathSpeech.addMessage("I was weak..");
					GamePanel.newUnCreatedUnit.portrait=GamePanel.portraits[0][0];
				}
				else if(GamePanel.newUnCreatedUnit.unitName.equals("Jeb")){
					GamePanel.newUnCreatedUnit.deathSpeech.addMessage("I failed... I'm..sorry...");
					GamePanel.newUnCreatedUnit.portrait=GamePanel.portraits[1][0];
				}
				else if(GamePanel.newUnCreatedUnit.unitName.equals("William")){
					GamePanel.newUnCreatedUnit.deathSpeech.addMessage("I can't see, it's so dark...");
					GamePanel.newUnCreatedUnit.portrait=GamePanel.portraits[2][0];
				}
				else if(GamePanel.newUnCreatedUnit.unitName.equals("Tythus")){
					GamePanel.newUnCreatedUnit.deathSpeech.addMessage("Oof...");
					GamePanel.newUnCreatedUnit.portrait=GamePanel.portraits[3][0];
				}
			}

		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {




	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		//check if the mouse is currently over any units
		ArrayList <Unit>units = GamePanel.levels.get(GamePanel.level).Units;
		boolean changed = false;//make sure the mouse is still over a unit
		for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.size();i++){
			if(MouseInfo.getPointerInfo().getLocation().x>=(units.get(i).xpos*32)&&MouseInfo.getPointerInfo().getLocation().y<=(units.get(i).ypos*32)+32&&MouseInfo.getPointerInfo().getLocation().x<(units.get(i).xpos*32)+32&&MouseInfo.getPointerInfo().getLocation().y>=(units.get(i).ypos*32)){
				//set hoveredUnit to this unit
				hoveredUnit = i;
				changed = true;
			}
		}
		if(!changed){
			hoveredUnit=-1;
		}
		if(mouseClickState==1||mouseClickState==2||mouseClickState==5||mouseClickState==6||mouseClickState==7||GamePanel.titleScreen){


			if((MouseInfo.getPointerInfo().getLocation().y/32>0&&MouseInfo.getPointerInfo().getLocation().x/32>0&&MouseInfo.getPointerInfo().getLocation().y/32<GamePanel.levels.get(GamePanel.level).levelHeight&&MouseInfo.getPointerInfo().getLocation().x/32<GamePanel.levels.get(GamePanel.level).levelWidth)){
				//System.out.println("checking"+GamePanel.randomNumber(1, 10));
				//check if the mouse is inside a movable area, if it isn't then move it back to a valid location
				if(moving&&GamePanel.menuHidden&&GamePanel.levels.get(GamePanel.level).tileMap[MouseInfo.getPointerInfo().getLocation().y/32][MouseInfo.getPointerInfo().getLocation().x/32].actionType!=1){
					Robot robot = null;
					try {
						robot = new Robot();
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
					robot.mouseMove(lastValidMousePosition.x,lastValidMousePosition.y);
				}
				//if the menu is open
				else if(GamePanel.menuHidden == false&&(MouseInfo.getPointerInfo().getLocation().x<=GamePanel.menu.xpos||MouseInfo.getPointerInfo().getLocation().x>=GamePanel.menu.xpos+180||MouseInfo.getPointerInfo().getLocation().y<=GamePanel.menu.ypos||MouseInfo.getPointerInfo().getLocation().y>=(GamePanel.menu.ypos+(30*GamePanel.menu.buttons.size())))){
					//System.out.println("triggered");
					Robot robot = null;
					try {
						robot = new Robot();
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
					robot.mouseMove(lastValidMousePosition.x,lastValidMousePosition.y);
				}
				else{

					lastValidMousePosition = new Point(MouseInfo.getPointerInfo().getLocation().x,MouseInfo.getPointerInfo().getLocation().y);


					//if the menu is visible
					if(GamePanel.menuHidden==false){
						//System.out.println("checking menu");
						//loop through each button in the menu
						for(int i = 0; i<GamePanel.menu.buttons.size();i++){
							//System.out.println("checking button number: "+i);
							//check if the mouse position is inside any of the buttons
							if(MouseInfo.getPointerInfo().getLocation().x>=GamePanel.menu.buttons.get(i).xpos&&MouseInfo.getPointerInfo().getLocation().x<=(GamePanel.menu.buttons.get(i).xpos+180)&&MouseInfo.getPointerInfo().getLocation().y>=GamePanel.menu.buttons.get(i).ypos&&MouseInfo.getPointerInfo().getLocation().y<(GamePanel.menu.buttons.get(i).ypos+30)){
								GamePanel.menu.currentButton = i;
								//System.out.println("changed currentbutton to "+i);
							}
						}
					}
				}

			}
		}
	}
}
