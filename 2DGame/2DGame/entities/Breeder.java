 package entities;

import java.awt.Color;
import java.util.ArrayList;

import main.GameMath;

public class Breeder extends EntityAI{
	
	double baseAngle = 0;
	int spawnTimer = 0;
	int MAX_SPAWN_TIMER = 200;
	
	public Breeder(double x, double y) {
		super(x, y, Color.ORANGE);
		speed = 0.5;
		baseAngle = ((rand.nextDouble() - 0.5) * 2) * Math.PI;

	}
	public void updateTarget(ArrayList<? extends Entity> friendlyArray) {
		double dist;
		double prevDist = Double.POSITIVE_INFINITY;
		for(int i = 0; i < friendlyArray.size(); i++){
			if(friendlyArray.get(i).dead == false){
				dist = GameMath.getDistance(this, friendlyArray.get(i));
				if(dist < prevDist){
					target = friendlyArray.get(i);
					prevDist = dist;
				}
			}
		}
	}
	public Alien spawnAlien(){
		spawnTimer--;
		if(spawnTimer < 1 && !this.dead){
			spawnTimer = MAX_SPAWN_TIMER;
			return new Alien((int)x, (int)y);
		}
		else{
			return null;
		}
	}
	public void moveAI(){
		baseAngle += ((rand.nextDouble() - 0.5) * 2) * Math.PI * 0.07;
		if(baseAngle > Math.PI / 2){
			baseAngle = Math.PI / 2;
		}
		if(baseAngle < -Math.PI / 2){
			baseAngle = -Math.PI / 2;
		}

		deltaX = 0;
		deltaY = 0;
		if(!dead){
			if(target == null){
				return;
			}
			targetAngle = Math.atan2(target.y - y, target.x - x);
			targetAngle =+ baseAngle;
			if(targetAngle > Math.PI){
				targetAngle = Math.PI;
			}
			if(targetAngle < -Math.PI){
				targetAngle = -Math.PI;
			}
			deltaX = speed * Math.cos(targetAngle);
			deltaY = speed * Math.sin(targetAngle); 
		}
		this.x -= deltaX;
		this.y -= deltaY;
	}

}
