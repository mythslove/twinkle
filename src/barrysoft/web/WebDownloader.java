package barrysoft.web;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import barrysoft.xml.XMLUtils;
import barrysoft.xml.XMLizable;

/**
 * The <code>WebDownloader</code> can be used to download web data using HTTP
 * requests.
 * 
 * @author Daniele Rapagnani
 * 
 */

public class WebDownloader implements XMLizable
{
	public final static String XML_ROOT = "downloader";
	public final static String XML_URL = "url";
	public final static String XML_METHOD = "method";
	public final static String XML_UA = "ua";
	public final static String XML_BUFFER = "buffer";
	public final static String XML_FORM = "form";

	/** HTTP GET method, data is sent directly in the URL. */
	public final static String METHOD_GET = "GET";
	/** HTTP POST method, data is sent separate from the URL. */
	public final static String METHOD_POST = "POST";

	/** User agent for the Mozilla Firefox browser 3.6b browser. */
	public final static String FIREFOX_UA = "Mozilla/5.0 (Windows; U; Windows NT 6.1; de; rv:1.9.2b1) Gecko/20091029 Firefox/3.6b1";

	/** User agent for the Microsoft Internet Explorer Browser 7.0b browser. */
	public final static String IE_UA = "Mozilla/5.0 (compatible; MSIE 7.0b; Windows NT 6.0)";

	/** User agent for the Opera 9.8 browser. */
	public final static String OPERA_UA = "Opera/9.80 (Windows NT 6.1; U; en-US) Presto/2.2.15 Version/10.01";

