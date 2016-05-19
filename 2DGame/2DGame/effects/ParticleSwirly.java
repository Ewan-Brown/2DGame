package effects;

import java.awt.Color;

public class ParticleSwirly extends Particle{
	//go in a direction for 2-3 pixels then randomly change using RAND
	//speed is always -1, 0 or 1
	boolean b;
	double baseSpeedX;
	double baseSpeedY;
	public ParticleSwirly(double x, double y, double speedX, double speedY, Color c) {
		super(x, y, speedX, speedY, c);
		baseSpeedX = speedX;
		baseSpeedY = speedY;
	}
	public void update(){
		b = (Math.random() < 0.5) ? true : false;
		if(speedX != 0){
			speedX = 0.0;
			speedY = 1.0;
			if(b){
				speedY = -speedY;
			}
		}
		else{
			speedY = 0.0;
			speedX = 1.0;
			if(b){
				speedX = -speedX;
			}
		}
		//TODO CLEAR THIS DUPLICATED CODE
		this.baseSpeedX -= baseSpeedX / 100;
		this.baseSpeedY -=baseSpeedY / 100;
		if(Math.abs(baseSpeedX) < 0.2 && Math.abs(baseSpeedY) < 0.2){
			this.dead = true;
		}		
		x += speedX + baseSpeedX;
		y += speedY + baseSpeedY;
		
	}

}
