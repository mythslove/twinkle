package barrysoft.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import barrysoft.localization.Localization;
import barrysoft.xml.XMLizable;
import barrysoft.xml.XMLUtils;

public class PluginInfo implements XMLizable {

	public final static String XML_ROOT = "info";
	
	public final static String XML_NAME = "name";
	public final static String XML_CLASS = "class";
	public final static String XML_JAR = "jar";
	public final static String XML_TYPE = "type";
	public final static String XML_HOST = "host";
	public final static String XML_VERSION = "version";
	public final static String XML_AUTHOR = "author";
	public final static String XML_DESC = "description";
	
	public final static String DEF_NAME = "No Name";
	public final static String DEF_TYPE = "Plugin";
	public final static String DEF_VERSION = "1.0";
	public final static String DEF_AUTHOR = "Unknown";
	public final static String DEF_INFO = "Generic plugin";
	
	private Hashtable<String, String> values;
	
	public PluginInfo() {
		values = new Hashtable<String, String>();
		
		setInfo(XML_NAME, DEF_NAME);
		setInfo(XML_CLASS, "");
		setInfo(XML_JAR, "");
		setInfo(XML_TYPE, DEF_TYPE);
		setInfo(XML_HOST, "");
		setInfo(XML_VERSION, DEF_VERSION);
		setInfo(XML_AUTHOR, DEF_AUTHOR);
		setInfo(XML_DESC, DEF_INFO);
	}
	
	public void setInfo(String name, String value) {
		values.put(name, value);
	}
	
	public String getInfo(String name) {
		return values.get(name);
	}
	
	public static PluginInfo loadFromFile(File f) 
			throws FileNotFoundException, 
					ParserConfigurationException, 
					SAXException, 
					IOException
	
	{
		
		if (f == null)
			throw new IllegalArgumentException("File was null");
		
		if (!f.exists())
			throw new FileNotFoundException(f.getAbsolutePath());
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		Document doc = db.parse(f);
		doc.getDocumentElement().normalize();
		
		NodeList list = doc.getElementsByTagName(XML_ROOT);
		
		if (list.getLength() == 0)
			throw new IOException("Can't find root node: "+XML_ROOT);
		
		// Only one object is created.
		// If multiple info sections are found
		// than the last one is taken.
		
		Node infoNode = list.item(list.getLength()-1);
		
		PluginInfo pi = new PluginInfo();
		pi.loadFromXML(infoNode);
		
		return pi;
		
	}
	
	public String getXML(int indentation) {
		return getXML(indentation, null);
	}
	
	public String getXML(int indentation, String id) {
		
		String xml = new String();
		
		xml += XMLUtils.openTag(XML_ROOT, id, indentation);
		
		for (String name : values.keySet())
			xml += XMLUtils.element(name, values.get(name), indentation+1);
		
		xml += XMLUtils.closeTag(XML_ROOT, indentation);
		
		return xml;
		
	}

	public void loadFromXML(Node node) {
		
		if (node == null)
			throw new IllegalArgumentException("Null input XML node");
		
		NodeList list = node.getChildNodes();
		
		for (int i=0; i < list.getLength(); i++) {
			
			Node n = list.item(i);
			
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				
				if (n.getFirstChild() == null || 
						n.getFirstChild().getNodeType() != Node.TEXT_NODE)
				{
					continue;
				}
				
				String value = n.getFirstChild().getNodeValue();
				
				if (value == null)
					continue;
				
				setInfo(n.getNodeName(), value);
				
			}
			
		}
		
	}

	public String getAuthor() {
		return getInfo(XML_AUTHOR);
	}

	protected void setAuthor(String author) {
		setInfo(XML_AUTHOR, author);
	}

	public String getHostApplication() {
		return getInfo(XML_HOST);
	}

	protected void setHostApplication(String hostApplication) {
		setInfo(XML_HOST, hostApplication);
	}

	public String getJarClass() {
		return getInfo(XML_CLASS);
	}

	protected void setJarClass(String jarClass) {
		setInfo(XML_CLASS, jarClass);
	}

	public String getJarName() {
		return getInfo(XML_JAR);
	}

	protected void setJarName(String jarName) {
		setInfo(XML_JAR, jarName);
	}

	public String getName() {
		return getInfo(XML_NAME);
	}

	protected void setName(String name) {
		setInfo(XML_NAME, name);
	}

	public String getType() {
		return getInfo(XML_TYPE);
	}

	protected void setType(String type) {
		setInfo(XML_TYPE, type);
	}

	public String getVersion() {
		return getInfo(XML_TYPE);
	}

	protected void setVersion(String version) {
		setInfo(XML_VERSION, version);
	}
	
	public String getDescription() {
		return getInfo(XML_DESC);
	}

	public String getDescription(Localization loc) {
		return loc.getLocalized(getDescription());
	}

	protected void setDescription(String description) {
		setInfo(XML_DESC, description);
	}

}
