package Project;

import java.awt.Container;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class DialogPane extends JDialog{
	private JFrame frame;
	private ImageIcon icon = new ImageIcon("src/Image/scoreboard.png");
	private Image img = icon.getImage();
	private int score = 0;
	private int bossScore = 0;
	private ImageIcon icon2 = new ImageIcon("src/Image/fuel.png");
	private Image fuelImg = icon2.getImage().getScaledInstance(40, 50, Image.SCALE_SMOOTH);
	private ImageIcon fuelImage = new ImageIcon(fuelImg);
	private int fuelNum = 3;
	public DialogPane() {
		setSize(400,400);
		setLocation(460,20);
		setContentPane(new R1DialogPanel(this));
		System.out.println("생성");
	}
	public void init() {
		score = 0;
	}
	public int getFuel() {
		return fuelNum;
	}
	public void setFuel() {
		fuelNum--;
		System.out.println("연료: " + fuelNum);
	}
	public int getScore() {
		return score;
	}
	public void setScore() {
		score++;
		//System.out.println(score);
	}
	public void drawScore() {
		Container c = getContentPane();
		c.repaint();
		System.out.println("드로우 스코어");
	}
	public void setBossScore() {
		bossScore++;
	}
	public int getBossScore() {
		return bossScore;
	}
}
