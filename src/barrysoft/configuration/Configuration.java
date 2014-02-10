package barrysoft.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import barrysoft.xml.XMLUtils;
import barrysoft.xml.XMLizable;

public class Configuration implements XMLizable {

	public static final String DEF_XML_ROOT = "configuration";

	private String xmlRoot = DEF_XML_ROOT;

	private Vector<ConfigurationEntry> configuration;

	public Configuration() {
		configuration = new Vector<ConfigurationEntry>();
	}

	public Configuration(String xmlRoot) {
		this();
		setXmlRoot(xmlRoot);
	}

	public void addConfiguration(String xmlElement, XMLizable object) {
		addConfiguration(xmlElement, null, object);
	}

	public void addConfiguration(String xmlElement, String xmlId, XMLizable object) {
		configuration.add(new ConfigurationEntry(object, xmlElement, xmlId));
	}
	
	public void addConfiguration(ConfigurationEntry ce) {
		configuration.add(ce);
	}

	public String getXmlRoot() {
		return xmlRoot;
	}

	public void setXmlRoot(String xmlRoot) {
		this.xmlRoot = xmlRoot;
	}

	public String getRootLessXML(int indentation) {

		String xml = new String();

		for (ConfigurationEntry ce : configuration) {

			if (ce.isReadOnly())
				continue;
			
			XMLizable object = ce.getObject();

			if (ce.getId() == null)
				xml += object.getXML(indentation);
			else
				xml += object.getXML(indentation, ce.getId());

		}

		return xml;

	}

	public String getXML(int indentation) {
		return getXML(indentation, null);
	}

	public String getXML(int indentation, String id) {

		String xml = new String();

		xml += XMLUtils.openTag(getXmlRoot(), id, indentation);

		xml += getRootLessXML(indentation + 1);

		xml += XMLUtils.closeTag(getXmlRoot(), indentation);

		return xml;

	}

	public void loadFromXML(Node node) {

		if (!node.getNodeName().equals(getXmlRoot()))
			throw new IllegalArgumentException("Can't load configuration,"
					+ " wrong node passed: " + node.getNodeName());

		Document doc = node.getOwnerDocument();

		for (ConfigurationEntry ce : configuration) {

			XMLizable object = ce.getObject();

			NodeList nodes = doc.getElementsByTagName(ce.getTag());

			Node n = loadNode(ce.getId(), nodes);

			if (n == null)
				continue;

			object.loadFromXML(n);

		}
	}
	
	protected Node loadNode(String idValue, NodeList nodes) {
		
		for (int i=0; i < nodes.getLength(); i++) {
			
			if (checkNodeId(nodes.item(i), idValue))
				return nodes.item(i);
			
		}
		
		return null;
		
	}
	
	protected boolean checkNodeId(Node node, String idValue) {
		
		if (idValue == null)
			return true;
		
		try {
			
			return (node.getAttributes().getNamedItem("id").
					getNodeValue().equals(idValue));
			
		} catch (NullPointerException e) {
			return false;
		}
		
	}

	public void save(File outFile) throws IOException {
		if (!outFile.exists())
			outFile.createNewFile();

		FileOutputStream fos = new FileOutputStream(outFile);
		fos.write(String.format("%s\n%s", XMLUtils.getXMLHeader(), getXML(0))
				.getBytes());
		fos.close();
	}

}