	/** User agent for the Safari 4.0 browser. */
	public final static String SAFARI_UA = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US) AppleWebKit/530.19.1"
			+ " (KHTML, like Gecko) Version/4.0.2 Safari/530.19.1";

	/** User agent for the Google Chrome 4.0 browser. */
	public final static String CHROME_UA = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/532.4"
			+ " (KHTML, like Gecko) Chrome/4.0.242.0 Safari/532.4";

	/** The default buffer size that will be used to download chunks */
	protected final static int DEF_BUFFER_SIZE = 1024;

	/** The default user agent */
	protected final static String DEF_UA = FIREFOX_UA;

	/** The default HTTP method for requests */
	protected final static String DEF_METHOD = METHOD_GET;

	private URL url;
	private String method = DEF_METHOD;
	private String userAgent = DEF_UA;
	private int bufferSize = DEF_BUFFER_SIZE;
	private boolean followsRedirects = true;
	private boolean cacheEnabled = true;

	private final Vector<ProgressListener<WebDownloader>> listeners;
	private final HashMap<String, String> formElements;
	private byte[] postData;

	/**
	 * Creates an empty <code>WebDownloader</code> object instance.
	 * 
	 */

	public WebDownloader()
	{
		// Create a cookie handler if not present
		if (CookieHandler.getDefault() == null)
		{
			CookieManager cm = new CookieManager(new PersistentCookieStore(),
					CookiePolicy.ACCEPT_ALL);

			CookieHandler.setDefault(cm);
		}

		listeners = new Vector<ProgressListener<WebDownloader>>();
		formElements = new HashMap<String, String>();
	}

	/**
	 * Creates an instance of <code>WebDownloader</code> with the specified url
	 * as source.
	 * 
	 * @param url
	 *            The url to be used by this instance
	 */

	public WebDownloader(URL url)
	{
		this();
		setUrl(url);
	}

	/**
	 * Creates an instance of <code>WebDownloader</code> with the specified url
	 * as source.
	 * 
	 * @param url
	 *            The url to be used by this instance as a String
	 * @throws MalformedURLException
	 *             if the provided URL string is not a valid URL
	 */

	public WebDownloader(String url) throws MalformedURLException
	{
		this();
		setUrl(url);
	}

	/**
	 * Downloads data from a specified URL using the current HTTP method and
	 * user agent.
	 * 
	 * @param url
	 *            The url location of the data as an URL object
	 * 
	 * @return The desired data as a byte array or <code>null</code> if no URL
	 *         was specified.
	 * 
	 * @throws IllegalStateException
	 *             if the URL has not been set before calling this method
	 * @throws IOException
	 */

	public byte[] download() throws IOException, IllegalStateException
	{
		if (getUrl() == null)
			throw new IllegalStateException(
					"URL wasn't set, nothing to download.");

		URL url = getUrl();

		// Builds the GET query if needed
		if (getMethod() == METHOD_GET)
			url = assembleUrlQuery(getUrl(), getEncodedFormElements());

		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();

		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(isCacheEnabled());
		urlConnection.setRequestMethod(getMethod());
		urlConnection.setRequestProperty("User-agent", getUserAgent());

		// Do some HTTP POST stuff
		if (getMethod() == METHOD_POST)
		{
			byte[] postData = getDataForPost();

			if (postData != null)
			{
				buildPostHeader(urlConnection, postData);
				upload(urlConnection, postData);
			}
		}

		boolean oldValue = HttpURLConnection.getFollowRedirects();
		HttpURLConnection.setFollowRedirects(isFollowsRedirects());

		byte[] data = download(urlConnection);

		// Restore the previous value of follow redirects
		HttpURLConnection.setFollowRedirects(oldValue);

		return data;

	}

	/**
	 * Downloads data from the given <code>HttpURLConnection</code> instance.<br>
	 * To do so a buffer size of chunk size is used.
	 * 
	 * @param urlConnection
	 *            The <code>HttpURLConnection</code> instance to use to download
	 *            data
	 * 
	 * @return The data downloaded from the url
	 * 
	 * @throws IOException
	 */

	public byte[] download(HttpURLConnection urlConnection) throws IOException
	{
		InputStream is = urlConnection.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int chunkSize = getBufferSize();

		fireProgressStart(new ProgressEvent<WebDownloader>(this));

		while (true)
		{
			byte[] chunk = new byte[chunkSize];

			int read = is.read(chunk);

			if (read == -1) break;

			baos.write(chunk, 0, read);

			fireProgressUpdate(new ProgressEvent<WebDownloader>(this,
					baos.size()));
		}

		fireProgressFinish(new ProgressEvent<WebDownloader>(this, baos.size()));

		return baos.toByteArray();
	}

	/**
	 * Uploads some data using the provided <code>HttpURLConnection</code>.<br>
	 * This method can be used to upload post data on an HTTP post request.
	 * 
	 * @param urlConnection
	 *            The connection to be used to upload the data
	 * @param data
	 *            The data to be uploaded
	 * 
	 * @throws IOException
	 */

	public static void upload(HttpURLConnection urlConnection, byte[] data)
			throws IOException
	{
		DataOutputStream wr = new DataOutputStream(
				urlConnection.getOutputStream());
		wr.write(data);
		wr.flush();
		wr.close();
	}

	/**
	 * <p>
	 * Downloads data from a specified URL using the current HTTP method and
	 * user agent and saves the data in the specified file.
	 * </p>
	 * <p>
	 * If the provided file is a directory, the method attempts to extract a
	 * file name from the provided URL using it as the target file name if it
	 * succeed.
	 * </p>
	 * <p>
	 * If a file name couldn't be inferred from the URL than a random file name
	 * is used.
	 * </p>
	 * 
	 * @param url
	 *            The url location of the data as an URL object
	 * @param out
	 *            The output file where the data will be saved. The file will be
	 *            overwritten in case it already exists without any kind of
	 *            warning
	 * 
	 * @return The data that was wrote on the file or null if no URL was
	 *         specified
	 * 
	 * @throws IOException
	 */

	public File download(File out) throws IOException
	{
		byte[] data = download();

		if (data == null) return null;

		if (out.isDirectory())
		{
			String fileName = getFileNameFromURL(getUrl());

			if (fileName == null)
				out = File.createTempFile("data", "null", out);
			else
				out = new File(out + File.separator + fileName);
		}

		FileOutputStream fos = new FileOutputStream(out);
		fos.write(data);
		fos.flush();
		fos.close();

		return out;
	}

	/**
	 * Registers a {@link ProgressListener} instance to be notified of the
	 * progress of a download operation.
	 * 
	 * @param listener
	 *            The listener to register
	 */

	public void addProgressListener(ProgressListener<WebDownloader> listener)
	{
		listeners.add(listener);
	}

	/**
	 * Unregisters a {@link ProgressListener}. The provided listener will no
	 * longer be notified of any progress.
	 * 
	 * @param listener
	 *            The listener to unregister
	 */

	public void removeProgressListener(ProgressListener<WebDownloader> listener)
	{
		listeners.remove(listener);
	}

	protected void fireProgressStart(ProgressEvent<WebDownloader> event)
	{
		for (ProgressListener<WebDownloader> listener : listeners)
			listener.progressStart(event);
	}

	protected void fireProgressFinish(ProgressEvent<WebDownloader> event)
	{
		for (ProgressListener<WebDownloader> listener : listeners)
			listener.progressFinish(event);
	}

	protected void fireProgressUpdate(ProgressEvent<WebDownloader> event)
	{
		for (ProgressListener<WebDownloader> listener : listeners)
			listener.progressUpdate(event);
	}

	/**
	 * Add a web form element to this <code>WebDownloader</code> instance. The
	 * fields added with this method will be sent according to the selected HTTP
	 * method at any request.
	 * 
	 * @param name
	 *            Name of the form element
	 * @param value
	 *            Unencoded value of the form element
	 */

	public void addFormElement(String name, String value)
	{
		formElements.put(name, value);
	}

	/**
	 * Deletes all the web form elements added so far to this
	 * <code>WebDownloader</code> instance.
	 * 
	 */

	public void clearFormElements()
	{
		formElements.clear();
	}

	/**
	 * Prepares the data to be sent by an HTTP post request.<br>
	 * The data is prepared by merging the user-set post data with the web form
	 * elements (if any).<br>
	 * If available, all form elements are put before the user data.
	 * 
	 * @return The data ready to be sent by an HTTP post request. If there's no
	 *         data to be sent than <code>null</code> is returned
	 */

	public byte[] getDataForPost()
	{
		String formElements = getEncodedFormElements();
		byte[] postData = getPostData();

		if (postData == null && formElements == null) return null;

		if (postData == null) return formElements.getBytes();

		if (formElements == null) return postData;

		byte[] data = new byte[postData.length + formElements.length()];

		System.arraycopy(formElements.getBytes(), 0, data, 0,
				formElements.getBytes().length);

		System.arraycopy(postData, 0, data, formElements.getBytes().length,
				postData.length);

		return data;
	}

	/**
	 * Gets a string representing all the form elements ready to be sent with
	 * the current http method.
	 * 
	 * @return A string with all the form elements encoded for the current http
	 *         method.
	 */

	public String getEncodedFormElements()
	{
		if (formElements.isEmpty()) return null;

		String data = new String();

		for (String name : formElements.keySet())
		{
			String value = encodeElement(formElements.get(name));
			data += String.format("%s=%s&", name, value);
		}

		if (data.endsWith("&")) data = data.substring(0, data.length() - 1);

		return data;
	}

	/**
	 * Encodes a form element according to the currently set http method.
	 * 
	 * @param element
	 *            The element to be encoded
	 * @return The encoded element
	 */

	public String encodeElement(String element)
	{
		if (getMethod() == METHOD_GET)
			return encodeGetElement(element);
		else
			return encodePostElement(element);
	}

	/**
	 * <p>
	 * Extracts the file name from an URL.
	 * </p>
	 * <p>
	 * For example, with an URL like:
	 * <p>
	 * <blockquote> <code>x://host/path/to/file.ext</code> </blockquote>
	 * </p>
	 * the file name <code>file.ext</code> is returned. </p>
	 * <p>
	 * If the URL doesn't contain a valid path null is returned.
	 * </p>
	 * 
	 * @param url
	 *            The URL containing the file name.
	 * @return The filname if found, null otherwise.
	 */

	public static String getFileNameFromURL(URL url)
	{
		if (url.getPath().isEmpty()) return null;

		String[] parts = url.getPath().split("/");

		if (parts.length == 0) return null;

		return parts[parts.length - 1];
	}

	/**
	 * This function should encode a query element that will be sent with a GET
	 * request. At the present time the whole URL is encoded in the
	 * <code>assembleQuery</code> function, so there's no need to do anything
	 * here.
	 * 
	 * @see #assembleUrlQuery(URL, String)
	 * 
	 * @param element
	 *            The element to be encoded
	 * @return The encoded element
	 */

	public static String encodeGetElement(String element)
	{
		return element;
	}

	/**
	 * Encodes a form element using the
	 * <code>application/x-www-form-urlencoded</code> format ready to be sent by
	 * a post request.
	 * 
	 * @param element
	 *            The element to be encoded
	 * @return The encoded element
	 */

	public static String encodePostElement(String element)
	{
		try
		{
			return URLEncoder.encode(element, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * This method assemble a given query to an existing URL object. Anything in
	 * the URL or query will be encoded using RFC2396.
	 * 
	 * @param url
	 *            The url object to update
	 * @param query
	 *            The query to add to the url. If null the original URL object
	 *            is returned.
	 * 
	 * @return A new URL object with the assembled query or <code>null</code> on
	 *         error
	 */

	public static URL assembleUrlQuery(URL url, String query)
	{
		if (query == null || query.isEmpty()) return url;

		String currentQuery = url.getQuery();

		if (currentQuery != null)
			query = String.format("%s&%s", currentQuery, query);

		String newAddress = new String();
		newAddress += url.getHost();
		newAddress += url.getPath();
		newAddress += "?" + query;

		try
		{
			return (new URI(url.getProtocol(), "//" + newAddress, null))
					.toURL();
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Adds the required HTTP headers to send post data to the
	 * <code>HttpURLConnection</code> object specified.
	 * 
	 * @param connection
	 *            The connection object that you want to prepare to send post
	 *            data
	 * @param data
	 *            The data that will be sent as post data with this connection
	 */

	public static void buildPostHeader(HttpURLConnection connection, byte[] data)
	{
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		connection.setRequestProperty("Content-Length",
				Integer.toString(data.length));
		connection.setRequestProperty("Content-Language", "en-US");

		connection.setDoOutput(true);
	}

	/**
	 * Gets the HTTP method that will be used for any kind of HTTP request.
	 * 
	 * @return The currently active HTTP method
	 */

	public String getMethod()
	{
		return method;
	}

	/**
	 * Sets the HTTP method that will be used for any kind of HTTP request.
	 * 
	 * @param method
	 *            The HTTP method to be used, either <code>METHOD_GET</code> or
	 *            <code>
	 * METHOD_POST</code>
	 * 
	 * @see #getMethod()
	 */

	public void setMethod(String method)
	{
		this.method = method;
	}

	/**
	 * Gets the <i>user agent</i> that will currently be attached to any HTTP
	 * request from this <code>WebDownloade</code> instance.
	 * 
	 * @return The currently set user agent
	 * 
	 * @see #setMethod(String)
	 */

	public String getUserAgent()
	{
		return userAgent;
	}

	/**
	 * Sets the <i>user agent</i> that will currently be attached to any HTTP
	 * request from this <code>WebDownloade</code> instance.<br>
	 * You can specify one manually or use one of the provided ones, they are:<br>
	 * <ul>
	 * <li>FIREFOX_UA</li>
	 * <li>IE_UA</li>
	 * <li>OPERA_UA</li>
	 * <li>SAFARI_UA</li>
	 * <li>CHROME_UA</li>
	 * </ul>
	 * The default user agent is <code>FIREFOX_UA</code>
	 * 
	 * @param userAgent
	 *            The user agent to be used for future requests
	 * 
	 */

	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	/**
	 * Gets the current buffer size that is used to download data chunks by any
	 * request.<br>
	 * The default value is <code>
	 * DEF_BUFFER_SIZE</code>.
	 * 
	 * @return The current buffer size
	 */

	public int getBufferSize()
	{
		return bufferSize;
	}

	/**
	 * Sets the current buffer size that is used to download data chunks by any
	 * request.<br>
	 * The default value is <code>
	 * DEF_BUFFER_SIZE</code>.
	 * 
	 * @param bufferSize
	 *            The size of the buffer in bytes
	 */

	public void setBufferSize(int bufferSize)
	{
		this.bufferSize = bufferSize;
	}

	/**
	 * Gets whether this <code>WebDownloader</code> instance automatically
	 * follows any redirect found (301-302 responses).
	 * 
	 * @return True if this instance automatically follows any redirect found
	 *         and false if it doesn't
	 */

	public boolean isFollowsRedirects()
	{
		return followsRedirects;
	}

	/**
	 * Sets whether this <code>WebDownloader</code> instance should
	 * automatically follow any redirect found (301-302 responses).
	 * 
	 * @param floowsRedirects
	 *            True if this instance should automatically follow any redirect
	 *            found and false if it shouldn't
	 */

	public void setFollowsRedirects(boolean followsRedirects)
	{
		this.followsRedirects = followsRedirects;
	}

	/**
	 * Gets the post data that will be sent with the next post request.
	 * 
	 * @return The currently set post data
	 */

	public byte[] getPostData()
	{
		return postData;
	}

	/**
	 * Sets the post data to be set with the next post request in addition to
	 * any web form element that may be present.
	 * 
	 * @param postData
	 *            The data to be sent
	 */

	public void setPostData(byte[] postData)
	{
		this.postData = postData;
	}

	/**
	 * Check whether this <code>WebDownloader</code> instance has caching
	 * enabled.<br>
	 * If caching is enabled than a cached version of the requested data will be
	 * used if found. If false cached data will always be ignored.
	 * 
	 * @return True if caching is enabled false otherwise
	 */

	public boolean isCacheEnabled()
	{
		return cacheEnabled;
	}

	/**
	 * Sets whether this <code>WebDownloader</code> instance has caching
	 * enabled.<br>
	 * If caching is enabled than a cached version of the requested data will be
	 * used if found. If false cached data will always be ignored.
	 * 
	 * @param cacheEnabled
	 *            True if caching is enabled false otherwise
	 */

	public void setCacheEnabled(boolean cacheEnabled)
	{
		this.cacheEnabled = cacheEnabled;
	}

	/**
	 * Gets the URL from which this <code>WebDownloader</code> instance will try
	 * to download the data.
	 * 
	 * @return The current URL.
	 */

	public URL getUrl()
	{
		return url;
	}

	/**
	 * Sets the URL from which this <code>WebDownloader</code> instance will try
	 * to download the data.
	 * 
	 * @param url
	 *            The url from which data will be downloaded
	 */

	public void setUrl(URL url)
	{
		this.url = url;
	}

	/**
	 * Sets the URL from which this <code>WebDownloader</code> instance will try
	 * to download the data.
	 * 
	 * @param url
	 *            The url in String format from which data will be downloaded
	 * 
	 * @throws MalformedURLException
	 *             if the specified URL string is not a valid URL
	 */

	public void setUrl(String url) throws MalformedURLException
	{
		this.url = new URL(url);
	}

	@Override
	public String getXML(int indentation)
	{
		return getXML(indentation, null);
	}

	@Override
	public String getXML(int indentation, String id)
	{
		String xml = new String();

		Vector<String> attr = new Vector<String>();

		if (id != null) attr.add("id=\"" + id + "\"");

		if (!isCacheEnabled())
			attr.add("cache=\"" + Boolean.toString(isCacheEnabled()) + "\"");

		if (!isFollowsRedirects())
			attr.add("follows-redirects=\""
					+ Boolean.toString(isFollowsRedirects()) + "\"");

		if (getMethod() != DEF_METHOD)
			attr.add("method=\"" + getMethod() + "\"");

		xml += XMLUtils
				.openTag(XML_ROOT, attr.toArray(new String[attr.size()]),
						indentation, true, false);

		xml += XMLUtils.element(XML_URL, getUrl().toString(), indentation + 1);

		if (getUserAgent() != DEF_UA)
			xml += XMLUtils.element(XML_UA, getUserAgent(), indentation + 1);

		if (getBufferSize() != DEF_BUFFER_SIZE)
			xml += XMLUtils.element(XML_BUFFER,
					Integer.toString(getBufferSize()), indentation + 1);

		for (String name : formElements.keySet())
		{
			xml += XMLUtils.element(XML_FORM, formElements.get(name),
					new String[] { String.format("name=\"%s\"", name) },
					indentation + 1);
		}

		xml += XMLUtils.closeTag(XML_ROOT, indentation);

		return xml;
	}

	@Override
	public void loadFromXML(Node node)
	{
		if (!node.getNodeName().equals(XML_ROOT))
			throw new IllegalArgumentException("Wrong XML node passed" + " to "
					+ getClass().getName() + ".");

		// Parse attributes //

		try
		{
			setCacheEnabled(Boolean.parseBoolean(node.getAttributes()
					.getNamedItem("cache").getNodeValue()));

		}
		catch (NullPointerException e)
		{
			setCacheEnabled(true);
		}

		try
		{
			setFollowsRedirects(Boolean.parseBoolean(node.getAttributes()
					.getNamedItem("follows-redirects").getNodeValue()));
		}
		catch (NullPointerException e)
		{
			setFollowsRedirects(true);
		}

		try
		{
			setMethod(node.getAttributes().getNamedItem("method")
					.getNodeValue());
		}
		catch (NullPointerException e)
		{
			setMethod(DEF_METHOD);
		}

		// Load defaults for optional elements //

		setUserAgent(DEF_UA);
		setBufferSize(DEF_BUFFER_SIZE);

		// Parse elements //

		NodeList nodes = node.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++)
		{
			Node n = nodes.item(i);

			try
			{
				if (n.getNodeType() == Node.ELEMENT_NODE)
				{
					if (n.getNodeName().equalsIgnoreCase(XML_URL))
					{
						setUrl(n.getFirstChild().getNodeValue());
					}
					else if (n.getNodeName().equalsIgnoreCase(XML_UA))
					{
						setUserAgent(n.getFirstChild().getNodeValue());
					}
					else if (n.getNodeName().equalsIgnoreCase(XML_BUFFER))
					{
						setBufferSize(Integer.parseInt(n.getFirstChild()
								.getNodeValue()));
					}
					else if (n.getNodeName().equalsIgnoreCase(XML_FORM))
					{
						addFormElement(n.getAttributes().getNamedItem("name")
								.getNodeValue(), n.getFirstChild()
								.getNodeValue());
					}
				}
			}
			catch (NullPointerException e)
			{
				continue;
			}
			catch (MalformedURLException e)
			{
				throw new RuntimeException("Malformed url for this "
						+ getClass().getSimpleName(), e);
			}
			catch (DOMException e)
			{
				continue;
			}
		}
	}
}
