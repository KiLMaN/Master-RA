package gameplay;

public class Enemie {

	// TODO : ADD CLASS OF ENEMIE (HEALTH, SPEED)
	private Position position;
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

	public void move() {
		if (!spawned)
			return;
		Position objectif = Game.currentGame.getObjectiveEnemie();
		position.moveTo(objectif, speed);
	}

	public Position getPosition() {
		return this.position;
	}

	/* Prendre des dommages par une arme, retourne true si mort */
	public boolean hitBy(Weapon weapon) {
		if (!spawned)
			return false;

		System.out.println("Tir avec arme : " + weapon.getNameWeapon());
		this.health -= weapon.getNumberDamage();
		if (this.health <= 0)
			this.health = 0;

		return this.health == 0;
	}

}
