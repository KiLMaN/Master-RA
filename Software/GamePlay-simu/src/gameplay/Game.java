package gameplay;

import java.util.ArrayList;

public class Game {

	private int numberEnemiesKilled;
	private int numberWaveGame;

	private Player currentPlayer;
	private Wave currentWave;

	public ArrayList<Tower> currentTower;
	public ArrayList<Weapon> currentWeapon;
	public ArrayList<Enemies> currentEnemies;

	public Game(int numberEnemiesKilled, int numberWaveGame, Player currentPlayer, Wave currentWave, ArrayList<Tower> currentTower, ArrayList<Weapon> currentWeapon, ArrayList<Enemies> currentEnemies) {
		super();
		
		
		this.numberEnemiesKilled = numberEnemiesKilled;
		this.numberWaveGame = numberWaveGame;
		this.currentPlayer = currentPlayer;
		this.currentWave = currentWave;
		this.currentTower = currentTower;
		this.currentWeapon = currentWeapon;
		this.currentEnemies = currentEnemies;
	}
	


	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Wave getCurrentWave() {
		return currentWave;
	}

	public void setCurrentWave(Wave currentWave) {
		this.currentWave = currentWave;
	}

	public int getNumberEnemiesKilled() {
		return numberEnemiesKilled;
	}

	public void setNumberEnemiesKilled(int numberEnemiesKilled) {
		this.numberEnemiesKilled = numberEnemiesKilled;
	}

	public int getNumberWaveGame() {
		return numberWaveGame;
	}

	public void setNumberWaveGame(int numberWaveGame) {
		this.numberWaveGame = numberWaveGame;
	}

}
