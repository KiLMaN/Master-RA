package gameplay;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tools.MessageBox;

public class XMLParser {

	private String fileName = "default.xml";
	private boolean loaded = false;
	private Document xmlDoc = null;

	public XMLParser(String fileName) {
		this.fileName = fileName;
		this.loaded = false;
		this.xmlDoc = null;
	}

	public void loadFile() {
		try {
			File fXmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			xmlDoc = dBuilder.parse(fXmlFile);
			xmlDoc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ xmlDoc.getDocumentElement().getNodeName());
			this.loaded = true;
		} catch (IOException ioExt) {
			MessageBox.ShowError(ioExt);
		} catch (ParserConfigurationException e) {
			MessageBox.ShowError(e);
		} catch (SAXException e) {
			MessageBox.ShowError(e);
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

	public Node getSubNode(Node parent, String name, int index) {
		NodeList list;
		if ((list = getSubNodes(parent, name)) != null)
			return list.item(index);
		else
			return null;
	}

	public Node getSubNode(Node parent, String name) {
		return getSubNode(parent, name, 0);
	}

	public NodeList getSubNodes(Node parent, String name) {
		if (this.loaded)
			if (parent instanceof Element)
				return ((Element) parent).getElementsByTagName(name);
			else
				return null;
		else
			return null;
	}

	public NodeList getNodes(String name) {
		return getSubNodes(xmlDoc, name);
	}
}