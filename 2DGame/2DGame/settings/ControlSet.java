package settings;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.BitSet;

import main.GameMath;

public class ControlSet {
	
	int upKey,leftKey,downKey,rightKey;
	int sprintKey;
	int fasterKey,slowerKey;
	public double speed = 1;
	int cooldown = 20;
	final int BASE_COOL = 20;
	int shootUpKey,shootLeftKey,shootDownKey,shootRightKey;
	int targetKey = KeyEvent.VK_TAB;
	
	boolean up,down,left,right;
	public boolean sprint;
	boolean faster,slower;
	boolean sUp,sLeft,sDown,sRight;
	
	public ControlSet(int[] keys){
		this.upKey = keys[0];
		this.leftKey = keys[1];
		this.downKey = keys[2];
		this.rightKey = keys[3]; 
		this.fasterKey = keys[4];
		this.slowerKey = keys[5];
		this.sprintKey = keys[6];
		this.shootUpKey = keys[7];
		this.shootDownKey = keys[8];
		this.shootLeftKey = keys[9];
		this.shootRightKey = keys[10];
	}
	public void updateKeys(BitSet bitset){
		cooldown--;
		up = bitset.get(upKey);
		left = bitset.get(leftKey);
		right = bitset.get(rightKey);
		down = bitset.get(downKey);
		sprint = bitset.get(sprintKey);
		faster = bitset.get(fasterKey);
		slower = bitset.get(slowerKey);
		sUp = bitset.get(shootUpKey);
		sDown = bitset.get(shootDownKey);
		sLeft = bitset.get(shootLeftKey);
		sRight = bitset.get(shootRightKey);
		//73,75,74,76
		//73,74,75,76
		if(cooldown == 0){
			if(faster){
				speed += 1;
			}
			if(slower){
				speed -= 1;
			}
			cooldown = BASE_COOL;
		}	
		if(speed < 0.5){
			speed = 0.5;
		}
		if(speed > 20){
			speed = 40;
		}
	}
	public double getX(){
		double x = 0;
		if(left){
			x -= 1;
		}
		if(right){
			x += 1;
		}
		return x;
	}
	public double getY(){
		double y = 0;
		if(up){
			y += 1;
		}
		if(down){
			y -= 1;
		}
		return y;
	}
	public Point getSword(){
		double y = 0;
		double x = 0;
		if(sUp){
			y -= 1;
		}
		if(sDown){
			y += 1;
		}
		if(sRight){
			x += 1;
		}
		if(sLeft){
			x -= 1;
		}
		return new Point((int)x,(int)y);
	}
	
}