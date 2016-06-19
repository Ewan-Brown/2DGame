package effects;

import java.awt.Color;


public class ParticleFlip extends ParticleImplode{

	public ParticleFlip(double x, double y, double speedX, double speedY, Color c) {
		super(x, y, speedX, speedY, c);
	}
	@Override
	public void update(){
		this.life -= 1;
		if(life < 1){
			dead = true;
			return;
		}
	
		this.speedX -= antiX + speedX / 100;
		this.speedY -= antiY + speedX / 100;	
		this.x += speedX;
		this.y += speedY;
	}

}
