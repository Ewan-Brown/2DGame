package entities;

import java.awt.Color;
import java.awt.geom.Line2D;

public class Laser extends Entity{

	double X1,X2,Y1,Y2;
	
	public Laser(double x1, double y1, double x2, double y2) {
		super(0, 0, Color.green);
		maxHealth = 100;
		health = maxHealth;
		X1 = x1;
		X2 = x2;
		Y1 = y1;
		Y2 = y2;
	}
	public Line2D getLine(){
		return new Line2D.Double(X1, Y1, X2, Y2);
	}
	public void setLine(double x1, double x2, double y1, double y2){
		X1 = x1;
		X2 = x2;
		Y1 = y1;
		Y2 = y2;
	}
	public void setLine(Line2D line){
		X1 = line.getX1();
		X2 = line.getX2();
		Y1 = line.getY1();
		Y2 = line.getY2();
	}
	public void update(){
		health--;
		double a;
		a = ((double)health / (double)maxHealth) * 255;
		if(a < 1){
			a = 0;
		}
		if(a > 255){
			a = 255;
		}
		color = new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)a);
		if(health < 1){
			this.dead = true;
		}
	}
}
