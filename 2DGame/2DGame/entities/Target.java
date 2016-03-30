package entities;

public class Target extends Alien{
	boolean sX,sY;
	public Target(int width, int height, int x, int y) {
		super(width, height, x, y);
	}
	public void moveAI(){
		//TODO duplicated code! 
		//TODO add AI for if hit wall to move across it 
		super.moveAI();
		if(sX){
			deltaX = 0;
			if(deltaY < 0){
				deltaY = -speed;
			}
			else if(deltaY < 0){
				deltaY = speed;
			}
		}
		if(sY){
			deltaY = 0;
			if(deltaX < 0){
				deltaX = -speed;
			}
			else if(deltaX < 0){
				deltaX = speed;
			}
			
		}
		this.x -= deltaX;
		this.y -= deltaY;
		checkWallCollision();
	}
	public void checkWallCollision(){
		l = (this.x - (this.w - 1) / 2);
		r = (this.x +(this.w - 1) / 2);
		u = (this.y - (this.h - 1) / 2);
		d = (this.y +(this.h - 1) / 2);
		if(l < 0){
			this.x = (this.w - 1) / 2;
			sX = true;
		}
		if(r > MAX_X){
			this.x = MAX_X - (this.w - 1) / 2;
			sX = true;
		}
		if(u < 0){
			this.y = (this.h - 1) / 2;
			sY = true;
		}
		if(d > MAX_Y){
			this.y = MAX_Y - (this.h - 1) / 2;
			sY = true;
		}
	}

}
