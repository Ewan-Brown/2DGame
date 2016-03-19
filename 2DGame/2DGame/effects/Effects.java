package effects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Effects {

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
	public static ArrayList<Particle> fireworks(ArrayList<Particle> array){
		Random rand = new Random();
		for(int i = 0; i < array.size(); i++){
			array.get(i).color = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
		}
		return array;
	}
	/*TODO
	 * Implode
	 * Warp-Speed line things?
	 * Swirly explosion
	 * Sparks
	 * flying squares?
	 * 
	 * 
	 */
	
	
}
