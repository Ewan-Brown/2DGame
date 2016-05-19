package entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class LandMine extends Entity{
	
	int timer = 300;
	int bullets = 2000;

	public LandMine(double x, double y) {
		super(x, y,Color.MAGENTA);
	}
	public void tick(){
		timer--;
		if(timer < 1){
			this.dead = true;
		}
	}
	public ArrayList<Bullet> explode(){
		//TODO add code so the total bullet speed is always the same
		ArrayList<Bullet> p = new ArrayList<Bullet>();
		Random rand = new Random();
		for(int i = 0; i < bullets; i++){
			p.add(new Bullet((int)x,(int)y,(rand.nextDouble() - 0.5) * 10 ,(rand.nextDouble() - 0.5) * 10,this));
		}

		return p;
	}
}
