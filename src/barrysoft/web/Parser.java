package barrysoft.web;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import barrysoft.logs.Logger;
import barrysoft.xml.XMLUtils;
import barrysoft.xml.XMLizable;

/**
 * The <code>Parser</code> class can be used
 * to parse some input using a set of given
 * <code>ParserRule</code>(s).<br>
 * <br>
 * One of the possible uses of this class
 * maybe to parse informations from a 
 * web page with minor efforts.
 * 
 * @author Daniele Rapagnani
 *
 */

public class Parser {
	
	public final static String XML_ROOT = "parser";
	
	private String name = "Generic Parser";
	private Vector<ParserRule> rules;
	
	/**
	 * Creates an empty <code>Parser</code>
	 * object.
	 */
	
	public Parser() { 
		rules = new Vector<ParserRule>();
	}
	
	/**
	 * Creates a <code>Parser</code> with the
	 * given name.
	 * 
	 * @param name The name for this parser.
	 */
	
	public Parser(String name) {
		this();
		setName(name);
	}
	
	/**
	 * Creates a <code>Parser</code> object
	 * with all the rules specified by the
	 * supplied array and the provided name.
	 * 
	 * @param name The name of this parser
	 * @param rules An array of RegEx rules
	 */
	
	public Parser(String name, ParserRule[] rules) {
		this(name);
		Collections.addAll(this.rules, rules);
	}
	
	/**
	 * Creates a parser with the specified
	 * rule and name.
	 * 
	 * @param name The name of this parser
	 * @param rule A RegEx rule
	 */
	
	public Parser(String name, ParserRule rule) {
		this(name);
		addRule(rule);
	}
	
	/**
	 * This method executes a rule
	 * on data downloaded by a
	 * <code>WebDownloader</code>
	 * object.<br>
	 * The <code>WebDownloader</code>
	 * object should be already configured
	 * and its <i>url</i> should be set
	 * with the appropriate method.
	 * 
	 * @param wd The <code>WebDownloader</code> object
	 * 				to use to download the data to parse
	 * 
	 * @return Null if no groups were specified or found
	 * 			and an array with the specified groups values
	 * 			if found
	 * 
	 * @throws NoMatchException if the rule doesn't match
	 * @throws IllegalStateException if the URL for the web
	 * 			downloader instance was not set
	 * @throws IOException 
	 * 
	 * @see #parseData()
	 */
	
	public void parseData(WebDownloader wd) 
		throws IOException, IllegalStateException
	{	
		String data = new String(wd.download());
		
		parseData(data);
	}
	
	/**
	 * This method executes all the added rules
	 * on a given data string.<br>
	 * 
	 * @param data The data you want to be checked agains
	 * 				the rule
	 * 
	 * @return Null if no groups were specified or found
	 * 			and an array with the specified groups values
	 * 			if found
	 * 
	 * @see #parseData(String, String)
	 */
	
	public void parseData(String data) {
		
		for (ParserRule rule: rules)
			parseData(data, rule);
		
	}
	
	/**
	 * This method executes a rule on a given
	 * data string.<br>
	 * First all the rule's parameters are 
	 * replaced and the rule is build,
	 * then we check the data agains the rule.
	 * If they don't match a NoMatchException is
	 * thrown. If they match we can proceed by
	 * extracting any group may be specified in the
	 * rule. If there are no groups null is
	 * returned otherwise an array of all the extracted
	 * groups data is returned.
	 * 
	 * @param data The data you want to be checked agains
	 * 				the rule
	 * @param ruleNumber The rule you want to use on the data
	 * 
	 * @return Null if no groups were specified or found
	 * 			and an array with the specified groups values
	 * 			if found
	 * 
	 * @throws IndexOutOfBoundsException if the specified
	 * 			rule number is invalid
	 */
	
	public void parseData(String data, int ruleNumber) 
			throws IndexOutOfBoundsException
	{
		
		ParserRule rule = getRule(ruleNumber);
		
		parseData(data, rule);
		
	}
	
