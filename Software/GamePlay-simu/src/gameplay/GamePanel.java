package gameplay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import simulation.SimulationPC;

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

	public GamePanel() {

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SimulationPC.game.isPaused())
					for (Tower tower : SimulationPC.game.getTowers()) {
						if ((tower.getPosition().getPositionX() <= e.getX() && tower
								.getPosition().getPositionX() + 50 >= e.getX())
								&& (tower.getPosition().getPositionY() <= e
										.getY() && tower.getPosition()
										.getPositionY() + 50 >= e.getY())) {
							index = SimulationPC.game.getTowers()
									.indexOf(tower);
							moveVertex(e.getX(), e.getY());
							holdedInsideCircle = true;
							break;
						}
					}

				else {
					// ArrayList<Tower> Towers = SimulationPC.game.getTowers();
					for (Tower tower : SimulationPC.game.getTowers()) {
						if ((tower.getPosition().getPositionX() <= e.getX() && tower
								.getPosition().getPositionX() + 50 >= e.getX())
								&& (tower.getPosition().getPositionY() <= e
										.getY() && tower.getPosition()
										.getPositionY() + 50 >= e.getY())) {
							controledByPlayer = !tower.isControledByPlayer(); // à
																				// revoir
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

							// SimulationPC.game.getTowers().get(indexOfLastTower[0]).setControledByPlayer(controledByPlayer);

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
								.getPosition().getPositionX() + 20 >= e.getX())
								&& (enemie.getPosition().getPositionY() <= e
										.getY() && enemie.getPosition()
										.getPositionY() + 20 >= e.getY())) {
							indexEnemie = SimulationPC.game.getCurrentWave()
									.getEnemiesAlive().indexOf(enemie);
							// moveVertex(e.getX(), e.getY());
							holdedInsideCircleEnemie = true;
							break;
						}
					}

					// for (Enemie enemie : SimulationPC.game.getCurrentWave()
					// .getEnemiesAlive()) {
					// g.drawOval((int) enemie.getPosition().getPositionX()
					// - 3,
					// (int) enemie.getPosition().getPositionY() - 3, 6, 6);
					// g.drawOval((int) enemie.getPosition().getPositionX(),
					// (int) enemie.getPosition().getPositionY(), 20,
					// 20);
					// }
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
					// TODO: analyser distance entre la tour et l'ennemi et agir
					// en conséquence

					// tower.tickReloadTimers();

					// Enemie target = tower.getTarget();
					// if (target == null || !target.isAlive()) {
					// System.out.println("recherche d'un enemie");
					// tower.targetClosestEnemi(enemiesAlive);
					// target = tower.getTarget();
					// }
					// si la cible ennemie est à portée
					Enemie enemie = SimulationPC.game.getCurrentWave()
							.getEnemiesAlive().get(indexEnemie);

					Tower tower = SimulationPC.game.getTowers().get(
							indexOfLastTower[0]);
					tower.setTarget(enemie);
					if (tower.targetedEnemieInRange()) {

						/*
						 * System.out.println("tir d'une tour @ " +
						 * tower.getPosition().toString() + " sur enemie @ " +
						 * target.getPosition().toString());
						 */
						// renvoie false si l'ennemi n'est pas touché right
						// sinon
						if (tower.shootTargetedEnemie()) {
							/* System.out.println("Ennemi Mort !"); */
							SimulationPC.game.getCurrentWave().enemieKilled(
									enemie);
						}
					}
					// Tirer avec la tour

					// SimulationPC.game.getCurrentWave().enemieKilled(
					// SimulationPC.game.getCurrentWave()
					// .getEnemiesAlive().get(indexEnemie));

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
					.getPosition().getPositionY(), 40 + OFFSET, 40 + OFFSET);

			tower.getPosition().setPositionX(x - 10);
			tower.getPosition().setPositionY(y - 10);

			repaint((int) tower.getPosition().getPositionX(), (int) tower
					.getPosition().getPositionY(), 40 + OFFSET, 40 + OFFSET);

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
							+ " TYPE= " + pweapon.Weapon.getWeaponType()
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
					.getPosition().getPositionY(), 50, 50);
			g.fillOval((int) tower.getPosition().getPositionX(), (int) tower
					.getPosition().getPositionY(), 50, 50);

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
							.getPosition().getPositionX() + 25, (int) tower
							.getPosition().getPositionY() + 25);
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
					.getPosition().getPositionY(), 20, 20);
			g.fillOval((int) enemie.getPosition().getPositionX(), (int) enemie
					.getPosition().getPositionY(), 20, 20);
		}

		g.setColor(Color.GREEN);
		// g.drawOval(
		// (int) SimulationPC.game.getObjectiveEnemie().getPositionX() - 5,
		// (int) SimulationPC.game.getObjectiveEnemie().getPositionY() - 5,
		// 10, 10);
		g.drawOval((int) SimulationPC.game.getObjectiveEnemie().getPositionX(),
				(int) SimulationPC.game.getObjectiveEnemie().getPositionY(),
				40, 40);

		g.setColor(Color.BLACK);
		// g.drawOval(
		// (int) SimulationPC.game.getStartPointEnemie().getPositionX() - 5,
		// (int) SimulationPC.game.getStartPointEnemie().getPositionY() - 5,
		// 10, 10);
		g.drawOval(
				(int) SimulationPC.game.getStartPointEnemie().getPositionX(),
				(int) SimulationPC.game.getStartPointEnemie().getPositionY(),
				40, 40);

	}
}

// class KeyboardDetection implements KeyListener {
//
// public KeyboardDetection() {
//
// }
//
// public void keyPressed(KeyEvent e) {
//
// if (e.getKeyCode() == 32)
// SimulationPC.game.setPaused(false);
//
// }
//
// public void keyReleased(KeyEvent e) {
// System.out.println("Touche relâchée : " + e.getKeyCode() + " ("
// + e.getKeyChar() + ")");
// }
//
// public void keyTyped(KeyEvent e) {
// // on ne fait rien
// }
// }

