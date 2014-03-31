package gameplay;

import java.util.ArrayList;

public class GameConfig {
	public static int ENEMIE_SPAWN_COOLDOWN = 1; /* 10 cycles entre apparitions */
	public static float PATH_THRESHOLD = 2 ; /* Seuil pour passer au point suivant */

	public static ArrayList<Wave> defaultwaves;

	public static Position startPoint = new Position(0, 0);
	public static Position objective = new Position(100, 100);

	public static ArrayList<Tower> defaultTower;
	public static Path defaulPath;

	static {
		defaulPath = new Path();
		defaulPath.addPoint(new Position(10, 50));
		defaulPath.addPoint(new Position(40, 50));
		defaulPath.addPoint(new Position(40, 20));
		defaulPath.addPoint(new Position(100, 100));
		
		defaultwaves = new ArrayList<Wave>();

		ArrayList<Enemie> enemies = new ArrayList<Enemie>();

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
		
		
		enemies = new ArrayList<Enemie>();

		cpt = 100;
		while (cpt > 0) {
			Enemie enemie = new Enemie(30, 2);
			enemies.add(enemie);
			cpt--;
		}
		Wave wave = new Wave(enemies);
		defaultwaves.add(wave);
	}

	static {

		Weapon weapon = new Weapon(1, "Lance Roquette", 20, 20, 0, 1,
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
