package gameplay;

public class Weapon {

	private int idWeapon;
	private String nameWeapon;

	private int defaultDamage;
	private float defaultRange;

	private int reloadingTime;

	// Statistics only
	private int numberEnemiesKilled;

	/* Locked by default and cost to unlock */
	private boolean defaultLocked;
	private int costUnlockWeapon = 0;
	/* Upgrades, maximums and costs */
	private int maxDamage = 0;
	private float maxRange = 0;
	private int costUpgradeRange = 0;
	private int costUpgradeDamage = 0;

	public Weapon(int idWeapon, String nameWeapon, int numberDamage,
			int maxDamage, int costDamage, float range, int maxRange,
			int costRange, int timeToReload, int costUnclockWeapon,
			boolean lockedDefaultWeapon) {
		this.idWeapon = idWeapon;
		this.nameWeapon = nameWeapon;
		this.defaultDamage = numberDamage;
		this.defaultRange = range;

		this.reloadingTime = timeToReload;

		this.defaultLocked = lockedDefaultWeapon;
		this.costUnlockWeapon = costUnclockWeapon;

		this.maxDamage = maxDamage;
		this.maxRange = maxRange;
		this.costUpgradeRange = costRange;
		this.costUpgradeDamage = costDamage;

		this.numberEnemiesKilled = 0;
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

	public int getDefaultDamage() {
		return defaultDamage;
	}

	// public void setNumberDamage(int numberDamage) {
	// this.defaultDamage = numberDamage;
	// }

	public float getDefaultRange() {
		return defaultRange;
	}

	// public void setRange(float range) {
	// this.defaultRange = range;
	// }

	public int getNumberEnemiesKilled() {
		return numberEnemiesKilled;
	}

	public void setNumberEnemiesKilled(int numberEnemiesKilled) {
		this.numberEnemiesKilled = numberEnemiesKilled;
	}

	public int getCostWeapon() {
		return costUnlockWeapon;
	}

	public void setCostWeapon(int costWeapon) {
		this.costUnlockWeapon = costWeapon;
	}

	public boolean isLockedByDefault() {
		return this.defaultLocked;
	}

	public int getReloadingTime() {
		return this.reloadingTime;
	}

	public float getMaxRange() {
		return maxRange;
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	public int getCostDamage() {
		return costUpgradeDamage;
	}

	public int getCostRange() {
		return costUpgradeRange;
	}

}
