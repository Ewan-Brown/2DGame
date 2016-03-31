package effects;

import java.awt.Color;

public class ParticleImplode extends Particle{

	double antiX;
	double antiY;
	public ParticleImplode(double x, double y, double speedX, double speedY, Color c) {
		super(x, y, speedX, speedY, c);
		antiY = speedY / 50;
		antiX = speedX / 50;
		this.speedY += speedY / 5;
		this.speedX += speedX / 5;
	}
	public void update(){
		this.life -= 1;
		if(life < 1){
			dead = true;
			return;
		}
		//TODO HOLY CRAP THIS ONE IS COOl
//		this.speedX -= antiX + speedX / 10;
//		this.speedY -= antiY + speedX / 10;		
		this.speedX -= antiX;
		this.speedY -= antiY;	
		this.x += speedX;
		this.y += speedY;
		if(this.x > main.Main.w || this.x < 0){
			this.dead = true;
		}
		if(this.y > main.Main.h || this.y < 0){
			this.dead = true;
		}
	}

}
