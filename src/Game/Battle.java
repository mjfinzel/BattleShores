package Game;

import java.awt.Graphics;



public class Battle {
	Unit unit;
	Unit target;
	int battleState = 0;
	Unit winner;
	Unit loser;
	public Battle(Unit u1, Unit u2){
		unit = u1;
		target = u2;
		
		
		if(unit.currentHealth==0){
			winner = target;
			loser = unit;
		}
		else if(target.currentHealth==0){
			winner = unit;
			loser = target;
		}
		
	}
	public void update(){//advance to the next stage of the battle
		
	}
	public int calculateXP(){
		if(unit.currentHealth==0){
			winner = target;
			loser = unit;
		}
		else if(target.currentHealth==0){
			winner = unit;
			loser = target;
		}
		double winnerTotal = winner.Speed+winner.Stamina+winner.Strength+winner.Magic+winner.armour+winner.resistance;
		double loserTotal = loser.Speed+loser.Stamina+loser.Strength+loser.Magic+loser.armour+loser.resistance;
		return (int)(((double)(loserTotal/winnerTotal))*100);
	}
	public void Draw(Graphics g){
		g.drawImage(GamePanel.battleFade, 0, 0,1920, 1080, null);
		//System.out.println("drawing battle");
		if(winner!=null&&GamePanel.battle){
			//draw the unit's experience bar if it is still gaining experience
			if(winner.incomingXP>0){
				g.drawImage(GamePanel.experienceBarBackground, 856, 526,208, 28, null);
				for(int i = 0; i<winner.currentXP;i++){
					g.drawImage(GamePanel.xpBar, 860+(2*i), 530,2, 20, null);
				}
			}
			
		}
		if(GamePanel.battle){
			//draw the unit's hp
			for(int i = 0; i<100;i++){
				if(i<=(unit.currentHealth/unit.maxHealth)*100){
					if(unit.team==0)
						g.drawImage(GamePanel.healthBar, 974+(i), 583,1, 20, null);
					else
						g.drawImage(GamePanel.healthBar, 846+(i), 583,1, 20, null);
				}
			}
			//draw the target's hp
			for(int i = 0; i<100;i++){
				if(i<=(target.currentHealth/target.maxHealth)*100){
					if(target.team==1)
						g.drawImage(GamePanel.healthBar, 846+(i), 583,1, 20, null);
					else
						g.drawImage(GamePanel.healthBar, 974+(i), 583,1, 20, null);
				}
			}
			//draw the unit's battle animation
			if(unit.battleAnimation!=null){
				unit.battleAnimation.Draw(g);
			}
			//draw the target's battle animation
			if(target.battleAnimation!=null){
				target.battleAnimation.Draw(g);
			}
			//draw the unit's death speech
			if(unit.deathSpeech.speechState>=0){
				unit.deathSpeech.Draw(g);
			}
			//draw the target's death speech
			if(target.deathSpeech.speechState>=0){
				target.deathSpeech.Draw(g);
			}
		}
	}
}
