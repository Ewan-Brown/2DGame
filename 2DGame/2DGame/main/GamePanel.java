package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import javax.swing.JPanel;

import effects.Effects;
import effects.Particle;

public class GamePanel extends JPanel implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Player player1;
	Player player2;
	Alien cAlien;
	int aliens = 1;;
	int panelWidth;
	int panelHeight;
	enum Direction{Left,Right,Up,Down};
	Direction direction;
	BitSet keySet = new BitSet(256);
	double playerSpeedControl = 1;
	double player2SpeedControl = 1;
	int count = 0;
	int count2 = 0;
	boolean sprint;
	boolean i,j,k,l,w,a,s,d;
	public ArrayList<Particle> particleArray = new ArrayList<Particle>();
	public ArrayList<Alien> alienArray = new ArrayList<Alien>();
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
		player1 = new Player(panelWidth, panelHeight, panelWidth / 2, panelHeight / 2,Color.GREEN);
		player2 = new Player(panelWidth, panelHeight, panelWidth / 2, panelHeight / 2,Color.BLUE);
		for(int i = 0; i < aliens; i++){
			alienArray.add(new Alien(panelWidth,panelHeight,rand.nextInt(panelHeight - 4),rand.nextInt(panelWidth - 4)));
		}
	}

	public void update(){
		updateEffects();
		doKeyActions();
		moveAlien();
		checkCollisions();
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
		drawPlayer(g2);
		drawPlayer2(g2);
		drawAliens(g2);
		drawEffects(g2);
		g.setColor(Color.BLACK);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
    		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	}
	public void drawEffects(Graphics g){
		for(int i = 0; i < particleArray.size();i++){
			p = particleArray.get(i);
			g.setColor(p.color);
			g.drawRect((int)p.x, (int)p.y, 1, 1);
		}
	}
	public void drawPlayer(Graphics g){
		g.setColor(player1.color);
		// TODO MAKE IT SO IT GETS PANELHEIGHT - PLAYER.Y SO THINGS ARE THE RIGHT WAY AROUND! (UPSIDE DOWN)
		g.fillRect((int)player1.x - ((player1.w - 1) / 2), (int)player1.y - ((player1.h - 1) / 2), player1.w ,player1.h);
//		g.fillRect(player.x, player.y, 1, 1);
//		g.drawLine(0, 0, 400, 400);
	}
	public void drawPlayer2(Graphics g){
		g.setColor(player2.color);
		g.fillRect((int)player2.x - ((player2.w - 1) / 2), (int)player2.y - ((player2.h - 1) / 2), player2.w ,player2.h);

	}
	public void drawAliens(Graphics g){
		for(int i = 0; i < alienArray.size(); i ++){
			cAlien = alienArray.get(i);
			g.setColor(cAlien.color);
			g.fillRect((int)cAlien.x - ((cAlien.w - 1) / 2), (int)cAlien.y - ((cAlien.h - 1) / 2), cAlien.w ,cAlien.h);
		}
	}
	public void moveAlien(){
		for(int i = 0; i < alienArray.size(); i ++){
			cAlien = alienArray.get(i);
			cAlien.updateTarget(player1, player2);
			cAlien.moveAlien();
		}
	}
	public void checkCollisions(){
		//Create a for loop for if there's more than 1 alien
		for(int i = 0; i < alienArray.size(); i ++){
			cAlien = alienArray.get(i);
			if((GameMath.getDistance(player1,cAlien) < ((player1.w + 1) / 2)  + (cAlien.w + 1) / 2)){
				if(player1.dead == false){
					player1.onCollision();
//					particleArray.addAll(Effects.explode(player1.x, player1.y,player1.color));
					particleArray.addAll(Effects.fireworks(Effects.explode(player1.x, player1.y,player1.color)));
					particleArray.addAll(Effects.swirlyParticle(player1.x, player1.y, player1.color));
				}
			}
			if((GameMath.getDistance(player2,cAlien) < ((player2.w + 1) / 2) + (cAlien.w + 1) / 2)){
				if(player2.dead == false){
					player2.onCollision();
					
//					particleArray.addAll(Effects.explode(player2.x, player2.y,player2.color));
					particleArray.addAll(Effects.fireworks(Effects.explode(player2.x, player2.y,player2.color)));
					particleArray.addAll(Effects.swirlyParticle(player2.x, player2.y, player2.color));
				}
			}
		}
		if(player1.dead == true && player2.dead == true){
			//TODO make this a full 'restart' method 
			startGame();
		}
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
