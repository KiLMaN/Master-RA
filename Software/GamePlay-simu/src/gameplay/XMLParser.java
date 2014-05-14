package gameplay;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import comon.FileReaderInterface;

public class XMLParser {

	private String fileName = "default.xml";
	private Document xmlDoc = null;

	public XMLParser(String fileName) {
		this.fileName = fileName;
		this.xmlDoc = null;
	}

	public void loadFile(FileReaderInterface reader) {
		try {
			// File fXmlFile = new File(fileName);
			reader.setFile(fileName);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			xmlDoc = dBuilder.parse(reader.getStream());
			xmlDoc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ xmlDoc.getDocumentElement().getNodeName());
		} catch (IOException ioExt) {
			System.err.println(ioExt);
		} catch (ParserConfigurationException e) {
			System.err.println(e);
		} catch (SAXException e) {
			System.err.println(e);
		}
	}

	public Node getRoot() {
		return xmlDoc.getDocumentElement();
	}

	public Node getNode(String name, int index) {
		return getSubNode(xmlDoc, name, index);
	}

	public Node getNode(String name) {
		return getNode(name, 0);
	}

	public NodeList getNodes(String name) {
		return getSubNodes(xmlDoc, name);
	}

	public static Node getSubNode(Node parent, String name, int index) {
		NodeList list;
		if ((list = getSubNodes(parent, name)) != null)
			return list.item(index);
		else
			return null;
	}

	public static Node getSubNode(Node parent, String name) {
		return getSubNode(parent, name, 0);
	}

	public static NodeList getSubNodes(Node parent, String name) {
		if (parent instanceof Element)
			return ((Element) parent).getElementsByTagName(name);
		else
			return null;

	}

	public static int getIntAttribute(Node Parent, String elementName) {
		Node data = Parent.getAttributes().getNamedItem(elementName);
		if (data != null) {
			String str = data.getTextContent();
			try {
				int ret = Integer.parseInt(str);
				return ret;
			} catch (NumberFormatException e) {
				return 0;
			}
		} else
			return 0;
	}

	public static float getFloatAttribute(Node Parent, String elementName) {
		Node data = Parent.getAttributes().getNamedItem(elementName);
		if (data != null) {
			String str = data.getTextContent();
			try {
				float ret = Float.parseFloat(str);
				return ret;
			} catch (NumberFormatException e) {
				return 0;
			}
		} else
			return 0;
	}

	public static String getStringAttribute(Node Parent, String elementName) {
		Node data = Parent.getAttributes().getNamedItem(elementName);
		if (data != null) {
			String str = data.getTextContent();
			return str;
		} else
			return "";
	}

	public static boolean getBooleanAttribute(Node Parent, String elementName) {
		// final Pattern patternBool = Pattern.compile(Pattern.quote("1|true"),
		// Pattern.CASE_INSENSITIVE);

		Node data = Parent.getAttributes().getNamedItem(elementName);
		if (data != null) {
			String str = data.getTextContent();
			if (str.equals("true") || str.equals("1"))
				return true;
			else
				return false;
			// return patternBool.matcher(str).find();
		} else
			return false;
	}
}