package settings;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import main.Game;

public class Settings {

	public JFrame frame;
	public boolean done = false;
	private JTextField txtPWidth;
	private JTextField txtPHeight;
	private JTextField txtGHeight;
	private JTextField txtGWidth;
	private JLabel lblGameDim;
	private JTextField txtAliens;
	private JTextField txtTargets;
	private JLabel lblTargets;
	private JTextField txtMissiles;
	private JLabel lblMissiles;
	private JTextField txtWalls;
	private JLabel lblWalls;
	private JTextField txtBreeders;
	private JRadioButton followCam;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
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
	public void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 540, 266);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblPanelDim = new JLabel("Panel Dimensions");
		lblPanelDim.setBounds(12, 13, 140, 26);
		
		txtPWidth = new JTextField();
		txtPWidth.setText("1920");
		txtPWidth.setBounds(12, 44, 76, 22);
		txtPWidth.setColumns(10);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblPanelDim);
		frame.getContentPane().add(txtPWidth);
		
		txtPHeight = new JTextField();
		txtPHeight.setText("1080");
		txtPHeight.setColumns(10);
		txtPHeight.setBounds(100, 44, 76, 22);
		frame.getContentPane().add(txtPHeight);
		
		txtGHeight = new JTextField();
		txtGHeight.setText("1080");
		txtGHeight.setColumns(10);
		txtGHeight.setBounds(100, 110, 76, 22);
		frame.getContentPane().add(txtGHeight);
		
		txtGWidth = new JTextField();
		txtGWidth.setText("1920");
		txtGWidth.setColumns(10);
		txtGWidth.setBounds(12, 110, 76, 22);
		frame.getContentPane().add(txtGWidth);
		
		lblGameDim = new JLabel("Game Dimensions");
		lblGameDim.setBounds(12, 79, 112, 26);
		frame.getContentPane().add(lblGameDim);
		
		followCam = new JRadioButton("Camera Follows Player");
		followCam.setBounds(12, 141, 164, 25);
		frame.getContentPane().add(followCam);
		
		JLabel lblEntities = new JLabel("Entities");
		lblEntities.setBounds(240, 18, 56, 16);
		frame.getContentPane().add(lblEntities);
		
		JLabel lblAliens = new JLabel("Aliens");
		lblAliens.setBounds(210, 47, 56, 16);
		frame.getContentPane().add(lblAliens);
		
		txtAliens = new JTextField();
		txtAliens.setText("3");
		txtAliens.setBounds(278, 44, 44, 22);
		frame.getContentPane().add(txtAliens);
		txtAliens.setColumns(10);
		
		txtTargets = new JTextField();
		txtTargets.setText("1");
		txtTargets.setColumns(10);
		txtTargets.setBounds(278, 79, 44, 22);
		frame.getContentPane().add(txtTargets);
		
		lblTargets = new JLabel("Targets");
		lblTargets.setBounds(210, 82, 112, 16);
		frame.getContentPane().add(lblTargets);
		
		txtMissiles = new JTextField();
		txtMissiles.setText("0");
		txtMissiles.setColumns(10);
		txtMissiles.setBounds(278, 110, 44, 22);
		frame.getContentPane().add(txtMissiles);
		
		lblMissiles = new JLabel("Missiles");
		lblMissiles.setBounds(210, 113, 112, 16);
		frame.getContentPane().add(lblMissiles);
		
		txtWalls = new JTextField();
		txtWalls.setText("10");
		txtWalls.setColumns(10);
		txtWalls.setBounds(278, 141, 44, 22);
		frame.getContentPane().add(txtWalls);
		
		lblWalls = new JLabel("Walls");
		lblWalls.setBounds(210, 144, 112, 16);
		frame.getContentPane().add(lblWalls);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.setBounds(386, 93, 97, 25);
		frame.getContentPane().add(btnEnter);
		btnEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
		    {
				done = true;
		    }
		});
		
		txtBreeders = new JTextField();
		txtBreeders.setText("0");
		txtBreeders.setColumns(10);
		txtBreeders.setBounds(278, 173, 44, 22);
		frame.getContentPane().add(txtBreeders);
		
		JLabel lblBreeders = new JLabel("Breeders");
		lblBreeders.setBounds(210, 176, 112, 16);
		frame.getContentPane().add(lblBreeders);
		
	}
	public void setWindowData(List<String> data){
		
	}
	public void setGameData(Game game,ControlSet[] controls){
		game.aliens = Integer.parseInt(txtAliens.getText());
		game.missiles = Integer.parseInt(txtMissiles.getText());
		game.breeders = Integer.parseInt(txtBreeders.getText());
		game.walls = Integer.parseInt(txtWalls.getText());
		game.targets = Integer.parseInt(txtTargets.getText());
		game.cameraFollowPlayer = followCam.isSelected();
		game.init(Integer.parseInt(txtGWidth.getText()), Integer.parseInt(txtGHeight.getText()), controls);
	}
}
