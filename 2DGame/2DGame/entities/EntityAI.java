package entities;

public class EntityAI extends Entity{
	Entity target;
	double angle;
	double deltaX,deltaY;

	public EntityAI(int width, int height, int x, int y) {
		super(width, height, x, y);
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
