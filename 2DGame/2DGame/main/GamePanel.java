package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import javax.swing.JPanel;

import effects.Particle;
import entities.Alien;
import entities.Bullet;
import entities.Entity;
import entities.Player;
import entities.Target;

public class GamePanel extends JPanel implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Player player1;
	Player player2;
	//TODO GET RID OF ALL THESE 'c' TEMPORARY OBJECTS!
	Alien cAlien;
	Entity cFriendly;
	Target cTarget;
	Bullet cBullet;
	int aliens = 1;
	int panelWidth;
	int panelHeight;
	public enum Direction{Left,Right,Up,Down};
	Direction direction;
	BitSet keySet = new BitSet(256);
	double playerSpeedControl = 1;
	double player2SpeedControl = 1;
	int speedShiftCount = 0;
	int speedShiftCount2 = 0;
	boolean i,j,k,l,w,a,s,d;
	//TODO SORT THIS OUT
	public ArrayList<Particle> particleArray = new ArrayList<Particle>();
	public ArrayList<Alien> alienArray = new ArrayList<Alien>();
	public ArrayList<Player> playerArray = new ArrayList<Player>();
	public ArrayList<Target> targetArray = new ArrayList<Target>();
	public ArrayList<Bullet> bulletArray = new ArrayList<Bullet>();
	Particle cParticle;
	Random rand;
	public GamePanel(int w, int h){
		rand = new Random();
		setPreferredSize(new Dimension(w,h));
		this.setFocusable(true);
		addKeyListener(this);
		panelWidth = w;
		panelHeight = h;
		startGame();
		this.setBackground(Color.BLACK);
	}
	public void startGame(){
		bulletArray.clear();
		alienArray.clear();
		playerArray.clear();
		targetArray.clear();
		player1 = new Player(panelWidth, panelHeight, panelWidth / 2, panelHeight / 2,Color.GREEN);
		player2 = new Player(panelWidth, panelHeight, panelWidth / 2, panelHeight / 2,Color.BLUE);
		for(int i = 0; i < aliens; i++){
			spawnAlien();
			spawnTarget();
		}
		playerArray.add(player1);
		playerArray.add(player2);
	}
	public void spawnAlien(){
		alienArray.add(new Alien(panelWidth,panelHeight,rand.nextInt(panelWidth),rand.nextInt(panelHeight)));
	}
	public void spawnTarget(){
		targetArray.add(new Target(panelWidth,panelHeight,rand.nextInt(panelWidth),rand.nextInt(panelHeight)));
	}
	public void update(){
		updateEffects();
		doKeyActions();
		moveAliens();
		moveTargets();
		moveBullets();
		checkCollisions();
		if(checkAllDead()){
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
	public void getKeys(){
		i = keySet.get(KeyEvent.VK_I);
		j = keySet.get(KeyEvent.VK_J);
		k = keySet.get(KeyEvent.VK_K);
		l = keySet.get(KeyEvent.VK_L);
		w = keySet.get(KeyEvent.VK_W);
		a = keySet.get(KeyEvent.VK_A);
		s = keySet.get(KeyEvent.VK_S);
		d = keySet.get(KeyEvent.VK_D);
	}
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		drawTargets(g2);
		drawPlayers(g2);
		drawAliens(g2);
		drawBullets(g2);
		drawEffects(g2);
		g2.setColor(Color.BLACK);
//		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//    		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//	    		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	}
	public void drawEffects(Graphics g){
		for(int i = 0; i < particleArray.size();i++){
			cParticle = particleArray.get(i);
			g.setColor(cParticle.color);
			g.fillRect((int)cParticle.x, (int)cParticle.y, 2, 2);
		}
	}
	public void drawBullets(Graphics g){
		for(int i = 0; i < bulletArray.size(); i++){
			cBullet = bulletArray.get(i);
			g.setColor(cBullet.color);
			g.fillRect((int)cBullet.x, (int)cBullet.y, cBullet.width, cBullet.height);
		}
	}
	public void drawTargets(Graphics g){
		for(int i = 0; i < targetArray.size(); i++){
			cTarget = targetArray.get(i);
			g.setColor(cTarget.color);
			g.fillRect((int)cTarget.x - ((cTarget.width - 1) / 2), (int)cTarget.y - ((cTarget.height - 1) / 2), cTarget.width ,cTarget.height);
		}
	}
	public void drawPlayers(Graphics g){
		for(int i = 0; i < playerArray.size(); i++){
			cFriendly = playerArray.get(i);
			g.setColor(cFriendly.color);
			g.fillRect((int)cFriendly.x - ((cFriendly.width - 1) / 2), (int)cFriendly.y - ((cFriendly.height - 1) / 2), cFriendly.width ,cFriendly.height);

		}
	}
	public void drawAliens(Graphics g){
		for(int i = 0; i < alienArray.size(); i ++){
			cAlien = alienArray.get(i);
			g.setColor(cAlien.color);
			g.fillRect((int)cAlien.x - ((cAlien.width - 1) / 2), (int)cAlien.y - ((cAlien.height - 1) / 2), cAlien.width ,cAlien.height);
		}
	}
	public void moveTargets() {
		for(int i = 0; i < targetArray.size(); i++){
			cTarget = targetArray.get(i);
			cTarget.updateTarget(alienArray);
			cTarget.moveAI();
		}
	}
	public void moveAliens(){
		for(int i = 0; i < alienArray.size(); i ++){
			cAlien = alienArray.get(i);
			ArrayList<Entity> tempArray = new ArrayList<Entity>();
			tempArray.addAll(playerArray);
			tempArray.addAll(targetArray);
			cAlien.updateTarget(tempArray);
			cAlien.moveAI();
			cBullet = cAlien.tryShoot();
			if(cBullet != null){
				bulletArray.add(cBullet);
			}
			
			
		}
	}
	public void moveBullets(){
		for(int i = 0; i < bulletArray.size(); i++){
			cBullet = bulletArray.get(i);
			cBullet.moveAI();
		}
	}
	public void checkCollisions(){
		ArrayList<Entity> tempArray = new ArrayList<Entity>();
		tempArray.addAll(playerArray);
		tempArray.addAll(targetArray);
		for(int i = 0; i < alienArray.size(); i ++){
			cAlien = alienArray.get(i);
			for(int j = 0; j < tempArray.size(); j++){
				cFriendly = tempArray.get(j);
				if(cFriendly.dead == false){
					if(GameMath.getDistance(cAlien, cFriendly) < ((cAlien.width + 1) / 2) + ((cFriendly.width + 1) / 2)){
						cFriendly.onCollision();
						particleArray.addAll(cFriendly.onDeath());
					}
				}
				
			}
		}
		for(int i = 0; i < bulletArray.size(); i++){
			cBullet = bulletArray.get(i);
			for(int j = 0; j < tempArray.size(); j++){
				cFriendly = tempArray.get(j);
				if(cFriendly.dead == false){
					if(GameMath.getDistance(cBullet, cFriendly) < ((cAlien.width + 1) / 2) + ((cFriendly.width + 1) / 2)){
						cFriendly.onCollision();
						particleArray.addAll(cFriendly.onDeath());
					}
				}
				
			}
		}
	}
	public boolean checkAllDead(){
		for(int i = 0; i < playerArray.size();i++){
			if(playerArray.get(i).dead == false){
				return false;
			}
		}
		for(int i = 0; i < targetArray.size(); i++){
			if(targetArray.get(i).dead == false){
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
	public void doKeyActions(){
		speedShiftCount++;
		getKeys();
		if(keySet.get(KeyEvent.VK_Q)){
			if(speedShiftCount > 30){
				speedShiftCount = 0;
				if(playerSpeedControl < 30){
					playerSpeedControl += 1;
				}
			}
		}
		if(keySet.get(KeyEvent.VK_E)){
			if(speedShiftCount > 30){
				speedShiftCount = 0;
				if(playerSpeedControl > 1){
					playerSpeedControl -= 1;
				}
			}
		}
		speedShiftCount2++;
		if(keySet.get(KeyEvent.VK_U)){
			if(speedShiftCount2 > 30){
				speedShiftCount2 = 0;
				if(player2SpeedControl < 30){
					player2SpeedControl += 1;
				}
			}
		}
		if(keySet.get(KeyEvent.VK_O)){
			if(speedShiftCount2 > 30){
				speedShiftCount2 = 0;
				if(player2SpeedControl > 1){
					player2SpeedControl -= 1;
				}
			}
		}
		if(keySet.get(KeyEvent.VK_SHIFT)){
			player1.sprint = true;
		}
		else{
			player1.sprint = false;
		}
		if(keySet.get(KeyEvent.VK_SLASH)){
			player2.sprint = true;
		}
		else{
			player2.sprint = false;
		}
		double x = 0;
		double y = 0;
		double x2 = 0;
		double y2 = 0;
		if(w){
			y += 1;
		}
		if(s){
			y -= 1;
		}
		if(a){
			x -= 1;
		}
		if(d){
			x += 1;
		}
		if(i){
			y2 += 1;
		}
		if(k){
			y2 -= 1;
		}
		if(j){
			x2 -= 1;
		}
		if(l){
			x2 += 1;
		}
		player1.speed = this.playerSpeedControl;
		player2.speed = this.player2SpeedControl;
		player1.moveEntity(x, y);
		player2.moveEntity(x2, y2);
	}

}
