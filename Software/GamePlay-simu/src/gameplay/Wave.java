package gameplay;

import java.util.ArrayList;

public class Wave {

	private int numberWave;
	private int totalEnemiesWave;
	private int remainingEnemies;
	
	public ArrayList<Enemies> currentEnemies;
		
	public Wave(int numberWave, int totalEnemiesWave, int remainingEnemies, ArrayList<Enemies> currentEnemies) {
		super();
		this.numberWave = numberWave;
		this.totalEnemiesWave = totalEnemiesWave;
		this.remainingEnemies = remainingEnemies;
		this.currentEnemies = currentEnemies;
	}
	
	public int getNumberWave() {
		return numberWave;
	}
	public void setNumberWave(int numberWave) {
		this.numberWave = numberWave;
	}
	public int getTotalEnemiesWave() {
		return totalEnemiesWave;
	}
	public void setTotalEnemiesWave(int totalEnemiesWave) {
		this.totalEnemiesWave = totalEnemiesWave;
	}
	public int getRemainingEnemies() {
		return remainingEnemies;
	}
	public void setRemainingEnemies(int remainingEnemies) {
		this.remainingEnemies = remainingEnemies;
	}
}
