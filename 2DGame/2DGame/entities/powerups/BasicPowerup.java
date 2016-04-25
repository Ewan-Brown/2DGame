package entities.powerups;

import java.awt.Color;

import entities.Entity;

public class BasicPowerup extends Entity{

	public BasicPowerup(int x, int y) {
		super(x, y, Color.PINK);
	}
	public void onEntityCollision(){}
	public Entity onPickup(Entity e){
		e.health = 5;
		this.dead = true;
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
