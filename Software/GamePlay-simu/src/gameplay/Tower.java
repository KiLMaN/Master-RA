package gameplay;

import java.util.ArrayList;

import communication.CommunicationTower;

public class Tower extends CommunicationTower {

	/*
	 * private int idTower; private String nameTower; private int levelTower;
	 * private boolean free; private int puissanceTour = 0;
	 */

	// private ArrayList<Weapon> weapons;
	private ArrayList<Pweapon> pweapons;
	private Enemie target;
	private Position position;
	private boolean enemieEngaged = false;

	/*
	 * @Deprecated private int killSuccessRatio; // sur 10
	 * 
	 * @Deprecated private int killSuccess = 0;
	 * 
	 * @Deprecated private int frequencyShoot = 1; // sur 10
	 */

	private boolean controledByPlayer = false;
	private int idTower;

	/*
	 * public Tower(Position position) { // this.position = position; //
	 * this.pweapons = new ArrayList<Pweapon>(); this(position, 7); }
	 */

	public Tower(int idTower, Position position) {
		this.position = position;
		// this.pweapons = new ArrayList<Pweapon>();
		// this(position, 7);
		this.idTower = idTower;

	}

	/*
	 * public Tower(Position position, int killSuccessRatio) { this.position =
	 * position; this.pweapons = new ArrayList<Pweapon>(); this.killSuccessRatio
	 * = killSuccessRatio; }
	 */

	public Tower(Position position, ArrayList<Pweapon> pweapons) {
		this.position = position;
		this.pweapons = pweapons;
	}

	/*
	 * public Tower(Position position, ArrayList<Pweapon> pweapons, int
	 * killSuccessRatio) { this.position = position; this.pweapons = pweapons;
	 * //this.killSuccessRatio = killSuccessRatio; }
	 */

	public Enemie getTarget() {
		return this.target;
	}

	public void setTarget(Enemie enemie) {
		this.target = enemie;
	}

	public Position getPosition() {
		return this.position;
	}

	public void setPosition(Position _pos) {
		this.position = _pos;
	}

	/*
	 * public int getKillSuccessRatio() { return this.killSuccessRatio; }
	 * 
	 * public int getKillSuccess() { return this.killSuccess; }
	 * 
	 * public void setKillSuccess(int killSuccess) { this.killSuccess =
	 * killSuccess; }
	 * 
	 * public int getFrequencyShoot() { return this.frequencyShoot; }
	 * 
	 * public void setFrequencyShoot(int frequencyShoot) { this.frequencyShoot =
	 * frequencyShoot; }
	 */

	public boolean isControledByPlayer() {
		return this.controledByPlayer;
	}

	public void setControledByPlayer(boolean controledByPlayer) {
		this.controledByPlayer = controledByPlayer;
	}

	public int getIdTower() {
		return this.idTower;
	}

	// calcul l'ennemile plus proche de la Tour et le cible
	public void targetClosestEnemi(ArrayList<Enemie> enemiesAlive) {
		if (enemieEngaged) // Si on a d j attaqu un enemie, on se
							// verouille
							// dessus
			return;

		Enemie nearest = null;
		float distanceClosest = 0;
		float newDistance = 0;
		for (Enemie enemie : enemiesAlive) {
			if (enemie.isAlive()) {
				if (nearest == null) {
					nearest = enemie;
					distanceClosest = nearest.getPosition().distanceTo(
							this.getPosition());
				} else {
					if (distanceClosest > (newDistance = enemie.getPosition()
							.distanceTo(this.getPosition()))) {
						nearest = enemie;
						distanceClosest = newDistance;
					}
				}
			}
		}
		this.target = nearest;
	}

	public boolean isTargetedEnemieInRange() {
		if (this.target == null)
			return false;
		return this.target.getPosition().distanceTo(this.position) < this
				.getMaxRange();
	}

	private float getMaxRange() {
		float maxRange = 0;
		for (Pweapon pweapon : pweapons) {
			if (!pweapon.isLocked()) {
				if (maxRange < pweapon.getRange())
					maxRange = pweapon.getRange();
			}
		}
		return maxRange;
	}

