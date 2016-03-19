package main;

import java.awt.Color;

import main.GamePanel.Direction;

public class Alien extends Entity{
	Entity target;
	double dist, dist2;
	double angle;
	
	public Alien(int w, int h, int x, int y) {
		super(w, h, x, y);
		this.color = Color.red;
		this.speed = 0.5;
	}
	public void updateTarget(Player player1, Player player2){
		dist = GameMath.getDistance(player1.x, player1.y, this.x, this.y);
		dist2 = GameMath.getDistance(player2.x, player2.y, this.x, this.y);
		if(player1.dead == false && player2.dead == false){
			if(dist < dist2){
				target = player1;
			}
			else{
				target = player2;
			}
		}
		else if(player1.dead == true && player2.dead == false){
			target = player2;
		}
		else if(player1.dead == false && player2.dead == true){
			target = player1;
		}
		else{
			target = null;
		}
	}
	public void moveAlien(){
		if(target == null){
			return;
		}
		double deltaX = 0;
		double deltaY = 0;
		angle = 0;
		angle = GameMath.getAngle(x, y, target.x,target.y);
		angle = Math.toRadians(angle);
		deltaX = 1 * Math.sin(angle);
		deltaY = 1 * Math.cos(angle);
		//FLIP X
		if(target.x - x < 0){
			deltaX = - deltaX;
		}
		//FLIP Y
		if(target.y - y < 0){
			deltaY = -deltaY;
		}
		this.x += deltaX;
		this.y += deltaY;
		checkWallCollision();
	}

}
