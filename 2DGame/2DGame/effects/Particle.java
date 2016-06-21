package effects;

import java.awt.Color;

public class Particle {
	
	public Color color;
	protected double x,y;
	double speedX,speedY;
	int life = 1000;
	public boolean dead = false;
	public Particle(double x, double y,double speedX,double speedY,Color c){
		color = c;
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
	}
	public void update(){
		
	}
	public double getX(){return x;}
	public double getY(){return y;}

}
