package barrysoft.web;

import java.io.IOException;

import org.w3c.dom.Node;

import barrysoft.xml.XMLUtils;

public class Extractor {
	
	public final static String XML_ROOT = "extractor";
	
	private WebDownloader downloader;
	private Parser parser;
	
	public Extractor() {
		downloader = new WebDownloader();
		parser = new Parser();
	}
	
	public void extract() throws IllegalStateException, IOException {
		getParser().parseData(getDownloader());
	}

	public WebDownloader getDownloader() {
		return downloader;
	}

	protected void setDownloader(WebDownloader downloader) {
		this.downloader = downloader;
	}

	public Parser getParser() {
		return parser;
	}

	protected void setParser(Parser parser) {
		this.parser = parser;
	}

	public String getXML(int indentation) {
		
		String xml = new String();
		
		xml += XMLUtils.openTag(XML_ROOT, indentation);
		xml += getDownloader().getXML(indentation+1);
		xml += getParser().getXML(indentation+1);
		xml += XMLUtils.closeTag(XML_ROOT, indentation);
		
		return xml;
		
	}

	public void loadFromXML(Node node) {
		// TODO Auto-generated method stub
		
	}
	
	

}
