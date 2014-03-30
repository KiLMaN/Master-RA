package gameplay;

import java.util.ArrayList;

public class GameConfig {
	public static int ENEMIE_SPAWN_COOLDOWN = 10; /* 10 cycles entre apparitions */

	public static ArrayList<Wave> defaultwaves;

	public static Position startPoint = new Position(0, 0);
	public static Position objective = new Position(100, 100);

	public static ArrayList<Tower> defaultTower;

	static {
		defaultwaves = new ArrayList<Wave>();

		ArrayList<Enemies> enemies = new ArrayList<Enemies>();

		int cpt = 10;
		/*while (cpt > 0) {
			Enemies enemie = new Enemies(1, 1);
			enemies.add(enemie);
			cpt--;
		}
		Wave wave = new Wave(enemies);
		defaultwaves.add(wave);

		enemies = new ArrayList<Enemies>();

		cpt = 20;
		while (cpt > 0) {
			Enemies enemie = new Enemies(5, 2);
			enemies.add(enemie);
			cpt--;
		}
		wave = new Wave(enemies);
		defaultwaves.add(wave);*/
		
		
		enemies = new ArrayList<Enemies>();

		cpt = 20;
		while (cpt > 0) {
			Enemies enemie = new Enemies(30, 2);
			enemies.add(enemie);
			cpt--;
		}
		Wave wave = new Wave(enemies);
		defaultwaves.add(wave);
	}

	static {

		Weapon weapon = new Weapon(1, "Lance Roquette", 2, 10, 0, 4,
				WeaponType.Wood, 0, false);
		Weapon weapon2 = new Weapon(2, "coup de point ", 5, 2, 0, 2,
				WeaponType.Wood, 0, false);
		ArrayList<Weapon> defaultArsenal = new ArrayList<Weapon>();
		defaultArsenal.add(weapon);
		defaultArsenal.add(weapon2);

		Tower tower = new Tower(new Position(50, 50), defaultArsenal);
		

		defaultTower = new ArrayList<Tower>();
		defaultTower.add(tower);
		tower = new Tower(new Position(30, 30), defaultArsenal);
		defaultTower.add(tower);
		tower = new Tower(new Position(60, 60), defaultArsenal);
		defaultTower.add(tower);
		tower = new Tower(new Position(70, 70), defaultArsenal);
		defaultTower.add(tower);
	}
}
