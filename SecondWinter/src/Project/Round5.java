package Project;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import Project.Round4.BulletThread;
import Project.Round4.MonsterAttackThread;
import Project.Round4.MonsterThread;

public class Round5 extends JPanel{
	private JFrame frame;
	private DialogPane dialog;
	private ImageIcon icon1 = new ImageIcon("src/Image/round5.png");
	private ImageIcon icon2 = new ImageIcon("src/Image/enemy4.png");
	private ImageIcon icon3 = new ImageIcon("src/Image/newEnemy4.png");
	private ImageIcon icon4 = new ImageIcon("src/Image/enemy5.png");
	private ImageIcon icon5 = new ImageIcon("src/Image/boss.png");
	private ImageIcon icon6 = new ImageIcon("src/Image/airplane.png");
	private ImageIcon icon7 = new ImageIcon("src/Image/bullet.png");
	private ImageIcon icon8 = new ImageIcon("src/Image/meteor.png");
	private ImageIcon icon9 = new ImageIcon("src/Image/bossAttack1.png");
	private ImageIcon icon10 = new ImageIcon("src/Image/bossAttack2.png");
	
	private Image img1 = icon1.getImage();
	private Image img2 = icon2.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
	private Image img3 = icon3.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
	private Image img4 = icon4.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	private Image img5 = icon5.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
	private Image img6 = icon6.getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH);
	private Image img7 = icon7.getImage().getScaledInstance(30, 40, Image.SCALE_SMOOTH);
	private Image img8 = icon8.getImage().getScaledInstance(10, 30, Image.SCALE_SMOOTH);
	private Image img9 = icon9.getImage().getScaledInstance(100,90, Image.SCALE_SMOOTH);
	private Image img10 = icon10.getImage().getScaledInstance(30, 40, Image.SCALE_SMOOTH);
	
	private ImageIcon enemy4Image = new ImageIcon(img2);
	private ImageIcon newEnemy4Image = new ImageIcon(img3);
	private ImageIcon enemy5Image = new ImageIcon(img4);
	private ImageIcon bossImage = new ImageIcon(img5);
	private ImageIcon plane = new ImageIcon(img6);
	private ImageIcon bulletImage = new ImageIcon(img7);
	private ImageIcon meteorImage = new ImageIcon(img8);
	private ImageIcon bossAttackImage1 = new ImageIcon(img9);
	private ImageIcon bossAttackImage2 = new ImageIcon(img10);
	
	private JLabel airplane = new JLabel();
	private JLabel bullet;
	private JLabel monster;
	private JLabel boss;
	private JLabel bossAttack1;
	private JLabel bossAttack2;
	
	private Vector<JLabel> bulletVector = new Vector<JLabel>();
	private Vector<JLabel> monsterVector = new Vector<JLabel>(); // 고정 몬스터
	private Vector<JLabel> moveMonsterVector = new Vector<JLabel>(); // 움직이는 몬스터 
	private Vector<JLabel> monsterShoot = new Vector<JLabel>();
	private Vector<JLabel> bossShoot = new Vector<JLabel>();
	
	private ArrayList<Integer> monsterList = new ArrayList<Integer>();
	private boolean canShoot = true;
	private boolean first = false; // 처음 총알 발사시 스레드 스타트 플래그
	private boolean monCheck = false; // 처음 몬스터 생성시 스레드 스타트 플래그
	private boolean bossFlag = false; // 보스 생성 조건 플래그
	private boolean createBoss = false;
	private Timer reloadTimer;
	private Timer monsterTimer;

	private int x,y; // 비행기 좌표
	private int mx,my; // 고정 몬스터 좌표
	private int state = 99; // 연료개수 저장할 변수
	private int infiniteHeight = 130;
	
	private MonsterThread monsterThread;
	private BulletThread bulletThread;
	private MonsterAttackThread attackThread;
	public Round5(JFrame frame,DialogPane dialog) {
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
				if(bossFlag == true) {
					boss = new JLabel();
					boss.setIcon(bossImage);
					boss.setSize(200,200);
					boss.setLocation(frame.getWidth()/2 - 120 , 20);
					add(boss);
					removeMonster();
					removeMoveMonster();
					System.out.println("보스 생성");
					bossFlag = false;
					createBoss = true;
				}
				if(monCheck == false) {
					monsterThread = new MonsterThread();
					monsterThread.start();
					attackThread = new MonsterAttackThread(Round5.this);
					attackThread.start();
					monCheck = true;
				}
				monster = new JLabel();
				i = (int)(Math.random()*2);
				if(i == 0) { // enemy3
					mx = (int)(Math.random()*frame.getWidth() - enemy5Image.getIconWidth() - 30);
					my = (int)(Math.random()*infiniteHeight);
					monster.setLocation(mx,my);
					monster.setIcon(enemy5Image);
					monster.setSize(40,40);
					monsterVector.add(monster);
				}
				else { // enemy4
					mx = (int)(Math.random()*frame.getWidth() - enemy4Image.getIconWidth() - 30);
					my = (int)(Math.random()*130);
					monster.setLocation(mx,my);
					monster.setIcon(enemy4Image);
					monster.setSize(70,100);
					moveMonsterVector.add(monster);
					monsterList.add(0);
				}
				add(monster);
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
					bullet.setLocation(x, y);
					add(bullet);
					bulletVector.add(bullet);
					
					canShoot = false;
					if(reloadTimer == null || !reloadTimer.isRunning()) {
						reloadTimer = new Timer(150, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								canShoot = true;
							}
						});
						reloadTimer.start();
					}
				}
				if(first == false) {
					bulletThread = new BulletThread();
					bulletThread.start();
					first = true;
				}
			}
		});
	}
	public void removeMonster() {
		Rectangle bossBounds = new Rectangle(boss.getX(), boss.getY(), bossImage.getIconWidth(), bossImage.getIconHeight());
		for(int i=0;i<monsterVector.size(); i++) {
			JLabel mon = monsterVector.get(i);
			Rectangle monsterBounds = new Rectangle(mon.getX(), mon.getY(), enemy5Image.getIconWidth(), enemy5Image.getIconHeight());
			if(bossBounds.intersects(monsterBounds)) {
				remove(mon);
				monsterVector.remove(i);
			}
		}
		repaint();
		System.out.println("고정 몬스터 지움");
	}
	public void removeMoveMonster() {
		Rectangle bossBounds = new Rectangle(boss.getX(), boss.getY(), bossImage.getIconWidth(), bossImage.getIconHeight());
		for(int i=0;i<moveMonsterVector.size(); i++) {
			JLabel mon = moveMonsterVector.get(i);
			Rectangle monsterBounds = new Rectangle(mon.getX(), mon.getY(), enemy4Image.getIconWidth(), enemy4Image.getIconHeight());
			if(bossBounds.intersects(monsterBounds)) {
				remove(mon);
				moveMonsterVector.remove(i);
			}
		}
		repaint();
		System.out.println("움직이는 몬스터 지움");
	}
	public boolean deleteFixedCheck() { // 고정 몬스터 전용
		JLabel mon;
		JLabel bul;
		boolean flag = false;
		for(int i=0;i<monsterVector.size();i++) {
			mon = monsterVector.get(i);
			for(int j=0;j<bulletVector.size();j++) {
				bul = bulletVector.get(j);
				Rectangle monsterBounds = new Rectangle(mon.getX(), mon.getY(), enemy5Image.getIconWidth(), enemy5Image.getIconHeight());
				Rectangle bulletBounds = new Rectangle(bul.getX(), bul.getY(), bulletImage.getIconWidth(), bulletImage.getIconHeight());
				if(monsterBounds.intersects(bulletBounds)) {
					remove(mon);
					remove(bul);
					monsterVector.remove(i);
					bulletVector.remove(j);
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
	public boolean deleteBoss() { // 고정 몬스터 전용
		JLabel bul;
		boolean flag = false;
		for(int i=0;i<bulletVector.size(); i++) {
			bul = bulletVector.get(i);
			Rectangle bossBounds = new Rectangle(boss.getX(), boss.getY(), bossImage.getIconWidth(), bossImage.getIconHeight());
			Rectangle bulletBounds = new Rectangle(bul.getX(), bul.getY(), bulletImage.getIconWidth(), bulletImage.getIconHeight());	
			if(bossBounds.intersects(bulletBounds)) {
				System.out.println("보스 공격당함");
				dialog.setBossScore();
				remove(bul);
				bulletVector.remove(i);
				flag = true;
			}
		}
		return flag;
	}
	private boolean deleteMovedCheck() {
		JLabel mon;
		JLabel bul;
		int shoot = 0;
		boolean flag = false;
		for(int i=0;i<moveMonsterVector.size();i++) {
			mon = moveMonsterVector.get(i);
			for(int j=0;j<bulletVector.size();j++) {
				bul = bulletVector.get(j);
				Rectangle moveMonsterBounds = new Rectangle(mon.getX(), mon.getY(), 40, 40); // 레이저 말고 , 몸통
				Rectangle bulletBounds = new Rectangle(bul.getX(), bul.getY(), bulletImage.getIconWidth(), bulletImage.getIconHeight());
				if(moveMonsterBounds.intersects(bulletBounds)) {
					mon.setIcon(newEnemy4Image);
					shoot = monsterList.get(i);
					System.out.println("전 : " + shoot);
					shoot++;
					monsterList.set(i,shoot);
					System.out.println("후 : " + shoot);
					if(shoot == 2) {
						remove(mon);
						remove(bul);
						monsterList.remove(i);
						moveMonsterVector.remove(i);
						bulletVector.remove(j);
						flag = true;
						break;
					}
					remove(bul);
					bulletVector.remove(j);
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
			Rectangle airplaneBounds = new Rectangle(airplane.getX(), airplane.getY(), plane.getIconWidth(), plane.getIconHeight());
			if(meteorBounds.intersects(airplaneBounds)) {
				dialog.setFuel();
				state = dialog.getFuel();
				remove(met);
				bossShoot.remove(i);
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
	public boolean crushBossShoot() {
		JLabel shoot;
		boolean flag = false;
		Rectangle airplaneBounds = new Rectangle(airplane.getX(), airplane.getY(), plane.getIconWidth(), plane.getIconHeight());
		Rectangle shootBounds;
		for(int i=0;i<bossShoot.size();i++) {
			shoot = bossShoot.get(i);
			if(shoot.getIcon() == bossAttackImage1) {
				shootBounds = new Rectangle(shoot.getX(), shoot.getY(), bossAttackImage1.getIconWidth(), bossAttackImage1.getIconHeight());
				if(shootBounds.intersects(airplaneBounds)) {
					dialog.setFuel();
					state = dialog.getFuel();
					remove(shoot);
					bossShoot.remove(i);
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
			else {
				shootBounds = new Rectangle(shoot.getX(), shoot.getY(), bossAttackImage2.getIconWidth(), bossAttackImage2.getIconHeight());
				if(shootBounds.intersects(airplaneBounds)) {
					dialog.setFuel();
					state = dialog.getFuel();
					remove(shoot);
					bossShoot.remove(i);
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
		}
		return flag;
	}
	class MonsterAttackThread extends Thread{
		private Round5 panel;
		private JLabel meteor;
		private JLabel mon;
		private boolean running = true;
		public MonsterAttackThread(Round5 panel) {
			this.panel = panel;
		}
		@Override
		public void run() {
			while(running) {
				try {
					attack();
					if(createBoss == true) {
						bossAttack();
					}
					sleep(400);
				} catch(InterruptedException e) {
					running = false;
					break;
				}
			}
		}
		public void bossAttack() {
			int rand = (int)(Math.random()*3);
			if(rand == 0) { // 왼쪽에서 공겨
				bossAttack2 = new JLabel();
				bossAttack2.setIcon(bossAttackImage2);
				bossAttack2.setSize(30, 40);
				bossAttack2.setLocation(boss.getX(), boss.getY() + bossImage.getIconHeight() - 40);
				add(bossAttack2);
				bossShoot.add(bossAttack2);
			}
			else if(rand == 1) { // 오른쪽에서 공격
				bossAttack2 = new JLabel();
				bossAttack2.setIcon(bossAttackImage2);
				bossAttack2.setSize(30, 40);
				bossAttack2.setLocation(boss.getX() + bossImage.getIconWidth() - 20, boss.getY() + bossImage.getIconHeight() - 40);
				add(bossAttack2);
				bossShoot.add(bossAttack2);
			}
			else { // 가운데서 공격 
				bossAttack1 = new JLabel();
				bossAttack1.setIcon(bossAttackImage1);
				bossAttack1.setSize(100, 90);
				bossAttack1.setLocation((boss.getX() + bossImage.getIconWidth())/2 + 60, boss.getY() + bossImage.getIconHeight() - 40);
				add(bossAttack1);
				bossShoot.add(bossAttack1);
			}
		}
		public void attack() {
			if(monsterVector.size() != 0) {
				for(int i=0;i<monsterVector.size();i++) {
					int rand = (int)(Math.random()*5);
					if(rand == 0) {
						mon = monsterVector.get(i);
						if(mon.getIcon() == enemy5Image) {
							meteor = new JLabel();
							meteor.setIcon(meteorImage);
							meteor.setSize(30, 40);
							meteor.setLocation(mon.getX() + enemy5Image.getIconWidth()/2, mon.getY() + enemy5Image.getIconHeight());
							monsterShoot.add(meteor);
							panel.add(meteor);
						}
					}
				}
			}
		}
	}
	class MonsterThread extends Thread{
		private boolean monRunning = true;
		@Override
		public void run() {
			while(monRunning) {
				try {
					fixMonster();
					moveMeteor();
					moveBossShoot(); 
					moveMonster();
					if(crushMeteor()) {
						revalidate();
						repaint();
					}
					if(crushBossShoot()) {
						revalidate();
						repaint();
					}
					sleep(300);
				} catch(InterruptedException e) {
					break;
				}
			}
		}
		public void stopThread() {
			monRunning = false;
			interrupt();
		}
		public void fixMonster() {
			int mmx = 0, mmy = 0;
			for(int i=0;i<monsterVector.size();i++) {
				JLabel mon = monsterVector.get(i);
				mmx = mon.getX();
				mmy = mon.getY();
				Rectangle monsterBounds = new Rectangle(mon.getX(), mon.getY(), enemy5Image.getIconWidth(), enemy5Image.getIconHeight());
				Rectangle planeBounds = new Rectangle(airplane.getX(), airplane.getY(), plane.getIconWidth(), plane.getIconHeight());
				if(monsterBounds.intersects(planeBounds)) {
					dialog.setFuel();
					state = dialog.getFuel();
					remove(mon);
					moveMonsterVector.remove(i);
					monsterList.remove(i);
					if(state == 0) {
						dialog.setVisible(false);
						attackThread.interrupt();
						stopThread();
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
				}
				if(mmy >= frame.getHeight() + 100) {
					remove(mon);
					moveMonsterVector.remove(i);
				}
				int rand = (int)(Math.random()*4); // 왼 오 위 아래
				if(rand == 0) { // 왼
					mmx -= 5;
					if(mmx < 0) 
						mmx = 0;
				}
				else if(rand == 1) { // 오
					mmx += 5;
					if(mmx + enemy5Image.getIconWidth() > frame.getWidth())
						mmx = frame.getWidth() - enemy5Image.getIconWidth();
				}
				else if(rand == 2) { // 위
					mmy -= 5;
					if(mmy < 0) 
						mmy = 0;
				}
				else if(rand == 3) { // 아래
					mmy += 5;
					if(mmy + enemy5Image.getIconHeight() > infiniteHeight)
						mmy = infiniteHeight - enemy5Image.getIconHeight();
				}
				mon.setLocation(mmx, mmy);
				repaint();
			}
		}
		
		public void moveMonster() {
			int mmx = 0, mmy = 0;
			for(int i=0;i<moveMonsterVector.size();i++) {
				JLabel mon = moveMonsterVector.get(i);
				mmx = mon.getX();
				mmy = mon.getY();
				Rectangle monsterBounds = new Rectangle(mon.getX(), mon.getY(), enemy4Image.getIconWidth(), enemy4Image.getIconHeight());
				Rectangle planeBounds = new Rectangle(airplane.getX(), airplane.getY(), plane.getIconWidth(), plane.getIconHeight());
				if(monsterBounds.intersects(planeBounds)) {
					dialog.setFuel();
					state = dialog.getFuel();
					remove(mon);
					moveMonsterVector.remove(i);
					monsterList.remove(i);
					if(state == 0) {
						dialog.setVisible(false);
						attackThread.interrupt();
						stopThread();
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
				}
				if(mmy >= frame.getHeight() + 100) {
					remove(mon);
					moveMonsterVector.remove(i);
					revalidate();
				}
				mmy += 10;
				mon.setLocation(mmx, mmy);
				repaint();
			}
		}
		public void moveMeteor() {
			int meteorX = 0, meteorY = 0;
			for(int i=0;i<monsterShoot.size();i++) {
				JLabel meteor = monsterShoot.get(i);
				meteorX = meteor.getX();
				meteorY = meteor.getY();
				if(meteorY >= frame.getHeight() + 30) {
					remove(meteor);
					monsterShoot.remove(i);
					revalidate();
				}
				meteorY += 20;
				meteor.setLocation(meteorX, meteorY);
				repaint();
			}
		}
		public void moveBossShoot() {
			int bShootX = 0, bShootY = 0;
			for(int i=0;i<bossShoot.size();i++) {
				JLabel bShoot = bossShoot.get(i);
				bShootX = bShoot.getX();
				bShootY = bShoot.getY();
				if(bShoot.getIcon() == bossAttack1) {
					if(bShootY >= frame.getHeight() + 90) {
						remove(bShoot);
						bossShoot.remove(i);
						revalidate();
					}
					bShootY += 25;
					bShoot.setLocation(bShootX, bShootY);
				}
				else {
					if(bShootY >= frame.getHeight() + 40) {
						remove(bShoot);
						bossShoot.remove(i);
						revalidate();
					}
					bShootY += 20;
					bShoot.setLocation(bShootX, bShootY);
				}
				repaint();
			}
		}
	}
	class BulletThread extends Thread{
		boolean kill = false;
		boolean moveKill = false;
		boolean bossKill = false;
		int bossPurpose = 0;
		int purpose = 0;
		boolean pass = false;
		int bossStart = 1; // 보스 생성 요건 = 잡몹2마리
		@Override
		public void run() {
			while(true) {
				try {
					moveBullet();
					sleep(100);
					kill = deleteFixedCheck();
					if(kill == true) { 
						dialog.setScore(); // 스코어 올리기
						dialog.drawScore(); // 다이얼로그 스코어 업데이트
						revalidate();
						repaint();
					}
					moveKill = deleteMovedCheck();
					if(moveKill == true) {
						dialog.setScore();
						dialog.drawScore();
						revalidate();
						repaint();
					}
					if(createBoss == true) {
						bossKill = deleteBoss();
						if(bossKill == true) {
							bossPurpose = dialog.getBossScore();
							dialog.setScore();
							dialog.drawScore();
							revalidate();
							repaint();
						}
					}
					if(bossPurpose == 3) {
						remove(boss);
						pass = true;
					}
					purpose = dialog.getScore();
					if(purpose == 2 && bossStart == 1) {
						bossStart = 2;
					}
					if(bossStart == 2) {
						bossFlag = true;
						bossStart = 0;
					}
					if(pass == true) { // 다이얼로그 패널 바꿔야함
						System.out.println("다음 라운드");
						attackThread.interrupt();
						monsterThread.stopThread();
						if(reloadTimer != null)
							reloadTimer.stop();
						GameOver overPanel = new GameOver(frame);
						dialog.setVisible(false);
						frame.setContentPane(overPanel);
						frame.revalidate();
						frame.repaint();
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
					remove(bul);
					bulletVector.remove(i);
					revalidate();
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
		g.drawImage(img1 , 0, 0, frame.getWidth(), frame.getHeight(),this);
	}
}