	/**
	 * This method executes a rule on a given
	 * data string.<br>
	 * First all the rule's parameters are 
	 * processed and all the possible 
	 * rules strings are produced for any 
	 * value of a parameter.<br>
	 * <br>
	 * To produce a rule from its parameters,
	 * the parameters values are set in the order
	 * they were added. If not all parameters
	 * have the same number of possible values than
	 * the last value is always used when the
	 * iterator reaches the values list
	 * end. For example if we have a 3 parameters
	 * with 4, 5, 2 possible values respectively,
	 * the rules will be produced in this way:
	 * 
	 * <ul>
	 * <li>rule1 = param1[1], param2[1], param3[1]</li>
	 * <li>rule2 = param1[2], param2[2], param3[2]</li>
	 * <li>rule3 = param1[3], param2[3], param3[2]</li>
	 * <li>rule4 = param1[4], param2[4], param3[2]</li>
	 * <li>rule5 = param1[5], param2[4], param3[2]</li>
	 * </ul>
	 * 
	 * According do this there will always be at most
	 * as much produced rules as the maximum number
	 * of possible values in a parameter (5 in our
	 * example).<br>
	 * <br>
	 * After the rules are produced, we check them
	 * against the data.
	 * If they don't match a NoMatchException is
	 * thrown. If they match we can proceed by
	 * extracting any group that may be specified by the
	 * rule. If there are no groups null is
	 * returned otherwise an array of all the extracted
	 * groups data is returned.
	 * 
	 * @param data The data you want to be checked agains
	 * 				the rule
	 * @param rule The rule you want to use on the data
	 * 
	 * @return Null if no groups were specified or found
	 * 			and an array with the specified groups values
	 * 			if found
	 * 
	 * @throws NoMatchException if the rule doesn't match
	 * 
	 * @see #parseDataProcessed(String, String)
	 */
	
	public void parseData(String data, ParserRule rule) 
			throws PatternSyntaxException
	{
		
		String[] rules = rule.build();
		String[] qrules = rule.build(true);
		
		for (int i=0; i < rules.length; i++)
			rule.addResult(parseData(data, Pattern.compile(rules[i]), qrules[i]));
		
	}
	
	/**
	 * Parses given data with the specified rule
	 * that should have been already processed,
	 * that is, parameter's values should have already
	 * been replaced in the rule because it will be
	 * used as it is as a RegEx.
	 * 
	 * @param data The data to parse
	 * @param procRule The processed rule
	 * 
	 * @return Null if no groups were specified or found
	 * 			and an array with the specified groups values
	 * 			if anyone is found
	 * 
	 * @throws NoMatchException if the rule doesn't match
	 * 
	 */
	
	public String[][] parseData(String data, Pattern pattern, String procQRule)
	{
		
		Matcher matcher = pattern.matcher(data);
		
		if (matcher.groupCount() == 0)
			return null;
		
		if (!quickTest(data, procQRule))
			return null;
		
		Vector<String[]> results = new Vector<String[]>();
		
		while (matcher.find()) {
		
			String[] groups = new String[matcher.groupCount()];
			
			for (int i=0; i < groups.length; i++)
				groups[i] = matcher.group(i+1);

			results.add(groups);
			
			// Test the quick rule for the rest of the data
			if (!quickTest(data.substring(matcher.end()), procQRule))
				break;
		
		}
		
		if (results.size() == 0)
			return null;
		
		return results.toArray(new String[results.size()][]);
		
	}
	
	/**
	 * Tests a quick rule on a string.
	 * If the string to test contains the quick
	 * rule string the test is successfull.
	 * 
	 * @param data The data to be tested
	 * @param test The quick rule test string
	 * 				that should be a substring
	 * 				of data
	 * 
	 * @return True if <code>test</code> is a 
	 * 			substring of <code>data</code>
	 * 			or <code>test</code> is null. 
	 * 			False if <code>test</code> is not a
	 * 			substring of <code>data</code>.
	 */
	
