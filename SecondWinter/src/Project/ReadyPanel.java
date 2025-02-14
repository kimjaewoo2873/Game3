package Project;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ReadyPanel extends JPanel{
	private String name;
	private ImageIcon icon = new ImageIcon("src/Image/ready.png");
	private Image backImage = icon.getImage();
	private JFrame frame;
	private ImageIcon icon2 = new ImageIcon("src/Image/1.png");
	private Image one = icon2.getImage();
	private ImageIcon icon3  = new ImageIcon("src/Image/2.png");
	private Image two = icon3.getImage();
	private ImageIcon icon4 = new ImageIcon("src/Image/3.png");
	private Image three = icon4.getImage();
	private Image img[] = { one , two , three };
	private int index = img.length - 1;
	private boolean flag = true;
	public ReadyPanel(JFrame frame,String name) {
		this.frame = frame;
		this.name = name;
		TimerThread timerThread = new TimerThread();
		timerThread.start();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backImage, 0, 0, frame.getWidth(), frame.getHeight(), this);
		if(index >= 0) {
			g.drawImage(img[index], 230,400,200,200,this);
		}
	}
	class TimerThread extends Thread{
		@Override
		public void run() {
			while(flag) {
				try {
					System.out.println(index);
					sleep(1000);
					index--;
					if(index < 0) {
						flag = false;
						Round1 round1 = new Round1(frame,name);
						frame.setContentPane(round1);
						frame.revalidate();
						frame.repaint();
						
						round1.setFocusable(true);
						round1.requestFocus();
					}
					repaint();
				} catch(InterruptedException e) {
					System.out.println("타이머 종료");
					interrupt();
				}
			}
		}
	}
}
