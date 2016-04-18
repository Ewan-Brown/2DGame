package effects;

import java.awt.Color;

public class ParticleExplode extends Particle{
	
	public ParticleExplode(double x, double y, double speedX, double speedY){
		this(x,y,speedX,speedY,Color.RED);
	}
	public ParticleExplode(double x, double y, double speedX, double speedY,Color c){
		super(x,y,speedX,speedY,c);
	}
	public void update(){
		this.life -= 1;
		if(life < 1){
			dead = true;
			return;
		}
		if(Math.abs(speedX) + Math.abs(speedY) < 0.5){
			this.dead = true;
		}
		
		this.x += speedX;
		this.y += speedY;
		this.speedX -= speedX / 100;
		this.speedY -= speedY / 100;
		//TODO MOVE CHECK-DEAD STUFF TO MAKE IT OOP-ABLE
		if(this.x > main.Main.w || this.x < 0){
			this.dead = true;
		}
		if(this.y > main.Main.h || this.y < 0){
			this.dead = true;
		}
	}
	

}
