package gameplay;

import java.util.ArrayList;

public class Game {

	public static Game currentGame;

	private Boolean playing = false;
	private Boolean paused = false;

	private int numberEnemiesKilled;
	private int currentWaveId = 0;

	private int totalPoints = 0;
	private int gamePoints = 0;

	private Player currentPlayer;

	private ArrayList<Wave> listWaves;
	private ArrayList<Tower> listTowers;
	private ArrayList<Weapon> defaultWeapons;

	private Position startPointEnemie;
	private Position objectiveEnemie;

	public Game(ArrayList<Tower> towers, ArrayList<Wave> waves,
			Position startPoint, Position objectivePoint) {
		currentGame = this;
		this.listTowers = towers; // utile ?
		this.listWaves = waves; // utile ?
		this.startPointEnemie = startPoint;
		this.objectiveEnemie = objectivePoint;
	}

	public Game(ArrayList<Tower> towers, ArrayList<Wave> waves) {

		this(towers, waves, new Position(20, 20), new Position(700, 700));

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

			// Apparition des ennemies
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
				ArrayList<Enemie> enemiesToKill = new ArrayList<Enemie>();
				for (Enemie enemie : enemiesAlive) {
					// enemie.move();
					if (enemie.move()) {

						System.out.println("enemie arrivé");
						int lifesPlayer = this.getCurrentPlayer()
								.getLifesPlayer();
						this.getCurrentPlayer().setLifesPlayer(--lifesPlayer);
						System.out.println("vies restantes: " + lifesPlayer);
						enemiesToKill.add(enemie);
						// currentWave.enemieKilled(enemie);

						// enemiesAlive = currentWave.getEnemiesAlive();
						if (lifesPlayer == 0) {
							System.out.println("game over");
							stop();
						}
					}

				}

				if (enemiesToKill.size() > 0) {

					for (Enemie enemie : enemiesToKill) {
						currentWave.enemieKilled(enemie);
					}
				}

				for (Tower tower : listTowers) {
					tower.tickReloadTimers();

					if (!tower.isControledByPlayer()) {
						Enemie target = tower.getTarget();
						if (target == null || !target.isAlive()) {
							// System.out.println("recherche d'un enemie");
							tower.targetClosestEnemi(enemiesAlive);
							target = tower.getTarget();
						}
						// si la cible ennemie est à portée
						if (tower.isTargetedEnemieInRange()) {

							/*
							 * System.out.println("tir d'une tour @ " +
							 * tower.getPosition().toString() + " sur enemie @ "
							 * + target.getPosition().toString());
							 */
							// renvoie false si l'ennemi n'est pas touché right
							// sinon
							if (tower.shootTargetedEnemie()) {
								/* System.out.println("Ennemi Mort !"); */
								currentWave.enemieKilled(target);
								this.addPoints(target.getPoints());
							}
						} else
							tower.targetClosestEnemi(enemiesAlive);
						// Tirer avec la tour
					}
				}
			} else {
				if (enemiesToSpawn.size() == 0) {
					// Tous les enemie du terrain sont mort et il n'y a plus
					// d'enemies a faire apparaitre
					// setPaused(true);
					/* System.out.println("Wave ended"); */
					if (listWaves.size() > currentWaveId + 1) {
						currentWaveId++;
						setPaused(true);
					} else
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
			ArrayList<Pweapon> pweapons = new ArrayList<Pweapon>();
			for (Weapon weapon : defaultWeapons) {
				Pweapon pweapon = null;
				pweapon = new Pweapon(weapon, weapon.getReloadingTime(),
						weapon.isLockedByDefault());
				pweapons.add(pweapon);
			}
			tower.setWeapons(pweapons);
		}
	}

	public void setWaves(ArrayList<Wave> waves) {
		this.listWaves = waves;
		this.currentWaveId = 0;
	}

	public void setDefaultWeapons(ArrayList<Weapon> weapons) {
		this.defaultWeapons = weapons;
	}

	// points of the game
	public int getPoints() {
		return gamePoints;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void addPoints(int nbPoints) {
		if (nbPoints > 0) {
			this.gamePoints += nbPoints;
			this.totalPoints += nbPoints;

		} else
			System.err.println("Cannot add '" + nbPoints + "' points");
	}

	public void removePoints(int nbPoints) {
		if (nbPoints > 0) {
			this.gamePoints -= nbPoints;

		} else
			System.err.println("Cannot remove '" + nbPoints + "' points");
	}
}
