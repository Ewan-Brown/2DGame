package entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import effects.Effects;
import effects.Particle;
import main.GameMath;

public class Alien extends EntityAI {
	double angle;
	int COOLDOWN = 50;
	int cooldownTimer = 250;
	Random rand;

	public Alien(int w, int h, int x, int y) {
		super(w, h, x, y);
		rand = new Random();
		this.color = Color.red;
		this.speed = 1;
	}
	//TODO ALIEN LOCKS ONTO TARGET ENTITIES AND WONT CHANGE?
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
	public ArrayList<Bullet> tryShoot(){
		ArrayList<Bullet> bArray = new ArrayList<Bullet>();
		if(!dead){
			cooldownTimer--;
			if(cooldownTimer < 1){
				cooldownTimer = COOLDOWN;
				double speedX,speedY;
				angle = Math.atan2(target.y - y, target.x - x) + ((rand.nextDouble() - 0.5) * 0.1);
				speedX = 2.0 * Math.cos(angle);
				speedY = 2.0 * Math.sin(angle);
				bArray.add(new Bullet(MAX_X,MAX_Y,(int)x,(int)y,speedX,speedY));
			}
		}
		return bArray;
	}
	public ArrayList<Bullet> burstShots(){
		ArrayList<Bullet> bArray = null;
		
		return bArray;
	}
	public ArrayList<? extends Particle> onDeath(){
//		return Effects.fireworks(Effects.explode(x, y, this.color));
		return Effects.implode(x, y, this.color);
	}
	public void moveAI() {
		super.moveAI();
		this.x += deltaX;
		this.y += deltaY;
		checkWallCollision();
	}

}
