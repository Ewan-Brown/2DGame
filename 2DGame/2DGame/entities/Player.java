package entities;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.BitSet;

import effects.Effects;
import effects.ParticleImplode;
import settings.ControlSet;

public class Player extends Entity{
	public boolean sprint;
	public ControlSet controls;
	public Player(int width, int height,int x, int y,Color c,ControlSet controls) {
		super(width,height,x,y,c);
		this.controls = controls;
		name = "PLAYER";
	}
	public void moveEntity(double x, double y){
		if(sprint){
			speed *= 2;
		}
//		System.out.println(x+" "+y);
		super.moveEntity(x, y);
	}
	public ArrayList<ParticleImplode> onDeath(){
		Color c = color;
		this.deadColor();
		return Effects.implode(this.x, this.y, c);

	}
	public void updateControls(BitSet bitset){
		controls.updateKeys(bitset);
		moveEntity(controls.getX(),controls.getY());
	}

}
