package Game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class Unit {
	BufferedImage [][]staticMapTexture;
	BufferedImage [][]staticMapTextureGrey;
	BufferedImage [][]DefaultStaticMapTexture;
	Animation staticMapSpriteAnim;
	public Animation battleAnimation;
	public Speech deathSpeech = new Speech(885, 500);

	Unit opponent = null;
	//BufferedImage[][] actionTiles = Images.cut("/tiles/ActionTiles.png", 32, 32);
	public BufferedImage[][] battleSprites;
	public BufferedImage portrait;
	public int xpos = 0;
	public int ypos = 0;
	public int team = 0;
	public int attackRange = 1;
	public int unitType = 0;
	public int mobility = 1;
	int baseUnitHP;
	double currentHealth;
	double maxHealth;
	int incomingXP = 0;
	double incomingHealth = 0;
	//unit stats
	public String unitName = "";
	public int currentXP = 0;//once exp = 100 the unit levels up
	public int Stamina = 0;//affects health
	public int Strength = 0;//affects physical dmg
	public int Magic = 0;//affects magic dmg
	public int Speed = 0;//affects attacks per turn
	public int moveRange = 6;//affects how far the unit can move
	public int armour = 0;//every point of armour negates one physical dmg
	public int resistance = 0;//every point of resistance negates one magic dmg

	public int currentLevel = 1;

	//level up related variables
	int powerLimit = 100;
	int levelLimit = 30;

	int chanceToGetStrength = 50;
	int chanceToGetArmour = 50;
	int chanceToGetMagic = 50;
	int chanceToGetSpeed = 50;
	int chanceToGetStamina = 50;
	int chanceToGetResistance = 50;

	int strengthLimit;
	int armourLimit;
	int magicLimit;
	int speedLimit;
	int staminaLimit;
	int resistanceLimit;

	int startingStrength=0;
	int startingArmour=0;
	int startingMagic=0;
	int startingSpeed=0;
	int startingStamina=0;
	int startingResistance=0;

	//unit items
	Item[] items = new Item[8];
	//unit weapon skill levels
	public int axeLevel = 0;
	public int swordLevel = 0;
	public int spearLevel = 0;
	public int lifeMagicLevel = 0;
	public int bloodMagicLevel = 0;
	public int assistMagicLevel = 0;
	public int bowLevel = 0;

	public int AI_ID=-1;
	public boolean greyedOut = false;
	public boolean isBoss = false;
	public Point posBeforeMove;//position before moving
	public ArrayList<Point> movePath = new ArrayList<Point>();
	public Unit(){//,int affiliation, int UnitType, String Name, ArrayList<Integer> Items
		this.init();
	}
	public void init(){
		if(this.unitType==0){//if the unit is a soldier
			if(this.team==0){//blue team
				this.staticMapTexture = GamePanel.mapSoldierA;
				this.DefaultStaticMapTexture=GamePanel.mapSoldierA;
				this.staticMapSpriteAnim = new Animation(this.staticMapTexture, 2, 1000, 0,this.xpos*32, (this.ypos*32)-20, true, 0, 0);
				this.battleSprites = GamePanel.soldierBattleSpritesA;
				this.items[0]=new Item(0);
				this.items[1]=new Item(1);
			}
			else if(this.team==1){//red team

				this.staticMapTexture=GamePanel.mapSoldierB;
				this.DefaultStaticMapTexture=GamePanel.mapSoldierB;
				//this.staticMapSpriteAnim = new Animation(this.staticMapTexture, 2, 1000, 0, this.xpos*32, (this.ypos*32)-20, true, 0, 0);
				this.battleSprites = GamePanel.soldierBattleSpritesB;
				this.AI_ID=0;
				System.out.println("new unit added");
				if(this.unitName.equals("Tough Guy")){
					this.Strength=2;
					this.Stamina=18;
				}
				this.items[0]=new Item(0);
				this.items[1]=new Item(1);
			}
		}
		if(this.unitType==1){//if the unit is a archer
			if(this.team==0){//blue team
				this.staticMapTexture = GamePanel.mapArcherA;
				this.DefaultStaticMapTexture=GamePanel.mapArcherA;
				//this.staticMapSpriteAnim = new Animation(this.staticMapTexture, 2, 1000, 0, this.xpos*32, (this.ypos*32)-20, true, 0, 0);
				this.battleSprites = GamePanel.archerBattleSpritesA;
				this.items[0]=new Item(2);
				this.items[1]=new Item(0);
			}
			else if(this.team==1){//red team
				if(this.unitType==0){//soldier
					this.staticMapTexture=GamePanel.mapSoldierB;
					this.DefaultStaticMapTexture=GamePanel.mapSoldierB;
					//this.staticMapSpriteAnim = new Animation(this.staticMapTexture, 2, 1000, 0, this.xpos*32, (this.ypos*32)-20, true, 0, 0);
					this.battleSprites = GamePanel.soldierBattleSpritesB;
					this.AI_ID=0;
					System.out.println("new unit added");
					if(this.unitName.equals("Tough Guy")){
						this.Strength=2;
						this.Stamina=18;
					}
					this.items[0]=new Item(2);
				}
			}
		}
	}
	public void moveToTile(){
		boolean crashed = false;
		posBeforeMove = new Point(xpos,ypos);
		for(int i = 0; i<movePath.size(); i++){
			for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
				if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=this.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=this&&movePath.get(i).x==GamePanel.levels.get(GamePanel.level).Units.get(j).xpos&&movePath.get(i).y==GamePanel.levels.get(GamePanel.level).Units.get(j).ypos){
					crashed = true;
				}
			}
			if(!crashed){

				xpos = movePath.get(i).x;
				ypos = movePath.get(i).y;
				staticMapSpriteAnim.xpos=xpos*32;
				staticMapSpriteAnim.ypos=(ypos*32)-20;
			}
		}
		//this.greyOut(true);
	}
	public void greyOut(boolean greyState){//change the unit to a grey texture and no longer allow movement

		this.greyedOut = greyState;
		if(greyState==true){
			if(this.unitType==0)
				this.staticMapSpriteAnim.spriteSheet=GamePanel.mapSoldierGrey;
			else if(this.unitType==1)
				this.staticMapSpriteAnim.spriteSheet=GamePanel.mapArcherGrey;
		}
		else{
			this.staticMapSpriteAnim.spriteSheet=DefaultStaticMapTexture;
		}
	}
	public void setCurrentLevel(int newLevel){
		//if the new level is a valid level
		if(newLevel<=30&&newLevel>0){
			this.Strength=this.startingStrength;
			this.Magic=this.startingMagic;
			this.Speed=this.startingSpeed;
			this.armour=this.startingArmour;
			this.Stamina=this.startingStamina;
			this.resistance=this.startingResistance;
			//for the number of levels gained
			for(int i = 0; i<newLevel;i++){
				//gain strength if the unit's current strength is less than it's limit
				if(GamePanel.randomNumber(1, 100)<=this.chanceToGetStrength&&this.Strength<this.strengthLimit){//chance to gain strength
					this.Strength++;
				}
				//gain magic if the unit's current magic is less than it's limit
				if(GamePanel.randomNumber(1, 100)<=this.chanceToGetMagic&&this.Magic<this.magicLimit){//chance to gain magic
					this.Magic++;
				}
				//gain speed if the unit's current speed is less than it's limit
				if(GamePanel.randomNumber(1, 100)<=this.chanceToGetSpeed&&this.Speed<this.speedLimit){//chance to gain speed
					this.Speed++;
				}
				//gain armour if the unit's current armour is less than it's limit
				if(GamePanel.randomNumber(1, 100)<=this.chanceToGetArmour&&this.armour<this.armourLimit){//chance to gain armour
					this.armour++;
				}
				//gain stamina if the unit's current stamina is less than it's limit
				if(GamePanel.randomNumber(1, 100)<=this.chanceToGetStamina&&this.Stamina<this.staminaLimit){//chance to gain stamina
					this.Stamina++;
				}
				//gain resistance if the unit's current resistance is less than it's limit
				if(GamePanel.randomNumber(1, 100)<=this.chanceToGetResistance&&this.resistance<this.resistanceLimit){//chance to gain strength
					this.resistance++;
				}
			}
		}
	}
	public void gainXP(int total){
		//set this unit's incomingXP to the amount of total experience to be gained
		incomingXP = total;
		//the game loop will keep granting experience to this unit until it's incomingXP is set to zero at which point the exp bar stops being drawn
	}
	public void die(){
		GamePanel.currentBattle.loser=this;
		deathSpeech.update();
	}
	public void levelUP(){
		this.currentLevel++;
		int staminaGain = 0;
		for(int i = 0; i<3; i++){
			if(GamePanel.randomNumber(1, 100)<=this.chanceToGetStamina){
				staminaGain+=1;
			}
			Stamina+=staminaGain;
		}
		if(this.startingStrength>0){//if the unit can use physical weapons
			if(GamePanel.randomNumber(1, 100)<=this.chanceToGetStrength){
				Strength+=1;
			}
		}
		if(this.startingMagic>0){//if the unit can use magic weapons
			if(GamePanel.randomNumber(1, 100)<=this.chanceToGetMagic){
				Magic+=1;
			}
		}
		if(GamePanel.randomNumber(1, 100)<=this.chanceToGetSpeed){
			Speed+=1;
		}
		if(GamePanel.randomNumber(1, 100)<=this.chanceToGetArmour){
			armour+=1;
		}
		if(GamePanel.randomNumber(1, 100)<=this.chanceToGetResistance){
			resistance+=1;
		}
		this.currentXP = 0;
	}
	/*
	 * returns the index of the weapon/spell that is currently equipped by the unit
	 * returns -1 if the unit has no usable weapons
	 */
	public int currentWeapon(){
		//loop through this unit's items
		for(int i = 0; i<this.items.length;i++){
			if(items[i]!=null){
				if(items[i].isWeapon||items[i].isSpell){
					//if the item is usable by this unit
					if(this.swordLevel>=items[i].swordLevelReq&&this.axeLevel<=items[i].axeLevelReq&&this.spearLevel<=items[i].spearLevelReq
							&&this.bloodMagicLevel<=items[i].bloodMagicLevelReq&&this.lifeMagicLevel<=items[i].lifeMagicLevelReq
							&&this.assistMagicLevel<=items[i].assistMagicLevelReq&&this.bowLevel<=items[i].bowLevelReq){
						return i;
					}
				}
			}
		}
		return -1;
	}
	public void findAllAttackablePositions(ArrayList <Tile>path, Tile node){
		path.add(node);
		if(path.size()<=moveRange&&path.size() > 0)
		{
			int temp = 0;
			//loop through every attackable position around this point
			//System.out.println("werwe "+node.ypos+","+node.xpos);
			if(currentWeapon()!=-1){
				for(int y = node.ypos/32-this.items[currentWeapon()].maxWeaponRange; y <= node.ypos/32+this.items[currentWeapon()].maxWeaponRange;y++){
					for(int x = node.xpos/32-this.items[currentWeapon()].maxWeaponRange; x <= node.xpos/32+this.items[currentWeapon()].maxWeaponRange;x++){
						//if(j>=x-range&&j<=x+range){
						if(x>=node.xpos/32-temp&&x<=node.xpos/32+temp){
							if(y>=0&&y<GamePanel.levels.get(GamePanel.level).levelHeight&&y>=0&&x>=0&&x<GamePanel.levels.get(GamePanel.level).levelWidth)
								GamePanel.levels.get(GamePanel.level).tileMap[y][x].actionType=2;
						}
					}
					if(y<node.ypos/32){
						temp++;
					}
					else if(y>=node.ypos/32){
						temp--;
					}

				}
			}




		}
		//check every side of the current tile
		for(int i = 0; i<getPassableAdjacentTiles(node).size();i++){
			if(path.size()<=this.moveRange) findAllAttackablePositions(path, getPassableAdjacentTiles(node).get(i));
		}
		path.remove(node);
	}
	public void findAllPaths(ArrayList <Tile>path, Tile node)
	{   
		path.add(node);
		if(path.size()<=moveRange&&path.size() > 0)
		{
			//System.out.println("path size = "+path.size());
			if(GamePanel.levels.get(GamePanel.level).Units.get(Controller.currentUnit).team==GamePanel.currentTurn){//if it is the selected unit's turn
				GamePanel.levels.get(GamePanel.level).tileMap[node.ypos/32][node.xpos/32].actionType=1;
			}
			else{
				GamePanel.levels.get(GamePanel.level).tileMap[node.ypos/32][node.xpos/32].actionType=4;
			}

		}
		//check every side of the current tile
		for(int i = 0; i<getPassableAdjacentTiles(node).size();i++){
			if(path.size()<=this.moveRange) findAllPaths(path, getPassableAdjacentTiles(node).get(i));
		}
		path.remove(node);

	}
	public void setPosition(int x, int y){
		this.xpos=x;
		this.ypos=y;
	}
	public ArrayList<Tile> getPassableAdjacentTiles(Tile tile){
		ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
		if(tile.xpos-32>=0){
			boolean crashed = false;
			//loop through units
			for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
				//check if any units are in the same position as this tile and they're not on this units team
				if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=this.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=this&&tile.ypos/32==GamePanel.levels.get(GamePanel.level).Units.get(j).ypos&&(tile.xpos-32)/32==GamePanel.levels.get(GamePanel.level).Units.get(j).xpos){
					crashed = true;
				}
			}
			if(GamePanel.levels.get(GamePanel.level).tileMap[tile.ypos/32][(tile.xpos-32)/32].collisionType<this.mobility&&!crashed)
				adjacentTiles.add(GamePanel.levels.get(GamePanel.level).tileMap[tile.ypos/32][(tile.xpos-32)/32]);
		}
		if(tile.ypos-32>=0){
			boolean crashed = false;
			//loop through units
			for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
				//check if any units are in the same position as this tile and they're not on this units team
				if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=this.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=this&&(tile.ypos-32)/32==GamePanel.levels.get(GamePanel.level).Units.get(j).ypos&&(tile.xpos)/32==GamePanel.levels.get(GamePanel.level).Units.get(j).xpos){
					crashed = true;
				}
			}
			if(GamePanel.levels.get(GamePanel.level).tileMap[(tile.ypos-32)/32][(tile.xpos/32)].collisionType<this.mobility&&!crashed)
				adjacentTiles.add(GamePanel.levels.get(GamePanel.level).tileMap[(tile.ypos-32)/32][(tile.xpos/32)]);
		}
		if(tile.xpos+32<=GamePanel.levels.get(GamePanel.level).levelWidth*32){
			boolean crashed = false;
			//loop through units
			for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
				//check if any units are in the same position as this tile and they're not on this units team
				if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=this.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=this&&tile.ypos/32==GamePanel.levels.get(GamePanel.level).Units.get(j).ypos&&(tile.xpos+32)/32==GamePanel.levels.get(GamePanel.level).Units.get(j).xpos){
					crashed = true;
				}
			}
			if(GamePanel.levels.get(GamePanel.level).tileMap[tile.ypos/32][(tile.xpos+32)/32].collisionType<this.mobility&&!crashed)
				adjacentTiles.add(GamePanel.levels.get(GamePanel.level).tileMap[tile.ypos/32][(tile.xpos+32)/32]);
		}
		if(tile.ypos+32<=GamePanel.levels.get(GamePanel.level).levelHeight*32){
			boolean crashed = false;
			//loop through units
			for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
				//check if any units are in the same position as this tile and they're not on this units team
				if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=this.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=this&&(tile.ypos+32)/32==GamePanel.levels.get(GamePanel.level).Units.get(j).ypos&&(tile.xpos)/32==GamePanel.levels.get(GamePanel.level).Units.get(j).xpos){
					crashed = true;
				}
			}
			if(GamePanel.levels.get(GamePanel.level).tileMap[(tile.ypos+32)/32][(tile.xpos/32)].collisionType<this.mobility&&!crashed)
				adjacentTiles.add(GamePanel.levels.get(GamePanel.level).tileMap[(tile.ypos+32)/32][(tile.xpos/32)]);
		}
		return adjacentTiles;
	}
	public ArrayList<Integer> getAttackableUnits(){

		ArrayList <Integer>attackableUnits = new ArrayList<Integer>();
		//if this unit has a usable weapon
		if(this.currentWeapon()!=-1){
			//loop through units
			for(int j = 0; j<GamePanel.levels.get(GamePanel.level).Units.size();j++){
				//if any of the units aren't on this unit's team and aren't this unit
				if(GamePanel.levels.get(GamePanel.level).Units.get(j).team!=this.team&&GamePanel.levels.get(GamePanel.level).Units.get(j)!=this){
					//if any of these units are within the attack range of this unit
					int temp = 0;
					//loop through every attackable position around this unit
					for(int y = this.ypos-this.items[currentWeapon()].maxWeaponRange; y <= this.ypos+this.items[currentWeapon()].maxWeaponRange;y++){
						for(int x = this.xpos-this.items[currentWeapon()].maxWeaponRange; x <= this.xpos+this.items[currentWeapon()].maxWeaponRange;x++){
							if(x>=this.xpos-temp&&x<=this.xpos+temp){
								//if any of these units are at an attackable position
								if(x==GamePanel.levels.get(GamePanel.level).Units.get(j).xpos&&y==GamePanel.levels.get(GamePanel.level).Units.get(j).ypos){
									attackableUnits.add(j);
								}
							}
						}
						if(y<this.ypos){
							temp++;
						}
						else if(y>=this.ypos){
							temp--;
						}

					}
				}
			}
		}
		return attackableUnits;
	}
	public void attackUnit(Unit target){//returns true if the target is killed
		//check which type of damage was dealt by this unit
		if(this.items[this.currentWeapon()].isWeapon){//physical weapon
			double dmgDealt = this.items[this.currentWeapon()].attackDmg+this.Strength-target.armour;
			//if the unit does no damage, do zero dmg (negative value would heal)
			if(dmgDealt<0){
				dmgDealt = 0;
			}
			if(dmgDealt>target.currentHealth){
				dmgDealt = target.currentHealth;
			}
			target.incomingHealth = -dmgDealt;
		}
		else if(this.items[this.currentWeapon()].isSpell){//magic weapon
			double dmgDealt = this.items[this.currentWeapon()].attackDmg+this.Magic-target.resistance;
			//if the unit does no damage, do zero dmg (negative value would heal)
			if(dmgDealt<0){
				dmgDealt = 0;
			}
			if(dmgDealt>target.currentHealth){
				dmgDealt = target.currentHealth;
			}
			target.incomingHealth = -dmgDealt;
		}
		//the weapon should lose durability
		this.items[this.currentWeapon()].loseDurability();
		//check if the weapon has no durability
		if(this.items[this.currentWeapon()].currentDurability<=0){
			this.items[this.currentWeapon()]=null;
		}
		System.out.println("Attacking a unit! Attack did "+target.incomingHealth+" damage!");

	}
	public void equipItem(int index){
		//remove the item at index
		Item tempItem= items[index];
		items[index]=null;
		//shift over all the items so the first inventory slot is open
		for(int i = items.length-1; i>0;i--){
			if(items[i]==null&&i!=0){
				items[i]=items[i-1];
				items[i-1]=null;
			}
			if(i==0){
				items[i]=tempItem;//assign the first inventory slot to equal the item that was equipped
			}
		}
	}
	public void getItem(Item item){
		for(int i = 0; i<items.length;i++){
			if(items[i]==null){
				items[i]=item;
				break;
			}
		}
	}
	public boolean hasInventorySpace(){
		for(int i = 0; i<items.length;i++){
			if(items[i]==null){
				return true;
			}
		}
		return false;
	}
	public void showMoveGrid(){
		int range = 0;
		for(int i = ypos-moveRange-attackRange; i<=ypos+moveRange+attackRange;i++){
			for(int j = xpos-moveRange-attackRange; j<=xpos+moveRange+attackRange;j++){
				if(j>=xpos-range&&j<=xpos+range){
					if(j>=0&&i>=0&&i<=39&&j<=59){
						if(GamePanel.levels.size()>GamePanel.level){
							if(j>=xpos+attackRange-(range)&&j<=xpos-attackRange+(range)){
								GamePanel.levels.get(GamePanel.level).tileMap[i][j].actionType=1;
							}
							else{
								GamePanel.levels.get(GamePanel.level).tileMap[i][j].actionType=2;
							}
							//set the tile at this position to be blue if it can be traveled to, red if it can be attacked, and nothing if it can't be attacked or moved to
						}
					}
				}

			}
			if(i<ypos){
				range++;
			}
			else if(i>=ypos){
				range--;
			}
		}
	}

	public void Draw(Graphics g){
		if(this.staticMapSpriteAnim!=null)
			staticMapSpriteAnim.Draw(g);
		if(this.isBoss){
			g.drawImage(GamePanel.bossFlag,this.xpos*32,(this.ypos*32)-16,32,48,null);
		}


	}
}
