package gameplay;

import java.util.ArrayList;

public class Game {

	public static Game currentGame;

	private Boolean playing = false;
	private Boolean paused = false;

	private int numberEnemiesKilled;
	private int currentWaveId = 0;

	private Player currentPlayer;

	private ArrayList<Wave> listWaves;
	private ArrayList<Tower> listTowers;
	private ArrayList<Weapon> defaultWeapons;

	private Position startPointEnemie;
	private Position objectiveEnemie;

	public Game(ArrayList<Tower> towers, ArrayList<Wave> waves,
			Position startPoint, Position objectivePoint) {
		currentGame = this;
		this.listTowers = towers;
		this.listWaves = waves;
		this.startPointEnemie = startPoint;
		this.objectiveEnemie = objectivePoint;
	}

	public Game(ArrayList<Tower> towers, ArrayList<Wave> waves) {

		this(towers, waves, new Position(20, 20), new Position(200, 200));

		// this.startPointsEnemie.add(new Position(20, 20));
		// this.objectivesEnemie.add(new Position(200, 200));
	}

	public Game() {
		this(new ArrayList<Tower>(), new ArrayList<Wave>());
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
					currentWave.spawnEnemies(1, startPointEnemie,
							objectiveEnemie);
					// Reset du cooldown
					currentWave
							.setSpawnCooldown(GameConfig.ENEMIE_SPAWN_COOLDOWN);

					/*
					 * System.out.println("Spawn d'un enemie @ " +
					 * startPointEnemie.toString());
					 */
				}
			} else {
				// stop();
			}

			// si il reste des ennemis en vie
			if (enemiesAlive.size() > 0) {
				for (Enemie enemie : enemiesAlive) {
					enemie.move();
					// TODO : TEST SI ARRIVEE
				}

				for (Tower tower : listTowers) {
					tower.tickReloadTimers();

					Enemie target = tower.getTarget();
					if (target == null || !target.isAlive()) {
						// System.out.println("recherche d'un enemie");
						tower.targetClosestEnemi(enemiesAlive);
						target = tower.getTarget();
					}
					// si un ennemi est � port�e
					if (tower.targetedEnemieInRange()) {

						/*
						 * System.out.println("tir d'une tour @ " +
						 * tower.getPosition().toString() + " sur enemie @ " +
						 * target.getPosition().toString());
						 */
						// renvoie false si l'ennemi n'est pas touch� right
						// sinon
						if (tower.shootTargetedEnemie()) {
							/* System.out.println("Ennemi Mort !"); */
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
					// setPaused(true);
					/* System.out.println("Wave ended"); */
					if (listWaves.size() > currentWaveId + 1)
						currentWaveId++;
					else
						stop();
				}
			}
		}
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

	public static Game getCurrentGame() {
		return currentGame;
	}

	public static void setCurrentGame(Game currentGame) {
		Game.currentGame = currentGame;
	}

	public Boolean getPlaying() {
		return playing;
	}

	public void setPlaying(Boolean playing) {
		this.playing = playing;
	}

	public Boolean getPaused() {
		return paused;
	}

	public void setPaused(Boolean paused) {
		this.paused = paused;
	}

	public int getCurrentWaveId() {
		return currentWaveId;
	}

	public void setCurrentWaveId(int currentWaveId) {
		this.currentWaveId = currentWaveId;
	}

	public ArrayList<Wave> getListWaves() {
		return listWaves;
	}

	public void setListWaves(ArrayList<Wave> listWaves) {
		this.listWaves = listWaves;
	}

	public ArrayList<Tower> getListTowers() {
		return listTowers;
	}

	public void setListTowers(ArrayList<Tower> listTowers) {
		this.listTowers = listTowers;
	}

	public void setStartPointEnemie(Position startPointEnemie) {
		this.startPointEnemie = startPointEnemie;
	}

	public void setObjectiveEnemie(Position objectiveEnemie) {
		this.objectiveEnemie = objectiveEnemie;
	}

	public Wave getCurrentWave() {
		return this.listWaves.get(currentWaveId);
	}

	public ArrayList<Weapon> getDefaultWeapons() {
		return defaultWeapons;
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

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Position getObjectiveEnemie() {
		return this.objectiveEnemie;
	}

	public Position getStartPointEnemie() {
		return startPointEnemie;
	}

	public ArrayList<Tower> getTowers() {
		return listTowers;
	}

	public void setTowers(ArrayList<Tower> towers) {
		this.listTowers = towers;
	}

	public void assignWeapons() {
		for (Tower tower : listTowers) {
			tower.setWeapons(defaultWeapons);
		}
	}

	public void setWaves(ArrayList<Wave> waves) {
		this.listWaves = waves;
		this.currentWaveId = 0;
	}

	public void setDefaultWeapons(ArrayList<Weapon> weapons) {
		this.defaultWeapons = weapons;
	}

}
