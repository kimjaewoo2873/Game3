package Project;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Round2 extends JPanel{
	private JFrame frame;
	private DialogPane dialog;
	private ImageIcon icon = new ImageIcon("src/Image/round2.png");
	private Image backImage = icon.getImage();
	private ImageIcon icon2 = new ImageIcon("src/Image/enemy2.png");
	private ImageIcon icon3 = new ImageIcon("src/Image/enemy3.png");
	private ImageIcon icon4 = new ImageIcon("src/Image/airplane.png");
	private ImageIcon icon5 = new ImageIcon("src/Image/bullet.png");
	private ImageIcon icon6 =  new ImageIcon("src/Image/meteor.png");
	
	private Image img2 = icon2.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	private Image img3 = icon3.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	private Image img4 = icon4.getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH);
	private Image img5 = icon5.getImage().getScaledInstance(30, 40, Image.SCALE_SMOOTH);
	private Image img6 = icon6.getImage().getScaledInstance(10, 30, Image.SCALE_SMOOTH);
	
	private ImageIcon enemy2Image = new ImageIcon(img2);
	private ImageIcon enemy3Image = new ImageIcon(img3);
	private ImageIcon plane = new ImageIcon(img4);
	private ImageIcon bulletImage = new ImageIcon(img5);
	private ImageIcon meteorImage = new ImageIcon(img6);
	
	private JLabel enemy2 = new JLabel();
	private JLabel enemy3 = new JLabel();
	private JLabel airplane = new JLabel();
	private JLabel bullet;
	private JLabel monster;
	
	private Vector<JLabel> bulletVector = new Vector<JLabel>();
	private Vector<JLabel> monsterVector = new Vector<JLabel>();
	private Vector<JLabel> monsterShoot = new Vector<JLabel>();
	
	private boolean canShoot = true;
	private boolean first = false; // 처음 총알 발사시 스레드 스타트 플래그
	private boolean monCheck = false; // 처음 몬스터 생성시 스레드 스타트 플래그
	private Timer reloadTimer;
	private Timer monsterTimer;
	private int x,y; // 비행기 좌표
	private int mx,my; // 몬스터 좌표
	private int state = 99; // 몬스터 잡은 상태 저장받는 변수
	
	private MonsterThread monsterThread;
	private BulletThread bulletThread;
	private MonsterAttackThread attackThread;
	
	public Round2(JFrame frame,DialogPane dialog) {
		this.frame = frame;
		this.dialog = dialog;
		setLayout(null);
		dialog.init();
		airplane.setIcon(plane);
		airplane.setSize(40,30);
		x = frame.getWidth() / 2 - 20;
		y = frame.getHeight() - 160;
		airplane.setLocation(x,y);
		add(airplane);
		
		monsterTimer = new Timer(2000, new ActionListener() {
			int i = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(monCheck == false) {
					monsterThread = new MonsterThread();
					monsterThread.start();
					attackThread = new MonsterAttackThread(Round2.this);
					attackThread.start();
					monCheck = true;
				}
				monster = new JLabel();
				monster.setSize(40,40);
				mx = (int)(Math.random()*frame.getWidth() - enemy2Image.getIconWidth() - 30);
				my = (int)(Math.random()*130);
				monster.setLocation(mx,my);
				i = (int)(Math.random()*2);
				if(i == 0)
					monster.setIcon(enemy2Image); 
				else
					monster.setIcon(enemy3Image);
				add(monster);
				monsterVector.add(monster);
				repaint();
			}
		});
		monsterTimer.start();
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					 x -= 20;
		                if (x <= 0) {
		                	x = 0;  // 왼쪽 경계에서 멈추도록 설정
		                }
		                airplane.setLocation(x, y);
		                repaint();
				}
				else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					x += 20;
	                if (x + plane.getIconWidth() + 10 >= frame.getWidth()) {
	                	x = frame.getWidth() - plane.getIconWidth() - 15;  // 오른쪽 경계에서 멈추도록 설정
	                }
	                airplane.setLocation(x, y);
	                repaint();
				}
				else if(e.getKeyCode() == KeyEvent.VK_UP) {
				    y -= 20;
	                if (y < 300) {  // 위 경계에서 멈추도록 설정
	                	y = 300;
	                }
	                airplane.setLocation(x, y);
	                repaint();
				}
				else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					  y += 20;
		                if (y + plane.getIconHeight() + 30>= frame.getHeight()) {  // 아래 경계에서 멈추도록 설정
		                	y = 630;
		                }
		                airplane.setLocation(x, y);
		                repaint();
				}
				else if(e.getKeyCode() == KeyEvent.VK_SPACE && canShoot) {
					bullet = new JLabel();
					bullet.setIcon(bulletImage);
					bullet.setSize(30, 40);
					bullet.setLocation(x,y);
					add(bullet);
					bulletVector.add(bullet);
					
					canShoot = false;
					if(reloadTimer == null || !reloadTimer.isRunning()) {
						reloadTimer = new Timer(1500, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								canShoot = true;
							}
						});
						reloadTimer.start();
					}
					if(first == false) {
						bulletThread = new BulletThread();
						bulletThread.start();
						first = true;
					}
				}
			}
		});
	} 
	public boolean deleteCheck() {
		JLabel mon;
		JLabel bul;
		boolean flag = false;
		for(int i=0;i<monsterVector.size();i++) {
			mon = monsterVector.get(i);
			for(int j=0;j<bulletVector.size();j++) {
				bul = bulletVector.get(j);
				Rectangle monsterBounds = new Rectangle(mon.getX(), mon.getY(), enemy2Image.getIconWidth(),enemy2Image.getIconHeight());
				Rectangle bulletBounds = new Rectangle(bul.getX(), bul.getY(), bulletImage.getIconWidth(), bulletImage.getIconHeight());
				if(monsterBounds.intersects(bulletBounds)) {
					remove(mon); // JLabel 지우기
					remove(bul);
					monsterVector.remove(i);
					bulletVector.remove(j);
					flag = true;
				}
			}
		}
		return flag;
	}
	public boolean crushMeteor() {
		JLabel met;
		boolean flag = false;
		for(int i=0;i<monsterShoot.size();i++) {
			met = monsterShoot.get(i);
			Rectangle meteorBounds = new Rectangle(met.getX(), met.getY(), meteorImage.getIconWidth(), meteorImage.getIconHeight());
			Rectangle airplaneBounds = new Rectangle(airplane.getX(), airplane.getY(), plane.getIconWidth(), plane.getIconWidth());
			if(meteorBounds.intersects(airplaneBounds)) {
				dialog.setFuel(); // 감소
				state = dialog.getFuel();
				remove(met);
				monsterShoot.remove(i);
				if(state == 0) {
					dialog.setVisible(false);
					attackThread.interrupt();
					monsterThread.stopThread();
					monsterTimer.stop();
					if(bulletThread != null)
						bulletThread.interrupt();
					if(reloadTimer != null)
						reloadTimer.stop();
					GameOver overPanel = new GameOver(frame);
					frame.setContentPane(overPanel);
					frame.revalidate();
					frame.repaint();
				}
				dialog.repaint();
				flag = true;
			}
		}
		return flag;
	}
	class MonsterAttackThread extends Thread{ // 몬스터의 공격 스레드
		private Round2 panel;
		private JLabel meteor;
		private JLabel mon;
		private boolean running = true;
		public MonsterAttackThread(Round2 panel) {
			this.panel = panel;
		}
		@Override
		public void run() {
			while(running) {
				try {
					attack();
					sleep(400);
				} catch (InterruptedException e) {
					running = false;
					break;
				}
			}
		} // 몬스터가 생성되고 몬스터의 공격이 시작되는거임
		public void attack() {
			if(monsterVector.size() != 0) {
				for(int i=0;i<monsterVector.size();i++) {
					int rand = (int)(Math.random()*5);
					if(rand == 0) {
						//System.out.println("운석 생성");
						meteor = new JLabel();
						meteor.setIcon(meteorImage);
						mon = monsterVector.get(i);
						meteor.setSize(30,40);
						meteor.setLocation(mon.getX() + enemy2Image.getIconWidth()/2,mon.getY() + enemy2Image.getIconHeight());
						monsterShoot.add(meteor);
						panel.add(meteor);
					}
				}
			}
		}
	}
	
	class MonsterThread extends Thread {
		private boolean monRunning = true;
	    @Override
	    public void run() {
	        while (monRunning) {
	            try {
	                moveMeteor();
	                if (crushMeteor()) {
	                    revalidate();
	                    repaint();
	                }
	                sleep(300);
	            } catch (InterruptedException e) {
	                break;
	            }
	        }
	    }
	    public void stopThread() {
	    	monRunning = false;
	    	interrupt();
	    }
		public void moveMonster() {
			int mmx = 0, mmy = 0;
			for(int i=0;i<monsterVector.size();i++) {
				JLabel mon = monsterVector.get(i);
				mmx = mon.getX();
				mmy = mon.getY();
				if(mmy >= frame.getHeight() + 30) {
					remove(mon);
					monsterVector.remove(i);
				}
				mmy += 10;
				mon.setLocation(mmx,mmy);
				repaint();
			}
		}
		public void moveMeteor() {
			int meteorX = 0, meteorY = 0;
			for(int i=0;i<monsterShoot.size();i++) {
				JLabel meteor = monsterShoot.get(i);
				meteorX = meteor.getX();
				meteorY = meteor.getY();
				if(meteorX >= frame.getHeight() + 30) {
					remove(meteor);
					monsterShoot.remove(i);
				}
				meteorY += 20;
				meteor.setLocation(meteorX, meteorY);
				repaint();
			}
		}
	}
	
	class BulletThread extends Thread{
		boolean kill = false;
		int purpose = 0;
		boolean pass = false;
		@Override
		public void run() {
			while(true) {
				try {
					moveBullet();
					sleep(100);
					kill = deleteCheck();
					if(kill == true) {
						dialog.setScore();
						dialog.drawScore();
						purpose = dialog.getScore(); // 스코어 가져오기
						if(purpose == 2) {
							pass = true;
						}
						revalidate();
						repaint();
					}
					if(pass == true) { // 다이얼로그 패널 바꿔야함
						System.out.println("다음 라운드");
						attackThread.interrupt();
						monsterThread.stopThread();
						if(reloadTimer != null)
							reloadTimer.stop();
						Round3 round3 = new Round3(frame,dialog);
						R3DialogPanel r3dialogPanel = new R3DialogPanel(dialog);
						dialog.setContentPane(r3dialogPanel);
						dialog.revalidate();
						dialog.repaint();
						frame.setContentPane(round3);
						frame.revalidate();
						frame.repaint();
						round3.setFocusable(true);
						round3.requestFocus();
						interrupt();
					} 
				} catch (InterruptedException e) {
					System.out.println("불릿 스레드 종료");
					Thread.currentThread().interrupt();
					break;
				}
			}
		}
		public void moveBullet() {
			int bx = 0, by = 0 ;
			for(int i=0;i<bulletVector.size();i++) {
				JLabel bul = bulletVector.get(i);
				bx = bul.getX();
				by = bul.getY();
				if(by <= -50){
					bulletVector.remove(i);
				}
				by -= 40;
				bul.setLocation(bx, by);
				repaint();
			}
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backImage, 0, 0, frame.getWidth(), frame.getHeight(), this);
	}
}
