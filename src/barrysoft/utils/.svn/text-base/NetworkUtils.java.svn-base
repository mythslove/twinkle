package barrysoft.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Properties;

import barrysoft.logs.Logger;
import barrysoft.web.PersistentCookieStore;

/**
 * @deprecated
 * @author Daniele Rapagnani
 *
 */

public class NetworkUtils {
	
	private final static int CHUNK_SIZE = 1024;
	
	/**
	 * Compose a url and a query into a web address
	 * @param url		Url of the site
	 * @param query		Query to use
	 * @return			A valid web address
	 */
	
	public static String composeQuery(String url, String query) {
		
		URI new_url = null;
		
		// Encode the URI
		try {
			new_url = new URI("http","","/"+url.replaceFirst("http://",""),query,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		String composedUrl = new_url.toString().replace("http:///","http://");
		
		if (query != null  && query.isEmpty() && composedUrl != null && composedUrl.endsWith("?"))
			composedUrl = composedUrl.substring(0,composedUrl.length()-1);
		
		return composedUrl;
	}
	
	/**
	 * Decompose url and query using simple string operation
	 * @param url	Url you want decomposed from the query
	 * @return	String array with url at element 0 and query at element 1
	 */
	
	public static String[] decomposeQuery(String url) {
		String[] urlInfo = url.split("\\?");
		
		if (urlInfo.length < 2)
			Logger.warning("Warning no query to decompose in decomposeQuery");
		
		return urlInfo;
	}
	
	/**
	 * Sets the proxy used by the network
	 * functions
	 * @param address	Proxy address or host name
	 * @param port		Proxy port
	 */
	
	public static void setSystemProxy(String address, String port) {
		Properties systemProperties = System.getProperties();
		systemProperties.setProperty("http.proxyHost",address);
		systemProperties.setProperty("http.proxyPort",port);
	}
	
	/**
	 * Gets the current proxy
	 * @return	Current proxy in the format
	 * 			"proxy:port"
	 */
	
	public static String getSystemProxy() {
		Properties systemProperties = System.getProperties();
		String proxy = systemProperties.getProperty("http.proxyHost");
		proxy += ":" + systemProperties.getProperty("http.proxyPort");
		
		return proxy;
	}
	
	/**
	 * Downloads data from a web address into a string or a file.
	 * @param url			Url to download from
	 * @param method		POST or GET method string
	 * @param fileName		If not null the downloaded data will 
	 * 						be written in this file
	 * @param ref			Referer to use if not null
	 * @param ua			User-Agent to use
	 * @param proxy			Proxy to use
	 * @return				The received data
	 */
	
	public static String getWebPage(String url) {
		return getWebPage(url, "GET");
	}
	
	public static String getWebPage(String url, String method) {
		return getWebPage(url, method, null, null, null);
	}
	
	public static String getWebPage(String url, String method, String fileName, String ref, String ua) {
		return getWebPage(url, method, fileName, ref, ua, null);
	}
	
	//	TODO: This should be cleaned once and for all
	public static String getWebPage(String url, String method, String fileName, String ref, String ua, Proxy proxy) {

		String source = new String();
		
		// Create a cookie handler if not present
		if (CookieHandler.getDefault() == null) {
			CookieManager cm = new CookieManager(new PersistentCookieStore(),CookiePolicy.ACCEPT_ALL);
			CookieHandler.setDefault(cm);
		}
		
		// Connect to the url
		try {
			
	    	String params = null;
	    	String[] a = null;
	    	
	    	if (method.equals("POST")) {
		    	if (url.split("\\?").length > 1) {
		    		a = url.split("\\?");
		    	} else if (url.split("\\%3F").length > 1) {
		    		a = url.split("\\%3F");
		    	} else {
		    		Logger.error("Malformed URL, can't retrive POST data: "+url);
		    		return null;
		    	}
		    	
		    	if (a != null && a.length > 1) {
		    		url = a[0];
		    		params = a[1];
		    	}
	    	}
		    
	    	HttpURLConnection connection = null;
			
			URL usite = new URL(url);
			URLConnection yc;
			
			if (proxy != null)
				yc = usite.openConnection(proxy);
			else
				yc = usite.openConnection();
			
		    //String cookies = getCookies(usite.getHost());
		    
			yc.setRequestProperty("User-agent",ua);
		    //yc.setRequestProperty("Cookie",cookies);
		    
		    if (yc instanceof HttpURLConnection) {
		        connection = ((HttpURLConnection)yc);
		    } else return null;
		    
		    connection.setInstanceFollowRedirects(false);
		    
		    //Set referer if needed
		    if (ref != null) {
		    	Logger.debug("Referer set to: "+ref, Logger.HIGHEST_VERBOSITY);
		    	yc.setRequestProperty("Referer", ref);
		    }
			
		    if (method.equals("POST")) {
		    
			    connection.setRequestMethod(method);
		    			    	
				connection.setRequestProperty("Content-Type", 
				"application/x-www-form-urlencoded");
					
				connection.setRequestProperty("Content-Length", "" + 
				Integer.toString(params.getBytes().length));
				connection.setRequestProperty("Content-Language", "en-US");  
						
				connection.setUseCaches (false);
				connection.setDoInput(true);
				connection.setDoOutput(true);

				//Send request
				DataOutputStream wr = new DataOutputStream (
				              connection.getOutputStream ());
				wr.writeBytes (params);
				wr.flush ();
				wr.close ();
		    }
	    	
	    	
	    	String newUrl = yc.getHeaderField("Refresh");
	    	if (newUrl != null && !newUrl.isEmpty()) {
	    		String newAddress = composeQuery(newUrl.split(";")[1].trim().replaceAll("(?i)url=", ""),null);
	    		Logger.debug("Redirected to (Refresh): "+newAddress,Logger.MEDIUM_VERBOSITY);
	    		return getWebPage(newAddress, method, fileName, ref, ua);
	    	}
	    	
	    	newUrl = yc.getHeaderField("Location");
	    	if (newUrl != null && !newUrl.isEmpty()) {
	    		newUrl = composeQuery(newUrl, null);
	    		Logger.debug("Redirected to (Location): "+newUrl,Logger.MEDIUM_VERBOSITY);
	    		return getWebPage(newUrl, method, fileName, ref, ua);
	    	}
	    	
		    if (fileName == null || fileName.isEmpty()) {
		    	// Not downloading on file
		    	
				BufferedReader in = new BufferedReader(new InputStreamReader(yc
						.getInputStream()));
				
				ArrayList<char[]> buffers = new ArrayList<char[]>();
				ArrayList<Integer> buffersDim = new ArrayList<Integer>();
				  int total = 0;
				  while (true) {
					char[] buffer = new char[CHUNK_SIZE];
				    int count = in.read(buffer,0,CHUNK_SIZE);
				    if (count < 0) {
				      break;
				    }
				    buffers.add(buffer);
				    buffersDim.add(count);
				    total += count;
				  }
				
				  in.close();
				
				source = new String(GenericUtils.concatCharArrays(buffers,buffersDim));
				  
				Logger.debug("Total: "+source.length()+" bytes", Logger.HIGHEST_VERBOSITY);
				
		    } else {
		    	
		    	// Downloading on file

		        RandomAccessFile file = new RandomAccessFile(fileName, "rw");
		        file.seek(0); 
		        
		        InputStream stream = yc.getInputStream();
		        
		        byte[] buffer = new byte[CHUNK_SIZE];
		        
		        while (true) {
		            int read = stream.read(buffer);
		            
		            if (read == -1) {
		                break;
		            }
		            
		            file.write(buffer, 0, read);
		        }

		        file.close();
		        
		    }
		    
		} catch (Exception e) {
			Logger.debug("Can't connect to site '"+url+"': "+e.getMessage(), Logger.LOW_VERBOSITY);
			return "";
		}
	
		if (source != null && source.isEmpty() && fileName.isEmpty())
			Logger.warning("Source for page "+url+" was empty");
		
		source = source.replace("\n", "");
		source = source.replace("\r", "");
		
		return (fileName == null || fileName.isEmpty() ? source : fileName);
		
	}

}
