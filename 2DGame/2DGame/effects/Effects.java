package effects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Effects {

	//TODO incorporate an enum so that it's easier to differ tham and looks cool
	public static enum particleType{
		EXPLODE,SWIRLY,FIREWORKS,IMPLODE,FLIP;
	}
	public static ArrayList<Particle> explode(double x, double y, Color c){
		int pNum = 100;
		Random rand = new Random();
		int speed = 20;
		ArrayList<Particle> pArray = new ArrayList<Particle>();
		for(int i = 0; i < pNum; i ++){
			pArray.add(new Particle(x,y,(rand.nextDouble() * speed) - (speed / 2),(rand.nextDouble() * speed) - speed / 2,c));
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
		return pArray;
	}
	//XXX Firework doesn't work with Particle Swirly because it returns a swirly and not a base 'particle'
	public static ArrayList<Particle> fireworks(ArrayList<Particle> array){
		Random rand = new Random();
		for(int i = 0; i < array.size(); i++){
			array.get(i).color = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
		}
		return array;
	}
	public static ArrayList<ParticleSwirly> fireworksSwirly(ArrayList<ParticleSwirly> array){
		Random rand = new Random();
		for(int i = 0; i < array.size(); i++){
			array.get(i).color = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
		}
		return array;
	}
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
		int pNum = 100;
		Random rand = new Random();
		int speed = 20;
		ArrayList<ParticleFlip> pArray = new ArrayList<ParticleFlip>();
		for(int i = 0; i < pNum; i ++){
			pArray.add(new ParticleFlip(x,y,(rand.nextDouble() * speed) - (speed / 2),(rand.nextDouble() * speed) - speed / 2,c));
		}
		return pArray;
		
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
