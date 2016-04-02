package entities;

import java.awt.Color;
import java.util.ArrayList;

import main.GameMath;

public class Alien extends EntityAI {
	double angle;
	int cooldown = 100;

	public Alien(int w, int h, int x, int y) {
		super(w, h, x, y);
		this.color = Color.red;
		this.speed = 1;
	}

	public void updateTarget(ArrayList<Entity> friendlyArray) {
		double targets = friendlyArray.size();
		double dist;
		double prevDist = 9999;
		for(int i = 0; i < targets; i++){
			if(friendlyArray.get(i).dead == false){
				dist = GameMath.getDistance(this, friendlyArray.get(i));
				if(dist < prevDist){
					target = friendlyArray.get(i);
				}
				prevDist = dist;
			}
		}
	}
	public Bullet tryShoot(){
		cooldown--;
		Bullet b = null;
		if(cooldown < 1){
			System.out.println("HEY");
			cooldown = 120;
			double speedX,speedY;
			angle = Math.atan2(target.y - y, target.x - x);
			speedX = 2.5 * Math.cos(angle);
			speedY = 2.5 * Math.sin(angle);
			b = new Bullet(MAX_X,MAX_Y,(int)x,(int)y,speedX,speedY);
		}
		return b;
	}
	public void moveAI() {
		super.moveAI();
		// System.out.println(deltaX+" "+deltaY+" "+angle);
		this.x += deltaX;
		this.y += deltaY;
		checkWallCollision();
	}

}
