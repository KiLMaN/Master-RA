package simulation;

import gameplay.Enemie;
import gameplay.Game;
import gameplay.Player;
import gameplay.Tower;
import gameplay.XMLParser;
import gameplay.XMLParserTower;
import gameplay.XMLParserWave;
import gameplay.XMLParserWeapon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.w3c.dom.Node;

public class SimulationPC {

	static JFrame mainMap;
	public static Game game = new Game();

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Loading Weapons");
		XMLParser parserWeapon = new XMLParser("config/weapons.xml");
		parserWeapon.loadFile(new FileReaderPC());
		Node rootWeapon = parserWeapon.getRoot();
		game.setDefaultWeapons(XMLParserWeapon.parseXMLWeapon(rootWeapon));

		System.out.println("Loading Tower Positions");
		XMLParser parserTower = new XMLParser("config/towers.xml");
		parserTower.loadFile(new FileReaderPC());
		Node rootTower = parserTower.getRoot();
		game.setTowers(XMLParserTower.parseXMLTowers(rootTower));

		game.assignWeapons();

		System.out.println("Loading Enemies Waves");
		XMLParser parserWaves = new XMLParser("config/waves.xml");
		parserWaves.loadFile(new FileReaderPC());
		Node rootWave = parserWaves.getRoot();
		game.setWaves(XMLParserWave.parseXMLWaves(rootWave));

		game.setCurrentPlayer(new Player(1, "player n°1", 20, 0, 0));

		initWindow();
		System.out.println("Starting Simulation GamePlay");
		game.start();
		waiting();
	}

	public static void initWindow() {
		mainMap = new JFrame();
		mainMap.setResizable(false);
		mainMap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMap.setSize(1000, 1000);
		mainMap.setLocationRelativeTo(null);
		mainMap.setContentPane(new GamePanel());
		mainMap.setVisible(true);
		mainMap.addKeyListener(new KeyboardDetection());
	}

	public static void repeat() throws InterruptedException {
		while (game.isPlaying() && !game.isPaused()) {
			game.gameTick();
			mainMap.repaint();
			Thread.sleep(100);
		}
	}

	public static void waiting() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					repeat();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};

		while (true) {

			Timer timer = new Timer();
			timer.scheduleAtFixedRate(task, 0, 1000);
		}
	}
}

class GamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean holdedInsideCircle = false;
	private int index;

	public GamePanel() {

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SimulationPC.game.isPaused())
					for (Tower tower : SimulationPC.game.getTowers()) {
						if ((tower.getPosition().getPositionX() <= e.getX() && tower
								.getPosition().getPositionX() + 10 >= e.getX())
								&& (tower.getPosition().getPositionY() <= e
										.getY() && tower.getPosition()
										.getPositionY() + 10 >= e.getY())) {
							index = SimulationPC.game.getTowers()
									.indexOf(tower);
							moveVertex(e.getX(), e.getY());
							holdedInsideCircle = true;
						}
					}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				holdedInsideCircle = false;
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (holdedInsideCircle)
					moveVertex(e.getX(), e.getY());
			}
		});

	}

	private void moveVertex(int x, int y) {
		int OFFSET = 1;
		Tower tower = SimulationPC.game.getTowers().get(index);
		if ((tower.getPosition().getPositionX() != x)
				|| (tower.getPosition().getPositionY() != y)) {

			repaint((int) tower.getPosition().getPositionX(), (int) tower
					.getPosition().getPositionY(), 10 + OFFSET, 10 + OFFSET);

			tower.getPosition().setPositionX(x - 10);
			tower.getPosition().setPositionY(y - 10);

			repaint((int) tower.getPosition().getPositionX(), (int) tower
					.getPosition().getPositionY(), 10 + OFFSET, 10 + OFFSET);

			repaint();
		}

	}

	public Dimension getPreferredSize() {
		return new Dimension(250, 200);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawString("Alives : "
				+ SimulationPC.game.getCurrentWave().getEnemiesAlive().size(),
				150, 150);
		g.drawString("Left : "
				+ SimulationPC.game.getCurrentWave().getEnemiesLeftToSpawn()
						.size(), 150, 160);
		g.drawString("Dead : "
				+ SimulationPC.game.getCurrentWave().getEnemiesDead().size(),
				150, 170);
		g.drawString(
				"SPC : " + SimulationPC.game.getCurrentWave().spawnCounter,
				150, 180);

		for (Tower tower : SimulationPC.game.getTowers()) {
			g.setColor(Color.BLUE);
			g.drawOval((int) tower.getPosition().getPositionX() - 4,
					(int) tower.getPosition().getPositionY() - 4, 8, 8);

			if (tower.targetedEnemieInRange())
				g.setColor(Color.MAGENTA);
			else
				g.setColor(Color.CYAN);
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(1));
			if (tower.getTarget() != null) {
				if (tower.getTarget().isAlive()) {

					g2.drawLine((int) tower.getTarget().getPosition()
							.getPositionX(), (int) tower.getTarget()
							.getPosition().getPositionY(), (int) tower
							.getPosition().getPositionX(), (int) tower
							.getPosition().getPositionY());
				}
			}
			g2.setStroke(new BasicStroke(1));
		}
		g.setColor(Color.RED);
		for (Enemie enemie : SimulationPC.game.getCurrentWave()
				.getEnemiesAlive()) {
			g.drawOval((int) enemie.getPosition().getPositionX() - 3,
					(int) enemie.getPosition().getPositionY() - 3, 6, 6);
		}

		g.setColor(Color.GREEN);
		g.drawOval(
				(int) SimulationPC.game.getObjectiveEnemie().getPositionX() - 5,
				(int) SimulationPC.game.getObjectiveEnemie().getPositionY() - 5,
				10, 10);

		g.setColor(Color.BLACK);
		g.drawOval(
				(int) SimulationPC.game.getStartPointEnemie().getPositionX() - 5,
				(int) SimulationPC.game.getStartPointEnemie().getPositionY() - 5,
				10, 10);

	}
}

class KeyboardDetection implements KeyListener {

	public KeyboardDetection() {

	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == 32)
			SimulationPC.game.setPaused(false);

	}

	public void keyReleased(KeyEvent e) {
		System.out.println("Touche relâchée : " + e.getKeyCode() + " ("
				+ e.getKeyChar() + ")");
	}

	public void keyTyped(KeyEvent e) {
		// on ne fait rien
	}
}
