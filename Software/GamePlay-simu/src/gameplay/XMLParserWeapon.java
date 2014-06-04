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

				int idWeapon = XMLParser.getIntAttribute(list.item(i), "ID");
				String nameWeapon = XMLParser.getStringAttribute(list.item(i),
						"NAME");

				/* Damage, maximum and cost upgrade */
				int numberDamage = XMLParser.getIntAttribute(list.item(i),
						"DAMAGE");
				int maxDamage = XMLParser.getIntAttribute(list.item(i),
						"MAX_DAMAGE");
				int costDamage = XMLParser.getIntAttribute(list.item(i),
						"COST_UPGRADE_DAMAGE");

				/* Range, maximum and cost upgrade */
				int range = XMLParser.getIntAttribute(list.item(i), "RANGE");
				int maxRange = XMLParser.getIntAttribute(list.item(i),
						"MAX_RANGE");
				int costRange = XMLParser.getIntAttribute(list.item(i),
						"COST_UPGRADE_RANGE");

				/* Reload Time */
				int timeToReload = XMLParser.getIntAttribute(list.item(i),
						"RELOADTIME");

				/* Lock and unloking cost */
				int costUnclockWeapon = XMLParser.getIntAttribute(list.item(i),
						"COST_UNLOCK");

				boolean lockedDefaultWeapon = XMLParser.getBooleanAttribute(
						list.item(i), "LOCKED");

				// WeaponType weaponType = WeaponType.valueOf(strTYPE);

				Weapon weapon = new Weapon(idWeapon, nameWeapon, numberDamage,
						maxDamage, costDamage, range, maxRange, costRange,
						timeToReload, costUnclockWeapon, lockedDefaultWeapon);

				weapons.add(weapon);
			}
		}
		return weapons;
	}
}
