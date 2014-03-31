package gameplay;

import java.util.ArrayList;

public class GameConfig {
	public static int ENEMIE_SPAWN_COOLDOWN = 1; /* 10 cycles entre apparitions */
	public static float PATH_THRESHOLD = 2; /*
											 * Seuil pour passer au point
											 * suivant
											 */

	public static ArrayList<Wave> defaultwaves;

	public static Position startPoint = new Position(0, 0);
	public static Position objective = new Position(100, 100);

	public static ArrayList<Tower> defaultTower;
	public static Path defaulPath;
	private static ArrayList<Weapon> defaulWeapons;

	static {

		Weapon weapon = new Weapon(1, "Lance Roquette", 50, 15, 0, 5,
				WeaponType.Wood, 0, false);
		Weapon weapon2 = new Weapon(2, "LASER", 1, 35, 0, 2, WeaponType.Wood,
				0, false);
		defaulWeapons = new ArrayList<Weapon>();
		defaulWeapons.add(weapon);
		defaulWeapons.add(weapon2);

		Tower tower = new Tower(new Position(50, 50), defaulWeapons);

		defaultTower = new ArrayList<Tower>();

		defaultTower.add(tower);
		tower = new Tower(new Position(30, 30), defaulWeapons);
		defaultTower.add(tower);
		tower = new Tower(new Position(60, 60), defaulWeapons);
		defaultTower.add(tower);
		tower = new Tower(new Position(70, 70), defaulWeapons);
		defaultTower.add(tower);

	}

	static {
		/*
		 * XMLParser parserTower = new XMLParser("config/towers.xml");
		 * parserTower.loadFile(); Node root = parserTower.getRoot(); NodeList
		 * list = parserTower.getSubNodes(root, "tower"); if (list != null) {
		 * for (int i = 0; i < list.getLength(); i++) { Node positionsNode =
		 * parserTower.getSubNode(list.item(i), "position");
		 * 
		 * String strX = positionsNode.getAttributes().getNamedItem("X")
		 * .getTextContent(); String strY =
		 * positionsNode.getAttributes().getNamedItem("Y") .getTextContent();
		 * 
		 * Position position = new Position();
		 * position.setPositionX(Float.parseFloat(strX));
		 * position.setPositionY(Float.parseFloat(strY));
		 * 
		 * Tower tower = new Tower(position, GameConfig.defaulWeapons);
		 * defaultTower.add(tower); } }
		 */

		defaulPath = new Path();
		defaulPath.addPoint(new Position(10, 50));
		defaulPath.addPoint(new Position(40, 50));

		defaulPath.addPoint(new Position(40, 20));

		defaulPath.addPoint(new Position(100, 100));

		defaultwaves = new ArrayList<Wave>();

		/*
		 * XMLParser parserWaves = new XMLParser("config/waves.xml");
		 * parserWaves.loadFile(); Node rootWave = parserWaves.getRoot();
		 * NodeList listWave = parserWaves.getSubNodes(rootWave, "wave"); if
		 * (list != null) { for (int i = 0; i < listWave.getLength(); i++) {
		 * NodeList enemiesRoot = parserWaves.getSubNodes( listWave.item(i),
		 * "enemie");
		 * 
		 * Node randomSpawn = listWave.item(i).getAttributes()
		 * .getNamedItem("RANDOM");
		 * 
		 * ArrayList<Enemie> enemiesToSpawn = new ArrayList<Enemie>(); for (int
		 * a = 0; a < enemiesRoot.getLength(); a++) { int COUNT =
		 * Integer.parseInt(enemiesRoot.item(a)
		 * .getAttributes().getNamedItem("COUNT") .getTextContent()); int SPEED
		 * = Integer.parseInt(enemiesRoot.item(a)
		 * .getAttributes().getNamedItem("SPEED") .getTextContent()); int HEALTH
		 * = Integer.parseInt(enemiesRoot.item(a)
		 * .getAttributes().getNamedItem("HEALTH") .getTextContent());
		 * 
		 * for (int cpt = 0; cpt < COUNT; cpt++) { Enemie en = new
		 * Enemie(HEALTH, SPEED); enemiesToSpawn.add(en); } }
		 * 
		 * Wave wave = new Wave(enemiesToSpawn); if (randomSpawn != null &&
		 * randomSpawn.getTextContent() .compareToIgnoreCase("1") == 0) {
		 * wave.randomiseSpawn(); }
		 * 
		 * defaultwaves.add(wave); } }
		 */
		defaultwaves = new ArrayList<Wave>();

		ArrayList<Enemie> enemies = new ArrayList<Enemie>();

		int cpt = 10;
		while (cpt > 0) {
			Enemie enemie = new Enemie(1, 1);
			enemies.add(enemie);
			cpt--;
		}
		Wave wave = new Wave(enemies);
		defaultwaves.add(wave);

		enemies = new ArrayList<Enemie>();

		cpt = 20;
		while (cpt > 0) {
			Enemie enemie = new Enemie(5, 2);
			enemies.add(enemie);
			cpt--;
		}
		wave = new Wave(enemies);
		defaultwaves.add(wave);

		enemies = new ArrayList<Enemie>();

		cpt = 20;
		while (cpt > 0) {
			Enemie enemie = new Enemie(30, 2);
			enemies.add(enemie);
			cpt--;
		}
		/*Wave */wave = new Wave(enemies);
		defaultwaves.add(wave);
	}
}
