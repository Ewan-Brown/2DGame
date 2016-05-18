package entities;

import java.awt.Color;
import java.util.ArrayList;

import effects.Effects;
import effects.Particle;
import effects.ParticleBasic;

public class Bullet extends EntityAI{

	double deltaX;
	double deltaY;
	Entity owner;
	boolean pierce = true;
	public Bullet(double x, double y, double dX, double dY, Entity e) {
		super(x, y,Color.PINK);
		owner = e;
		this.color = Color.PINK;
		this.width = 3;
		this.height = 3;
		deltaX = dX;
		deltaY = dY;
		//Prevents shooter from being hit by own bullets!
		this.x += 5 *deltaX;
		this.y += 5 *deltaY;
		
	}
	public Bullet(double x, double y, double dX, double dY, Entity e, int Damage){
		this(x,y,dX,dY,e);
		damage = Damage;
	}
	
	public void moveAI(){
		this.x += deltaX;
		this.y += deltaY;
	}
	public  ArrayList<? extends ParticleBasic> onWallCollide(){
		this.dead = true;
		return Effects.explode(x, y, Color.RED, 3);
	}
	public Entity attackEntity(Entity e){
		
		if(!pierce){
			this.dead = true;
		}
		if(!(e.getClass() == owner.getClass())){
			super.attackEntity(e);
		}
		return e;
	}
	public ArrayList<? extends Particle> onDeath(){
		return new ArrayList<ParticleBasic>();
	}

}
