package gameplay;

import java.util.ArrayList;

public class Tower {

	/*
	 * private int idTower; private String nameTower; private int levelTower;
	 * private boolean free; private int puissanceTour = 0;
	 */

	private ArrayList<Weapon> weapons;
	private Enemie target;
	private Position position;
	private boolean enemieEngaged = false;

	public Tower(Position position) {
		this.position = position;
		this.weapons = new ArrayList<Weapon>();
	}

	public Tower(Position position, ArrayList<Weapon> weapons) {
		this.position = position;
		this.weapons = weapons;
	}

	public Enemie getTarget() {
		return this.target;
	}

	public Position getPosition() {
		return this.position;
	}

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
		for (Weapon weapon : weapons) {
			if (!weapon.isLocked()) {
				if (maxRange < weapon.getRange())
					maxRange = weapon.getRange();
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
		Weapon best = null;
		for (Weapon weapon : weapons) {
			if (!weapon.isLocked()) {
				if (distanceEnemie < weapon.getRange()) {
					if (maxDamage < weapon.getNumberDamage()) {
						if (!weapon.isReloading()) {
							best = weapon;
							maxDamage = weapon.getNumberDamage();
						}
					}
				}
			}
		}
		if (best != null) {
			boolean dead = target.hitBy(best);
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
		for (Weapon weapon : weapons) {
			if (!weapon.isLocked()) {
				if (weapon.isReloading()) {
					weapon.tickReloading();
				}
			}
		}
	}

	public void setWeapons(ArrayList<Weapon> weapons) {
		this.weapons = weapons;

	}
}
