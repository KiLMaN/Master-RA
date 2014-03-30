package towerDefender;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import gameplay.Enemie;
import gameplay.Game;
import gameplay.Tower;

public class GamePlaySimu {

	static JFrame mainMap;
	static Game game = new Game();

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
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

				for (Tower tower : game.getTowers()) {
					g.setColor(Color.BLUE);
					g.drawOval((int) tower.getPosition().getPositionX() - 4,
							(int) tower.getPosition().getPositionY() - 4, 8, 8);

					if (tower.targetedEnemieInRange())
						g.setColor(Color.MAGENTA);
					else
						g.setColor(Color.CYAN);
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(3));
					if (tower.getTarget() != null) {
						g2.drawLine((int) tower.getTarget().getPosition()
								.getPositionX(), (int) tower.getTarget()
								.getPosition().getPositionY(), (int) tower
								.getPosition().getPositionX(), (int) tower
								.getPosition().getPositionY());
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
