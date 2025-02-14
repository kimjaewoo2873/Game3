package Project;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class MainPro extends JFrame{
	public MainPro() {
		setTitle("Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	

		StartPanel startPanel = new StartPanel(this);
		setContentPane(startPanel);
		setSize(650,700);
		setLocation(850,20);
		setVisible(true);
		
	}
	public static void main(String[] args) {
		new MainPro();
	}
}
