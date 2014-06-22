package gameplay;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParserWave {

	public static ArrayList<Wave> parseXMLWaves(Node root) {
		int countSpawn = 0;
		ArrayList<Wave> waves = new ArrayList<Wave>();

		NodeList listWave = XMLParser.getSubNodes(root, "wave");
		if (listWave != null) {
			for (int i = 0; i < listWave.getLength(); i++) {
				NodeList list = XMLParser.getSubNodes(listWave.item(i),
						"enemie");

				ArrayList<Enemie> enemiesToSpawn = new ArrayList<Enemie>();
				for (int a = 0; a < list.getLength(); a++) {
					int count = XMLParser
							.getIntAttribute(list.item(a), "COUNT");
					int speed = XMLParser
							.getIntAttribute(list.item(a), "SPEED");
					int health = XMLParser.getIntAttribute(list.item(a),
							"HEALTH");
					int points = XMLParser.getIntAttribute(list.item(a),
							"POINTS");

					for (int cpt = 0; cpt < count; cpt++) {
						Enemie en = new Enemie(health, speed, points);
						en.setId(++countSpawn);
						enemiesToSpawn.add(en);
					}
				}

				Wave wave = new Wave(enemiesToSpawn);

				boolean randomSpawn = XMLParser.getBooleanAttribute(
						listWave.item(i), "RANDOM");
				if (randomSpawn)
					wave.randomiseSpawn();

				waves.add(wave);
			}
		}
		return waves;
	}
}
