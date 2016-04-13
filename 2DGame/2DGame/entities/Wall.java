package entities;

import java.awt.Color;

public class Wall extends Entity{

	public Wall(int x, int y,int w, int h) {
		super(x, y, Color.blue);
		this.width = w;
		this.height = h;
	}

}