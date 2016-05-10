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
import entities.Bullet;
import entities.Entity;
import entities.LandMine;
import entities.Laser;
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
	int slideSpeed = 2;
	int wallShrinkCount = 10;
	int WALL_SHRINK_MAX = 3;
	Player player;
	public double safeSpawnDistance = 200;
	int aliens = 10;
	int walls = 15;
	int panelWidth;
	int panelHeight;
	BitSet keySet = new BitSet(256);
	public ArrayList<Particle> particleArray = new ArrayList<Particle>();
	public ArrayList<Alien> alienArray = new ArrayList<Alien>();
	public ArrayList<Player> playerArray = new ArrayList<Player>();
	public ArrayList<Target> targetArray = new ArrayList<Target>();
	public ArrayList<Bullet> bulletArray = new ArrayList<Bullet>();
	public ArrayList<LandMine> mineArray = new ArrayList<LandMine>();
	public ArrayList<Wall> wallArray = new ArrayList<Wall>();
	public ArrayList<BasicPowerup> powerupArray = new ArrayList<BasicPowerup>();	
	public ArrayList<Laser> laserArray = new ArrayList<Laser>();
	ParticleBasic cParticle;
	Random rand;
	int wallSpawnCounter = 0;
	public Game(int w, int h,ControlSet controls[]){
		rand = new Random();
		setPreferredSize(new Dimension(w,h));
		this.setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);
		panelWidth = w;
		panelHeight = h;
		player = new Player(panelWidth / 2, panelHeight / 2,Color.GREEN,controls[0]);
		this.setBackground(Color.BLACK);
		startGame();
	}
	public void startGame(){
		laserArray.clear();
		wallArray.clear();
		bulletArray.clear();
		alienArray.clear();
		playerArray.clear();
		targetArray.clear();
		mineArray.clear();
		powerupArray.clear();
		powerupArray.add(new BasicPowerup(400,500));
		player.respawn(panelWidth / 2, panelHeight / 2);

		for(int i = 0; i < aliens; i++){
			spawnAlien();
		}
		for(int i = 0; i < walls; i++){
			spawnWall();
		}
		playerArray.add(player);
	}
	public void nextLevel(){
		laserArray.clear();
		mineArray.clear();
		bulletArray.clear();
		alienArray.clear();
		targetArray.clear();
		for(int i = 0; i < aliens; i++){
			spawnAlien();
			//			spawnTarget();
		}
	}
	public void spawnWall(){
		wallArray.add(new Wall(rand.nextInt(panelWidth),rand.nextInt(panelHeight),rand.nextInt(100) + 10,rand.nextInt(100) + 10));

	}
	public void spawnMine(){
		mineArray.add(new LandMine(rand.nextInt(panelWidth),rand.nextInt(panelHeight)));
	}
	public void spawnAlien(){
		Alien cAlien;
		//XXX stupid loop
		do{
			cAlien = new Alien(rand.nextInt(panelWidth),rand.nextInt(panelHeight));
		}while(GameMath.getDistance(cAlien, player) < safeSpawnDistance);
		alienArray.add(cAlien);
	}
	public void spawnTarget(){
		targetArray.add(new Target(rand.nextInt(panelWidth),rand.nextInt(panelHeight)));
	}
	public void slide(){
		ArrayList<Wall> tempArray = new ArrayList<Wall>();
		tempArray.addAll(wallArray);
		Wall w;
		for(int i = 0; i < wallArray.size(); i++){
			w = wallArray.get(i);
			w.x -= slideSpeed;
			if(w.getRightSide() < 0){
				wallArray.remove(i);
				spawnWall();
			}
		}
		ArrayList<Entity> tempArray2 = new ArrayList<Entity>();
		tempArray2.addAll(playerArray);
		tempArray2.addAll(alienArray);
		tempArray2.addAll(targetArray);
		tempArray2.addAll(mineArray);
		for(Entity e : tempArray2){
			if(e.getRightSide() < 0){
				e.onEntityCollision();
			}
		}		
	}
	public void shrinkWalls(){
		ArrayList<Wall> tempArray = new ArrayList<Wall>();
		tempArray.addAll(wallArray);
		Wall w;
		wallShrinkCount--;
		//XXX Stupid counter crap
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
	public void update(){
		updateEffects();
		player.updateControls(keySet);
		//addBullets(player.shoot());
		updateLasers();
		updateMines();
		updateAliens();
		updateTargets();
		updateBullets();
		checkCollisions();
		checkObstacleCollisions();
		if(checkGameWin()){
			nextLevel();
		}
		if(checkGameLose()){
			startGame();
		}
		this.repaint();
	}
	public void updateEffects(){
		for(int i = 0; i < particleArray.size();i++){
			if(particleArray.get(i).dead){
				particleArray.remove(i);
			}
			else{
				particleArray.get(i).update();
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
	public int enemiesAlive(){
		int a = 0;
		ArrayList<Entity> tempArray = new ArrayList<Entity>();
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
			g2.draw(laser.getLine());
		}
	}
	public Laser cropLaser(Laser laser){
		Line2D line = laser.getLine();
		double x = line.getX2() - line.getX1();
		double y = line.getY2() - line.getY1();
		line.setLine(line.getX1(), line.getY1(), line.getX2() + x * 2000, line.getY2() + y * 2000);
		Rectangle2D rect = null;
		Rectangle2D r;
		Wall w;
		double m = (line.getY2() - line.getY1()) / (line.getX2() - line.getX1());
		double b = line.getY1() - (m * line.getX1());;
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
		for(int i = 0; i < particleArray.size(); i++){
			cParticle = particleArray.get(i);
			g.setColor(cParticle.color);
			g.fillRect((int)cParticle.x, (int)cParticle.y, 2, 2);
		}
	}
	public void drawEntities(Graphics2D g2){
		ArrayList<Entity> tempArray = new ArrayList<Entity>();
		tempArray.addAll(alienArray);
		tempArray.addAll(playerArray);
		tempArray.addAll(targetArray);
		for(Entity e : tempArray){
			drawHealthBar(g2,e);
		}
		tempArray.addAll(mineArray);
		tempArray.addAll(bulletArray);
		tempArray.addAll(wallArray);
		tempArray.addAll(powerupArray);
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
		g.fillRect((int)e.x - ((e.width - 1) / 2), (int)e.y - ((e.height - 1) / 2), 	e.width, e.height);
	}
	public void drawHealthBar(Graphics g, Entity e){
		g.setColor(e.color);
		double health = e.health;
		int barWidth = 35;
		double x = e.x - (barWidth * (health / e.maxHealth)) / 2;
		double y = e.getBottomSide() + 5;
		g.fillRect((int)x, (int)y,(int) (barWidth * (health / e.maxHealth)), 3);
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
	public void updateAliens(){
		for(Alien cAlien : alienArray){
			ArrayList<Entity> tempArray = new ArrayList<Entity>();
			tempArray.addAll(playerArray);
			tempArray.addAll(targetArray);
			cAlien.updateTarget(tempArray);
			cAlien.moveAI();
			addBullets(cAlien.tryShoot());
		}
	}
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
			tempArray.addAll(this.alienArray);
			tempArray.addAll(this.bulletArray);
			tempArray.addAll(this.mineArray);
			tempArray.addAll(this.targetArray);
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
		allEntities.addAll(this.alienArray);
		allEntities.addAll(this.bulletArray);
		allEntities.addAll(this.mineArray);
		allEntities.addAll(this.playerArray);
		allEntities.addAll(this.targetArray);
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
				if(r > panelWidth){
					e.onWallCollide();
					e.x = panelWidth - (e.width - 1) / 2;
				}
				if(u < 0){
					e.onWallCollide();
					e.y = (e.height - 1) / 2;
				}
				if(d > panelHeight){
					e.onWallCollide();
					e.y = panelWidth - (e.height - 1) / 2;
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
		player.click(new Point(e.getX(),e.getY()));
	}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

}
