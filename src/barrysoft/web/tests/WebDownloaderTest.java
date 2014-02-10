package barrysoft.web.tests;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import barrysoft.web.WebDownloader;

public class WebDownloaderTest extends TestCase {

	public void testDownload() {
		
		WebDownloader downloader = new WebDownloader();
		
		try {
			downloader.setUrl("http://it.wikipedia.org/wiki/CSS_(informatica)");
			String source = new String(downloader.download());
			
			assertTrue(source.length() > 0);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
	public void testDownloadToFile() {
		
		WebDownloader downloader = new WebDownloader();
		
		try {
			downloader.setUrl("http://it.wikipedia.org/wiki/CSS_(informatica)");
			downloader.download(new File("test_page.html"));
			
			File f = new File("test_page.html");
			
			if (!f.exists())
				assertTrue("File doesn't exists", false);
			
			assertTrue(true);
			
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
	public void testSaving() {
		WebDownloader downloader = new WebDownloader();
		
		try {
			downloader.setUrl("http://www.google.com");
			downloader.setMethod(WebDownloader.METHOD_POST);
			downloader.setBufferSize(512);
			downloader.setFollowsRedirects(false);
			downloader.setCacheEnabled(true);
			downloader.setUserAgent(WebDownloader.CHROME_UA);
			downloader.addFormElement("q", "Your mother");
			downloader.addFormElement("name", "die!");
			
			String source = downloader.getXML(0);
			
			System.out.println(source);
			
			downloader = new WebDownloader();
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			
			DocumentBuilder db = null;
			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				fail(e1.getMessage());
			}
			
			Document doc = null;
			try {
				doc = db.parse(new InputSource(new StringReader(source)));
			} catch (SAXException e) {
				fail(e.getMessage());
			} catch (IOException e) {
				fail(e.getMessage());
			}
			
			downloader.loadFromXML(doc.getFirstChild());
			
			assertEquals(source, downloader.getXML(0));
			
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
	}

	public void testRedirect() {
		
		WebDownloader downloader = new WebDownloader();
		
		try {
			downloader.setUrl("http://www.barrysoft.it/SiteV3/Products.asp?ID=10");
			String source = new String(downloader.download());
			
			if (source.contains("Marathon Infinity"))
				assertTrue(true);
			else
				fail("Can't find the string:\n\n"+source);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
	public final String querySite = "http://www.timeanddate.com/worldclock/city.html?bla=1&bla2=3";
	
	public void testQuery() {
		
		WebDownloader downloader = new WebDownloader();
		downloader.addFormElement("n", "215");
		
		try {
			downloader.setUrl(querySite);
			String source = new String(downloader.download());
			
			if (source.contains("Rome, Italy"))
				assertTrue(true);
			else
				fail("Can't find the string:\n\n"+source);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
	public final String postSite = "http://www.snee.com/xml/crud/posttest.cgi";
	
	public void testPost() {
		
		WebDownloader downloader = new WebDownloader();
		downloader.addFormElement("fname", "Masked");
		downloader.addFormElement("lname", "Man");
		downloader.setMethod(WebDownloader.METHOD_POST);
		
		try {
			downloader.setUrl(postSite);
			String source = new String(downloader.download());
			
			if (source.contains("First name: \"Masked\""))
				assertTrue(true);
			else
				fail("Can't find the string:\n\n"+source);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
}
