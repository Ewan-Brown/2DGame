package entities;

import java.awt.Color;

public class Wall extends Entity{

	public Wall(double x, double y,int w, int h) {
		super(x, y, Color.blue);
		this.width = w;
		this.height = h;
	}

}