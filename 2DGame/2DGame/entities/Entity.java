package entities;

import java.awt.Color;

import main.GameMath;
import main.GamePanel.Direction;

public class Entity {

	public double x, y;
	int health;
	public int w = 10;
	public int h = 10;
	int MAX_X;
	int MAX_Y;
	public double speed;
	double l,r,u,d;
	public boolean dead;
	public Color color;
	public Entity(int width, int height, int x, int y){
		this.x = x;
		this.y = y;
		this.health = 100;
		this.MAX_X = width;
		this.MAX_Y = height;
	}
	public void onCollision(){
		this.dead = true;
	}
	public void moveEntity(double x, double y){
		if(!dead){
			if(Math.abs(x) + Math.abs(y) == 2){
				x *= GameMath.SIN_OF_45;
				y *= GameMath.SIN_OF_45;
			}
			this.x += x * speed;
			this.y -= y * speed;
		}
		//TODO may want to move this?
		this.checkWallCollision();
	}
	public void checkWallCollision(){
		l = (this.x - (this.w - 1) / 2);
		r = (this.x +(this.w - 1) / 2);
		u = (this.y - (this.h - 1) / 2);
		d = (this.y +(this.h - 1) / 2);
		if(l < 0){
			this.x = (this.w - 1) / 2;
		}
		if(r > MAX_X){
			this.x = MAX_X - (this.w - 1) / 2;
		}
		if(u < 0){
			this.y = (this.h - 1) / 2;
		}
		if(d > MAX_Y){
			this.y = MAX_Y - (this.h - 1) / 2;
		}
	}
}
