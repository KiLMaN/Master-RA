package gameplay;

public class Pweapon {
	private boolean locked;
	private int reloadingTime;
	private int currentReload;

	private int damage;
	private float range;

	public Weapon Weapon;

	public Pweapon(Weapon weapon, int timeToRecharge, boolean lockedWeapon) {
		this.Weapon = weapon;
		this.reloadingTime = timeToRecharge;
		this.currentReload = 0;
		this.locked = lockedWeapon;
		damage = weapon.getDefaultDamage();
		range = weapon.getDefaultRange();
	}

	public Weapon getWeapon() {
		return Weapon;
	}

	public boolean isLocked() {
		return this.locked;
	}

	public void setLocked(boolean isLocked) {
		this.locked = isLocked;
	}

	public boolean isReloading() {
		return currentReload != 0;
	}

	public void tickReloading() {
		this.currentReload--;
	}

	public void startReload() {
		this.currentReload = this.reloadingTime;
	}

	public int getDamage() {
		return this.damage;
	}

	public float getRange() {
		return this.range;
	}

	public boolean canUpgrade(UpgradeType upgradeType) {
		switch (upgradeType) {
		case UPGRADE_POWER:
			if (this.damage >= this.Weapon.getMaxDamage())
				return false;
			else {
				return true;
			}

		case UPGRADE_RANGE:
			if (this.range >= this.Weapon.getMaxRange())
				return false;
			else {
				return true;
			}
		default:
			return false;
		}
	}

	public int getCostUpgrade(UpgradeType upgradeType) {
		switch (upgradeType) {
		case UPGRADE_POWER:
			return this.Weapon.getCostDamage();
		case UPGRADE_RANGE:
			return this.Weapon.getCostRange();
		default:
			return Integer.MAX_VALUE;
		}
	}

	protected boolean upgrade(UpgradeType upgradeType) {
		switch (upgradeType) {
		case UPGRADE_POWER:

			if (this.damage >= this.Weapon.getMaxDamage())
				return false;
			else {
				this.damage++;
				return true;
			}

		case UPGRADE_RANGE:

			if (this.range >= this.Weapon.getMaxRange())
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
