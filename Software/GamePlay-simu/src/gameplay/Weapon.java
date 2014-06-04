package gameplay;

public class Weapon {

	private int idWeapon;
	private String nameWeapon;

	private int numberDamage;
	private float range;

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
		this.numberDamage = numberDamage;
		this.range = range;

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
		return costUnlockWeapon;
	}

	public void setCostWeapon(int costWeapon) {
		this.costUnlockWeapon = costWeapon;
	}

	/*
	 * public boolean isReloading() { return currentReload != 0; }
	 * 
	 * public void tickReloading() { this.currentReload--; }
	 * 
	 * public void startReload() { this.currentReload = this.reloadgingTime; }
	 */

	public boolean isLockedByDefault() {
		return this.defaultLocked;
	}

	public int getReloadingTime() {
		return this.reloadingTime;
	}

	/*
	 * public WeaponType getWeaponType() { return this.weaponType; }
	 */

	public int getCostUpgrade(UpgradeType upgradeType) {
		switch (upgradeType) {
		case UPGRADE_POWER:
			return this.costUpgradeDamage;
		case UPGRADE_RANGE:
			return this.costUpgradeRange;
		default:
			return Integer.MAX_VALUE;
		}
	}

	public boolean upgrade(UpgradeType upgradeType) {
		switch (upgradeType) {
		case UPGRADE_POWER:
			if (this.numberDamage < this.maxDamage)
				return false;
			else {
				this.numberDamage++;
				return true;
			}

		case UPGRADE_RANGE:
			if (this.range < this.maxRange)
				return false;
			else {
				this.range++;
				return true;
			}
		default:
			return false;

		}
	}

}
