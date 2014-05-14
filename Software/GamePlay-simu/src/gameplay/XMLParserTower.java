package gameplay;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParserTower {

	public static ArrayList<Tower> parseXMLTowers(Node root) {
		ArrayList<Tower> towers = new ArrayList<Tower>();

		NodeList list = XMLParser.getSubNodes(root, "tower");
		if (list != null) {
			for (int i = 0; i < list.getLength(); i++) {
				Node positionsNode = XMLParser.getSubNode(list.item(i),
						"position");

				String strX = positionsNode.getAttributes().getNamedItem("X")
						.getTextContent();
				String strY = positionsNode.getAttributes().getNamedItem("Y")
						.getTextContent();

				Position position = new Position();
				position.setPositionX(Float.parseFloat(strX));
				position.setPositionY(Float.parseFloat(strY));

				Tower tower = new Tower(i, position);
				towers.add(tower);
			}
		}
		return towers;
	}
}
