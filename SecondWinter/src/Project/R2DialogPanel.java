package Project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class R2DialogPanel extends JPanel{
	private DialogPane dialog;
	private ImageIcon icon = new ImageIcon("src/Image/scoreBoard.png");
	private Image img = icon.getImage();
	private JLabel subText = new JLabel("Score Board");
	private int score = 0;
	private JLabel scoreText = new JLabel("Kill : ");
	
	private ImageIcon icon2 = new ImageIcon("src/Image/fuel.png");
	private Image fuelImg = icon2.getImage().getScaledInstance(40, 50, Image.SCALE_SMOOTH);
	private ImageIcon fuelImage = new ImageIcon(fuelImg);
	
	public R2DialogPanel(DialogPane dialog) {
		this.dialog = dialog;
		setLayout(null);
		
		subText.setFont(new Font("Arial",Font.BOLD,30));
		subText.setSize(330,100);
		subText.setLocation(110,20);
		subText.setForeground(Color.YELLOW);
		
		scoreText.setFont(new Font("Arial",Font.BOLD,30));
		scoreText.setSize(350,120);
		scoreText.setLocation(60,160);
		scoreText.setForeground(Color.RED);
		add(subText);
		add(scoreText);
		
		JLabel mission = new JLabel("[R2] 10마리를 해치우세요!!");
		mission.setFont(new Font("Gothic",Font.BOLD,25));
		mission.setSize(350,30);
		mission.setLocation(60,150);
		mission.setForeground(Color.GREEN);
		add(mission);
		
		JLabel battery = new JLabel("연료 : ");
		battery.setFont(new Font("Gothic",Font.BOLD,30));
		battery.setSize(300,30);
		battery.setLocation(60,255);
		battery.setForeground(Color.ORANGE);
		add(battery);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, dialog.getWidth(), dialog.getHeight(), this);
		g.setFont(new Font("Gothic",Font.BOLD,30));
		g.setColor(Color.RED);
		g.drawString(Integer.toString(dialog.getScore()), 150, 230);
		for(int i=0;i<dialog.getFuel();i++) {
			g.drawImage(fuelImg, 150+i*70,250,40,40, dialog);
		}
	}
}
