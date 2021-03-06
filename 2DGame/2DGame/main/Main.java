package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.KeyStroke;

import settings.ControlSet;
import settings.Settings;

public class Main{
	static Game panel;
	static JFrame frame;
	static Settings settings;
	public static int w = 1000;
	public static int h = 1000;
	int speed = 1;
	Dimension preferredSize;
	static Main main;
	static ControlSet[] controls = new ControlSet[2];
	public static Random rand = new Random();
	public static void main(String[] args){
		try {
			updateControls();
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}
	public static void updateControls() throws IOException{
		List<String> text = TextFileReader.readFile("Controls.text");
		int lineNum = 0;
		int[] keys = new int[11];
		KeyStroke stroke;
		for(int i = 0; i < 1; i++){
			for(int j = 0; j < 11;j++){
				stroke = KeyStroke.getKeyStroke(text.get(lineNum));
				keys[j] = stroke.getKeyCode();
				lineNum++;
			}
			controls[i] = new ControlSet(keys);
		}


	}
	public static void gameLoop(){
		//TODO Increase framerate but keep gamespeed?!?!
		while(true){
			panel.update();

			try {
				Thread.sleep(7);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void start(){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					settings = new Settings();
					settings.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(settings != null){
				if(settings.done){
					break;
				}
			}
		}
		frame = new JFrame("2D Game");
		panel = new Game();
		settings.setGameData(panel, controls);
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		//TODO fix frame/panel
		//Somehow fix the JFrame so that the panel and frame are 400x400
		//		int panelX = (frame.getWidth() - panel.getWidth() - frame.getInsets().left - frame.getInsets().right) / 2;
		//        int panelY = ((frame.getHeight() - panel.getHeight() - frame.getInsets().top - frame.getInsets().bottom) / 2);
		//		panel.setLocation(panelX, panelY);
		frame.setResizable(false);
		panel.startGame();
		gameLoop();
	}

}
