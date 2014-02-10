package barrysoft.web.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import barrysoft.web.Parser;
import barrysoft.web.ParserRule;
import barrysoft.web.ParserRuleParam;
import barrysoft.web.WebDownloader;

public class ParserTest {
	
	public final static String UA = "Mozilla/5.0 (Windows; Windows NT 5.1; en-US; rv:1.9.2a1pre) "+
										"Gecko/20090402 Firefox/3.6a1pre";

	public final static String[] regexs = {
		"(?i)(?m).*?&lt;tr id=\"[series]\"&gt;\\s*.*?&lt;a href=\"([^&lt;&gt;]+?)\"&gt; ([A-Za-z ]+?)&lt;/a&gt;&lt;/a&gt;&lt;/h3&gt;&lt;/td&gt;.*",
		"(?i)func=fileinfo&amp;id=(\\d+?)\"&gt;(.*?[subname].*?)&lt;/a&gt;",
		"(?i)(?m).*?&lt;tr id=\"Stagione [season]\"&gt;\\s*&lt;td&gt;&lt;h3&gt;&lt;img.*?&gt;&lt;a name=\".*?\"&gt;&lt;a href=\"(.+?)\"&gt; (.+?)&lt;/a&gt;&lt;/a&gt;&lt;/h3&gt;&lt;/td&gt;.*",
		"(?i)(?m).*?&lt;a href=\"([^&lt;&gt;]+?)\"&gt;[subname]&lt;/a&gt;&lt;/dd&gt;.*",
		"(?i)(?m).*?&lt;td&gt;&lt;h3&gt;&lt;img.*?&gt;&lt;a name=\"\\d*?\"&gt;&lt;a href=\"([^&lt;&gt;]+?)\"&gt;\\s*([^&lt;&gt;]+?)&lt;/a&gt;&lt;/a&gt;&lt;/h3&gt;&lt;/td&gt;.*",
		"&lt;td&gt;&lt;h3&gt;&lt;img.*?&gt;&lt;a name=\"\\d*?\"&gt;&lt;a href=\"([^&lt;&gt;]+?)\"&gt;\\s*[type]&lt;/a&gt;&lt;/a&gt;&lt;/h3&gt;&lt;/td&gt;",
		"adasdasdas",
		"(?i)(?m)asdasdasdasd",
		"Rome is the capital of (.*)"
	};
	
	public final static int[] regexsResults = {2,2,2,1,2,1,0,0,1};
	
	public void testGroupCount() {
		
		for (int i=0; i < regexs.length; i++) {
			int groups = Parser.getGroupsCount(regexs[i]);
			assertEquals("Testing RegEx "+i, regexsResults[i], groups);
		}
		
	}
	
	public final static String plainData = "Rome, Italy\n"+
											"Rome is also known as Roma\n"+
											"Rome is the capital of Italy\n"+
											"The native name of Italy is Italia";
	
	public final static String parseRule = "(?i)(?s)^[city], [country]\\s"+
											"[city] is also known as (.+?)\\s"+
											"[city] is the capital of [country]\\s"+
											"The native name of [country] is (.+?)$";
	@Test
	public void testParse() {
		
		Parser p = new Parser("Test Parser");
		/*p.addRule(parseRule);
		p.addRuleParam(parseRule, p.new ParserRuleParam("city", "Rome"));
		p.addRuleParam(parseRule, p.new ParserRuleParam("country", "Italy"));*/
		
		ParserRule pr = new ParserRule(parseRule);
		pr.addParam(new ParserRuleParam("city", "Rome"));
		pr.addParam(new ParserRuleParam("country", "Italy"));
		
		p.addRule(pr);
			
		p.parseData(plainData);
		
		String[][] results = p.getRule(0).getResults();
		checkResults(results, 1, new int[] {2});
		
		assertEquals("Wrong group data.", "Roma", results[0][0]);
		assertEquals("Wrong group data.", "Italia", results[0][1]);
		
		assertTrue(true);
		
	}
	
