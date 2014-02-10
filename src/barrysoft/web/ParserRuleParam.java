package barrysoft.web;

import java.util.Collections;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import barrysoft.logs.Logger;
import barrysoft.xml.XMLUtils;
import barrysoft.xml.XMLizable;

/**
 * This class represents a single
 * rule parameter that can be added
 * to an existing rule.
 * Every parameter can have more than
 * one value, this will result in
 * a different rule build for every
 * possible value.
 * 
 * @author Daniele Rapagnani
 *
 */

public class ParserRuleParam implements Comparable<ParserRuleParam> {
	
	public final static String XML_ROOT = "param";
	public final static String XML_VALUE = "value";
	
	private String name;
	private Vector<String> values;
	
	public ParserRuleParam() {
		values = new Vector<String>();	
	}
	
	/**
	 * Creates a parameter that can be
	 * added to a rule without any
	 * value.
	 * 
	 * @param name The parameter's name.<br>
	 * 				The corresponding
	 * 				placeholder will be
	 * 				<b>[name]</b>
	 * 
	 */
	
	public ParserRuleParam(String name) {
		this();
		this.name = name;
	}
	
	
	/**
	 * Creates a parameter that can be
	 * added to a rule.
	 * 
	 * @param name The parameter's name.<br>
	 * 				The corresponding
	 * 				placeholder will be
	 * 				<b>[name]</b>
	 * 
	 * @param value The parameter's value
	 */
	
	public ParserRuleParam(String name, String value) {
		this(name);
		addValue(value);
	}
	
	/**
	 * Creates a parameter that can be
	 * added to a rule with the given
	 * possible values.
	 * 
	 * @param name The parameter's name.<br>
	 * 				The corresponding
	 * 				placeholder will be
	 * 				<b>[name]</b>
	 * 
	 * @param values The parameter's values
	 */
	
	public ParserRuleParam(String name, String[] values) {
		this(name);
		Collections.addAll(this.values, values);
	}

	/**
	 * Gets the name of this rule's
	 * parameter.
	 * 
	 * @return The parameter's name
	 */
	
	public String getName() {
		return name;
	}
	
	/**
	 * Adds a value to this rule's
	 * parameter.
	 * 
	 * @return The parameter's value
	 */
	
	public void addValue(String value) {
		this.values.add(value);
	}
	
	/**
	 * Clear all the values associated
	 * to this parameter.
	 *
	 */
	
	public void clearValues() {
		values.clear();
	}
	
	/**
	 * Gets all the possibile values
	 * for this parameter.
	 * 
	 * @return An array containing all
	 * 			the possibile values,
	 * 			or <code>null</code> if no 
	 * 			value was specified
	 */
	
	public String[] getValues() {
		
		if (values.isEmpty())
			return null;
		
		return values.toArray(new String[values.size()]);
	}

	public int compareTo(ParserRuleParam o) {
		return getName().compareTo(o.getName());
	}


	public String getXML(int indentation) {
		
		String xml = new String();

		xml += XMLUtils.openTag(XML_ROOT, 
				new String[] {"name=\""+getName()+"\""},
				indentation, true, false);
		
		for (String value : values)
			xml += XMLUtils.element(XML_VALUE, 
					value, indentation+1);
		
		xml += XMLUtils.closeTag(XML_ROOT, indentation);

		return xml;
		
	}


	public void loadFromXML(Node node) {
		
		if (!node.getNodeName().equals(XML_ROOT))
			throw new IllegalArgumentException("Wrong XML node passed"+
					" to ParserRuleParam.");
		
		try {
			this.name = node.getAttributes().getNamedItem("name").getNodeValue();
		} catch (NullPointerException e) {
			Logger.warning("No name specified in XML file for this Parser.");
		}
		
		NodeList nodes = node.getChildNodes();
		
		for (int i=0; i < nodes.getLength(); i++) {
			
			Node n = nodes.item(i);
			
			try {
				
				if (n.getNodeType() == Node.ELEMENT_NODE)
					if (n.getNodeName().equals(XML_VALUE))
						addValue(n.getFirstChild().getNodeValue());
				
			} catch (NullPointerException e) {
				continue;
			}
			
		}
		
	}
	
}