package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import javax.swing.JPanel;

import effects.Effects;
import effects.Particle;
import effects.ParticleBasic;
import entities.Alien;
import entities.Breeder;
import entities.Bullet;
import entities.Entity;
import entities.LandMine;
import entities.Laser;
import entities.Missile;
import entities.Player;
import entities.Target;
import entities.Wall;
import entities.powerups.BasicPowerup;
import settings.ControlSet;

public class Game extends JPanel implements KeyListener,MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final DecimalFormat df = new DecimalFormat("#.##");
	//config settings
	final boolean cameraFollowPlayer = true;
	int slideSpeed = 2;
	int wallShrinkCount = 10;
	int WALL_SHRINK_MAX = 3;
	Player player;
	public double safeSpawnDistance = 400;
	int aliens = 2;
	int walls = 20;
	//used to keep from spawning things outside of frame!
	int panelWidth;
	int panelHeight;
	int gameWidth;
	int gameHeight;
	/* A set of bits, each bit representing a possible character from the keyboard. When one is detected to be pressed,
	 * that key is flipped ON, and if that key is released, the equivilant bit is flipped OFF.
	 * The number-key reference used is from the KeyEvent class.
	 */
	BitSet keySet = new BitSet(256);
	//Arraylists for each kind of entity, and one for particles.
	//TODO make this less messy somehow?
	public ArrayList<Breeder> breederArray = new ArrayList<Breeder>();
	public ArrayList<Particle> particleArray = new ArrayList<Particle>();
	public ArrayList<Alien> alienArray = new ArrayList<Alien>();
	public ArrayList<Player> playerArray = new ArrayList<Player>();
	public ArrayList<Target> targetArray = new ArrayList<Target>();
	public ArrayList<Bullet> bulletArray = new ArrayList<Bullet>();
	public ArrayList<LandMine> mineArray = new ArrayList<LandMine>();
	public ArrayList<Wall> wallArray = new ArrayList<Wall>();
	public ArrayList<BasicPowerup> powerupArray = new ArrayList<BasicPowerup>();	
	public ArrayList<Laser> laserArray = new ArrayList<Laser>();
	public ArrayList<Missile> missileArray = new ArrayList<Missile>();
	//A Random instantiation used for spawning and other random things.
	//TODO should this be a method wide variable, seeing as global use has no benefit?
	Random rand;
	//The constructor, for initializing the JPanel settings and fitting it in frame
	public Game(int w, int h,ControlSet controls[]){
		rand = new Random();
		//XXX weird hack to keep JPanel in the JFrame
		setPreferredSize(new Dimension(w,h));
		///
		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);
		panelWidth = w;
		panelHeight = h;
		gameWidth = panelWidth * 2;
		gameHeight = panelHeight * 2;
		player = new Player(gameWidth / 2, gameHeight / 2,Color.GREEN,controls[0]);
		setBackground(Color.BLACK);
		startGame();
	}
	//Called when game first starts, and every time a 'game-over' occurs(when player dies)
	//Clears all entities then spawns in new ones
	public void startGame(){
		//Clearing all entites
		laserArray.clear();
		wallArray.clear();
		bulletArray.clear();
		alienArray.clear();
		playerArray.clear();
		targetArray.clear();
		mineArray.clear();
		powerupArray.clear();
		breederArray.clear();
		missileArray.clear();
		//XXX Temporary testing Missile
//		missileArray.add(new Missile(300,300));
		//TODO Should player be RESPAWNED, or RECREATED?
		player.respawn(gameWidth / 2, gameHeight / 2);
		
		//Spawning the requested amount of Aliens and Walls
		for(int i = 0; i < aliens; i++){
			spawnAlien();
		}
		for(int i = 0; i < walls; i++){
			spawnWall();
		}
		//This does not CREATE player, but instead places the player in an array that can be accessed by AI-Targetting or other such things
		playerArray.add(player);
	}
	//Called when player wins! Most entities are cleared, except for walls
	public void nextLevel(){
		laserArray.clear();
		mineArray.clear();
		bulletArray.clear();
		alienArray.clear();
		targetArray.clear();
		breederArray.clear();
		missileArray.clear();
		//Spawns requested amount of aliens
		for(int i = 0; i < aliens; i++){
			spawnAlien();
			//			spawnTarget();
		}
	}
	//Method to spawn a wall, with random dimensions(with limits) in a random location
	public void spawnWall(){
		//Added the '+10' for the dimensions so that the walls would not be too small, allowing entites to pass through objects
		wallArray.add(new Wall(rand.nextInt(gameWidth),rand.nextInt(gameHeight),rand.nextInt(100) + 10,rand.nextInt(100) + 10));

	}
	//Method to spawn a mine, in a random location
	public void spawnMine(){
		mineArray.add(new LandMine(rand.nextInt(gameWidth),rand.nextInt(gameHeight)));
	}
	//Method to spawn an alien, in a random location but atleast a certain distance from the player.
	public void spawnAlien(){
		Alien cAlien;
		//XXX hack-loop respawns alien continually in random locations until a 'safe' spot is chosen.
		do{
			cAlien = new Alien(rand.nextInt(gameWidth),rand.nextInt(gameHeight));
		}while(GameMath.getDistance(cAlien, player) < safeSpawnDistance);
		alienArray.add(cAlien);
	}
	//Method to spawn a target in a random location
	public void spawnTarget(){
		targetArray.add(new Target(rand.nextInt(gameWidth),rand.nextInt(gameHeight)));
	}
	//Slides all the stationary objects(Walls/powerups) 1 unit to the left,
	//Removes these if they exit the left side of the game.
	public void slide(){
		Wall w;
		//Separate methods needed because they need to be removed from their respected arrays.
		for(int i = 0; i < wallArray.size(); i++){
			w = wallArray.get(i);
			w.x -= slideSpeed;
			if(w.getRightSide() < 0){
				wallArray.remove(i);
				spawnWall();
			}
		}
		BasicPowerup bp;
		for(int i = 0; i < powerupArray.size();i++){
			bp = powerupArray.get(i);
			bp.x -= slideSpeed;
			if(bp.getRightSide() < 0){
				powerupArray.remove(i);
				spawnWall();
			}
		}
		//Kills entites if they go too far off the left side of the game.
		ArrayList<Entity> tempArray = new ArrayList<Entity>();
		tempArray.addAll(playerArray);
		tempArray.addAll(alienArray);
		tempArray.addAll(targetArray);
		tempArray.addAll(mineArray);
		for(Entity e : tempArray){
			if(e.getRightSide() < 0){
				//TODO Why call this method? doesn't make sense
				e.onEntityCollision();
			}
		}		
	}
	//Shrinks all the walls by one unit Width and Height.
	//If walls get too small, they are removed and a new one is spawned in.
	public void shrinkWalls(){
		ArrayList<Wall> tempArray = new ArrayList<Wall>();
		tempArray.addAll(wallArray);
		Wall w;
		//XXX Counter to slow down how fast the walls shrink
		wallShrinkCount--;
		for(int i = 0; i < wallArray.size();i++){
			w = wallArray.get(i);
			if(wallShrinkCount < 1){
				w.width -= 1;
				w.height -= 1;
			}
			if(w.width < 1 || w.height < 1){
				wallArray.remove(i);
				spawnWall();
			}
		}
		if(wallShrinkCount < 1){
			wallShrinkCount = WALL_SHRINK_MAX;
		}
	}
	//'TICK' of the game. Called continually by the 'main loop'.
	//Updates all Entities and then calls a repaint on the game window.
	public void update(){
		updateEffects();
		player.updateControls(keySet);
		addBullets(player.shoot());
		addMine(player.mine());
		updateLasers();
		updateMines();
		updateAliens();
		updateTargets();
		updateBullets();
		updateBreeders();
		updateMissiles();
		checkCollisions();
		checkObstacleCollisions();
		//Checks if the game has been lost or won, and then proceeds to either game-over or next-level
		if(checkGameWin()){
			nextLevel();
		}
		if(checkGameLose()){
			startGame();
		}
		repaint();
	}
	//Updates particle logic
	public void updateEffects(){
		Particle cParticle;
		for(int i = 0; i < particleArray.size();i++){
			cParticle = particleArray.get(i);
			if(cParticle.x < 0 || cParticle.x > gameWidth || cParticle.y < 0 || cParticle.y > gameHeight){
				cParticle.dead = true;
			}
			if(cParticle.dead){
				particleArray.remove(i);
			}
			else{
				cParticle.update();
			}
		}
	}
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		drawLasers(g2);
		drawEffects(g2);
		drawEntities(g2);
		g.setColor(Color.white);
		g.drawString(particleArray.size()+"", panelWidth / 2, panelHeight / 2);
		g2.setColor(Color.BLACK);
	}
	//Returns the amount of enemies and hostile entities alive
	public int enemiesAlive(){
		int a = 0;
		ArrayList<Entity> tempArray = new ArrayList<Entity>();
		tempArray.addAll(breederArray);
		tempArray.addAll(missileArray);
		tempArray.addAll(alienArray);
		tempArray.addAll(mineArray);
		tempArray.addAll(bulletArray);
		for(Entity cEntity : tempArray){
			if(cEntity.dead == false){
				a++;
			}
		}
		return a;
	}
	public void drawLasers(Graphics2D g2){
		for(Laser laser : laserArray){
			g2.setColor(laser.color);
			Point2D p1 = laser.getLine().getP1();
			Point2D p2 = laser.getLine().getP2();
			p1 = adjustToCamera(p1);
			p2 = adjustToCamera(p2);
			g2.draw(new Line2D.Double(p1,p2));
		}
	}
	public Point2D adjustToCamera(double x, double y){
		if(cameraFollowPlayer){
			x = (x - player.x + panelWidth / 2);
			y = (y - player.y + panelHeight / 2);
		}
		return new Point2D.Double(x, y);
	}
	public Point2D adjustToCamera(Point2D p){
		return (adjustToCamera(p.getX(),p.getY()));
	}
	//Takes a 'laser' after a click from the player, crops it appropiately if it is intersecting a wall, and then makes it much longer so
	//that it appears to be infinitely long.
	public Laser cropLaser(Laser laser){
		Line2D line = laser.getLine();
		double x = line.getX2() - line.getX1();
		double y = line.getY2() - line.getY1();
		line.setLine(line.getX1(), line.getY1(), line.getX2() + x * 2000, line.getY2() + y * 2000);
		Rectangle2D rect = null;
		Rectangle2D r;
		Wall w;
		double prevDist = 9999999;
		//Goes through all walls that laser intersects, and finds the closest one
		for(int i = 0; i < wallArray.size();i++){
			w = wallArray.get(i);
			r = new Rectangle2D.Double(w.x - ((w.width - 1) / 2), w.y - ((w.height - 1) / 2),w.width,w.height);
			if(line.intersects(r)){
				if(GameMath.getDistance(player, w) < prevDist){
					prevDist = GameMath.getDistance(player, wallArray.get(i));
					rect = r;
				}
			}
		}
		if(rect != null){
			//Splits up the rectangle(wall) into 4 lines, then finds which 1-2(4 for exceptions!) it intersects, finally finds the closest of those and crops laser
			Line2D[] lines = new Line2D[4];
			Line2D[] intLines = new Line2D[4];
			Point2D[] intPoints = new Point2D[4];
			Point2D[] p = new Point2D[4];
			p[0] = new Point2D.Double(rect.getX(),rect.getY());
			p[1] = new Point2D.Double(rect.getX() + rect.getWidth(),rect.getY());
			p[2] = new Point2D.Double(rect.getX() + rect.getWidth(),rect.getY() + rect.getHeight());
			p[3] = new Point2D.Double(rect.getX(),rect.getY() + rect.getHeight());
			lines[0] = new Line2D.Double(p[0], p[1]);
			lines[1] = new Line2D.Double(p[1], p[2]);
			lines[2] = new Line2D.Double(p[2], p[3]);
			lines[3] = new Line2D.Double(p[3], p[0]);
			int f = 0;
			for(int i = 0; i < 4;i++){
				if(lines[i].intersectsLine(line)){
					intLines[f] = lines[i];
					f++;
				}
			}
			for(int i = 0; i < f ; i++){
				intPoints[i] = GameMath.getIntersect(line, intLines[i]);
			}
			if(intPoints[1] == null || GameMath.getDistance(player.getPoint(), intPoints[1]) > GameMath.getDistance(player.getPoint(), intPoints[0])){
				line.setLine(line.getP1(),intPoints[0]);
			}
			// If player is shooting through wall and temporarily passes the wall's boundaries it will have 0 intersections 
			// but still be in the wall so a null check is needed
			else if (intPoints[0] != null){
				line.setLine(line.getP1(),intPoints[1]);
			}
		}
		laser.setLine(line);
		return laser;
	}
	public void drawEffects(Graphics g){
		Particle cParticle;
		Point2D p;
		for(int i = 0; i < particleArray.size(); i++){
			cParticle = particleArray.get(i);
			g.setColor(cParticle.color);
			int x = (int) cParticle.x;
			int y = (int) cParticle.y;
			p = adjustToCamera(x,y);
			g.fillRect((int)p.getX(), (int)p.getY(), 2, 2);
		}
	}
	//Main entity-drawing method.
	public void drawEntities(Graphics2D g2){
		ArrayList<Entity> tempArray = new ArrayList<Entity>();
		tempArray.addAll(alienArray);
		tempArray.addAll(playerArray);
		tempArray.addAll(targetArray);
		tempArray.addAll(breederArray);
		for(Entity e : tempArray){
			drawHealthBar(g2,e);
		}
		tempArray.addAll(mineArray);
		tempArray.addAll(bulletArray);
		tempArray.addAll(wallArray);
		tempArray.addAll(powerupArray);
		tempArray.addAll(missileArray);
		for(Entity e : tempArray){
			drawEntity(g2,e);
		}
		Line2D sword = player.getSwordLine();
		if(sword != null){
			g2.setColor(player.color);
			g2.draw(sword);
		}
	}
	public void drawEntity(Graphics g,Entity e){
		g.setColor(e.color);
		int x = (int) e.x;
		int y = (int) e.y;
		Point2D p = adjustToCamera(x,y);
		g.fillRect((int)p.getX() - ((e.width - 1) / 2), (int)p.getY() - ((e.height - 1) / 2), 	e.width, e.height);
	}
	//Draws a health bar below entities
	public void drawHealthBar(Graphics g, Entity e){
		g.setColor(e.color);
		double health = e.health;
		int barWidth = 35;
		double x = e.x - (barWidth * (health / e.maxHealth)) / 2;
		double y = e.getBottomSide() + 5;
		Point2D p = adjustToCamera(x,y);
		g.fillRect((int)p.getX(), (int)p.getY(),(int) (barWidth * (health / e.maxHealth)), 3);
	}
	public void updateMines(){
		for(int i = 0; i < mineArray.size(); i++){
			LandMine cMine = mineArray.get(i);
			cMine.tick();
			if(cMine.dead){
				addBullets(cMine.explode());
				mineArray.remove(i);
			}
		}
	}
	public void updateTargets() {
		for(Target cTarget : targetArray){
			cTarget.updateTarget(alienArray);
			cTarget.moveAI();
		}
	}
	public void updateMissiles(){
		for(Missile missile : missileArray){
			if(!missile.dead){
				missile.updateTarget(playerArray);
				missile.moveAI();
				if(missile.dead){
					bulletArray.addAll(missile.explode());
				}
			}
		}
	}
	public void updateBreeders(){
		Alien cAlien;
		for(Breeder cBreeder : breederArray){
			if(!cBreeder.dead){
				cAlien = cBreeder.spawnAlien();
				if(cAlien != null){
					alienArray.add(cAlien);
				}
				cBreeder.moveAI();
				cBreeder.updateTarget(playerArray);

			}
		}
	}
	public void updateAliens(){
		ArrayList<Entity> tempArray = new ArrayList<Entity>();
		tempArray.addAll(playerArray);
		tempArray.addAll(targetArray);
		for(Alien cAlien : alienArray){
			if(!cAlien.dead){
				cAlien.updateTarget(tempArray);
				cAlien.moveAI();
				addBullets(cAlien.tryShoot());
			}
		}
	}
	//Methods to add bullets and mines to the game, needed so that null things are accidentally put in because then things go BOOM :(
	public void addBullet(Bullet cBullet){
		if(cBullet != null){
			bulletArray.add(cBullet);
		}
	}
	public void addBullets(ArrayList<Bullet> bArray){
		if(bArray != null){
			for(Bullet cBullet : bArray){
				if(cBullet != null){
					bulletArray.add(cBullet);
				}
			}
		}
	}
	public void addMine(LandMine cMine){
		if(cMine != null){
			mineArray.add(cMine);
		}
	}
	public void updateBullets(){
		Bullet cBullet;
		for(int i = 0; i < bulletArray.size(); i++){
			cBullet = bulletArray.get(i);
			cBullet.moveAI();
			if(cBullet.dead){
				bulletArray.remove(i);
			}
		}
	}
	public void checkCollisions(){
		ArrayList<Entity> tempArray = new ArrayList<Entity>();
		tempArray.addAll(playerArray);
		tempArray.addAll(targetArray);
		for(Alien cAlien : alienArray){
			if(!cAlien.dead){
				for(Entity cEntity : tempArray){
					if(!cEntity.dead){
						if(GameMath.doCollide(cEntity, cAlien)){
							cEntity = cAlien.attackEntity(cEntity);
							if(cEntity.dead){
								particleArray.addAll(cEntity.onDeath());
							}
						}
					}

				}
			}
		}
		tempArray.addAll(alienArray);
		for(Bullet cBullet : bulletArray){
			if(!cBullet.dead){
				for(Entity cEntity : tempArray){
					if(!cEntity.dead){
						if(GameMath.doCollide(cEntity, cBullet)){
							cEntity = cBullet.attackEntity(cEntity);
							if(cEntity.dead){
								particleArray.addAll(cEntity.onDeath());
							}
						}
					}

				}
			}
		}
		for(BasicPowerup bP : powerupArray){
			for(Entity cEntity : tempArray){
				if(!cEntity.dead){
					if(GameMath.doCollide(cEntity,bP)){
						cEntity = bP.onPickup(cEntity);
					}

				}
			}
		}
		tempArray.clear();
		tempArray.addAll(alienArray);
		tempArray.addAll(bulletArray);
		Rectangle tempRectangle;
		Line2D swordLine = player.getSwordLine();
		if(player.dead == false && swordLine != null){
			for(Entity cEntity : tempArray){
				if(cEntity.dead == false){
					tempRectangle = new Rectangle((int)cEntity.x - ((cEntity.width - 1) / 2), (int)cEntity.y - ((cEntity.height - 1) / 2),cEntity.width,cEntity.height);
					if(swordLine.intersects(tempRectangle)){
						cEntity = player.attackEntity(cEntity);
						cEntity.onWallCollide();
						if(cEntity.dead){
							particleArray.addAll(cEntity.onDeath());
						}
					}
				}
			}
		}
	}
	public void updateLasers(){
		Laser l = player.laser();
		if(l != null){
			l = cropLaser(l);
			laserArray.add(l);
		}
		Laser cLaser;
		for(int i = 0; i < laserArray.size();i++){
			cLaser = laserArray.get(i);
			cLaser.update();
			if(cLaser.dead){
				laserArray.remove(i);
			}
			Line2D line = cLaser.getLine();
			ArrayList<Entity> tempArray = new ArrayList<Entity>();
			tempArray.addAll(alienArray);
			tempArray.addAll(bulletArray);
			tempArray.addAll(mineArray);
			tempArray.addAll(targetArray);
			for(Entity cEntity : tempArray){
				if(!cEntity.dead){
					if(line.intersects(new Rectangle((int)cEntity.x - ((cEntity.width - 1) / 2), (int)cEntity.y - ((cEntity.height - 1) / 2), 	cEntity.width, cEntity.height))){
						cEntity.damage(2);
						particleArray.addAll(Effects.explode(cEntity.x, cEntity.y, Color.green, 1,4));
						if(cEntity.dead){
							particleArray.addAll(cEntity.onDeath());
						}
					}
				}
			}
		}
	}
	public void checkObstacleCollisions(){
		ArrayList<Entity> allEntities = new ArrayList<Entity>();
		allEntities.addAll(alienArray);
		allEntities.addAll(bulletArray);
		allEntities.addAll(mineArray);
		allEntities.addAll(playerArray);
		allEntities.addAll(targetArray);
		allEntities.addAll(breederArray);
		double l,r,u,d;
		double l2,r2,u2,d2;
		int minIndex;
		ArrayList<Double> overLap = new ArrayList<Double>(4);
		for(Entity e : allEntities){
			if(!e.dead){
				l = e.getLeftSide();
				r = e.getRightSide();
				u = e.getUpSide();
				d = e.getBottomSide();
				if(l < 0){
					e.onWallCollide();
					e.x = (e.width - 1) / 2;
				}
				if(r > gameWidth){
					e.onWallCollide();
					e.x = gameWidth - (e.width - 1) / 2;
				}
				if(u < 0){
					e.onWallCollide();
					e.y = (e.height - 1) / 2;
				}
				if(d > gameHeight){
					e.onWallCollide();
					e.y = gameWidth - (e.height - 1) / 2;
				}
				for(Wall w : wallArray){
					if(GameMath.doCollide(e, w)){
						particleArray.addAll(e.onWallCollide());
						l2 = w.getLeftSide();
						r2 = w.getRightSide();
						u2 = w.getUpSide();
						d2 = w.getBottomSide();
						overLap.add(0, Math.abs(l2 - r));
						overLap.add(1, Math.abs(r2 - l));
						overLap.add(2, Math.abs(u2 - d));
						overLap.add(3, Math.abs(d2 - u));
						minIndex = 0;
						for(int i = 1; i < 4;i++){
							if(overLap.get(i) < overLap.get(minIndex)){
								minIndex = i;
							}
						}
						if(minIndex == 0){
							e.x = l2 - (e.width - 1) / 2 - 2;
						}
						if(minIndex == 1){
							e.x = r2 + (e.width - 1) / 2 + 2;
						}
						if(minIndex == 2){
							e.y = u2 - (e.height - 1) / 2 - 2;
						}
						if(minIndex == 3){
							e.y = d2 + (e.height - 1) / 2 + 2;
						}
					}
				}
			}
		}
	}
	public boolean checkGameLose(){
		if(!player.dead){
			return false;
		}
		for(Target cTarget : targetArray){
			if(cTarget.dead == false){
				return false;
			}
		}
		return true;
	}
	public boolean checkGameWin(){
		ArrayList<Entity> tempArray = new ArrayList<Entity>();
		tempArray.addAll(alienArray);
		tempArray.addAll(mineArray);
		tempArray.addAll(bulletArray);
		tempArray.addAll(missileArray);
		tempArray.addAll(breederArray);
		for(Entity cEntity : tempArray){
			if(cEntity.dead == false){
				return false;
			}
		}
		return true;
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override

	public void keyPressed(KeyEvent e) {
		keySet.set(e.getKeyCode());
	}
	@Override
	public void keyReleased(KeyEvent e) {
		keySet.clear(e.getKeyCode());

	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		Point2D p = e.getPoint();
		double x = p.getX();
		double y = p.getY();
		if(cameraFollowPlayer){
			x = (p.getX() + (player.x - panelWidth / 2));
			y = (p.getY() + (player.y - panelHeight / 2));
		}
		player.click(new Point2D.Double(x,y),e.getButton());
	}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

}
