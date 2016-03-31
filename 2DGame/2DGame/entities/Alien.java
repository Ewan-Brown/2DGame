package entities;

import java.awt.Color;
import java.util.ArrayList;

import main.GameMath;

public class Alien extends EntityAI {
	double angle;

	public Alien(int w, int h, int x, int y) {
		super(w, h, x, y);
		this.color = Color.red;
		this.speed = 0.5;
	}

	public void updateTarget(ArrayList<Entity> targetArray) {
		double targets = targetArray.size();
		double dist;
		double prevDist = 9999;
		for(int i = 0; i < targets; i++){
			if(targetArray.get(i).dead == false){
				dist = GameMath.getDistance(this, targetArray.get(i));
				if(dist < prevDist){
					target = targetArray.get(i);
				}
				prevDist = dist;
			}
		}
	}

	public void moveAI() {
		super.moveAI();
		// System.out.println(deltaX+" "+deltaY+" "+angle);
		this.x += deltaX;
		this.y += deltaY;
		checkWallCollision();
	}

}
