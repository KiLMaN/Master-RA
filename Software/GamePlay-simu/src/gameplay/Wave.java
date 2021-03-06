package gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Wave {

	private int cooldown_spawn;

	private ArrayList<Enemie> enemiesToSpawn;
	private ArrayList<Enemie> enemiesAlive;
	private ArrayList<Enemie> enemiesDead;
	public int spawnCounter = 0;

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

	public ArrayList<Enemie> spawnEnemies(int countSpawn,
			Position positionSpawn, Position positionObjectif) {

		ArrayList<Enemie> list = new ArrayList<Enemie>();
		while (countSpawn > 0 && enemiesToSpawn.size() > 0) {
			Enemie enemie = enemiesToSpawn.remove(0);
			enemie.spawn(positionSpawn.clone());
			// enemie.setPath(GameConfig.defaulPath.clone());
			enemie.setObjectif(positionObjectif);
			enemie.computePath();
			list.add(enemie);
			enemiesAlive.add(enemie);
			countSpawn--;
			spawnCounter++;
		}
		return list;

	}

	public ArrayList<Enemie> getEnemiesLeftToSpawn() {
		return enemiesToSpawn;
	}

	public ArrayList<Enemie> getEnemiesAlive() {
		return enemiesAlive;
	}

	public void enemieKilled(Enemie target) {
		if (!enemiesAlive.remove(target)) {
			System.err.println("Error removing !");
		} else {
			enemiesDead.add(target);
		}

	}

	public ArrayList<Enemie> getEnemiesDead() {
		return enemiesDead;
	}

	public void randomiseSpawn() {
		long seed = System.nanoTime();
		Collections.shuffle(enemiesToSpawn, new Random(seed));
	}

}
