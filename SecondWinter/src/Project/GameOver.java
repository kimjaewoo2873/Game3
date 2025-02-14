package Project;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameOver extends JPanel{
	private ImageIcon icon1 = new ImageIcon("src/Image/gameOver.png");
	private Image img1 = icon1.getImage();
	private ImageIcon icon2 = new ImageIcon("src/Image/gameOver2.png");
	private Image img2 = icon2.getImage();
	private int num = 0;
	private JFrame frame;
	private int time = 0;
	public GameOver(JFrame frame) {
		this.frame = frame;
		ScreenThread screenThread = new ScreenThread();
		screenThread.start();
	}
	class ScreenThread extends Thread{
		@Override
		public void run() {
			while(time <= 7) {
				try {
					sleep(300);
					if(num == 0) 
						num = 1;
					else
						num = 0;
					time++;
					repaint();
				} catch (InterruptedException e) {
					System.out.println("스크린스레드 종료");
					Thread.currentThread().interrupt();
					break;
				}
			}
			onStartPanel();
		}
	}
	public void onStartPanel() {
		StartPanel startPanel = new StartPanel(frame);
		frame.setContentPane(startPanel);
		frame.revalidate();
		frame.repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(num == 0) {
			g.drawImage(img1, 0, 0, frame.getWidth(), frame.getHeight(),this);
		}
		else {
			g.drawImage(img2, 0, 0, frame.getWidth(), frame.getHeight(),this);
		}
	}
}