	public boolean quickTest(String data, String test) {
		
		if (test == null)
			return true;
		
		if (data == null)
			throw new NullPointerException("Null data string");
		
		return data.contains(test);
		
	}
	
	/**
	 * Gets the number of round brackets
	 * groups in a given RegEx.<br>
	 * <br>
	 * To achieve this, we count the number
	 * of both open unescaped round brackets.
	 * To make sure that the rule is a valid
	 * RegEx so that we can rely on the number
	 * of open brackets we first try to compile
	 * the rule.
	 * 
	 * @param rule The rule of which you want to
	 * 				know the number of groups
	 *
	 * @return The number of round brackets
	 * 			groups found in this RegEx rule
	 */
	
	public static int getGroupsCount(String rule) {
		
		int groups = 0;
		
		Pattern.compile(rule);
		
		for (int i=0; i < rule.length(); i++) {
			
			if (rule.charAt(i) == '(') {
				
				if (i != 0 && rule.charAt(i-1) == '\\')
					continue;
	
				if (i != rule.length()-1 && rule.charAt(i+1) == '?')
					continue;
				
				groups++;
			
			}
		}	
		
		return groups;
		
	}
	
	/**
	 * Prints a debug message showing
	 * some informations on the originating
	 * <code>Parser</code> object with the
	 * default level of HIGH_VERBOSITY.
	 * 
	 * @param message The debug message to use
	 */
	
	public void debug(String message) {
		debug(message, Logger.HIGH_VERBOSITY);
	}
	
	/**
	 * Prints a debug message showing
	 * some informations on the originating
	 * <code>Parser</code> object.
	 * 
	 * @param message The debug message to use
	 * @param level The vebrosity level
	 */
	
	public void debug(String message, int level) {
		
		Logger.debug(String.format("Parser '%s' %s", 
				getName(), message), level);
		
	}
	
	/**
	 * Gets the rule with the specified number.<br>
	 * If the index is invalid an 
	 * <code>IndexOutOfBoundsException</code> is
	 * thrown.
	 * 
	 * @param number Number of the rule you want
	 *
	 * @return The requested rule
	 * 
	 * @throws IndexOutOfBoundsException if the specified
	 * 			rule number is invalid
	 */
	
	public ParserRule getRule(int number) throws IndexOutOfBoundsException {
		return rules.get(number);
	}
	
	/**
	 * Adds a rule to this parser.
	 * 
	 * @param rule The rule to add
	 */
	
	public void addRule(ParserRule rule) {
		rules.add(rule);
	}
	
	/**
	 * Gets the total number of rules
	 * in this parser.
	 * 
	 * @return Number of rules in this
	 * 			parser
	 */
	
	public int getRulesCount() {
		return rules.size();
	}

	/**
	 * Gets the name of this parser.
	 * 
	 * @return The name of this parser
	 */
	
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this parser.
	 * 
	 * @param name The name of this parser
	 */
	
	public void setName(String name) {
		this.name = name;
	}

	public String getXML(int indentation) {
		
		String xml = new String();
		
		xml += XMLUtils.openTag(XML_ROOT, 
				new String[] {"name=\""+getName()+"\""},
				indentation, true, false);
		 
		for (ParserRule rule : rules)
			xml += rule.getXML(indentation+1);
		
		xml += XMLUtils.closeTag(XML_ROOT, indentation);
		
		return xml;
		
	}

	public void loadFromXML(Node node) {
		
		if (!node.getNodeName().equals(XML_ROOT))
			throw new IllegalArgumentException("Wrong XML node passed"+
					" to Parser: "+getName());

		try {
			setName(node.getAttributes().getNamedItem("name").getNodeValue());
		} catch (NullPointerException e) {
			Logger.warning("No name specified in XML file for this Parser.");
		}
		
		NodeList nodes = node.getChildNodes();
		
		for (int i=0; i < nodes.getLength(); i++) {
			
			Node n = nodes.item(i);
			
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				ParserRule pr = new ParserRule();
				pr.loadFromXML(n);
				addRule(pr);
			}
		}
		
	}

}
