package settings;

import java.util.BitSet;

public class ControlSet {
	
	int upKey,leftKey,downKey,rightKey;
	int sprintKey;
	int fasterKey,slowerKey;
	double speed = 0.1;
	int cooldown = 20;
	final int BASE_COOL = 20;
	
	boolean up,left,down,right;
	boolean sprint;
	boolean faster,slower;
	
//	public ControlSet(int up, int down, int left, int right, int sprint, int faster,int slower){
//		this.upKey = up;
//		this.leftKey = left;
//		this.rightKey = right; 
//		this.downKey = down;
//		this.sprintKey = sprint;
//		this.fasterKey = faster;
//		this.slowerKey = slower;
//	}
	public ControlSet(int[] keys){
		this.upKey = keys[0];
		this.leftKey = keys[1];
		this.downKey = keys[2];
		this.rightKey = keys[3]; 
		this.fasterKey = keys[4];
		this.slowerKey = keys[5];
		this.sprintKey = keys[6];
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
		if(speed > 40){
			speed = 40;
		}
	}
	public double getX(){
		double x = 0;
		if(left){
			x = -1;
		}
		if(right){
			x = 1;
		}
		if(sprint){
			x *= 2;
		}
		return x * speed;
	}
	public double getY(){
		double y = 0;
		if(up){
			y = 1;
		}
		if(down){
			y = -1;
		}
		if(sprint){
			y *= 2;
		}
		return y * speed;
	}
}