	public final static String timeURL = "http://www.worldtimeserver.com/current_time_in_IT.aspx";
	public final static String timeRule = "(?i)(?s).*The current time.*?(\\d+[.:]+\\d\\d *[AP]*M*).*";
	
	@Test
	public void testParseURL() {
		
		Parser p = new Parser("Time Parser");
		p.addRule(new ParserRule(timeRule));
		
		try {
			
			p.parseData(new WebDownloader(timeURL));
			
			String[][] results = p.getRule(0).getResults();
			
			checkResults(results, 1, new int[] {1});

			System.out.println(results[0][0]);
			
			assertTrue(true);
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
	public final static String itsaRule = "(?i)(?s).*?<a href=\"([^<>]+)\">\\s[series]</a>.*?";
	
	public final static String[] series = new String[] {
		"Dexter", "How I Met Your Mother", "Lost", "Glee"
	};
	
	@Test
	public void testParserITSA() {
		
		Parser p = new Parser("ITSA Parser");
		
		ParserRuleParam prp = new ParserRuleParam("series");
		
		for (String s : series)
			prp.addValue(s);
		
		ParserRule pr = new ParserRule(itsaRule, prp);
		pr.setQuickRule(" [series]<");
		
		p.addRule(pr);
		
		try {
			
			WebDownloader wd = new WebDownloader("http://www.italiansubs.net/index.php");
			wd.addFormElement("option", "com_remository");
			
			p.parseData(wd);

			String[][] results = p.getRule(0).getResults();
			
			int[] expected = new int[series.length];
			Arrays.fill(expected, 1);
			
			checkResults(results, series.length, expected);
			
			for (String[] s : results)
				System.out.println(s[0]);
			
			assertTrue(true);
			
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
	public final static String[] multipleRules = new String[] {
		"(?i)(?s).*<div id='remositoryfileinfo'>.*?<center>.*?</a>(.+?)</center>.*",
		"(?i)(?s).*<div id='remositoryfileinfo'>.*?<dd>.*?Inviato il (.+?)\r\n.*?</dd>.*",
		"(?i)(?s).*<div id='remositoryfileinfo'>.*?<dd>.*?Dimensioni del file: (.+?)\r\n.*?</dd>.*"
	};
	
	@Test
	public void testParserMultipleRules() {
		
		Parser p = new Parser("ITSA Parser Multi");
		
		for (String rule : multipleRules)
			p.addRule(new ParserRule(rule));
		
		try {
			
			WebDownloader wd = new WebDownloader("http://www.italiansubs.net/index.php");
			wd.addFormElement("option", "com_remository");
			wd.addFormElement("func", "fileinfo");
			wd.addFormElement("id", "2126");
			
			p.parseData(wd);

			for (int i=0; i < multipleRules.length; i++) {
				String[][] results = p.getRule(i).getResults();
				
				checkResults(results, 1, new int[] {1});
				
				System.out.println(results[0][0].trim());
			}
			
			assertTrue(true);
			
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
	public final static String[] multipleParamsRules = new String[] {
		"(?i)(?s).*<div id='remositoryfileinfo'>.*?<center>.*?</a>(.+?)</center>.*",
		"(?i)(?s).*<div id='remositoryfileinfo'>.*?<dd>.*?[tag](.+?)\r\n.*?</dd>.*"
	};
	
	@Test
	public void testParserMultipleParamsRules() {
		
		Parser p = new Parser("ITSA Parser Multi");
		
		for (String rule : multipleParamsRules)
			p.addRule(new ParserRule(rule));
		
		ParserRule pr = p.getRule(multipleParamsRules.length-1);
		pr.addParam(new ParserRuleParam("tag", "Inviato il "));
		
		try {
			
			pr.getParam("tag").addValue("Dimensioni del file: ");
			
			WebDownloader wd = new WebDownloader("http://www.italiansubs.net/index.php");
			wd.addFormElement("option", "com_remository");
			wd.addFormElement("func", "fileinfo");
			wd.addFormElement("id", "2126");
			
			p.parseData(wd);
			
			String[][] results = p.getRule(0).getResults();
			checkResults(results, 1, new int[] {1});
			System.out.println(results[0][0]);
			
			results = p.getRule(1).getResults();
			checkResults(results, 2, new int[] {1, 1});
			
			for(String[] s : results)
				System.out.println(s[0].trim());
			
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testSave() {
		
		Parser p = new Parser("Save Parser");
		
		for (String rule : regexs) {
			ParserRule pr = new ParserRule(rule);
			
			for (int i=0; i < 3; i++) {
				
				ParserRuleParam prp = new ParserRuleParam("param"+(i+1));
				
				for (int j=0; j < 5; j++)
					prp.addValue("value"+(j+1));
				
				pr.addParam(prp);
				
			}
			
			pr.setGroupName("group1", 0);
			pr.setGroupName("group2", 1);
			
			p.addRule(pr);
		}
		
		String xmlSource = p.getXML(0);
		
		System.out.println(xmlSource);
		
		InputSource is = new InputSource(new StringReader(xmlSource));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			fail(e1.getMessage());
		}
		
		Document doc = null;
		try {
			doc = db.parse(is);
		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		Parser p2 = new Parser();
		p2.loadFromXML(doc.getFirstChild());
		
		assertEquals(p2.getRulesCount(), regexs.length);
		
		for (int i=0; i < p2.getRulesCount(); i++) {
			
			ParserRule pr = p2.getRule(i);
			
			assertEquals(pr.getRule(), regexs[i]);
			assertEquals(3, pr.getParamsCount());
			
			for (int j=0; j < pr.getParamsCount(); j++) {
				try {
					ParserRuleParam prp = pr.getParam("param"+(j+1));
					
					assertEquals(5, prp.getValues().length);
					
					for (int k=0; k < prp.getValues().length; k++)
						assertEquals("value"+(k+1), prp.getValues()[k]);
					
				} catch (IllegalArgumentException e) {
					fail(e.getMessage());
				}
			}
			
			try {
				assertEquals(0, pr.getGroupNumber("group1"));
				assertEquals(1, pr.getGroupNumber("group2"));
			} catch (NoSuchElementException e) {
				fail(e.getMessage());
			}
			
		}
		
	}
	
	public static final String tvstRegex = "<span style=\"[^\"]*\"><b>(\\w+)\\s*subtitles</b></span></div>\\s*<a href=\"([^\"]*)\">";
	
	public void testMultipleResults() {
		
		Parser p = new Parser("TVSubtitles.net Parser");
		ParserRule rule = new ParserRule(tvstRegex);
		rule.setGroupName("Language", 0);
		rule.setGroupName("Link", 1);
		
		p.addRule(rule);
		
		WebDownloader dl = new WebDownloader();
		try {
			dl.setUrl("http://www.tvsubtitles.net/episode-3079.html");
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
		
		try {
			p.parseData(dl);
		} catch (IllegalStateException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}

		try {
			String[] langs = rule.getResults("Language");
			String[] links = rule.getResults("Link");
			
			if (langs == null || langs.length == 0)
				fail("Can't get langs.");
			
			if (links == null || links.length == 0)
				fail("Can't get links.");
			
			assertEquals(langs.length, links.length);
			
			for (int i=0; i < langs.length; i++)
				System.out.println(String.format("%s (%s)", langs[i], links[i]));
			
		} catch (IndexOutOfBoundsException e) {
			fail(e.getMessage());
		} catch (NoSuchElementException e) {
			fail(e.getMessage());
		}
		
		
	}
	
	public void checkResults(String[][] results, int[] expected) {
		checkResults(results, -1, expected);
	}
	
	public void checkResults(String[][] results, int totExpected, int[] expected) {
		
		if (results == null || results.length == 0)
			fail("No groups data.");
		
		if (totExpected >= 0)
			assertEquals("Wrong number of results",totExpected,results.length);
		
		if (expected == null)
			return;
		
		assertEquals("Internal error: Wrong number of expected values",results.length, expected.length);
		
		for (int i=0; i < results.length; i++) {
			assertNotNull("Result number "+i+" couldn't be parsed",results[i]);
			assertEquals("Wrong number of groups data",expected[i],results[i].length);
		}
		
	}
	
}
