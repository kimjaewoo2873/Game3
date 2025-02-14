package Project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class StartPanel extends JPanel{
	private JFrame frame;

	private ImageIcon icon = new ImageIcon("src/Image/startlog.png");
	private Image backImage = icon.getImage();
	private ImageIcon icon2 = new ImageIcon("src/Image/logframe.png");
	private Image logFrame = icon2.getImage();
	private ImageIcon icon3 = new ImageIcon("src/Image/pilot.png");
	private JLabel pilotImage = new JLabel();
	private ImageIcon startImage = new ImageIcon("src/Image/start.png");
	private JLabel startBtn = new JLabel();
	private ImageIcon startUpImage = new ImageIcon("src/Image/startup.png");
	private ImageIcon icon4 = new ImageIcon("src/Image/sub.png");
	private Image sub = icon4.getImage();
	private ImageIcon icon5 = new ImageIcon("src/Image/title.png");
	private Image sub2 = icon5.getImage();
	private Image img[] = {sub,sub2};
	private int index = 0;
	private JTextField id = new JTextField(20);
	private String name = null;

	public StartPanel(JFrame frame) {
		this.frame = frame;
		
	
		setLayout(null);
		id.setLocation(200,450);
		id.setSize(120,20);
		id.setBackground(Color.BLACK);
		id.setForeground(Color.WHITE);
		
		BorderThread borderThread = new BorderThread();
		TitleThread titleThread = new TitleThread();
		borderThread.start();
		titleThread.start();
		
		id.setFont(new Font("Gulim",Font.BOLD,15));
		add(id);
		
		Image scaledImage1 = icon3.getImage().getScaledInstance(190,170, Image.SCALE_SMOOTH);
	    ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);
	    pilotImage.setIcon(scaledIcon1);

	    // pilotImage 설정
	    pilotImage.setSize(200,200);
	    pilotImage.setLocation(290, 400);
	    add(pilotImage);
	    
	    Image scaledImage2 = startImage.getImage().getScaledInstance(90,40, Image.SCALE_SMOOTH);
	    ImageIcon scaledIcon2 = new ImageIcon(scaledImage2);
	    startBtn.setIcon(scaledIcon2);
	    startBtn.setSize(90,40);
	    startBtn.setLocation(220,488);
	    add(startBtn);
	    
	    // 스타트 버튼에 마우스 올라갔을때 이미지 사이즈 조정
	    Image scaledImage3 = startUpImage.getImage().getScaledInstance(90,40, Image.SCALE_SMOOTH);
	    ImageIcon scaledIcon3 = new ImageIcon(scaledImage3);
	    
	    startBtn.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mousePressed(MouseEvent e) {
	    		System.out.println("눌름");
	    		name = id.getText();
	    		borderThread.interrupt();
	    		titleThread.interrupt();
	    		
	    		frame.setContentPane(new ReadyPanel(frame,name));
	    		frame.revalidate();
	    		frame.repaint();
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
	    		startBtn.setIcon(scaledIcon3);
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		startBtn.setIcon(scaledIcon2);
	    	}
	    });
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backImage, 0, 0, frame.getWidth(), frame.getHeight(), this);
		g.drawImage(logFrame, 150, 400, 350, 200, this);
		g.drawImage(img[index], 160,60,340,200,this);
	}
	class TitleThread extends Thread{
		@Override
		public void run() {
			while(!isInterrupted()) {
				try {
					sleep(400);
					index++;
					index %= img.length;
					repaint();
				}
				catch(InterruptedException e) {
					System.out.println("타이틀 종료");
					interrupt();
				
				}
			}
		}
	}
	class BorderThread extends Thread{
		Color color[] = {Color.BLACK,Color.BLUE,Color.MAGENTA,Color.DARK_GRAY};
		@Override
		public void run() {
			int i = 0;
			while(!isInterrupted()) {
				try {
					sleep(200);
					id.setBorder(new LineBorder(color[i], 2));	
					i++;
					i %= color.length;
				} catch(InterruptedException e){
					System.out.println("테두리 종료");
					interrupt();
				}
			}
		}
	}
}
