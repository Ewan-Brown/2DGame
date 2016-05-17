package entities;

import java.awt.Color;

public class EntityAI extends Entity{
	public Entity target;
	double targetAngle;
	public double deltaX,deltaY;

	public EntityAI(double x, double y,Color c) {
		super(x, y,c);
		speed = 1.0;
		// TODO Auto-generated constructor stub
	}
	public void moveAI(){
		if(!dead){
			if(target == null){
				return;
			}
			targetAngle = Math.atan2(target.y - y, target.x - x);
			deltaX = speed * Math.cos(targetAngle);
			deltaY = speed * Math.sin(targetAngle);
		}
	}

}
