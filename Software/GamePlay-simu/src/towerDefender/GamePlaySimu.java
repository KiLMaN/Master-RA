package towerDefender;

import gameplay.Enemie;
import gameplay.Game;
import gameplay.GameConfig;
import gameplay.Position;
import gameplay.Tower;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePlaySimu {

	static JFrame mainMap;
	static Game game = new Game();

	public static void main(String[] args) throws InterruptedException {
		initWindow();
		System.out.println("Starting Simulation GamePlay");
		game.start();
		while (game.isPlaying() && !game.isPaused()) {
			game.gameTick();
			mainMap.repaint();
			Thread.sleep(100);
		}
	}

	public static void initWindow() {
		mainMap = new JFrame();
		mainMap.setResizable(false);

		mainMap.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel pannel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8463156070648999928L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.drawString("Alives : "
						+ game.getCurrentWave().getEnemiesAlive().size(), 150,
						150);
				g.drawString("Left : "
						+ game.getCurrentWave().getEnemiesLeftToSpawn().size(),
						150, 160);
				g.drawString("Dead : "
						+ game.getCurrentWave().getEnemiesDead().size(), 150,
						170);
				g.drawString("SPC : " + game.getCurrentWave().spawnCounter,
						150, 180);

				Iterator<Position> ite = GameConfig.defaulPath.getPath()
						.iterator();
				Position old = ite.next();
				while (ite.hasNext()) {
					Position pos = ite.next();
					g.setColor(Color.GRAY);
					g.drawLine((int) old.getPositionX(),
							(int) old.getPositionY(), (int) pos.getPositionX(),
							(int) pos.getPositionY());
					old = pos;
				}

				for (Tower tower : game.getTowers()) {
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
				for (Enemie enemie : game.getCurrentWave().getEnemiesAlive()) {
					g.drawOval((int) enemie.getPosition().getPositionX() - 3,
							(int) enemie.getPosition().getPositionY() - 3, 6, 6);
				}

				g.setColor(Color.GREEN);
				g.drawOval((int) game.getObjectiveEnemie().getPositionX() - 5,
						(int) game.getObjectiveEnemie().getPositionY() - 5, 10,
						10);

				g.setColor(Color.BLACK);
				g.drawOval((int) game.getStartPointEnemie().getPositionX() - 5,
						(int) game.getStartPointEnemie().getPositionY() - 5,
						10, 10);

			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(1000, 1000);
			}
		};
		mainMap.add(pannel);
		mainMap.pack();
		mainMap.setVisible(true);
	}
}
