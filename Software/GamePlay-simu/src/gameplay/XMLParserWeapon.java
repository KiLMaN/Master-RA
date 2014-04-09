package gameplay;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParserWeapon {

	public static ArrayList<Weapon> parseXMLWeapon(Node root) {
		ArrayList<Weapon> weapons = new ArrayList<Weapon>();

		NodeList list = XMLParser.getSubNodes(root, "weapon");
		if (list != null) {
			for (int i = 0; i < list.getLength(); i++) {
				/*
				 * <weapon NAME="Lance Flame" DAMAGE="10" LOCKED="0"
				 * RELOADTIME="2" RANGE="10" />
				 */
				String strNAME = XMLParser.getStringAttribute(list.item(i),
						"NAME");

				int intDAMAGE = XMLParser.getIntAttribute(list.item(i),
						"DAMAGE");
				int intRELOADTIME = XMLParser.getIntAttribute(list.item(i),
						"RELOADTIME");
				int intRANGE = XMLParser.getIntAttribute(list.item(i), "RANGE");
				int intCOST = XMLParser.getIntAttribute(list.item(i), "COST");

				boolean bLOCKED = XMLParser.getBooleanAttribute(list.item(i),
						"LOCKED");

				Weapon weapon = new Weapon(i, strNAME, intDAMAGE, intRANGE, 0,
						intRELOADTIME, WeaponType.Fire, intCOST, bLOCKED);
				weapons.add(weapon);
			}
		}
		return weapons;
	}
}
