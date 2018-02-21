package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;






public class AppletUI extends JFrame{

	private static final long serialVersionUID = -6215774992938009947L;
	public static final long milisecInNanosec = 1000000L;
	public static final long secInNanosec = 1000000000L;
	private int GAME_FPS = 1000;
	private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
	public long lastDrawTime = System.currentTimeMillis();
	public static int windowWidth=700;
	public static int windowHeight=450;
	
	public BufferStrategy myStrategy;

	//variables for sparkles
	long lastSparkleUpdateTime = 0;
	int sparkleUpdateDelay = 10;
	int sparkleXpos = 725;//1195

	Controller ctrl;
	public static void main(String[] args){
		AppletUI f = new AppletUI ();
		f.setSize(windowWidth,windowHeight);
		f.setVisible(true);
	}
	public AppletUI() {

		setSize(windowWidth,windowHeight);
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());

		JPanel drawPanel = new GamePanel();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawPanel.setBackground(Color.BLACK);
		ctrl = new Controller();
		this.addKeyListener(ctrl);

		ctrl.setGamePanel(drawPanel);

		this.setFocusable(true);
		pane.add(drawPanel);

		this.setExtendedState(Frame.MAXIMIZED_BOTH);  
		this.setUndecorated(true);  
		//We start game in new thread.
		Thread gameThread = new Thread() {
			@Override
			public void run(){
				gameLoop();
			}
		};
		gameThread.start();
		//BattleshorePanel.game_is_running=true;
	}
	int delay = 0;
	public void gameLoop(){
		if(delay>=60){
			delay=0;
		}
		delay++;


		// This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
		long beginTime, timeTaken, timeLeft;
		int ticks = 0;
		while(true)
		{
			//System.out.println("gamelooping " + GamePanel.titleScreen+ "menu: "+GamePanel.menuHidden);
			//System.out.println("looping");
			beginTime = System.nanoTime();

			//update all event messages
			if(GamePanel.eventMessages.size()>0){
				GamePanel.eventMessages.get(0).update();
			}
			//update all lock mechanisms
			for(int i = 0;i<GamePanel.levels.get(GamePanel.level).chests.size();i++){
				for(int j = 0;j<GamePanel.levels.get(GamePanel.level).chests.get(i).mechanisms.size();j++){
					GamePanel.levels.get(GamePanel.level).chests.get(i).mechanisms.get(j).update();
				}
			}
			//create move paths for the selected unit
			Controller.updateMovePath();
			//create sparkles for the level complete animation
			if(GamePanel.sparkling){
				if(GamePanel.levelCompleteTextAnimation.getCurrentFrame()!=lastSparkleUpdateTime){

					lastSparkleUpdateTime=GamePanel.levelCompleteTextAnimation.getCurrentFrame();
					//create sparkles in a vertical line 80 pixels tall with random displacement left to right of 30 pixels
					sparkleXpos+=3;
					int numSparkles = GamePanel.randomNumber(3,8);
					for(int j = 0; j<numSparkles;j++){
						GamePanel.sparkles.add(new Animation(GamePanel.sparkle, 9, 40, 0, GamePanel.randomNumber(sparkleXpos,sparkleXpos+15), GamePanel.randomNumber(500,580), false, 0, 0));
					}

				}
				if(GamePanel.levelCompleteTextAnimation.getCurrentFrame()==GamePanel.levelCompleteTextAnimation.getFrameCount()){
					sparkleXpos=725;
					GamePanel.sparkling=false;
				}
			}
			//load the next level if the screen is faded to black
			if(GamePanel.goToNextLevelAnim!=null&&GamePanel.goToNextLevelAnim.getCurrentFrame()==26){
				GamePanel.sparkling=true;
				GamePanel.levelCompleteTextAnimation = new Animation(GamePanel.levelCompleteText,150,40,0,725,490,false,0,0);
				Controller.loadLevel(GamePanel.levelQueue.get(GamePanel.currentLevel));//load next level
			}
			//set current unit to the proper unit if it is the red team's turn
			if(GamePanel.currentTurn==1){
				for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
					//if any of the units are on this team and aren't grey'd out
					if(GamePanel.levels.get(GamePanel.level).Units.get(j).team==1&&GamePanel.levels.get(GamePanel.level).Units.get(j).greyedOut==false&&!GamePanel.battle){
						Controller.currentUnit=j;
						System.out.println("updated AI");
						AI.update();
						GamePanel.levels.get(GamePanel.level).Units.get(j).greyOut(true);
					}
				}
				boolean allGrey=true;
				for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){

					//if any of the units aren't grey
					if(GamePanel.levels.get(GamePanel.level).Units.get(j).team==1&&GamePanel.levels.get(GamePanel.level).Units.get(j).greyedOut==false){
						allGrey=false;
					}

				}
				if(allGrey){//end the current turn if all units are grey'd out



					for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.size();i++){
						GamePanel.levels.get(GamePanel.level).Units.get(i).greyOut(false);
					}
					GamePanel.currentTurn=0;
					System.out.println("current turn set to: "+GamePanel.currentTurn);
					Controller.mouseClickState=0;
				}
			}

			// Repaint the screen 100 times per second.
			if(System.currentTimeMillis()-lastDrawTime>=10){
				repaint();
				lastDrawTime=System.currentTimeMillis();
			}
			GamePanel.levels.get(GamePanel.level).update();
			if(GamePanel.currentBattle!=null){
				if(GamePanel.battle&&GamePanel.currentBattle.winner!=null&&delay==1){
					//grant experience to the winner of the most recent battle
					if(GamePanel.currentBattle.winner.incomingXP>0){
						GamePanel.currentBattle.winner.incomingXP--;
						GamePanel.currentBattle.winner.currentXP++;
						if(GamePanel.currentBattle.winner.currentXP==100){
							GamePanel.currentBattle.winner.levelUP();
						}
					}
					else if(GamePanel.currentBattle.loser.deathSpeech.speechState>GamePanel.currentBattle.loser.deathSpeech.messages.size()-1){
						boolean wasBoss = GamePanel.currentBattle.loser.isBoss;
						GamePanel.levels.get(GamePanel.level).Units.remove(GamePanel.currentBattle.loser);
						GamePanel.battle=false;
						GamePanel.currentBattle=null;
						if(wasBoss){
							GamePanel.endLevel();
						}
						Controller.mouseClickState = 0;
					}
				}
				if(GamePanel.battle&&GamePanel.currentBattle.unit.incomingXP>0){
					GamePanel.currentBattle.unit.incomingXP--;
					GamePanel.currentBattle.unit.currentXP++;
					if(GamePanel.currentBattle.unit.currentXP==100){
						GamePanel.currentBattle.unit.levelUP();
					}
				}
			}
			if(GamePanel.battle&&delay==1){
				//loop through every unit
				for(int i = 0; i<GamePanel.levels.get(GamePanel.level).Units.size();i++){
					//if one of them has incoming health
					if(GamePanel.levels.get(GamePanel.level).Units.get(i).incomingHealth!=0){
						if(GamePanel.levels.get(GamePanel.level).Units.get(i).incomingHealth>0){
							GamePanel.levels.get(GamePanel.level).Units.get(i).currentHealth++;
							GamePanel.levels.get(GamePanel.level).Units.get(i).incomingHealth--;
						}
						else if(GamePanel.levels.get(GamePanel.level).Units.get(i).incomingHealth<0){
							GamePanel.levels.get(GamePanel.level).Units.get(i).currentHealth--;
							GamePanel.levels.get(GamePanel.level).Units.get(i).incomingHealth++;
						}
					}
				}
			}
			if(GamePanel.currentBattle!= null&&GamePanel.battle){
				System.out.println("current battle state: "+GamePanel.currentBattle.battleState);
				//stage 0 of the battle
				if(GamePanel.currentBattle.battleState==0){
					//draw the unit's attack animation
					GamePanel.currentBattle.unit.battleAnimation = new Animation(GamePanel.currentBattle.unit.battleSprites, 14, 120, 0, 832, 451, false, 0, 0);
					GamePanel.currentBattle.battleState = 1;
				}
				//stage 1 of the battle
				if(GamePanel.currentBattle.battleState==1){
					//check if the first unit's attack animation is over
					if(GamePanel.currentBattle.unit.battleAnimation.getCurrentFrame()==GamePanel.currentBattle.unit.battleAnimation.getFrameCount()-1){
						GamePanel.currentBattle.unit.attackUnit(GamePanel.currentBattle.target);
						GamePanel.currentBattle.battleState = 2;
					}
				}
				//stage 2 of the battle
				if(GamePanel.currentBattle.battleState==2){
					//check if the target is done taking damage
					if(GamePanel.currentBattle.target.incomingHealth==0){
						GamePanel.currentBattle.battleState=3;
					}
				}
				//stage 3 of the battle
				if(GamePanel.currentBattle.battleState==3){
					//if the target isn't dead
					if(GamePanel.currentBattle.target.currentHealth>0){
						boolean enemyCanCounter = false;
						for(int i = 0; i<GamePanel.currentBattle.target.getAttackableUnits().size();i++){
							//if the target is close enough to attack the unit
							if(GamePanel.levels.get(GamePanel.level).Units.get(GamePanel.currentBattle.target.getAttackableUnits().get(i)).equals(GamePanel.currentBattle.unit)){
								enemyCanCounter=true;
							}
						}
						if(enemyCanCounter){
							GamePanel.currentBattle.battleState=4;
						}
						else{
							GamePanel.currentBattle.battleState=7;
						}
					}
					//if the target is dead
					else{
						GamePanel.currentBattle.winner = GamePanel.currentBattle.unit;
						//System.out.println("target died!");
						if(GamePanel.currentBattle.target.deathSpeech.speechState==-1)
							GamePanel.currentBattle.target.die();
					}
				}
				//stage 4 of the battle
				if(GamePanel.currentBattle.battleState==4){
					//draw the target's attack animation
					GamePanel.currentBattle.target.battleAnimation = new Animation(GamePanel.currentBattle.target.battleSprites, 14, 120, 0, 832, 451, false, 0, 0);
					GamePanel.currentBattle.battleState = 5;
				}
				//stage 5 of the battle
				if(GamePanel.currentBattle.battleState==5){
					//check if the target's attack animation is over
					if(GamePanel.currentBattle.target.battleAnimation.getCurrentFrame()==GamePanel.currentBattle.target.battleAnimation.getFrameCount()-1){
						GamePanel.currentBattle.target.attackUnit(GamePanel.currentBattle.unit);
						GamePanel.currentBattle.battleState = 6;
					}
				}
				//stage 6 of the battle
				if(GamePanel.currentBattle.battleState==6){
					//check if the unit is done taking damage
					if(GamePanel.currentBattle.unit.incomingHealth==0){
						GamePanel.currentBattle.battleState=7;
					}
				}
				//stage 7 of the battle
				if(GamePanel.currentBattle.battleState==7){
					//if the unit isn't dead
					if(GamePanel.currentBattle.unit.currentHealth>0){
						boolean enemyCanCounter=false;
						for(int i = 0; i<GamePanel.currentBattle.unit.getAttackableUnits().size();i++){
							//if the unit is close enough to attack the target
							if(GamePanel.levels.get(GamePanel.level).Units.get(GamePanel.currentBattle.unit.getAttackableUnits().get(i)).equals(GamePanel.currentBattle.target)){
								enemyCanCounter = true;
							}
						}
						if(enemyCanCounter&&GamePanel.currentBattle.unit.Speed>=(GamePanel.currentBattle.target.Speed*2)){

							GamePanel.currentBattle.battleState=9;

						}
						else if(!enemyCanCounter&&GamePanel.currentBattle.target.Speed>=(GamePanel.currentBattle.unit.Speed*2)){
							GamePanel.currentBattle.battleState=12;
						}
						else{
							GamePanel.currentBattle.unit.incomingXP=1;
							GamePanel.battle=false;
							GamePanel.currentBattle=null;
							Controller.mouseClickState = 0;
						}


					}
					//if the unit is dead
					else{
						GamePanel.currentBattle.winner = GamePanel.currentBattle.target;
						//System.out.println("unit died!");
						if(GamePanel.currentBattle.unit.deathSpeech.speechState==-1)
							GamePanel.currentBattle.unit.die();
					}
				}
				//stage 8 of the battle
				//				if(GamePanel.currentBattle.battleState==8){
				//					//check if the unit has double or more the speed of the target
				//					if(GamePanel.currentBattle.unit.Speed>=(GamePanel.currentBattle.target.Speed*2)){
				//						GamePanel.currentBattle.battleState=9;
				//					}
				//					else if(GamePanel.currentBattle.target.Speed>=(GamePanel.currentBattle.unit.Speed*2)){
				//						GamePanel.currentBattle.battleState=12;
				//					}
				//					else{//end the battle
				//						GamePanel.currentBattle.unit.incomingXP=1;
				//						GamePanel.battle=false;
				//						GamePanel.currentBattle=null;
				//						Controller.mouseClickState = 0;
				//					}
				//				}
				if(GamePanel.battle){
					//stage 9 of the battle
					if(GamePanel.currentBattle.battleState==9){
						//draw the unit's attack animation
						GamePanel.currentBattle.unit.battleAnimation = new Animation(GamePanel.currentBattle.unit.battleSprites, 14, 120, 0, 832, 451, false, 0, 0);
						GamePanel.currentBattle.battleState = 10;
					}
					//stage 10 of the battle
					if(GamePanel.currentBattle.battleState==10){
						//check if the first unit's attack animation is over
						if(GamePanel.currentBattle.unit.battleAnimation.getCurrentFrame()==GamePanel.currentBattle.unit.battleAnimation.getFrameCount()-1){
							GamePanel.currentBattle.unit.attackUnit(GamePanel.currentBattle.target);
							GamePanel.currentBattle.battleState = 11;
						}
					}
					//stage 11 of the battle
					if(GamePanel.currentBattle.battleState==11){
						//check if the target is done taking damage
						if(GamePanel.currentBattle.target.incomingHealth==0){
							GamePanel.currentBattle.battleState=12;
						}
					}
					//stage 12 of the battle
					if(GamePanel.currentBattle.battleState==12){
						//if the target isn't dead
						if(GamePanel.currentBattle.target.currentHealth>0){
							boolean enemyCanCounter=false;
							for(int i = 0; i<GamePanel.currentBattle.target.getAttackableUnits().size();i++){
								//if the target is close enough to attack the unit
								if(GamePanel.levels.get(GamePanel.level).Units.get(GamePanel.currentBattle.target.getAttackableUnits().get(i)).equals(GamePanel.currentBattle.unit)){
									enemyCanCounter=true;
								}
							}
							if(enemyCanCounter){
								GamePanel.currentBattle.battleState=13;
							}
							else{
								GamePanel.currentBattle.battleState=16;
							}
						}
						//if the target is dead
						else{
							GamePanel.currentBattle.winner = GamePanel.currentBattle.unit;
							//System.out.println("target died2!");
							if(GamePanel.currentBattle.target.deathSpeech.speechState==-1)
								GamePanel.currentBattle.target.die();
						}
					}
					//stage 13 of the battle
					if(GamePanel.currentBattle.battleState==13){
						//draw the target's attack animation
						GamePanel.currentBattle.target.battleAnimation = new Animation(GamePanel.currentBattle.target.battleSprites, 14, 120, 0, 832, 451, false, 0, 0);
						GamePanel.currentBattle.battleState = 14;
					}
					//stage 14 of the battle
					if(GamePanel.currentBattle.battleState==14){
						//check if the target's attack animation is over
						if(GamePanel.currentBattle.target.battleAnimation.getCurrentFrame()==GamePanel.currentBattle.target.battleAnimation.getFrameCount()-1){
							GamePanel.currentBattle.target.attackUnit(GamePanel.currentBattle.unit);
							GamePanel.currentBattle.battleState = 15;
						}
					}
					//stage 15 of the battle
					if(GamePanel.currentBattle.battleState==15){
						//check if the unit is done taking damage
						if(GamePanel.currentBattle.unit.incomingHealth==0){
							GamePanel.currentBattle.battleState=16;
						}
					}
					//stage 16 of the battle
					if(GamePanel.currentBattle.battleState==16){
						//if the unit isn't dead
						if(GamePanel.currentBattle.unit.currentHealth>0){
							GamePanel.currentBattle.unit.incomingXP=1;
							GamePanel.battle=false;
							GamePanel.currentBattle=null;
							Controller.mouseClickState = 0;
						}
						//if the unit is dead
						else if(GamePanel.currentBattle.unit.currentHealth<=0&&GamePanel.currentBattle.target.incomingXP<=0){
							GamePanel.currentBattle.winner = GamePanel.currentBattle.target;
							//System.out.println("unit died 2!");
							if(GamePanel.currentBattle.unit.deathSpeech.speechState==-1)
								GamePanel.currentBattle.unit.die();
						}
						//if the target isn't dead
						else if(GamePanel.currentBattle.unit.currentHealth>0){
							GamePanel.currentBattle.unit.incomingXP=1;
							GamePanel.battle=false;
							GamePanel.currentBattle=null;
							Controller.mouseClickState = 0;
						}
						//if the target is dead and the winner is no longer gaining experience
						else if(GamePanel.currentBattle.target.currentHealth<=0&&GamePanel.currentBattle.unit.incomingXP<=0){
							GamePanel.currentBattle.winner = GamePanel.currentBattle.unit;
							//System.out.println("unit died 2!");
							if(GamePanel.currentBattle.target.deathSpeech.speechState==-1)
								GamePanel.currentBattle.target.die();
						}

					}
					//System.out.println("battle state: "+GamePanel.currentBattle.battleState);
				}
			}

			// Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
			// If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
			if (timeLeft < 10) 
				timeLeft = 1; //set a minimum
			try {
				//Provides the necessary delay and also yields control so that other thread can do work.
				Thread.sleep(timeLeft);
			} catch (InterruptedException ex) { }
			ticks++;
			if(ticks>600){
				ticks=0;
			}
		}
	}
	public void Draw(Graphics g){

	}

}

