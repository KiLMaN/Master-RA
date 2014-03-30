package gameplay;

import java.util.ArrayList;

public class Wave {

	private int cooldown_spawn;

	private ArrayList<Enemie> enemiesToSpawn;
	private ArrayList<Enemie> enemiesAlive;
	private ArrayList<Enemie> enemiesDead;

	public Wave(ArrayList<Enemie> enemiesToSpawn) {
		this.enemiesToSpawn = enemiesToSpawn;
		enemiesAlive = new ArrayList<Enemie>();
		enemiesDead = new ArrayList<Enemie>();
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
			Enemie enemie = enemiesToSpawn.remove(0);
			enemie.spawn(positionSpawn.clone());

			enemiesAlive.add(enemie);
			countSpawn--;
		}
	}

	public ArrayList<Enemie> getEnemiesLeftToSpawn() {
		return enemiesToSpawn;
	}

	public ArrayList<Enemie> getEnemiesAlive() {
		return enemiesAlive;
	}

	public void enemieKilled(Enemie target) {
		enemiesAlive.remove(target);
		enemiesDead.add(target);
	}

}
