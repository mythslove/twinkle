package barrysoft.localization;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import barrysoft.localization.Localization;
import barrysoft.localization.LocalizationException;

public class Localizer {
	
	private static final Localizer instance = new Localizer();
	
	private final Localization lang = new Localization();
	
	public static Localizer getInstance()
	{
		return instance;
	}
	
	protected static String getLocaleName()
	{
		try {
			
			return Localization.getSystemLocale();
			
		} catch (LocalizationException e) {
			
			return Localization.DEFAULT_LOCALE;
			
		}
	}
	
	private Localizer()
	{
		
	}
	
	/**
	 * Attempt to load an appropriate localization
	 * based on the system locale.
	 * If there's no localization available for
	 * the current locale the <code>en_US</code> 
	 * localization is used as fallback.
	 *
	 */
	
	protected void initialize()
	{
		initialize(getLocaleName());		
	}
	
	/**
	 * Create the current application localization
	 * with the specified locale.
	 * 
	 * @param locale The locale for the desired language
	 */
	
	public void initialize(String locale)
	{
		lang.setCurrentLocalization(locale);
		
		Logger.getLogger(getClass()).
			debug("Current localization is: "+lang.getCurrentLanguage());
	}
	
	/**
	 * Returns the locale the application
	 * is currently using.
	 * 
	 * @return The current locale code
	 */
	
	public String getCurrentLocale()
	{
		return lang.getCurrentLanguage();
	}
	
	public void loadLocalization(InputStream is)
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			Logger.getLogger(getClass()).error("Can't load localization file.");
			e.printStackTrace();
			
			return;
		}
		
		Document doc;
		try {
			doc = db.parse(is);
		} catch (SAXException e) {
			Logger.getLogger(getClass()).error("Can't parse localization file.");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			Logger.getLogger(getClass()).error("Can't open/read localization file.");
			e.printStackTrace();
			return;
		}
		
		lang.loadFromXML(doc.getFirstChild());
		
		initialize();
	}
	
	protected String localize(String locName, String... params)
	{
		return lang.getLocalized(locName, params);
	}
	
	/**
	 * Gets the localized strings corresponding
	 * to the provided value name and parametrize
	 * the corresponding string with the provided
	 * values.
	 * 
	 * @param locName The name of the localization value
	 * @param params Params to be sobstituted in the localized
	 * 					string (any occurrence of %1, %2, ..., %n)
	 * 
	 * @return The localized, parametrized String
	 */
	
	public static String L(String locName, String... params)
	{
		return getInstance().localize(locName, params);
	}
	
	/**
	 * Gets the localized strings corresponding
	 * to the provided value name.
	 * 
	 * @param locName The name of the localization value
	 * 
	 * @return The localized String
	 */
	
	public static String L(String locName)
	{
		return getInstance().localize(locName, (String[])null);
	}

}
