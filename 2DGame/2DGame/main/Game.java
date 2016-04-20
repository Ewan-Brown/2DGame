package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import javax.swing.JPanel;

import effects.Particle;
import effects.ParticleBasic;
import entities.Alien;
import entities.Bullet;
import entities.Entity;
import entities.LandMine;
import entities.Player;
import entities.Target;
import entities.Wall;
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
		wallArray.clear();
		bulletArray.clear();
		alienArray.clear();
		playerArray.clear();
		targetArray.clear();
		mineArray.clear();
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
		slide();
		shrinkWalls();
		updateEffects();
		player.updateControls(keySet);
		addBullets(player.shoot());
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
		drawEffects(g2);
		drawEntities(g2);
		drawHealthBar(g,player);
		g.setColor(player.color);
		Line2D sword = player.getSwordLine();
		g2.draw(sword);
		g.setColor(Color.white);
		g.drawString(particleArray.size()+"", panelWidth / 2, panelHeight / 2);
		g2.setColor(Color.BLACK);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
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
	public void drawEffects(Graphics g){
		Particle cParticle;
		for(int i = 0; i < particleArray.size(); i++){
			cParticle = particleArray.get(i);
			g.setColor(cParticle.color);
			g.fillRect((int)cParticle.x, (int)cParticle.y, 2, 2);
		}
	}
	public void drawEntities(Graphics g){
		ArrayList<Entity> tempArray = new ArrayList<Entity>();
		tempArray.addAll(alienArray);
		tempArray.addAll(mineArray);
		tempArray.addAll(bulletArray);
		tempArray.addAll(playerArray);
		tempArray.addAll(targetArray);
		tempArray.addAll(wallArray);
		for(Entity e : tempArray){
			g.setColor(e.color);
			drawEntity(g,e);
		}
	}
	public void drawEntity(Graphics g,Entity e){
		g.setColor(e.color);
		g.fillRect((int)e.x - ((e.width - 1) / 2), (int)e.y - ((e.height - 1) / 2), 	e.width, e.height);
	}
	public void drawHealthBar(Graphics g, Entity e){
		double health = e.health;
		int barWidth = 30;
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
						if(GameMath.getDistance(cAlien, cEntity) < ((cAlien.width + 1) / 2) + ((cEntity.width + 1) / 2)){
							cEntity.onEntityCollision();
							particleArray.addAll(cEntity.onDeath());
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
						if(GameMath.getDistance(cBullet, cEntity) < ((cBullet.width + 1) / 2) + ((cEntity.width + 1) / 2)){
							if(cEntity.onBulletHit()){
								particleArray.addAll(cEntity.onDeath());
							}
						}
					}

				}
			}
		}
		tempArray.clear();
		tempArray.addAll(alienArray);
		tempArray.addAll(bulletArray);
		Rectangle tempRectangle;
		Line2D swordLine = player.getSwordLine();
		if(player.dead == false){
			for(Entity cEntity : tempArray){
				if(cEntity.dead == false){
					tempRectangle = new Rectangle((int)cEntity.x - ((cEntity.width - 1) / 2), (int)cEntity.y - ((cEntity.height - 1) / 2),cEntity.width,cEntity.height);
					if(swordLine.intersects(tempRectangle)){
						cEntity.onMeleeHit();
						particleArray.addAll(cEntity.onDeath());
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
