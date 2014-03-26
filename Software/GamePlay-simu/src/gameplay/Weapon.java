package gameplay;

public class Weapon {

	
	private int idWeapon;
	private String nameWeapon;
	private int numberDamage;
	private float range;
	private int numberEnemiesKilled;
	private int costWeapon;
	private int timeToRecharge;
	private boolean rechargeWeapon;
	
	
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
	public int getTimeToRecharge() {
		return timeToRecharge;
	}
	public void setTimeToRecharge(int timeToRecharge) {
		this.timeToRecharge = timeToRecharge;
	}
	public boolean isRechargeWeapon() {
		return rechargeWeapon;
	}
	public void setRechargeWeapon(boolean rechargeWeapon) {
		this.rechargeWeapon = rechargeWeapon;
	}
}
