package effects;

import java.awt.Color;

public class ParticleSpiral extends Particle{

	
	double deltaX = 0.1;
	double deltaY = 0.1;
 ParticleSpiral(double x, double y, double speedX, double speedY, Color c) {
		super(x, y, speedX, speedY, c);
		this.speedX = 0;
		this.speedY = 0;
	}
	public void update(){
		//TODO DOESN'T WORK AT ALL
		if(speedX > 1 || speedX < -1){
			deltaX = -deltaX;
		}
		if(speedY > 1 || speedY < -1){
			deltaY = -deltaY;
		}
		speedX += deltaX;
		speedY += deltaY;
		this.x += speedX;
		this.y += speedY;
	}

}
