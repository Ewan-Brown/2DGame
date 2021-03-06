package entities;

import java.awt.Color;
import java.util.ArrayList;
import effects.Effects;
import effects.Particle;
import main.GameMath;

public class Alien extends EntityAI {
	double angle;
	int COOLDOWN = 50;
	int cooldownTimer = 200;
	final double SHOTGUN_CHOKE = 0.08;
	double shotAccuracy = 0.1;

	public Alien(double x, double y) {
		super(x, y,Color.RED);
		this.width = 10;
		this.height = 10;
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
		if(target != null){
			double speedX,speedY;
			angle = Math.atan2(target.y - y, target.x - x) + ((rand.nextDouble() - 0.5) * shotAccuracy);
			speedX = 2.0 * Math.cos(angle);
			speedY = 2.0 * Math.sin(angle);
			return new Bullet((int)x,(int)y,speedX,speedY,this);
		}
		return null;
	}
	public ArrayList<Bullet> shotgun(){
		ArrayList<Bullet> bArray = new ArrayList<Bullet>();
		double speedX,speedY;
		angle = Math.atan2(target.y - y, target.x - x) + ((rand.nextDouble() - 0.5) * shotAccuracy);
		angle -= SHOTGUN_CHOKE;
		for(int i = 0; i < 3;i++){
			speedX = 2.0 * Math.cos(angle);
			speedY = 2.0 * Math.sin(angle);
			bArray.add(new Bullet((int)x,(int)y,speedX,speedY,this));
			angle += SHOTGUN_CHOKE;
		}
		return bArray;
	}
	@Override
	public ArrayList<? extends Particle> onDeath(){
		Color c = color;
		this.deadColor();
		return Effects.flip(x, y,c);
	}
	@Override
	public void moveAI() {
		super.moveAI();
		this.x += deltaX;
		this.y += deltaY;
	}
	@Override
	public void onEntityCollision(){
		this.dead = true;
	}

}