	/*
	 * Tirer avec l'arme la plus puissante sur l'enemie selectionne , retourne
	 * true si l'enemie est mort
	 */
	public boolean shootTargetedEnemie() {
		float distanceEnemie = this.position.distanceTo(target.getPosition());
		float maxDamage = 0;
		Pweapon best = null;
		for (Pweapon pweapon : pweapons) {
			if (!pweapon.isLocked()) {
				if (distanceEnemie < pweapon.getRange()) {
					if (maxDamage < pweapon.getDamage()) {
						if (!pweapon.isReloading()) {
							best = pweapon;
							maxDamage = pweapon.getDamage();
						}
					}
				}
			}
		}
		if (best != null) {
			boolean dead = target.hitBy(best, this);
			if (dead == true) {
				//Game currentGame = Game.getCurrentGame();

				// player score incremented
				//currentGame.addPoints(GameConfig.NUMBER_POINTS_AT_KILL);

				/*
				 * System.out.println("Points player :" +
				 * SimulationPC.game.getCurrentPlayer() .getPointsPlayer());
				 */
				/*
				 * int numberEnemiesKilled =
				 * best.Weapon.getNumberEnemiesKilled();
				 * best.Weapon.setNumberEnemiesKilled(numberEnemiesKilled +
				 * GameConfig.NUMBER_POINTS_AT_KILL); // number of enemies
				 * killed with weapon incremented
				 * 
				 * // maximum de points pour débloquer la prochaine armeatteint
				 * if ((best.Weapon.getNumberEnemiesKilled() %
				 * GameConfig.NUMBER_KILLED_ENEMIE_TO_UPGRADE) == 0) {
				 * 
				 * UnlockNextWeapon(best.Weapon.getWeaponType());
				 * 
				 * }
				 */
			}

			best.startReload();
			/*
			 * if (dead) target = null;
			 */

			return dead;
		} else {
			return false;
		}
	}

	/*
	 * public void UnlockNextWeapon(WeaponType weaponType) { for (Pweapon
	 * pweapon : pweapons) { // TODO: voir s'il n'y a pas moyen de // gagner en
	 * rapidité par un système // de type requète if
	 * (pweapon.Weapon.getWeaponType() == weaponType) { if (pweapon.isLocked())
	 * { pweapon.setLocked(false);
	 * 
	 * // SimulationPC.gamePanel.setPrintWeaponUnblocked(true); //
	 * SimulationPC.gamePanel.getWeaponUnlocked(pweapon); // // juste // pour //
	 * affichage return; } } }
	 * 
	 * }
	 */
	/**
	 * Débloque une arme pour la tour courrante
	 * 
	 * @param weapon
	 * @return 1 si le débloquage est validé , 0 Si le joueur n'as pas assez de
	 *         points, -1 si l'arme est déjà débloquée , -10 si erreur inconnue
	 * 
	 */
	public int unlockWeapon(Weapon weapon) {
		for (Pweapon pweapon : pweapons) {
			if (pweapon.getWeapon().equals(weapon)) {
				if (pweapon.isLocked()) {
					Game currentGame = Game.getCurrentGame();
					// Si on à assez d'argent pour débloquer l'arme
					if (pweapon.getWeapon().getCostWeapon() <= currentGame
							.getPoints()) {
						pweapon.setLocked(false);
						currentGame.removePoints(pweapon.getWeapon()
								.getCostWeapon());
						return 1;
					} else
						return 0;
				} else
					return -1;
			}
		}
		return -10;
	}

	/**
	 * Upgrade une arme
	 * 
	 * @param weapon
	 *            : Arme à upgrader
	 * @param upgradeType
	 *            : type de l'upgrade
	 * @return 1 si l'upgrade c'est bien passée, 0 si le joueur n'as pas assez
	 *         de points, -1, si l'arme ne peut pas être upgradé, -3 si arme pas
	 *         été débloquée, -10 si erreur inconnue
	 */
	public int upgradeWeapon(Weapon weapon, UpgradeType upgradeType) {
		for (Pweapon pweapon : pweapons) {
			if (pweapon.getWeapon().equals(weapon)) {
				if (!pweapon.isLocked()) {
					Game currentGame = Game.getCurrentGame();
					// Si on à assez d'argent pour débloquer l'arme
					int cost = pweapon.getCostUpgrade(upgradeType);
					if (cost <= currentGame.getPoints()) {
						if (pweapon.upgrade(upgradeType)) {
							currentGame.removePoints(cost);
							return 1;
						} else
							return -1;
					} else
						return 0;
				} else
					return -3;
			}
		}
		return -10;
	}

	public void tickReloadTimers() {
		for (Pweapon pweapon : pweapons) {
			if (!pweapon.isLocked()) {
				if (pweapon.isReloading()) {
					pweapon.tickReloading();
				}
			}
		}
	}

	public void setWeapons(ArrayList<Pweapon> pweapons) {
		this.pweapons = pweapons;

	}

	public ArrayList<Pweapon> getWeapons() {
		return this.pweapons;
	}

	public void setID(int idFromIP) {
		this.idTower = idFromIP;

	}
}
