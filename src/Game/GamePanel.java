package Game;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel{

	private static final long serialVersionUID = 7734877696044080629L;
	//public static enum Gamestate{appletui_init,Controller_init};
	//public static Gamestate gamestate;
	public static ArrayList<Level> levels = new ArrayList<Level>();
	public static Animation goToNextLevelAnim;
	public static Animation levelCompleteTextAnimation;
	public static ArrayList<Animation> sparkles = new ArrayList<Animation>();
	static BufferedImage infoWindow = Images.load("/textures/InfoBox.png");
	static BufferedImage modifiableValueArt = Images.load("/textures/ModifiableValueArt.png");
	static BufferedImage modifiableStringValueArt = Images.load("/textures/ModifiableStringValueArt.png");
	static BufferedImage saveScreen = Images.load("/textures/saveScreen.png");
	static BufferedImage titleScreenImg = Images.load("/textures/TitleScreen.png");
	static BufferedImage button = Images.load("/textures/Button.png");
	static BufferedImage battleScreen = Images.load("/textures/BattleScreen.png");
	static BufferedImage menuBorder = Images.load("/textures/MenuBorder.png");
	static BufferedImage buttonHighLight = Images.load("/textures/ButtonHighlight.png");
	static BufferedImage attackHighlight = Images.load("/textures/AttackHighlight.png");
	static BufferedImage experienceBarBackground = Images.load("/textures/ExperienceBarBackground.png");
	static BufferedImage xpBar = Images.load("/textures/ExperienceBar.png");
	static BufferedImage healthBar = Images.load("/textures/HealthBar.png");
	static BufferedImage speechBubble = Images.load("/textures/SpeechBubble.png");
	static BufferedImage battleFade = Images.load("/textures/BattleFade.png");
	static BufferedImage bossFlag = Images.load("/textures/BossFlag.png");
	static BufferedImage unitTooltip = Images.load("/textures/CharacterTooltip.png");
	public static BufferedImage [][] endLevelFade = Images.cut("/tiles/EndLevelFade.png", 1, 1);
	public static BufferedImage [][] portraits = Images.cut("/textures/CharacterPortraits.png", 40, 40);
	public static BufferedImage [][] arrows = Images.cut("/textures/MoveArrows.png", 32, 32);
	public static BufferedImage [][] sparkle = Images.cut("/textures/Sparkle.png", 10, 10);
	public static BufferedImage [][] animatedTiles = Images.cut("/tiles/AnimatedTiles.png", 32, 32);
	public static BufferedImage [][] characterPortraits = Images.cut("/textures/CharacterPortraits.png", 40, 40);
	public static BufferedImage [][] mapSoldierA = Images.cut("/textures/MapSoldierA.png", 32, 48);
	public static BufferedImage [][] mapSoldierB = Images.cut("/textures/MapSoldierB.png", 32, 48);
	public static BufferedImage [][] mapSoldierGrey = Images.cut("/textures/MapSoldierGrey.png", 32, 48);
	public static BufferedImage [][] mapArcherA = Images.cut("/textures/MapArcherA.png", 32, 48);
	public static BufferedImage [][] mapArcherGrey = Images.cut("/textures/MapArcherGrey.png", 32, 48);
	public static BufferedImage [][] levelCompleteText = Images.cut("/textures/LevelCompleteText.png", 3, 100);
	public static BufferedImage [][] miniGameBars = Images.cut("/textures/MiniGameBars.png", 3, 30);
	public static BufferedImage miniGameSelector = Images.load("/textures/MiniGameSelector.png");

	public static BufferedImage [][] soldierBattleSpritesA = Images.cut("/textures/SolderBattleSpritesA.png", 256, 128);
	public static BufferedImage [][] soldierBattleSpritesB = Images.cut("/textures/SolderBattleSpritesB.png", 256, 128);
	public static BufferedImage [][] archerBattleSpritesA = Images.cut("/textures/ArcherBattleSpritesA.png", 256, 128);
	public static BufferedImage [][] chestImages = Images.cut("/textures/Chest.png", 32, 32);

	public static BufferedImage [][] actionImg = Images.cut("/tiles/ActionTiles.png",32,32);
	public static int level = 0;
	public static ArrayList<String> levelQueue = new ArrayList<String>();//name of all the levels that should be loaded consecutively
	public static int mapY = 0;
	public static int mapX = 0;
	public static boolean battle = false;
	public static boolean titleScreen = true;
	public static Battle currentBattle;
	//public static ArrayList<LockMechanism> tempMechanism = new ArrayList<LockMechanism>();
	public static int currentTurn;
	public static int currentTurnIndex;
	public static int currentLevel=0;//the level that should be loaded
	public static int currentChest=-1;
	public static boolean menuHidden = true;
	public static boolean itemSubMenuOpen = false;
	public static boolean itemMenuOpen = false;
	public static boolean sparkling = false;
	public static boolean chestActive=false;//whether or not a chest is being opened atm

	//public static boolean showNewUnitWindow = false;
	public static Unit newUnCreatedUnit;
	public static ArrayList<ModifiableValue> modifiableValues = new ArrayList<ModifiableValue>();
	public static ArrayList<EventMessage> eventMessages = new ArrayList<EventMessage>();
	public static DropDownMenu menu = new DropDownMenu(new Point(-1,-1));//menu for making/choosing decisions
	public static int selectedItem = -1;
	Cursor cursor = new Cursor();

	public GamePanel(){
		int menuXpos=870;
		int menuYpos=500;
		GamePanel.menuHidden = false;
		GamePanel.menu = new DropDownMenu(new Point(menuXpos,menuYpos));
		Controller.lastValidMousePosition = new Point(menuXpos,menuYpos);
		this.setDoubleBuffered(true);
		GamePanel.levels.add(new Level(0,60,34));
		levelQueue.add("level1");
		levelQueue.add("level2");
		for(int i = 1; i<=10;i++){
			levels.get(level).chests.add(new LockedChest(5, 5+i, 1, i));
			System.out.println("added a chest with difficulty: "+i);
		}
		System.out.println("done adding chests");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Draw(g);


	}
	public static void endLevel(){
		//System.out.println("level queue size = "+levelQueue.size());
		//System.out.println("current level = "+currentLevel);
		currentLevel++;
		if(levelQueue.size()>currentLevel){
			System.out.println("ended level");
			goToNextLevelAnim = new Animation(endLevelFade,112,100,0,0,0,false,0,0);

		}
	}

	public static void removeMoveGrids(){//remove all active move grids from the map
		for(int i = 0; i<levels.get(level).levelHeight;i++){
			for(int j = 0; j<levels.get(level).levelWidth;j++){
				levels.get(level).tileMap[i][j].actionType=0;
			}
		}
	}
	static int randomNumber(int min, int max){
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	public void Draw(Graphics g){

		if(!titleScreen){
			for(int i = 0; i<levels.get(level).levelHeight;i++){
				for(int j = 0; j<levels.get(level).levelWidth;j++){
					levels.get(level).tileMap[i][j].Draw(g);
				}
			}
			if(levels.get(level).chests!=null&&!titleScreen){
				if(chestActive){
					g.drawImage(GamePanel.battleFade, 0, 0,1920, 1080, null);
				}
				for(int i = 0; i<levels.get(level).chests.size();i++){

					levels.get(level).chests.get(i).Draw(g);

				}
			}
			for(int i = 0; i<levels.get(level).Units.size();i++){
				levels.get(level).Units.get(i).Draw(g);
			}
			cursor.Draw(g);
			if(!Controller.saving&&!Controller.loading){
				//draw the info window
				if(Cursor.cursorPos.x>180||Cursor.cursorPos.y>90){
					g.drawImage(infoWindow, 0, 0,200, 100, null);
					Font font = new Font("Iwona Heavy",Font.BOLD,12);;
					g.setFont(font);
					g.drawString(levels.get(level).tileMap[(Cursor.cursorPos.y+2)/32][(Cursor.cursorPos.x+2)/32].name, 48, 18);
					g.drawImage(levels.get(level).tileMap[(Cursor.cursorPos.y+2)/32][(Cursor.cursorPos.x+2)/32].texture[levels.get(level).tileMap[(Cursor.cursorPos.y+2)/32][(Cursor.cursorPos.x+2)/32].tileX][levels.get(level).tileMap[(Cursor.cursorPos.y+2)/32][(Cursor.cursorPos.x+2)/32].tileY], 8, 8,32, 32, null);
					g.drawString("Replace with: "+Controller.currentTileName, 8, 53);
					Tile temp = new Tile(new Point(-20,-20), levels.get(level).tiles,GamePanel.levels.get(GamePanel.level).beaches,actionImg, Controller.currentTileID);
					g.drawImage(levels.get(level).tiles[temp.tileX][temp.tileY],8,58,32,32,null);
				}
				else{
					g.drawImage(infoWindow, 440, 0,200, 100, null);
					Font font = new Font("Iwona Heavy",Font.BOLD,12);
					g.setFont(font);
					g.drawString(levels.get(level).tileMap[(Cursor.cursorPos.y+2)/32][(Cursor.cursorPos.x+2)/32].name, 488, 18);
					g.drawImage(levels.get(level).tileMap[(Cursor.cursorPos.y+2)/32][(Cursor.cursorPos.x+2)/32].texture[levels.get(level).tileMap[(Cursor.cursorPos.y+2)/32][(Cursor.cursorPos.x+2)/32].tileX][levels.get(level).tileMap[(Cursor.cursorPos.y+2)/32][(Cursor.cursorPos.x+2)/32].tileY], 448, 8,32, 32, null);
					g.drawString("Replace with: "+Controller.currentTileName, 448, 53);
					Tile temp = new Tile(new Point(-20,-20), levels.get(level).tiles,GamePanel.levels.get(GamePanel.level).beaches,actionImg, Controller.currentTileID);
					g.drawImage(levels.get(level).tiles[temp.tileX][temp.tileY],448,58,32,32,null);
				}
			}
			else if(Controller.saving){
				g.drawImage(saveScreen, 0, 0,640, 640, null);
				Font font = new Font("Iwona Heavy",Font.BOLD,12);;
				g.setFont(font);
				g.drawString("Enter new level name:  "+Controller.text, 100, 500);
			}
			else if(Controller.loading){
				g.drawImage(saveScreen, 0, 0,640, 640, null);
				Font font = new Font("Iwona Heavy",Font.BOLD,12);;
				g.setFont(font);
				g.drawString("Enter saved level name:  "+Controller.text, 100, 500);
			}

			if(battle){
				g.drawImage(battleScreen, 832, 451,256, 178, null);
				if(currentBattle!=null){
					currentBattle.Draw(g);
				}
			}
			//draw character tooltip
			if(Controller.hoveredUnit!=-1){
				Point tooltipPos = new Point(800,0);
				//draw the tooltip image
				g.drawImage(this.unitTooltip,tooltipPos.x,tooltipPos.y,160,80,null);
				//draw the unit's name and hp
				Font font = new Font("Iwona Heavy",Font.BOLD,14);;
				g.setFont(font);
				g.drawString(levels.get(level).Units.get(Controller.hoveredUnit).unitName, tooltipPos.x+50, tooltipPos.y+30);
				//draw the unit's portrait
				g.drawImage(levels.get(level).Units.get(Controller.hoveredUnit).portrait,tooltipPos.x+5,tooltipPos.y+5,40,40,null);
				//draw the unit's hp
				font = new Font("Iwona Heavy",Font.BOLD,12);
				g.setFont(font);

				for(int i = 0; i<100;i++){
					if(i<=(levels.get(level).Units.get(Controller.hoveredUnit).currentHealth/levels.get(level).Units.get(Controller.hoveredUnit).maxHealth)*100){
						g.drawImage(GamePanel.healthBar, tooltipPos.x+27+(i), tooltipPos.y+50,1, 20, null);
					}
				}
				g.drawString("HP: ", tooltipPos.x+5, tooltipPos.y+64);
				g.drawString((int)levels.get(level).Units.get(Controller.hoveredUnit).currentHealth+"/"+(int)levels.get(level).Units.get(Controller.hoveredUnit).maxHealth, tooltipPos.x+60, tooltipPos.y+64);
				//draw the unit's current experience
				for(int i = 0; i<100;i++){
					if(i<=(levels.get(level).Units.get(Controller.hoveredUnit).currentXP)){
						g.drawImage(GamePanel.xpBar, tooltipPos.x+27+(i), tooltipPos.y+71,1, 7, null);
					}
				}
			}
			if(goToNextLevelAnim!=null){
				goToNextLevelAnim.xScale=1920;
				goToNextLevelAnim.yScale=1080;
				goToNextLevelAnim.Draw(g);
			}
			if(sparkles.size()>0){//draw the animated sparkles for the "Level Complete!" text
				for(int i = 0; i<sparkles.size();i++){
					sparkles.get(i).Draw(g);
				}
			}
			if(levelCompleteTextAnimation!=null){
				levelCompleteTextAnimation.additive=true;
				levelCompleteTextAnimation.Draw(g);
			}
			if(newUnCreatedUnit!=null){
				g.drawImage(infoWindow, 450, 150,950, 840, null);
				//if there are modifiable values
				if(modifiableValues.size()>0){
					//loop through all the modifiable values
					for(int i = 0; i<modifiableValues.size();i++){
						//draw all the modifiable values
						modifiableValues.get(i).Draw(g);
					}
				}
			}
			if(menuHidden==false&&menu!=null){
				//System.out.println("drawing menu");
				menu.Draw(g);
			}
			//draw the first eventMessage in the list
			if(!titleScreen&&eventMessages.size()>0){
				eventMessages.get(0).Draw(g);
			}
		}
		else{//title screen
			g.drawImage(titleScreenImg, 0, 0,1920, 1080, null);
			menu.Draw(g);
		}
		if(levels.get(level).chests!=null&&!this.titleScreen){

		}
	}
}
