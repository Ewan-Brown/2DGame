package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

public class Main{
	static GamePanel panel;
	static JFrame frame;
	public static int w = 400;
	public static int h = 400;
	int speed = 1;
	Dimension preferredSize;
	static Main main;
	public static void main(String[] args){
		Main();
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
	public static void Main(){
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
