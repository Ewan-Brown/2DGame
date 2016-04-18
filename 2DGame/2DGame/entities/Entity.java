package entities;

import java.awt.Color;
import java.util.ArrayList;

import effects.Particle;
import effects.ParticleExplode;
import main.GameMath;

public class Entity {

	public String name;
	public double x, y;
	int health;
	public int width = 10;
	public int height = 10;
	public double speed = 1;
	public boolean dead;
	public Color color;
	public Color baseColor;
	public Entity(int x, int y,Color c){
		name = "NULL";
		this.x = x;
		this.y = y;
		this.health = 100;
		color = c;
		baseColor = c;
	}
	public void respawn(int x, int y){
		this.x = x;
		this.y = y;
		this.dead = false;
		this.health = 100;
		this.speed = 1;
		this.color = baseColor;
	}
	public void onCollision(){
		this.dead = true;
	}
	public ArrayList<? extends Particle> onDeath(){
		return new ArrayList<Particle>();
	}
	public void deadColor(){
		Color c = this.color;
		int[] cV = new int[4];
		cV[0] = c.getRed();
		cV[1] = c.getGreen();
		cV[2] = c.getBlue();
		cV[3] = c.getAlpha() - 200;
		for(int i = 0; i < 4;i++){
			if(cV[i] < 0){
				cV[i] = 0;
			}
			if(cV[i] > 255){
				cV[i] = 255;
			}
		}
		this.color = new Color(cV[0],cV[1],cV[2],cV[3]);
		
	}
	public void moveEntity(double x, double y){
		if(!dead){
			if(Math.abs(x) + Math.abs (y) == 2){
				x *= (GameMath.SIN_OF_45);
				y *= (GameMath.SIN_OF_45);
			}
			this.x += x * speed;
			this.y -= y * speed;
		}
	}
	public  ArrayList<? extends Particle> onWallCollide(){
		return new ArrayList<Particle>();
	}

	public double getLeftSide(){
		return (this.x - (this.width - 1) / 2);
	}
	public double getRightSide(){
		return (this.x +(this.width - 1) / 2);
		
	}
	public double getUpSide(){
		return (this.y - (this.height - 1) / 2);
	}
	public double getDownSide(){
		return(this.y +(this.height - 1) / 2);
	}
}
