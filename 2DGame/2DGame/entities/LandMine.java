package entities;

import java.util.ArrayList;
import java.util.Random;

public class LandMine extends Entity{
	
	int timer = 300;
	int bullets = 15;

	public LandMine(int width, int height, int x, int y) {
		super(width, height, x, y);
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
			p.add(new Bullet(MAX_X,MAX_Y,(int)x,(int)y,(rand.nextDouble() - 0.5) * 4,(rand.nextDouble() - 0.5) * 4));
		}

		return p;
	}
}