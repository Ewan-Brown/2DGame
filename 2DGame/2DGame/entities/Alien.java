package entities;

import java.awt.Color;
import java.util.ArrayList;

import main.GameMath;

public class Alien extends EntityAI{
	double angle;
	
	public Alien(int w, int h, int x, int y) {
		super(w, h, x, y);
		this.color = Color.red;
		this.speed = 0.5;
	}
	public void updateTarget(ArrayList<Entity> targetArray){
		System.out.println(targetArray.size());
		double targets = targetArray.size();
		boolean allDead = true;
		double dist;
		double prevDist = 999999;
		for(int i = 0; i < targets;i++){
			if(targetArray.get(i).dead == false){
				allDead = false;
				dist = GameMath.getDistance(targetArray.get(i),this);
				if(dist < prevDist){
					target = targetArray.get(i);
				}
				prevDist = dist;
			}
			else{
				target = null;
			}
		}
	}
	public void moveAI(){
//		if(target == null){
//			return;
//		}
//		double deltaX = 0;
//		double deltaY = 0;
//		angle = 0;
////		angle = GameMath.getAngle(x, y, target.x,target.y);
//		angle = Math.atan2(target.y - y, target.x - x);
//		deltaX = speed * Math.cos(angle);
//		deltaY = speed * Math.sin(angle);
//		//FLIP X
//		if(target.x - x < 0){
//			deltaX = - deltaX;
//		}
//		//FLIP Y
//		if(target.y - y < 0){
//			deltaY = -deltaY;
//		}
		super.moveAI();
//		System.out.println(deltaX+" "+deltaY+" "+angle);
		this.x += deltaX;
		this.y += deltaY;
		checkWallCollision();
	}

}
