package entities;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import effects.Effects;
import effects.Particle;
import main.GameMath;
import settings.ControlSet;

public class Player extends Entity{
	public int sprintCharge;
	public final int TOTAL_SPRINT = 1000;
	public final int MIN_SPRINT = 100;
	double shotAccuracy = 0.1;
	public boolean sprint;
	public ControlSet controls;
	int swordLength = 100;
	public Point lastLaser;
	public Point lastShot;
	public Entity target;
	int MAX_AMMUNITION = 20;
	int ammunition = 100;
	int laserTimerMax = 10;
	int laserTimer;
	public Player(int x, int y,Color c,ControlSet controls) {
		super(x,y,c);
		maxHealth = 1000;
		health = maxHealth;
		rand = new Random();
		sprintCharge = TOTAL_SPRINT;
		this.controls = controls;
		name = "PLAYER";
		this.height = 10;
		this.width = 10;
		laserTimer = laserTimerMax;
	}
	public void moveEntity(double x, double y){
		if(sprint){
//			sprintCharge -= 5;
			speed *= 2;
		}
		else{
//			sprintCharge++;
		}
		super.moveEntity(x, y);
	}
	public ArrayList<? extends Particle> onDeath(){
		Color c = color;
		this.deadColor();
		double speedX = controls.getX();
		//TODO Why does this have to be negative?
		double speedY = -controls.getY();
		ArrayList<Particle> pArray = new ArrayList<Particle>();
		pArray.addAll(Effects.shockwave(this.x, this.y,speedX,speedY, c));
//		pArray.addAll(Effects.implode(x, y, c));
		return pArray;

	}
	public void updateControls(BitSet bitset){
		controls.updateKeys(bitset);
		this.sprint = controls.sprint;
		this.speed = controls.speed;
		moveEntity(controls.getX(),controls.getY());
	}
	public Point getSwordPoint(){
		Point p;
		p = controls.getSword();
		if(!dead){
			p.x *= swordLength;
			p.y *= swordLength;
			if(Math.abs(p.x) + Math.abs(p.y) == swordLength * 2){
				p.x *= GameMath.SIN_OF_45;
				p.y *= GameMath.SIN_OF_45;
			}
			p.x += this.x;
			p.y += this.y;
		}
		else{
			p.x = (int)x;
			p.y = (int)y;
		}
		return p;
	}
	public Line2D getSwordLine(){
		Point p = this.getSwordPoint();
		if((int)p.x - (int)x == 0 && (int)p.y - (int)y == 0){
			return null;
		}
		return new Line2D.Double(p.x, p.y, x, y);
	}
	public void click(Point click,int e){
		if(e == MouseEvent.BUTTON1){
			lastShot = click;
		}
		else if(e == MouseEvent.BUTTON3){
			lastLaser = click;
		}
	}
	public ArrayList<Bullet> shoot(){
		ArrayList<Bullet> bArray = new ArrayList<Bullet>();
		if(!dead && ammunition > 0){
			if(lastShot == null){
				return null;
			}
			ammunition--;
			double speedX = 0,speedY = 0,angle = 0;
			angle = Math.atan2(lastShot.y - y, lastShot.x - x);
			angle += (rand.nextDouble() - 0.5) * 0.3;
			speedX = 2.0 * Math.cos(angle);
			speedY = 2.0 * Math.sin(angle);
			lastShot = null;
			bArray.add(new Bullet((int)x,(int)y,speedX,speedY));
		}
		else{
			bArray = null;
		}
		return bArray;
	}
	public Laser laser(){
		laserTimer--;
		if(!dead && laserTimer < 1 && ammunition >= 5){
			if(lastLaser == null){
				return null;
			}
			ammunition -= 5;
			laserTimer = laserTimerMax;
			Laser l = new Laser(x, y, lastLaser.getX(), lastLaser.getY());
			lastLaser = null;
			return l;
		}
		return null;
	}
	public void respawn(int x, int y){
		super.respawn(x, y);
		sprintCharge = TOTAL_SPRINT;
		this.ammunition = 100;
	}

}
