package gameplay;

import java.util.ArrayList;

import simulation.SimulationPC;

public class Tower {

	/*
	 * private int idTower; private String nameTower; private int levelTower;
	 * private boolean free; private int puissanceTour = 0;
	 */

	// private ArrayList<Weapon> weapons;
	private ArrayList<Pweapon> pweapons;
	private Enemie target;
	private Position position;
	private boolean enemieEngaged = false;
	private int killSuccessRatio; // sur 10
	private int killSuccess = 0;
	private int frequencyShoot = 1; // sur 10

	public Tower(Position position) {
		// this.position = position;
		// this.pweapons = new ArrayList<Pweapon>();
		this(position, 7);
	}

	public Tower(Position position, int killSuccessRatio) {
		this.position = position;
		this.pweapons = new ArrayList<Pweapon>();
		this.killSuccessRatio = killSuccessRatio;
	}

	public Tower(Position position, ArrayList<Pweapon> pweapons) {
		this.position = position;
		this.pweapons = pweapons;
	}

	public Tower(Position position, ArrayList<Pweapon> pweapons,
			int killSuccessRatio) {
		this.position = position;
		this.pweapons = pweapons;
		this.killSuccessRatio = killSuccessRatio;
	}

	public Enemie getTarget() {
		return this.target;
	}

	public Position getPosition() {
		return this.position;
	}

	public int getKillSuccessRatio() {
		return this.killSuccessRatio;
	}

	public int getKillSuccess() {
		return this.killSuccess;
	}

	public void setKillSuccess(int killSuccess) {
		this.killSuccess = killSuccess;
	}

	public int getFrequencyShoot() {
		return this.frequencyShoot;
	}

	public void setFrequencyShoot(int frequencyShoot) {
		this.frequencyShoot = frequencyShoot;
	}

	// calcul l'ennemile plus proche de la Tour et le cible
	public void targetClosestEnemi(ArrayList<Enemie> enemiesAlive) {
		if (enemieEngaged) // Si on a déjà attaqué un enemie, on se verouille
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

	public boolean targetedEnemieInRange() {
		if (this.target == null)
			return false;
		return this.target.getPosition().distanceTo(this.position) < this
				.getMaxRange();
	}

	private float getMaxRange() {
		float maxRange = 0;
		for (Pweapon pweapon : pweapons) {
			if (!pweapon.isLocked()) {
				if (maxRange < pweapon.Weapon.getRange())
					maxRange = pweapon.Weapon.getRange();
			}
		}
		return maxRange;
	}

	/*
	 * Tirer avec l'arme la plus puissante sur l'enemie selectionné , retourne
	 * true si l'enemie est mort
	 */
	public boolean shootTargetedEnemie() {
		float distanceEnemie = this.position.distanceTo(target.getPosition());
		float maxDamage = 0;
		Pweapon best = null;
		for (Pweapon pweapon : pweapons) {
			if (!pweapon.isLocked()) {
				if (distanceEnemie < pweapon.Weapon.getRange()) {
					if (maxDamage < pweapon.Weapon.getNumberDamage()) {
						if (!pweapon.isReloading()) {
							best = pweapon;
							maxDamage = pweapon.Weapon.getNumberDamage();
						}
					}
				}
			}
		}
		if (best != null) {
			boolean dead = target.hitBy(best.Weapon, this);
			// incrémenter points du joueur
			if (dead == true) {
				int pointsPlayer = SimulationPC.game.getCurrentPlayer()
						.getPointsPlayer();
				SimulationPC.game.getCurrentPlayer().setPointsPlayer(
						pointsPlayer + 1);
				System.out.println("Points player :"
						+ SimulationPC.game.getCurrentPlayer()
								.getPointsPlayer());
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
}
