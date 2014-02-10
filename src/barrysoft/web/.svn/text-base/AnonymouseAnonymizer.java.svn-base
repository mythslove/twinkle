package barrysoft.web;

import barrysoft.utils.NetworkUtils;

public class AnonymouseAnonymizer implements Anonymizer {
	
	public static final String PROXY_URL = "http://anonymouse.org/cgi-bin/anon-www.cgi/";
	
	private String userAgent = "";
	
	public AnonymouseAnonymizer(String ua) {
		setUserAgent(ua);
	}
	
	public String getURLSource(String url) {
		return getURLSource(url, "GET", null, null);
	}
	
	public String getURLSource(String url, String method, String ref, String filename) {
		
		String[] urlInfo = NetworkUtils.decomposeQuery(url);
		
		if (urlInfo.length < 2) 
			return "";
		
		String anonUrl = NetworkUtils.composeQuery(PROXY_URL+urlInfo[0], urlInfo[1]);
		
		return NetworkUtils.getWebPage(anonUrl, method, filename, ref, getUserAgent());
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

}
