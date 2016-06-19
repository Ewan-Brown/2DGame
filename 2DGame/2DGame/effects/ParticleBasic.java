package effects;

import java.awt.Color;

public class ParticleBasic extends Particle{
	
	public ParticleBasic(double x, double y, double speedX, double speedY){
		this(x,y,speedX,speedY,Color.RED);
	}
	public ParticleBasic(double x, double y, double speedX, double speedY,Color c){
		super(x,y,speedX,speedY,c);
	}
	@Override
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
	}
	

}
