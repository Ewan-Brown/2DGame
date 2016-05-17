package entities;

import java.awt.Color;
import java.util.ArrayList;

import effects.Effects;
import effects.Particle;
import main.GameMath;

public class Target extends EntityAI{
	boolean sX,sY;
	public Target(double x, double y) {
		super(x, y,Color.white);
	}
	public void updateTarget(ArrayList<Alien> alienArray) {
		double targets = alienArray.size();
		double dist;
		double prevDist = 9999;
		for(int i = 0; i < targets; i++){
			if(alienArray.get(i).dead == false){
				dist = GameMath.getDistance(this, alienArray.get(i));
				if(dist < prevDist){
					target = alienArray.get(i);
				}
				prevDist = dist;
			}
		}
	}
	public ArrayList<? extends Particle> onDeath(){
		Color c = color;
		this.deadColor();
		return Effects.swirlyParticle(this.x, this.y, c);

	}
	public void moveAI(){
		super.moveAI();
		this.x -= deltaX;
		this.y -= deltaY;
	}

}
