package entities.powerups;

import java.awt.Color;

import entities.Entity;

public class BasicPowerup extends Entity{

	public BasicPowerup(int x, int y, Color c) {
		super(x, y, c);
	}
	public void onEntityCollision(){}
	public Entity onPickup(Entity e){
		e.color = Color.BLUE;
		return e;
	}
	public boolean onBulletHit(){
		//TODO variable/constant for bullet damage
		this.health -= 10;
		if(health < 1){
			this.dead = true;
			return true;
		}
		return false;
	}
	public boolean onMeleeHit(){
		//TODO variable/constant for melee damage
				this.health -= 50;
				if(health < 1){
					this.dead = true;
					return true;
				}
				return false;
	}

}
