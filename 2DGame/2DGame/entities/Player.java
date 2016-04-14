package entities;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.BitSet;

import effects.Effects;
import effects.ParticleImplode;
import main.GameMath;
import settings.ControlSet;

public class Player extends Entity{
	public boolean sprint;
	public ControlSet controls;
	int swordLength = 100;
	public Point lastClick;
	public Entity target;
	public Player(int x, int y,Color c,ControlSet controls) {
		super(x,y,c);
		this.controls = controls;
		name = "PLAYER";
		this.height = 10;
		this.width = 10;
	}
	public void moveEntity(double x, double y){
		if(sprint){
			speed *= 2;
		}
		super.moveEntity(x, y);
	}
	public ArrayList<ParticleImplode> onDeath(){
		Color c = color;
		this.deadColor();
		return Effects.implode(this.x, this.y, c);

	}
	public void updateControls(BitSet bitset){
		controls.updateKeys(bitset);
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
		if(!dead){
			if(lastClick == null){
				return null;
			}
			double speedX = 0,speedY = 0,angle = 0;
			angle = Math.atan2(lastClick.y - y, lastClick.x - x);
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

}
