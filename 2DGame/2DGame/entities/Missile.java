package entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import effects.Effects;
import effects.Particle;
import main.GameMath;

public class Missile extends EntityAI{

	int MAX_EXPLODE_TIMER = 700;
	int explodeTimer = MAX_EXPLODE_TIMER;
	int bullets = 50000;
	double dirAngle;
	public Missile(double x, double y) {
		super(x, y, Color.RED);
		deltaX = 0;
		deltaY = 0;
		width = 5;
		height = 5;
		speed = .8;
		targetAngle = 0;
		dirAngle = targetAngle;
	}
	public ArrayList<Bullet> explode(){
		ArrayList<Bullet> bArray = new ArrayList<Bullet>();
		Random rand = new Random();
		for(int i = 0; i < bullets; i++){
			bArray.add(new Bullet(x,y,(rand.nextDouble() - 0.5) * 10 ,(rand.nextDouble() - 0.5) * 10,this,20));
		}
		return bArray;
	}
	public void updateTarget(ArrayList<? extends Entity> friendlyArray) {
		double dist;
		double prevDist = 9999;
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
	public ArrayList<? extends Particle> onDeath(){
		this.deadColor();
		return new ArrayList<Particle>();
	}
	public void moveAI(){
		targetAngle = Math.atan2(target.y - y, target.x - x);
		double diff = dirAngle - targetAngle;
		diff = (diff + 3 * Math.PI) %(2 * Math.PI) - Math.PI;
		dirAngle -= diff / 20;
		deltaX = speed * Math.cos(dirAngle);
		deltaY = speed * Math.sin(dirAngle);
		explodeTimer--;
		if(explodeTimer < 0){
			this.dead = true;
			this.onDeath();
		}
		x += deltaX;
		y += deltaY;

	}
}
