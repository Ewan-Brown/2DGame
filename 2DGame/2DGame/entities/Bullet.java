package entities;

import java.awt.Color;
import java.util.ArrayList;

import effects.Effects;
import effects.Particle;
import effects.ParticleBasic;

public class Bullet extends EntityAI{

	double deltaX;
	double deltaY;
	public Bullet(int x, int y, double dX, double dY) {
		super(x, y,Color.PINK);
		this.color = Color.PINK;
		this.width = 3;
		this.height = 3;
		deltaX = dX;
		deltaY = dY;
		//Prevents shooter from being hit by own bullets!
		this.x += 5 *deltaX;
		this.y += 5 *deltaY;
		
	}
	
	public void moveAI(){
		this.x += deltaX;
		this.y += deltaY;
	}
	public  ArrayList<? extends ParticleBasic> onWallCollide(){
		this.dead = true;
		return Effects.explode(x, y, color, 3);
	}
	public ArrayList<? extends Particle> onDeath(){
		return new ArrayList<ParticleBasic>();
	}

}
