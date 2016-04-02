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

import effects.Effects;
import effects.Particle;
import entities.Alien;
import entities.Entity;
import entities.EntityAI;
import entities.Player;
import entities.Target;

public class GamePanel extends JPanel implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Player player1;
	Player player2;
	Alien cAlien;
	Entity cFriendly;
	Target cTarget;
	int aliens = 1;
	int panelWidth;
	int panelHeight;
	public enum Direction{Left,Right,Up,Down};
	Direction direction;
	BitSet keySet = new BitSet(256);
	double playerSpeedControl = 1;
	double player2SpeedControl = 1;
	int count = 0;
	int count2 = 0;
	boolean i,j,k,l,w,a,s,d;
	//TODO SORT THIS OUT
	public ArrayList<Particle> particleArray = new ArrayList<Particle>();
	public ArrayList<Alien> alienArray = new ArrayList<Alien>();
	public ArrayList<Player> playerArray = new ArrayList<Player>();
	public ArrayList<Entity> friendlyArray = new ArrayList<Entity>();
	Particle p;
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
		alienArray.clear();
		playerArray.clear();
		player1 = new Player(panelWidth, panelHeight, panelWidth / 2, panelHeight / 2,Color.GREEN);
		player2 = new Player(panelWidth, panelHeight, panelWidth / 2, panelHeight / 2,Color.BLUE);
		for(int i = 0; i < aliens; i++){
			spawnAlien();
			spawnAlien();
		}
		playerArray.add(player1);
		playerArray.add(player2);
	}
	public void spawnAlien(){
		alienArray.add(new Alien(panelWidth,panelHeight,rand.nextInt(panelWidth),rand.nextInt(panelHeight)));
	}
	public void update(){
		updateEffects();
		doKeyActions();
		moveAliens();
		moveTargets();
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
		drawEffects(g2);
		g2.setColor(Color.BLACK);
//		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//    		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//	    		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	}
	public void drawEffects(Graphics g){
		for(int i = 0; i < particleArray.size();i++){
			p = particleArray.get(i);
			g.setColor(p.color);
			g.fillRect((int)p.x, (int)p.y, 2, 2);
		}
	}
	public void drawTargets(Graphics g){
//		for(int i = 0; i < targetArray.size(); i++){
//			cFriendly = targetArray.get(i);
//			g.setColor(cFriendly.color);
//			g.fillRect((int)cFriendly.x - ((cFriendly.w - 1) / 2), (int)cFriendly.y - ((cFriendly.h - 1) / 2), cFriendly.w ,cFriendly.h);
//		}
	}
	public void drawPlayers(Graphics g){
		for(int i = 0; i < playerArray.size(); i++){
			cFriendly = playerArray.get(i);
			g.setColor(cFriendly.color);
			g.fillRect((int)cFriendly.x - ((cFriendly.w - 1) / 2), (int)cFriendly.y - ((cFriendly.h - 1) / 2), cFriendly.w ,cFriendly.h);

		}
	}
	public void drawAliens(Graphics g){
		for(int i = 0; i < alienArray.size(); i ++){
			cAlien = alienArray.get(i);
			g.setColor(cAlien.color);
			g.fillRect((int)cAlien.x - ((cAlien.w - 1) / 2), (int)cAlien.y - ((cAlien.h - 1) / 2), cAlien.w ,cAlien.h);
		}
	}
	public void moveTargets() {
//		for(int i = 0; i < targetArray.size(); i ++){
//		}
		
	}
	public void moveAliens(){
		for(int i = 0; i < alienArray.size(); i ++){
			cAlien = alienArray.get(i);
			ArrayList<Entity> tempArray = new ArrayList<Entity>();
			tempArray.addAll(playerArray);
			tempArray.addAll(playerArray);
			cAlien.updateTarget(tempArray);
			cAlien.moveAI();
		}
	}
	public void checkCollisions(){
		for(int i = 0; i < alienArray.size(); i ++){
			cAlien = alienArray.get(i);
			for(int j = 0; j < playerArray.size(); j++){
				cFriendly = playerArray.get(j);
				if(cFriendly.dead == false){
					if(GameMath.getDistance(cAlien, cFriendly) < ((cAlien.w + 1) / 2) + ((cFriendly.w + 1) / 2)){
						cFriendly.onCollision();
						particleArray.addAll(Effects.implode(cFriendly.x, cFriendly.y, cFriendly.color));
						particleArray.addAll(Effects.swirlyParticle(cFriendly.x, cFriendly.y, cFriendly.color));
					}
				}
				
			}
		}
	}
	public boolean checkAllDead(){
		boolean allDead = true;
		for(int i = 0; i < playerArray.size();i++){
			if(playerArray.get(i).dead == false){
				allDead = false;
				break;
			}
		}
		return allDead;
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
		count++;
		getKeys();
		if(keySet.get(KeyEvent.VK_Q)){
			if(count > 30){
				count = 0;
				if(playerSpeedControl < 30){
					playerSpeedControl += 1;
				}
			}
		}
		if(keySet.get(KeyEvent.VK_E)){
			if(count > 30){
				count = 0;
				if(playerSpeedControl > 1){
					playerSpeedControl -= 1;
				}
			}
		}
		count2++;
		if(keySet.get(KeyEvent.VK_U)){
			if(count2 > 30){
				count2 = 0;
				if(player2SpeedControl < 30){
					player2SpeedControl += 1;
				}
			}
		}
		if(keySet.get(KeyEvent.VK_O)){
			if(count2 > 30){
				count2 = 0;
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
