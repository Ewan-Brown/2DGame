package entities;

import java.awt.Color;
import java.util.ArrayList;

public class Bullet extends EntityAI{

	double deltaX;
	double deltaY;
	public Bullet(int x, int y, double dX, double dY) {
		super(x, y,Color.PINK);
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
	}
	public ArrayList<Bullet> clusterBomb(){
		ArrayList<Bullet> bArray = new ArrayList<Bullet>();
		angle = Math.acos(deltaX / 2);
		deltaX = 2.0 * Math.cos(angle);
		deltaY = 2.0 * Math.sin(angle);
		return bArray;
	}
	public void onWallCollide(){
		this.dead = true;
	}

}
