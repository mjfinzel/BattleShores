package Game;

public class Item {
	int itemID;
	String name = "";
	String description = "";
	int attackDmg = -1;
	int selfHealPower = -1;
	int currentDurability = -1;
	int durability = -1;
	public int minWeaponRange = 0;
	public int maxWeaponRange = 0;
	public boolean isWeapon = false;
	public boolean isSpell = false;
	public boolean isPotion = false;
	public boolean canDrop = false;
	
	public int axeLevelReq = 0;
	public int swordLevelReq = 0;
	public int spearLevelReq = 0;
	public int lifeMagicLevelReq = 0;
	public int bloodMagicLevelReq = 0;
	public int assistMagicLevelReq = 0;
	public int bowLevelReq = 0;
	
	public Item(int ID){
		itemID = ID;
		if(itemID == 0){
			this.name = "Blunt Shortsword";
			this.description = "A weak sword with low durability.";
			this.attackDmg = 5;
			this.durability = 30;
			this.currentDurability = durability;
			this.isWeapon = true;
			this.minWeaponRange = 1;
			this.maxWeaponRange = 1;
			this.swordLevelReq = 1;
		}
		else if(itemID == 1){
			this.name = "Herb";
			this.description = "A basic herb with healing properties.";
			this.selfHealPower = 10;
			this.durability = 3;
			this.currentDurability = durability;
			this.isPotion = true;
		}
		else if(itemID == 2){
			this.name = "Weak Short Bow";
			this.description = "A weak bow with low durability.";
			this.attackDmg = 3;
			this.durability = 20;
			this.currentDurability = durability;
			this.isWeapon = true;
			this.minWeaponRange = 2;
			this.maxWeaponRange = 2;
			this.bowLevelReq = 1;
		}
	}
	public void Use(Unit user){
		if(this.itemID==1){
			if(user.currentHealth+this.selfHealPower<user.maxHealth){
			user.currentHealth+=this.selfHealPower;
			}
			else{
				user.currentHealth = user.maxHealth;
			}
			this.durability--;
		}
	}
	public void loseDurability(){
		if(this.currentDurability>0){
			this.currentDurability--;
		}
	}
}
