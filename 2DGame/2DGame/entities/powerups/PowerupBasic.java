package entities.powerups;

import java.awt.Color;
import java.util.Random;

import entities.Entity;

public class PowerupBasic extends Entity{
	
	public static Random rand = new Random();

	public PowerupBasic(int x, int y) {
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
