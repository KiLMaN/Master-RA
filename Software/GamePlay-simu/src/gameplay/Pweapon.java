package gameplay;

public class Pweapon {
	private boolean locked;
	private int reloadingTime;
	private int currentReload;

	public Weapon Weapon;

	public Pweapon(Weapon weapon, int timeToRecharge, boolean lockedWeapon) {
		this.Weapon = weapon;
		this.reloadingTime = timeToRecharge;
		this.currentReload = 0;
		this.locked = lockedWeapon;
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

}
