package main;

import java.awt.Color;

public class Player extends Entity{
	boolean sprint;
	public Player(int width, int height,int x, int y,Color c) {
		super(width,height,x,y);
		this.color = c;
	}
	public void moveEntity(double x, double y){
		if(sprint){
			speed *= 2;
		}
		super.moveEntity(x, y);
	}

}
