package entities;

import java.awt.Color;

public class EntityAI extends Entity{
	public Entity target;
	double angle;
	public double deltaX,deltaY;

	public EntityAI(int x, int y,Color c) {
		super(x, y,c);
		speed = 1.0;
		// TODO Auto-generated constructor stub
	}
	public void moveAI(){
		deltaX = 0;
		deltaY = 0;
		if(!dead){
			if(target == null){
				return;
			}
			angle = Math.atan2(target.y - y, target.x - x);
			deltaX = speed * Math.cos(angle);
			deltaY = speed * Math.sin(angle);
		}
	}

}
