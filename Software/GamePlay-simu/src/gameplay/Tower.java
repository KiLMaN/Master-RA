package gameplay;

import java.util.ArrayList;

public class Tower {

	private int idTower;
	private String nameTower;
	private int levelTower;
	private boolean free;
	
	private int puissanceTour = 0;
	
	private ArrayList<Weapon> currentWeapon;
	
	private ArrayList<Enemies> currentEnemies;
	
		public Tower(int idTower, String nameTower, int levelTower, boolean free,
			ArrayList<Weapon> currentWeapon, ArrayList<Enemies> currentEnemies) {
		super();
		this.idTower = idTower;
		this.nameTower = nameTower;
		this.levelTower = levelTower;
		this.free = free;
		this.currentWeapon = currentWeapon;
		this.currentEnemies = currentEnemies;
	}

	public void shoot(){
		while(currentEnemies.size() != 0){
			//choix arme la plus puissante(arme)
			
	
	 	if( currentWeapon.get(puissanceTour).isRechargeWeapon() ==libre){
	 		
	  	choix ennemis dans champs d'action
	   tant que (ennemis dans champs d'action et vie.ennemie!=0 et tpsrecharge==0
	   	random (tire) //proba=0.75
	   si ennemi touche alors 
	   vie.ennemi=vie.ennemie-degat.arme
	  
	 	}
	  
	 
		}
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
