package barrysoft.localization;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import barrysoft.xml.XMLUtils;
import barrysoft.xml.XMLizable;

public class Localization implements XMLizable {
	
	public static final String XML_ROOT = "localizations";
	public static final String XML_LOC = "localization";
	public static final String XML_ENTRY = "entry";
	
	public static final String DEFAULT_LOCALE = "en_US";
	
	private Hashtable<String, Hashtable<String, String>> localizations;
	private Hashtable<String, String> currentLocalization;
	private String currentLanguage;
	
	public Localization() {
		localizations = new Hashtable<String, Hashtable<String,String>>();
	}
	
	public String getLocalized(String tag, String... params) 
			throws LocalizationException
	{
		
		String value = getCurrentLocalization().get(tag);
		
		if (value == null)
			throw new LocalizationException(tag);
		
		value = value.replaceAll("\\[br\\]", "\n");
		
		if (params == null || params.length == 0)
			return value;
		
		for (int i=0; i < params.length; i++) {
			
			if (params[i] == null)
				continue;
			
			value = value.replaceAll("%"+(i+1), params[i]);
		}
		
		return value;
	}
	
	public void setCurrentLocalization(String language) 
			throws LocalizationException
	{
		Hashtable<String, String> loc = localizations.get(language);
		
		if (loc == null)
			throw new LocalizationException(language);
		
		currentLocalization = loc;
		currentLanguage = language;
	}

	public Hashtable<String, String> getCurrentLocalization() {
		return currentLocalization;
	}
	
	public void addLocalization(String language, Hashtable<String, String> localization) {
		localizations.put(language, localization);
	}
	
	public void load(File config) 
			throws SAXException, 
				IOException, 
				ParserConfigurationException 
	{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		Document doc = db.parse(config);
		
		NodeList nodes = doc.getElementsByTagName(XML_ROOT);
		
		if (nodes.getLength() == 0)
			return;
		
		loadFromXML(nodes.item(nodes.getLength()-1));
		
	}
	
	public static String getSystemLocale() {
		
		return String.format("%s_%s",
					Locale.getDefault().getLanguage(),
					Locale.getDefault().getCountry());
	}
	
	@Override
	public String getXML(int indentation) {
		return getXML(indentation, null);
	}
	
	@Override
	public String getXML(int indentation, String id) {
		
		String xml = new String();

		String[] attributes = new String[2];
		
		xml += XMLUtils.openTag(XML_ROOT, id, indentation);
		
		for (String language : localizations.keySet()) {
			
			String[] attr = new String[] {"language=\""+language+"\""};
			
			xml += XMLUtils.openTag(XML_LOC, attr, indentation+1,
					true, false);
			
			Hashtable<String,String> localization = localizations.get(language);
		
			for (String tag : localization.keySet()) {
				attributes[0] = "tag=\""+tag+"\"";
				attributes[1] = "value=\""+localization.get(tag)+"\"";
				
				xml += XMLUtils.element(XML_ENTRY, null,
						attributes, indentation+2);
			}
			
			xml += XMLUtils.closeTag(XML_LOC, indentation+1);
		}
		
		xml += XMLUtils.closeTag(XML_ROOT, indentation);
		
		return xml;
		
	}

	@Override
	public void loadFromXML(Node node) {
		
		if (!node.getNodeName().equals(XML_ROOT))
			throw new IllegalArgumentException("Provided node is not"+
					" a root node");
		
		NodeList nodes = node.getChildNodes();
		
		try {
		
			for (int i=0; i < nodes.getLength(); i++) {
				
				Node n = nodes.item(i);
				
				if (n.getNodeName().equals(XML_LOC)) 
				{
					
					String language = n.getAttributes().
						getNamedItem("language").getNodeValue();
					
					Hashtable<String, String> localization = 
						new Hashtable<String, String>();
					
					NodeList entries = n.getChildNodes();
					
					for (int j=0; j < entries.getLength(); j++) {
						
						Node entry = entries.item(j);
						
						if (entry.getNodeName().equals(XML_ENTRY)) {
						
							String tag = entry.getAttributes().
								getNamedItem("tag").getNodeValue();
							
							String value = entry.getAttributes().
								getNamedItem("value").getNodeValue();
							
							localization.put(tag, value);
							
						}
						
					}
					
					addLocalization(language, localization);
					
				}
				
			}
		
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new NullPointerException("Some localization values are"+
					" probably missing: ");
		}
		
		
	}

	public String getCurrentLanguage() {
		return currentLanguage;
	}
	
}
