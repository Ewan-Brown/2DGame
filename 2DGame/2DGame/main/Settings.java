package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;
import javax.swing.JButton;

public class Settings {

	JFrame frame;
	private JTextField txtPWidth;
	private JTextField txtPHeight;
	private JTextField txtGheight;
	private JTextField txtGWidth;
	private JLabel lblGameDim;
	private JTextField txtAliens;
	private JTextField txtTargets;
	private JLabel lblTargets;
	private JTextField txtMissiles;
	private JLabel lblMissiles;
	private JTextField txtWalls;
	private JLabel lblWalls;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Settings window = new Settings();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Settings() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 540, 266);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblPanelDim = new JLabel("Panel Dimensions");
		lblPanelDim.setBounds(12, 13, 140, 26);
		
		txtPWidth = new JTextField();
		txtPWidth.setBounds(12, 44, 76, 22);
		txtPWidth.setColumns(10);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblPanelDim);
		frame.getContentPane().add(txtPWidth);
		
		txtPHeight = new JTextField();
		txtPHeight.setColumns(10);
		txtPHeight.setBounds(100, 44, 76, 22);
		frame.getContentPane().add(txtPHeight);
		
		txtGheight = new JTextField();
		txtGheight.setColumns(10);
		txtGheight.setBounds(100, 110, 76, 22);
		frame.getContentPane().add(txtGheight);
		
		txtGWidth = new JTextField();
		txtGWidth.setColumns(10);
		txtGWidth.setBounds(12, 110, 76, 22);
		frame.getContentPane().add(txtGWidth);
		
		lblGameDim = new JLabel("Game Dimensions");
		lblGameDim.setBounds(12, 79, 112, 26);
		frame.getContentPane().add(lblGameDim);
		
		JRadioButton followCam = new JRadioButton("Camera Follows Player");
		followCam.setBounds(12, 141, 164, 25);
		frame.getContentPane().add(followCam);
		
		JLabel lblEntities = new JLabel("Entities");
		lblEntities.setBounds(240, 18, 56, 16);
		frame.getContentPane().add(lblEntities);
		
		JLabel lblAliens = new JLabel("Aliens");
		lblAliens.setBounds(210, 47, 56, 16);
		frame.getContentPane().add(lblAliens);
		
		txtAliens = new JTextField();
		txtAliens.setBounds(278, 44, 44, 22);
		frame.getContentPane().add(txtAliens);
		txtAliens.setColumns(10);
		
		txtTargets = new JTextField();
		txtTargets.setColumns(10);
		txtTargets.setBounds(278, 79, 44, 22);
		frame.getContentPane().add(txtTargets);
		
		lblTargets = new JLabel("Targets");
		lblTargets.setBounds(210, 82, 112, 16);
		frame.getContentPane().add(lblTargets);
		
		txtMissiles = new JTextField();
		txtMissiles.setColumns(10);
		txtMissiles.setBounds(278, 110, 44, 22);
		frame.getContentPane().add(txtMissiles);
		
		lblMissiles = new JLabel("Missiles");
		lblMissiles.setBounds(210, 113, 112, 16);
		frame.getContentPane().add(lblMissiles);
		
		txtWalls = new JTextField();
		txtWalls.setColumns(10);
		txtWalls.setBounds(278, 141, 44, 22);
		frame.getContentPane().add(txtWalls);
		
		lblWalls = new JLabel("Walls");
		lblWalls.setBounds(210, 144, 112, 16);
		frame.getContentPane().add(lblWalls);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.setBounds(386, 93, 97, 25);
		frame.getContentPane().add(btnEnter);
	}
}
