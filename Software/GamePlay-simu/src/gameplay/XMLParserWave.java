package gameplay;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParserWave {

	public static ArrayList<Wave> parseXMLWaves(Node root) {
		ArrayList<Wave> waves = new ArrayList<Wave>();

		NodeList listWave = XMLParser.getSubNodes(root, "wave");
		if (listWave != null) {
			for (int i = 0; i < listWave.getLength(); i++) {
				NodeList enemiesRoot = XMLParser.getSubNodes(listWave.item(i),
						"enemie");

				ArrayList<Enemie> enemiesToSpawn = new ArrayList<Enemie>();
				for (int a = 0; a < enemiesRoot.getLength(); a++) {
					int COUNT = Integer.parseInt(enemiesRoot.item(a)
							.getAttributes().getNamedItem("COUNT")
							.getTextContent());
					int SPEED = Integer.parseInt(enemiesRoot.item(a)
							.getAttributes().getNamedItem("SPEED")
							.getTextContent());
					int HEALTH = Integer.parseInt(enemiesRoot.item(a)
							.getAttributes().getNamedItem("HEALTH")
							.getTextContent());

					for (int cpt = 0; cpt < COUNT; cpt++) {
						Enemie en = new Enemie(HEALTH, SPEED);
						enemiesToSpawn.add(en);
					}
				}

				Wave wave = new Wave(enemiesToSpawn);

				Node randomSpawn = listWave.item(i).getAttributes()
						.getNamedItem("RANDOM");
				if (randomSpawn != null
						&& (randomSpawn.getTextContent().compareToIgnoreCase(
								"1") == 0 || randomSpawn.getTextContent()
								.compareToIgnoreCase("true") == 0)) {
					wave.randomiseSpawn();
				}
				waves.add(wave);
			}
		}
		return waves;
	}
}
