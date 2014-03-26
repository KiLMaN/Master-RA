package gameplay;

import java.util.ArrayList;

public class Tower {

	private int idTower;
	private String nameTower;
	private float positionXTower;
	private float positionYTower;
	private int levelTower;
	private boolean free;
	
	private Weapon currentWeapon;
	
	public ArrayList<Enemies> currentEnemies;
	
	
	public Tower(int idTower, String nameTower, float positionXTower, float positionYTower, int levelTower, Weapon currentWeapon, boolean free, ArrayList<Enemies> currentEnemies) {
		super();
		this.idTower = idTower;
		this.nameTower = nameTower;
		this.positionXTower = positionXTower;
		this.positionYTower = positionYTower;
		this.levelTower = levelTower;
		this.currentWeapon = currentWeapon;
		this.free = free;
		this.currentEnemies = currentEnemies;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public ArrayList<Enemies> getCurrentEnemies() {
		return currentEnemies;
	}

	public void setCurrentEnemies(ArrayList<Enemies> currentEnemies) {
		this.currentEnemies = currentEnemies;
	}

	public int getIdTower() {
		return idTower;
	}
	public void setIdTower(int idTower) {
		this.idTower = idTower;
	}
	public String getNameTower() {
		return nameTower;
	}
	public void setNameTower(String nameTower) {
		this.nameTower = nameTower;
	}
	public float getPositionXTower() {
		return positionXTower;
	}
	public void setPositionXTower(float positionXTower) {
		this.positionXTower = positionXTower;
	}
	public float getPositionYTower() {
		return positionYTower;
	}
	public void setPositionYTower(float positionYTower) {
		this.positionYTower = positionYTower;
	}
	public int getLevelTower() {
		return levelTower;
	}
	public void setLevelTower(int levelTower) {
		this.levelTower = levelTower;
	}
	public Weapon getCurrentWeapon() {
		return currentWeapon;
	}
	public void setCurrentWeapon(Weapon currentWeapon) {
		this.currentWeapon = currentWeapon;
	}
	
	
	
}
