package main;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Main{
	static GamePanel panel;
	static JFrame frame;
	public static int w = 1900;
	public static int h = 1000;
	int speed = 1;
	Dimension preferredSize;
	static Main main;
	public static void main(String[] args){
		start();
//		r is the radius of the circle
//		w = 2 * pi * f, where f is the frequency of the rotation
//		t is the time (s) 
//		
//		x = r * sin(w*t);
//		y = r * cos(w*t);
	}
	public static void gameLoop(){
		//TODO Increase framerate but keep gamespeed?!?!
		while(true){
			panel.update();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void start(){
		frame = new JFrame("2D Game");
		panel = new GamePanel(w,h);
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		//Somehow fix the JFrame so that the panel and frame are 400x400
//		int panelX = (frame.getWidth() - panel.getWidth() - frame.getInsets().left - frame.getInsets().right) / 2;
//        int panelY = ((frame.getHeight() - panel.getHeight() - frame.getInsets().top - frame.getInsets().bottom) / 2);
//		panel.setLocation(panelX, panelY);
		frame.setResizable(false);
		gameLoop();
	}
	
}
