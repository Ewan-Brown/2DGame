package entities;

import java.awt.Color;
import java.awt.Point;
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
	public Point lastClick;
	public Entity target;
	int MAX_AMMUNITION = 20;
	int ammunition = 20;
	Random rand;
	public Player(int x, int y,Color c,ControlSet controls) {
		super(x,y,c);
		maxHealth = 1000;
		rand = new Random();
		sprintCharge = TOTAL_SPRINT;
		this.controls = controls;
		name = "PLAYER";
		this.height = 10;
		this.width = 10;
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
		return new Line2D.Double(p.x, p.y, x, y);
	}
	public void click(Point click){
		this.lastClick = click;
	}
	public ArrayList<Bullet> shoot(){
		ArrayList<Bullet> bArray = new ArrayList<Bullet>();
		if(!dead && ammunition > 0){
			if(lastClick == null){
				return null;
			}
			ammunition--;
			double speedX = 0,speedY = 0,angle = 0;
			angle = Math.atan2(lastClick.y - y, lastClick.x - x);
			angle += (rand.nextDouble() - 0.5) * 0.3;
			speedX = 2.0 * Math.cos(angle);
			speedY = 2.0 * Math.sin(angle);
			lastClick = null;
			bArray.add(new Bullet((int)x,(int)y,speedX,speedY));
		}
		else{
			bArray = null;
		}
		return bArray;
	}
	public void respawn(int x, int y){
		super.respawn(x, y);
		sprintCharge = TOTAL_SPRINT;
		this.ammunition = 20;
	}

}
