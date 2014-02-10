package barrysoft.web;

public interface Anonymizer {

	String	getURLSource(String url);
	String 	getURLSource(String url, String method, String ref, String filename);
	String 	getUserAgent();
	void	setUserAgent(String userAgent);
	
}
