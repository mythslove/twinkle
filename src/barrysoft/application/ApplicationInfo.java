package barrysoft.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ApplicationInfo 
{	
	public static String NAME_KEY 			= "software.name";
	public static String AUTHOR_KEY 		= "software.author";
	public static String COPYRIGHT_KEY 		= "software.copyright";
	public static String WEBSITE_KEY 		= "software.website";
	public static String VERSION_KEY		= "software.version";
	public static String ABOUT_IMAGE_KEY	= "software.about.image";
	public static String REVISION_KEY		= "software.revision";
	public static String BUILD_DATE_KEY		= "software.build.date";
	public static String BUILD_NUM_KEY		= "software.build.number";
	
	private final Properties defaultProperties = new Properties();
	private final Properties infoProperties;
	
	public ApplicationInfo(InputStream is) 
	{	
		this();
		
		if (is == null)
			return;
			
		try {
			infoProperties.load(is);
		} catch (IOException e) {
			Logger.getLogger(getClass()).
				error("Can't read application info properties.");
			
			e.printStackTrace();
		}
	}
	
	public ApplicationInfo() {
		defaultProperties.setProperty(NAME_KEY, "Software Name");
		defaultProperties.setProperty(AUTHOR_KEY, "Software Author");
		defaultProperties.setProperty(COPYRIGHT_KEY, "Copyright Unknown");
		defaultProperties.setProperty(WEBSITE_KEY, "");
		defaultProperties.setProperty(VERSION_KEY, "1.0");
		defaultProperties.setProperty(ABOUT_IMAGE_KEY, "about.png");
		defaultProperties.setProperty(REVISION_KEY, "0");
		defaultProperties.setProperty(BUILD_DATE_KEY, 
				Calendar.getInstance().getTime().toString());
		defaultProperties.setProperty(BUILD_NUM_KEY, "0");
		
		infoProperties = new Properties(defaultProperties);
	}
	
	public String getInfo(String name) {
		return infoProperties.getProperty(name);
	}
	
	public String getAboutImage() {
		return getInfo(ABOUT_IMAGE_KEY);
	}
	
	public String getBuildNumber() {
		return getInfo(BUILD_NUM_KEY);
	}

	public String getCopyRight() {
		return getInfo(COPYRIGHT_KEY);
	}
	
	public String getRevisionNumber() {
		return getInfo(REVISION_KEY);
	}

	public String getSoftwareAuthor() {
		return getInfo(AUTHOR_KEY);
	}
	
	public String getSoftwareName() {
		return getInfo(NAME_KEY);
	}

	public String getTimeStamp() {
		return getInfo(BUILD_DATE_KEY);
	}

	public String getVersion() {
		return getInfo(VERSION_KEY);
	}
	
	public String getWebSite() {
		return getInfo(WEBSITE_KEY);
	}
		
}
	