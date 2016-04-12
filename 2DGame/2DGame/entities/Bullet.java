package entities;

import java.awt.Color;

public class Bullet extends EntityAI{

	
	double deltaX;
	double deltaY;
	public Bullet(int width, int height, int x, int y, double dX, double dY) {
		super(width, height, x, y,Color.PINK);
		this.color = Color.PINK;
		this.width = 3;
		this.height = 3;
		deltaX = dX;
		deltaY = dY;
		//Prevents shooter from being hit by own bullets!
		this.x += 5 *deltaX;
		this.y += 5 *deltaY;
		
	}
	
	public void moveAI(){
		this.x += deltaX;
		this.y += deltaY;
		if(this.x > main.Main.w || this.x < 0){
			this.dead = true;
		}
		if(this.y > main.Main.h || this.y < 0){
			this.dead = true;
		}
	}
	public void onCollision(){
		
	}

}
