package entities;

import java.awt.Color;
import java.util.ArrayList;

import effects.Effects;
import effects.ParticleImplode;

public class Player extends Entity{
	public boolean sprint;
	public Player(int width, int height,int x, int y,Color c) {
		super(width,height,x,y);
		name = "PLAYER";
		this.color = c;
	}
	public void moveEntity(double x, double y){
		if(sprint){
			speed *= 2;
		}
		super.moveEntity(x, y);
	}
	public ArrayList<ParticleImplode> onDeath(){
		return Effects.implode(this.x, this.y, this.color);

	}

}
