package effects;

import java.awt.Color;

public class Particle {
	
	public Color color;
	public double x,y;
	double speedX,speedY;
	int life = 1000;
	public boolean dead = false;
	public Particle(double x, double y, double speedX, double speedY){
		this(x,y,speedX,speedY,Color.RED);
	}
	public Particle(double x, double y, double speedX, double speedY,Color c){
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
		this.color = c;
	}
	public void update(){
		this.life -= 1;
		if(life < 1){
			dead = true;
			return;
		}
		if(Math.abs(speedX) < 0.2 && Math.abs(speedY) < 0.2){
			this.dead = true;
		}
		
		this.x += speedX;
		this.y += speedY;
		this.speedX -= speedX / 100;
		this.speedY -= speedY / 100;
		if(this.x > main.Main.w || this.x < 0){
			this.dead = true;
		}
		if(this.y > main.Main.h || this.y < 0){
			this.dead = true;
		}
	}
	

}
