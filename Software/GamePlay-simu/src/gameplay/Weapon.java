package gameplay;

import java.util.ArrayList;

public class Weapon {

	
	private int idWeapon;
	private String nameWeapon;
	private int numberDamage;
	private float range;
	private int numberEnemiesKilled;
	private int costWeapon;
	private int timeToRecharge;
	private boolean rechargeWeapon;
	
	public ArrayList<WeaponType> currentWeaponType;
	
	public Weapon(int idWeapon, String nameWeapon, int numberDamage, float range, int numberEnemiesKilled, int costWeapon, int timeToRecharge, boolean rechargeWeapon, ArrayList<WeaponType> currentWeaponType) {
		super();
		this.idWeapon = idWeapon;
		this.nameWeapon = nameWeapon;
		this.numberDamage = numberDamage;
		this.range = range;
		this.numberEnemiesKilled = numberEnemiesKilled;
		this.costWeapon = costWeapon;
		this.timeToRecharge = timeToRecharge;
		this.rechargeWeapon = rechargeWeapon;
		this.currentWeaponType = currentWeaponType;
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
