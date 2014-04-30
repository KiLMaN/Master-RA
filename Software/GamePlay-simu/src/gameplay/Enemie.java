package gameplay;

public class Enemie {

	// TODO : ADD CLASS OF ENEMIE (HEALTH, SPEED)
	private Position position;
	private Position objectif;
	private Path path;
	private int health;
	private float speed;
	private boolean spawned = false;

	public Enemie(int health, int speed) {
		this.health = health;
		this.speed = speed;
	}

	public void spawn(Position position) {
		this.spawned = true;
		this.position = position;
		// TODO Auto-generated method stub
	}

	// Si l'ennemi a été généré et qu'on a un chemin alors on va au prochain
	// point du chemin
	public boolean move() {
		if (!spawned)
			return false;
		if (path == null)
			return false;
		// Position objectif = Game.currentGame.getObjectiveEnemie();
		Position objectif = path.getNextPoint();
		if (objectif != null) {
			position.moveTo(objectif, speed);
			if (path.isCloseToPath(position)) {
				path.removeFirstPoint();
			}
			return false;
		} else {
			return true;
		}
	}

	/* Prendre des dommages par une arme, retourne true si mort */
	public boolean hitBy(Weapon weapon, Tower tower) {
		if (!spawned)
			return false;

		// System.out.println("Tir avec arme : " + weapon.getNameWeapon());
		if (decideShootMiss(tower)) {
			this.health -= weapon.getNumberDamage();
		}
		if (this.health <= 0) {
			this.health = 0;
			this.spawned = false;
		}
		return this.health == 0;
	}

	public boolean decideShootMiss(Tower tower) {
		int x = (Math.random() < 0.5) ? 0 : 1;
		if (x == 1) {
			int killSuccess = tower.getKillSuccess();
			if (killSuccess < tower.getKillSuccessRatio()) {
				tower.setKillSuccess(++killSuccess);
				ReinitialyzeFrequencyShoot(tower);
				return true;
			} else {
				ReinitialyzeFrequencyShoot(tower);
				return false;
			}
		} else {
			if (tower.getKillSuccess() == tower.getKillSuccessRatio()) {
				ReinitialyzeFrequencyShoot(tower);
				return false;
			}
			if ((tower.getKillSuccess() < tower.getKillSuccessRatio())
					&& ((10 - tower.getFrequencyShoot()) > (tower
							.getKillSuccessRatio() - tower.getKillSuccess()))) {
				ReinitialyzeFrequencyShoot(tower);
				return false;
			}
			if ((tower.getKillSuccess() < tower.getKillSuccessRatio())
					&& ((10 - tower.getFrequencyShoot()) <= (tower
							.getKillSuccessRatio() - tower.getKillSuccess()))) {
				int killSuccess = tower.getKillSuccess();
				tower.setKillSuccess(++killSuccess);
				ReinitialyzeFrequencyShoot(tower);
				return true;
			}
		}
		ReinitialyzeFrequencyShoot(tower);
		return false;
	}

	private void ReinitialyzeFrequencyShoot(Tower tower) {
		int frequencyShoot = tower.getFrequencyShoot();
		tower.setFrequencyShoot(++frequencyShoot);
		if (tower.getFrequencyShoot() > 10) {
			tower.setFrequencyShoot(1);
			tower.setKillSuccess(0);
		}
	}

	public boolean isAlive() {
		return this.health > 0;
	}

	// calcul du chemin des ennemis à emprunter ici POUR LE MOMENT, ligne droite
	public boolean computePath() {
		if (this.objectif == null || this.position == null)
			return false;
		this.path = new Path();
		this.path.addPoint(objectif);
		return true;
	}

	public boolean isSpawned() {
		return spawned;
	}

	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public Position getPosition() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Position getObjectif() {
		return objectif;
	}

	public void setObjectif(Position positionObjectif) {
		this.objectif = positionObjectif;
	}

	public Path getPath() {
		return this.path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
}
