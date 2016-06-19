package entities.powerups;

import java.awt.Color;

import entities.Entity;

public class BasicPowerup extends Entity{

	public BasicPowerup(int x, int y) {
		super(x, y, Color.PINK);
	}
	@Override
	public void onEntityCollision(){}
	public Entity onPickup(Entity e){
		e.color = this.color;
		this.dead = true;
		return e;
	}

}
