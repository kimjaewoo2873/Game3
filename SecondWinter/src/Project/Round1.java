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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import Project.Round2.MonsterAttackThread;

public class Round1 extends JPanel{
	private JFrame frame;
	private ImageIcon icon = new ImageIcon("src/Image/round1.png");
	private Image backImage = icon.getImage();
	private ImageIcon icon2 =  new ImageIcon("src/Image/airplane.png");
	private Image img = icon2.getImage();
	private JLabel airplane = new JLabel();
	private int x,y;
	private Vector<JLabel> bulletVector = new Vector<JLabel>();
	private JLabel bullet;
	private ImageIcon icon3 = new ImageIcon("src/Image/bullet.png");
	private boolean first = false;
	private boolean canShoot = true;
	private Timer reloadTimer;
	private Timer monsterTimer;
	private Vector<JLabel> monsterVector = new Vector<JLabel>();
	private Vector<JLabel> monsterShoot = new Vector<JLabel>();
	private ImageIcon icon4 = new ImageIcon("src/Image/enemy1.png");
	private ImageIcon icon5 = new ImageIcon("src/Image/enemy2.png");
	private ImageIcon icon6 = new ImageIcon("src/Image/attack2.png");
	private JLabel monster;
	private int mx,my;
	private boolean monCheck = false;
	private BulletThread bulletThread;
	private MonsterThread monsterThread;
	private Image scaledImage = icon2.getImage().getScaledInstance(40,30, Image.SCALE_SMOOTH);
	private ImageIcon plane = new ImageIcon(scaledImage);
	
	private Image scaledImage2 = icon3.getImage().getScaledInstance(30, 40, Image.SCALE_SMOOTH);
	private ImageIcon bulletImage = new ImageIcon(scaledImage2);

	private Image scaledImage3 = icon4.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	private ImageIcon enemy1Image = new ImageIcon(scaledImage3);
	
	private Image scaledImage4 = icon5.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	private ImageIcon enemy2Image = new ImageIcon(scaledImage4);
	
	private Image scaledImage5 = icon6.getImage().getScaledInstance(10, 30, Image.SCALE_SMOOTH);
	private ImageIcon meteorImage = new ImageIcon(scaledImage5);
	
	private DialogPane dialog;
	private String name;
	private MonsterAttackThread attackThread;
	
	private int state = 99;
	public Round1(JFrame frame,String name) {
			this.frame = frame;
			this.name = name;
			setLayout(null);
			dialog = new DialogPane();
			
			dialog.setTitle(name);
			
			dialog.setVisible(true);
			airplane.setIcon(plane);
			airplane.setSize(40,30);
			x = frame.getWidth() / 2 - 20;
			y = frame.getHeight() - 160;
			airplane.setLocation(x,y);
		
			add(airplane);
			
			monsterTimer = new Timer(3000, new ActionListener() {
				int i = 0;
				@Override
				public void actionPerformed(ActionEvent e) {
					if(monCheck == false) {
						monsterThread = new MonsterThread();
						monsterThread.start();
						attackThread = new MonsterAttackThread(Round1.this);
						attackThread.start();
						monCheck = true;
					}
					monster = new JLabel();
					monster.setSize(40,40);
					mx = (int)(Math.random()*frame.getWidth() - enemy1Image.getIconWidth() - 30);
					my = (int)(Math.random()*130);
					monster.setLocation(mx,my);
					i = (int)(Math.random()*2);
					if(i == 0) 
						monster.setIcon(enemy1Image);
					else 
						monster.setIcon(enemy2Image);
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
						//System.out.println("왼쪽 x: " + x + ", y: " + y);
		                x -= 10;
		                if (x <= 0) {
		                	x = 0;  // 왼쪽 경계에서 멈추도록 설정
		                }
		                airplane.setLocation(x, y);
		                repaint();
					}
		            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
		            	//System.out.println("오른쪽 x: " + x + ", y: " + y);
		                x += 10;
		                if (x + plane.getIconWidth() + 10 >= frame.getWidth()) {
		                	x = frame.getWidth() - plane.getIconWidth() - 15;  // 오른쪽 경계에서 멈추도록 설정
		                }
		                airplane.setLocation(x, y);
		                repaint();
		            }
		            else if (e.getKeyCode() == KeyEvent.VK_UP) {
		            //	System.out.println("위 x: " + x + ", y: " + y);
		                y -= 10;
		                if (y < 300) {  // 위 경계에서 멈추도록 설정
		                	y = 300;
		                }
		                airplane.setLocation(x, y);
		                repaint();
		            }
		            else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
		              //  System.out.println("아래 x: " + x + ", y: " + y);
		                y += 10;
		                if (y + plane.getIconHeight() + 30>= frame.getHeight()) {  // 아래 경계에서 멈추도록 설정
		                	y = 630;
		                }
		                airplane.setLocation(x, y);
		                repaint();
		            }
		            else if(e.getKeyCode() == KeyEvent.VK_SPACE && canShoot) {
						//System.out.println("총알 발사");
						
						bullet = new JLabel();
						bullet.setIcon(bulletImage);
						bullet.setSize(30, 40);
						bullet.setLocation(x,y);
						add(bullet);
						bulletVector.add(bullet);
						
						canShoot = false; 
						if(reloadTimer == null || !reloadTimer.isRunning()) { // 타이머가 중복 생성되면 재장전이 존나 빨라지는 문제 발생해서 체크해야함
							reloadTimer = new Timer(2000,new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									canShoot = true;
									//System.out.println("재장전 완료! 다시 발사 가능");
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
					//System.out.println("몬스터 사망");
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
				System.out.println("맞음");
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
		private Round1 panel;
		private JLabel meteor;
		private JLabel mon;
		private boolean running = true;
		public MonsterAttackThread(Round1 panel) {
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
					System.out.println("어택 스레드 종료");
					break;
				}
			}
		} 
		// 몬스터가 생성되고 몬스터의 공격이 시작되는거임
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
						meteor.setLocation(mon.getX() + enemy1Image.getIconWidth()/2,mon.getY() + enemy1Image.getIconHeight());
						monsterShoot.add(meteor);
						panel.add(meteor);
					}
				}
			}
		}
	}
	class MonsterThread extends Thread {
		boolean monRunning = true;
	    @Override
	    public void run() {
	        while (monRunning) {
	            try {
	                moveMeteor();
	                if(crushMeteor()) {
	                    revalidate();
	                    repaint();
	                }
	                sleep(300);
	            } catch (InterruptedException e) {
	            	System.out.println("몬스터 스레드 종료");
	            	
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
					//System.out.println("운석 " + i + "지움");
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
			while(!Thread.currentThread().isInterrupted()) {
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
						Round2 round2 = new Round2(frame,dialog);
						R2DialogPanel r2dialogPanel = new R2DialogPanel(dialog);
						monsterThread.stopThread();
						attackThread.interrupt();
						monsterTimer.stop();
						if(reloadTimer != null)
							reloadTimer.stop();
						dialog.setContentPane(r2dialogPanel);
						dialog.revalidate();
						dialog.repaint();
						frame.setContentPane(round2);
						frame.revalidate();
						frame.repaint();
						round2.setFocusable(true);
						round2.requestFocus();
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
