package barrysoft.xml;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

public class XMLUtils {
	
	private static final String	XML_HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n";
	private static final char 	INDENTATION_CHAR = '\t';
	
	public static String openTag(String tag, int indent) {
		return openTag(tag, null, indent, true, false);
	}
	
	public static String openTag(String tag, String id, int indent) {
		if (id != null)
			return openTag(tag, new String[] { "id=\""+id+"\"" }, indent, true, false);
		else
			return openTag(tag, indent);
	}
	
	public static String openTag(String tag, 
								String[] attributes, 
								int indent, 
								boolean newLine,
								boolean close) 
	{
		String tagStart = tag;
		
		if (attributes != null) {		
			for (String att : attributes)
				tagStart += " "+att;
		}
		
		return String.format("%s<%s%s>%s",getIndentationLevel(indent),
				tagStart, (close ? " /" : ""),
				(newLine ? "\n" : ""));
	}
	
	public static String closeTag(String tag, int indent) {
		return openTag("/"+tag, indent);
	}
	
	public static String element(String tag, String value, int indent) {
		return element(tag, value, null, indent);
	}
	
	public static String element(String tag, String value, String[] attributes, int indent) {
		
		value = (value == null ? "" : value);
		
		if (hasInvalidCharacters(value))
			value = asCdata(value);			
		
		if (value == null || value.isEmpty())
			return openTag(tag, attributes, indent, true, true);
		
		String element = openTag(tag, attributes, indent, false, false);
		element += value;
		element += closeTag(tag, 0);
		
		return element;
		
	}
	
	public static boolean hasInvalidCharacters(String value) {
		
		if (value.indexOf('<') != -1)
			return true;
		
		if (value.indexOf('>') != -1)
			return true;
		
		if (value.indexOf('"') != -1)
			return true;
		
		if (value.indexOf('&') != -1)
			return true;
		
		return false;
		
	}
	
	public static String asCdata(String value) {
		return String.format("<![CDATA[%s]]>", value);
	}
	
	public static String nodeToString(Node node) 
		throws TransformerFactoryConfigurationError, 
				TransformerException 
	{
		
		StringWriter sw = new StringWriter();
		
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		t.transform(new DOMSource(node), new StreamResult(sw));
		
		return sw.toString();
		
	}
	
	/**
	 * Gets the standard XML file header
	 * @return The requested header
	 */
	
	public static String getXMLHeader() {
		return XML_HEADER;
	}
	
	
	/**
	 * Gets the string to use for a given level of indentation
	 * @param indentation	Desired level of indentation
	 * @return	The string to be attached before the indented string
	 */
	
	public static String getIndentationLevel(int indentation) {
		return repeatString(String.valueOf(INDENTATION_CHAR), indentation);
	}
	
	public static String repeatString(String str, int times) {
		String r = new String("");
		
		for (int i=0; i < times; i++)
			r += str;
		
		return r;
	}
	
}
