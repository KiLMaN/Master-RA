package gameplay;

public class Weapon {

	private int idWeapon;
	private String nameWeapon;

	private int numberDamage;
	private float range;

	private int reloadgingTime;
	// private int currentReload;

	private boolean locked;
	private int costWeapon;

	private int numberEnemiesKilled;

	public WeaponType WeaponType;

	// public Pweapon Pweapon;

	public Weapon(int idWeapon, String nameWeapon, int numberDamage,
			float range, int numberEnemiesKilled, int timeToRecharge,
			WeaponType WeaponType, int costWeapon, boolean lockedWeapon) {
		this.idWeapon = idWeapon;
		this.nameWeapon = nameWeapon;
		this.numberDamage = numberDamage;
		this.range = range;
		this.numberEnemiesKilled = numberEnemiesKilled;
		this.costWeapon = costWeapon;
		this.reloadgingTime = timeToRecharge;
		// this.currentReload = 0;
		this.locked = lockedWeapon;
		this.WeaponType = WeaponType;
		// Pweapon = new Pweapon(this,timeToRecharge,lockedWeapon);
	}

	public int getIdWeapon() {
		return idWeapon;
	}

	public void setIdWeapon(int idWeapon) {
		this.idWeapon = idWeapon;
	}

	public String getNameWeapon() {
		return nameWeapon;
	}

	public void setNameWeapon(String nameWeapon) {
		this.nameWeapon = nameWeapon;
	}

	public int getNumberDamage() {
		return numberDamage;
	}

	public void setNumberDamage(int numberDamage) {
		this.numberDamage = numberDamage;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public int getNumberEnemiesKilled() {
		return numberEnemiesKilled;
	}

	public void setNumberEnemiesKilled(int numberEnemiesKilled) {
		this.numberEnemiesKilled = numberEnemiesKilled;
	}

	public int getCostWeapon() {
		return costWeapon;
	}

	public void setCostWeapon(int costWeapon) {
		this.costWeapon = costWeapon;
	}

	/*
	 * public boolean isReloading() { return currentReload != 0; }
	 * 
	 * public void tickReloading() { this.currentReload--; }
	 * 
	 * public void startReload() { this.currentReload = this.reloadgingTime; }
	 */

	public boolean isLocked() {
		return this.locked;
	}

	public int getReloadingTime() {
		return this.reloadgingTime;
	}

}
