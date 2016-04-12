package entities;

import java.awt.Color;

public class Wall extends Entity{

	public Wall(int width, int height, int x, int y) {
		super(width, height, x, y, Color.blue);
		this.width = 10;
		this.height = 20;
	}

}