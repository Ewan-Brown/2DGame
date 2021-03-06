package effects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Effects {

	//TODO incorporate an enum so that it's easier to differ them and looks cool n stuff
	public static enum particleType{
		EXPLODE,SWIRLY,FIREWORKS,IMPLODE,FLIP;
	}
	public static ArrayList<ParticleBasic> explode(double x, double y, Color c){
		return explode(x,y,c,100);
	}
	public static ArrayList<ParticleBasic> explode(double x, double y, Color c, int pNum){
		return explode(x,y,c,pNum,20);
	}
	public static ArrayList<ParticleBasic> explode(double x, double y, Color c, int pNum, int speed){
		Random rand = new Random();
		ArrayList<ParticleBasic> pArray = new ArrayList<ParticleBasic>();
		for(int i = 0; i < pNum; i ++){
			pArray.add(new ParticleBasic(x,y,(rand.nextDouble() * speed) - (speed / 2),(rand.nextDouble() * speed) - speed / 2,c));
		}
		return pArray;
	}
	public static ArrayList<ParticleSwirly> swirlyParticle(double x, double y, Color c){
		ArrayList<ParticleSwirly> pArray= new ArrayList<ParticleSwirly>();
		int pNum = 100;
		Random rand = new Random();
		int speed = 10;
		for(int i = 0; i < pNum; i ++){
			pArray.add(new ParticleSwirly(x,y,(rand.nextDouble() * speed) - (speed / 2),(rand.nextDouble() * speed) - speed / 2,c));
		}
		//XXX Inheritance issue with arraylists
//		pArray = fireworks(pArray);
		return pArray;
	}
//	public static ArrayList<? extends Particle> fireworks(ArrayList<? extends Particle> array){
//		Random rand = new Random();
//		for(int i = 0; i < array.size(); i++){
//			array.get(i).color = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
//		}
//		return array;
//	}
	public static ArrayList<ParticleImplode> implode(double x, double y, Color c){
		int pNum = 100;
		Random rand = new Random();
		int speed = 20;
		ArrayList<ParticleImplode> pArray = new ArrayList<ParticleImplode>();
		for(int i = 0; i < pNum; i ++){
			pArray.add(new ParticleImplode(x,y,(rand.nextDouble() * speed) - (speed / 2),(rand.nextDouble() * speed) - speed / 2,c));
		}
		return pArray;
		
	}
	public static ArrayList<ParticleSpiral> spiral(double x, double y, Color c){
		int pNum = 100;
		Random rand = new Random();
		int speed = 20;
		ArrayList<ParticleSpiral> pArray = new ArrayList<ParticleSpiral>();
		for(int i = 0; i < pNum; i ++){
			pArray.add(new ParticleSpiral(x,y,(rand.nextDouble() * speed) - (speed / 2),(rand.nextDouble() * speed) - speed / 2,c));
		}
		return pArray;
		
	}
	public static ArrayList<ParticleFlip> flip(double x, double y, Color c){
		int pNum = 1000;
		Random rand = new Random();
		int speed = 20;
		ArrayList<ParticleFlip> pArray = new ArrayList<ParticleFlip>();
		for(int i = 0; i < pNum; i ++){
			pArray.add(new ParticleFlip(x,y,(rand.nextDouble() * speed) - (speed / 2),(rand.nextDouble() * speed) - speed / 2,c));
		}
		return pArray;
		
	}
	//TODO get angle from speeds and shoot in that direction!
	public static ArrayList<ParticleBasic> shockwave(double x, double y,double speedX,double speedY,Color c){
		int pNum = 50;
		Random rand = new Random();
		double angle = Math.atan2(speedX,speedY);
		double offset = 1.5;
		double speedY2 = Math.cos(angle);
		double speedX2 = Math.sin(angle);
		//Without this, if speedY is 0, the angle found is vertical and so the particles' Y speed = -1
		if(speedY == 0){
			speedY2 = 0;
		}
		ArrayList<ParticleBasic> pArray = new ArrayList<ParticleBasic>();
		for(int i = 0; i < pNum; i ++){
			pArray.add(new ParticleBasic(x,y,speedX2 * 2 + ((rand.nextDouble() - 0.5) * offset),speedY2 * 2 + ((rand.nextDouble() - 0.5) * offset),c));
		}
		return pArray;
	}
	public static ArrayList<byte[]> setName(ArrayList<String> l){
		ArrayList<byte[]> l2 = new ArrayList<byte[]>();
		for(int i = 0; i < l.size(); i++){
			l2.add(l.get(i).getBytes());
		}
		return l2;
	}
	/*TODO
	 * Implode - WIP
	 * Warp-Speed line things?
	 * Swirly explosion
	 * Sparks
	 * flying squares?
	 * 
	 * 
	 */
	
	
}
