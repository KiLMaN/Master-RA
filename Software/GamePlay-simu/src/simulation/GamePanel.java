package simulation;

import gameplay.Enemie;
import gameplay.Pweapon;
import gameplay.Tower;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	private boolean holdedInsideCircle = false;
	private int index;
	private int[] indexOfLastTower = { -1, -1 };
	// private int indexOfPreviousTower;
	private int i = 0;
	private boolean controledByPlayer = false;
	private int indexEnemie;
	private boolean holdedInsideCircleEnemie = false;
	private boolean printControledTowerInfo = false; // just to inform user that
														// the tower which has
														// been clicked on is
														// controled or not
	private boolean printWeaponUnblocked = false; // juste pour affichage
	private Pweapon pweapon; // juste pour affichage
	private int circleTowerRadius = 50;
	private int circleEnemieRadius = 20;

	public GamePanel() {

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SimulationPC.game.isPaused())
					for (Tower tower : SimulationPC.game.getTowers()) {
						if ((tower.getPosition().getPositionX() <= e.getX() && tower
								.getPosition().getPositionX()
								+ circleTowerRadius >= e.getX())
								&& (tower.getPosition().getPositionY() <= e
										.getY() && tower.getPosition()
										.getPositionY() + circleTowerRadius >= e
											.getY())) {
							index = SimulationPC.game.getTowers()
									.indexOf(tower);
							moveVertex(e.getX(), e.getY());
							holdedInsideCircle = true;
							break;
						}
					}

				else {
					for (Tower tower : SimulationPC.game.getTowers()) {
						if ((tower.getPosition().getPositionX() <= e.getX() && tower
								.getPosition().getPositionX()
								+ circleTowerRadius >= e.getX())
								&& (tower.getPosition().getPositionY() <= e
										.getY() && tower.getPosition()
										.getPositionY() + circleTowerRadius >= e
											.getY())) {
							controledByPlayer = !tower.isControledByPlayer();
							tower.setControledByPlayer(!tower
									.isControledByPlayer());

							indexOfLastTower[1] = indexOfLastTower[0];
							indexOfLastTower[0] = SimulationPC.game.getTowers()
									.indexOf(tower);

							if (indexOfLastTower[1] == indexOfLastTower[0]) {
								controledByPlayer = false;
							} else {
								controledByPlayer = true;
							}

							System.out.println("tower n°" + tower.getIdTower()
									+ "  controled:"
									+ tower.isControledByPlayer());
							printControledTowerInfo = true;
							break;
						}
					}
					if (controledByPlayer) {
						if (i != 0)
							SimulationPC.game.getTowers()
									.get(indexOfLastTower[1])
									.setControledByPlayer(false);
						i++;
						controledByPlayer = false;
					}

					for (Enemie enemie : SimulationPC.game.getCurrentWave()
							.getEnemiesAlive()) {
						if ((enemie.getPosition().getPositionX() <= e.getX() && enemie
								.getPosition().getPositionX()
								+ circleEnemieRadius >= e.getX())
								&& (enemie.getPosition().getPositionY() <= e
										.getY() && enemie.getPosition()
										.getPositionY() + circleEnemieRadius >= e
											.getY())) {
							indexEnemie = SimulationPC.game.getCurrentWave()
									.getEnemiesAlive().indexOf(enemie);

							holdedInsideCircleEnemie = true;
							break;
						}
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				holdedInsideCircle = false;

				if (indexOfLastTower[0] != -1
						&& holdedInsideCircleEnemie == true) { // si une tour a
																// été
																// séléctionnée
																// et si le
																// joueur a
																// cliqué sur un
																// ennemi

					Enemie enemie = SimulationPC.game.getCurrentWave()
							.getEnemiesAlive().get(indexEnemie);

					Tower tower = SimulationPC.game.getTowers().get(
							indexOfLastTower[0]);
					tower.setTarget(enemie);
					if (tower.isTargetedEnemieInRange()) {
						if (tower.shootTargetedEnemie()) {
							/* System.out.println("Ennemi Mort !"); */
							SimulationPC.game.getCurrentWave().enemieKilled(
									enemie);
						}
					}

					holdedInsideCircleEnemie = false;
				}
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
					.getPosition().getPositionY(), circleTowerRadius + OFFSET,
					circleTowerRadius + OFFSET);

			tower.getPosition().setPositionX(x - 10);
			tower.getPosition().setPositionY(y - 10);

			repaint((int) tower.getPosition().getPositionX(), (int) tower
					.getPosition().getPositionY(), circleTowerRadius + OFFSET,
					circleTowerRadius + OFFSET);

			repaint();
		}

	}

	public Dimension getPreferredSize() {
		return new Dimension(250, 200);
	}

	public void getWeaponUnlocked(Pweapon pweapon) {
		this.pweapon = pweapon;
	}

	public void setPrintWeaponUnblocked(boolean printWeaponUnblocked) {
		this.printWeaponUnblocked = printWeaponUnblocked;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawString("Alives : "
				+ SimulationPC.game.getCurrentWave().getEnemiesAlive().size(),
				400, 150);
		g.drawString("Left : "
				+ SimulationPC.game.getCurrentWave().getEnemiesLeftToSpawn()
						.size(), 400, 160);
		g.drawString("Dead : "
				+ SimulationPC.game.getCurrentWave().getEnemiesDead().size(),
				400, 170);
		g.drawString(
				"SPC : " + SimulationPC.game.getCurrentWave().spawnCounter,
				400, 180);

		if (printControledTowerInfo) {
			g.drawString(
					"tower n°"
							+ SimulationPC.game.getTowers()
									.get(indexOfLastTower[0]).getIdTower()
							+ "  controled:"
							+ SimulationPC.game.getTowers()
									.get(indexOfLastTower[0])
									.isControledByPlayer(), 400, 190);
		}

		if (printWeaponUnblocked) {

			g.drawString(
					"Weapon( NAME= " + pweapon.Weapon.getNameWeapon()

					+ " DAMAGE= " + pweapon.Weapon.getNumberDamage()
							+ " Locked= " + pweapon.isLocked()
							+ " RELOADTIME = "
							+ pweapon.Weapon.getReloadingTime() + " RANGE = "
							+ pweapon.Weapon.getRange() + ") unlocked", 300,
					200);
			// printWeaponUnblocked = false;
		}

		for (Tower tower : SimulationPC.game.getTowers()) {
			g.setColor(Color.BLUE);
			g.drawOval((int) tower.getPosition().getPositionX(), (int) tower
					.getPosition().getPositionY(), circleTowerRadius,
					circleTowerRadius);
			g.fillOval((int) tower.getPosition().getPositionX(), (int) tower
					.getPosition().getPositionY(), circleTowerRadius,
					circleTowerRadius);

			if (tower.isTargetedEnemieInRange())
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
							.getPosition().getPositionX()
							+ circleTowerRadius
							/ 2, (int) tower.getPosition().getPositionY()
							+ circleTowerRadius / 2);
				}
			}
			g2.setStroke(new BasicStroke(1));
		}
		g.setColor(Color.RED);
		for (Enemie enemie : SimulationPC.game.getCurrentWave()
				.getEnemiesAlive()) {
			// g.drawOval((int) enemie.getPosition().getPositionX() - 3,
			// (int) enemie.getPosition().getPositionY() - 3, 6, 6);
			g.drawOval((int) enemie.getPosition().getPositionX(), (int) enemie
					.getPosition().getPositionY(), circleEnemieRadius,
					circleEnemieRadius);
			g.fillOval((int) enemie.getPosition().getPositionX(), (int) enemie
					.getPosition().getPositionY(), circleEnemieRadius,
					circleEnemieRadius);
		}

		g.setColor(Color.GREEN);
		// g.drawOval(
		// (int) SimulationPC.game.getObjectiveEnemie().getPositionX() - 5,
		// (int) SimulationPC.game.getObjectiveEnemie().getPositionY() - 5,
		// 10, 10);
		g.drawOval((int) SimulationPC.game.getObjectiveEnemie().getPositionX(),
				(int) SimulationPC.game.getObjectiveEnemie().getPositionY(),
				circleTowerRadius, circleTowerRadius);

		g.setColor(Color.BLACK);
		// g.drawOval(
		// (int) SimulationPC.game.getStartPointEnemie().getPositionX() - 5,
		// (int) SimulationPC.game.getStartPointEnemie().getPositionY() - 5,
		// 10, 10);
		g.drawOval(
				(int) SimulationPC.game.getStartPointEnemie().getPositionX(),
				(int) SimulationPC.game.getStartPointEnemie().getPositionY(),
				circleTowerRadius, circleTowerRadius);

	}
}
