package gameplay;

import java.util.ArrayList;

public class Game {

	public static Game currentGame;

	private Boolean playing = false;

	private Boolean paused = false;

	private int numberEnemiesKilled;

	private Player currentPlayer;

	private int currentWaveId = 0;
	private ArrayList<Wave> listWaves;

	private ArrayList<Tower> listTowers;

	private Position startPointEnemie;
	private Position objectiveEnemie;


	public Game() {
		currentGame = this;
		this.listTowers = GameConfig.defaultTower;
		this.listWaves = GameConfig.defaultwaves;
		this.startPointEnemie = GameConfig.startPoint;
		this.objectiveEnemie = GameConfig.objective;
	}

	/* Global Game Tick */
	/* Toute la logique du jeu est inclue dans cette fonction */
	public void gameTick() {
		// TODO :
		// Si la partie n'est pas en pause
		if (!isPaused()) {
			/* Recuperer la vague */
			Wave currentWave = getCurrentWave();

			ArrayList<Enemie> enemiesToSpawn = currentWave
					.getEnemiesLeftToSpawn();
			ArrayList<Enemie> enemiesAlive = currentWave.getEnemiesAlive();

			if (enemiesToSpawn.size() > 0) {
				currentWave.decreaseSpawnCooldown();
				if (currentWave.getSpawnCooldown() == 0) {
					// Generer des enemis (1)
					// Et Supprimer 1 du compteur de la currentWave
					currentWave.spawnEnemies(2, startPointEnemie);
					// Reset du cooldown
					currentWave
							.setSpawnCooldown(GameConfig.ENEMIE_SPAWN_COOLDOWN);

					System.out.println("Spawn d'un enemie @ "
							+ startPointEnemie.toString());
				}
			} else {
				// stop();
			}

			if (enemiesAlive.size() > 0) {
				for (Enemie enemie : enemiesAlive) {
					enemie.move();
					//TODO : TEST SI ARRIVEE
				}

				for (Tower tower : listTowers) {
					tower.tickReloadTimers();

					Enemie target = tower.getTarget();
					if (target == null || !target.isAlive()) {
						System.out.println("recherche d'un enemie");
						tower.targetClosestEnemi(enemiesAlive);
						target = tower.getTarget();
					}

					
					if (tower.targetedEnemieInRange()) {
						
						/*System.out.println("tir d'une tour @ "
								+ tower.getPosition().toString()
								+ " sur enemie @ "
								+ target.getPosition().toString());*/

						if (tower.shootTargetedEnemie()) {
							System.out.println("Enemie Mort !");
							currentWave.enemieKilled(target);
						}
					} else
						tower.targetClosestEnemi(enemiesAlive);
					// Tirer avec la tour

				}
			} else {
				if (enemiesToSpawn.size() == 0) {
					// Tous les enemie du terrain sont mort et il n'y a plus
					// d'enemies a faire apparaitre
					//setPaused(true);
					System.out.println("Wave ended");
					if(listWaves.size() > currentWaveId+1)
						currentWaveId++;
				}
			}
		}
	}

	public Wave getCurrentWave() {
		return this.listWaves.get(currentWaveId);
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getNumberEnemiesKilled() {
		return numberEnemiesKilled;
	}

	public void setNumberEnemiesKilled(int numberEnemiesKilled) {
		this.numberEnemiesKilled = numberEnemiesKilled;
	}

	public boolean isPaused() {
		return this.paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isPlaying() {
		return this.playing;
	}

	public void start() {
		this.playing = true;
	}

	public void stop() {
		this.playing = false;
	}

	public Position getObjectiveEnemie() {
		return this.objectiveEnemie;
	}
	public Position getStartPointEnemie()
	{
		return startPointEnemie;
	}

	public ArrayList<Tower> getTowers() {
		return listTowers;
	}

}
