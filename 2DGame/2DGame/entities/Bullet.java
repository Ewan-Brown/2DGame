package entities;

import java.awt.Color;
import java.util.ArrayList;

import main.GameMath;

public class Bullet extends EntityAI{

	
	double deltaX;
	double deltaY;
	public Bullet(int width, int height, int x, int y, double dX, double dY) {
		super(width, height, x, y);
		this.color = Color.PINK;
		this.width = 2;
		this.height = 2;
		deltaX = dX;
		deltaY = dY;
		
	}
	
	public void moveAI(){
		this.x += deltaX;
		this.y += deltaY;
	}

}
