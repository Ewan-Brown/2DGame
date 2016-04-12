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
	final double SHOTGUN_CHOKE = 0.08;
	final double SHOT_MULT = 0.1;

	public Alien(int w, int h, int x, int y) {
		super(w, h, x, y,Color.RED);
		rand = new Random();
		this.speed = 1;
	}
	public void updateTarget(ArrayList<Entity> friendlyArray) {
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
	public double distToTarget(){
		return GameMath.getDistance(this, target);
	}
	public ArrayList<Bullet> tryShoot(){
		ArrayList<Bullet> bArray = new ArrayList<Bullet>();
		if(!dead){
			cooldownTimer--;
			if(cooldownTimer < 1){
				cooldownTimer = COOLDOWN;
				bArray.add(shoot());
			}
		}
		return bArray;
	}
	public Bullet shoot(){
		double speedX,speedY;
		angle = Math.atan2(target.y - y, target.x - x) + ((rand.nextDouble() - 0.5) * SHOT_MULT);
		speedX = 2.0 * Math.cos(angle);
		speedY = 2.0 * Math.sin(angle);
		return new Bullet(MAX_X,MAX_Y,(int)x,(int)y,speedX,speedY);
//		return null;
	}
	public ArrayList<Bullet> shotgun(){
		ArrayList<Bullet> bArray = new ArrayList<Bullet>();
		double speedX,speedY;
		angle = Math.atan2(target.y - y, target.x - x) + ((rand.nextDouble() - 0.5) * SHOT_MULT);
		angle -= SHOTGUN_CHOKE;
		for(int i = 0; i < 3;i++){
			speedX = 2.0 * Math.cos(angle);
			speedY = 2.0 * Math.sin(angle);
			bArray.add(new Bullet(MAX_X,MAX_Y,(int)x,(int)y,speedX,speedY));
			angle += SHOTGUN_CHOKE;
		}
		return bArray;
	}
	public Bullet shootSmart(){
		double speedX,speedY;
		double tX,tY;
		tY = target.y;
		tX = target.x;
		if(target instanceof Target){
			tY += ((Target)target).deltaY * 30;
			tX += ((Target) target).deltaY * 30;
		}
		angle = Math.atan2(tY - y, tX - x) + ((rand.nextDouble() - 0.5) * SHOT_MULT);
		speedX = 2.0 * Math.cos(angle);
		speedY = 2.0 * Math.sin(angle);
		return new Bullet(MAX_X,MAX_Y,(int)x,(int)y,speedX,speedY);
	}
	public ArrayList<? extends Particle> onDeath(){
		Color c = color;
		this.deadColor();
		return Effects.flip(x, y,c);
	}
	public void moveAI() {
		super.moveAI();
		this.x += deltaX;
		this.y += deltaY;
	}
	public void onCollision(){
		this.dead = true;
	}

}
