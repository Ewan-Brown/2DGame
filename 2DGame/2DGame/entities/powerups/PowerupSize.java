package entities.powerups;

import entities.Entity;

public class PowerupSize extends PowerupBasic{

	public PowerupSize(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onEntityCollision(){}
	
	@Override
	public Entity onPickup(Entity e){
		e.width += rand.nextInt(7) - 3;
		e.height += rand.nextInt(7) - 3;
		System.out.println(rand.nextInt(7));
		if(e.width < 2){
			e.width = 2;
		}
		if(e.height < 2){
			e.height = 2;
		}
		this.dead = true;
		return e;
	}

}
