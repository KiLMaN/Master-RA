package gameplay;

import java.util.ArrayList;

public class Wave {

	private int cooldown_spawn;

	private ArrayList<Enemies> enemiesToSpawn;
	private ArrayList<Enemies> enemiesAlive;
	private ArrayList<Enemies> enemiesDead;

	public Wave(ArrayList<Enemies> enemiesToSpawn) {
		this.enemiesToSpawn = enemiesToSpawn;
		enemiesAlive = new ArrayList<Enemies>();
		enemiesDead = new ArrayList<Enemies>();
		cooldown_spawn = GameConfig.ENEMIE_SPAWN_COOLDOWN;
	}

	public void decreaseSpawnCooldown() {
		this.cooldown_spawn--;
	}

	public int getSpawnCooldown() {
		return this.cooldown_spawn;
	}

	public void setSpawnCooldown(int cooldown) {
		this.cooldown_spawn = cooldown;
	}

	public void spawnEnemies(int countSpawn, Position positionSpawn) {

		while (countSpawn > 0 && enemiesToSpawn.size() > 0) {
			Enemies enemie = enemiesToSpawn.remove(0);
			enemie.spawn(positionSpawn.clone());

			enemiesAlive.add(enemie);
			countSpawn--;
		}
	}

	public ArrayList<Enemies> getEnemiesLeftToSpawn() {
		return enemiesToSpawn;
	}

	public ArrayList<Enemies> getEnemiesAlive() {
		return enemiesAlive;
	}

	public void enemieKilled(Enemies target) {
		enemiesAlive.remove(target);
		enemiesDead.add(target);
	}

}
