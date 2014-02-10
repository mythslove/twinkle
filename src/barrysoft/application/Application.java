package barrysoft.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import barrysoft.application.views.View;
import barrysoft.configuration.Configurable;
import barrysoft.configuration.Configuration;
import barrysoft.interaction.Interaction;
import barrysoft.localization.Localizable;
import barrysoft.localization.Localization;
import barrysoft.localization.LocalizationException;
import barrysoft.logs.Logger;
import barrysoft.options.OptionNotFoundException;
import barrysoft.options.Optionalizable;
import barrysoft.options.Options;
import barrysoft.utils.FileUtils;

public abstract class Application implements Localizable, 
											Configurable, 
											Optionalizable 
{
	
	public static final String 	XML_ROOT = "application";
	
	public static final String	DEFAULT_CONFIGDIR = "Config";
	public static final String	DEFAULT_CONFIGFILE = DEFAULT_CONFIGDIR+File.separator+
													 "Options.aof";
	
	private ApplicationInfo		info;
	
	private View				view;
	
	private Interaction			interactor;
	private String				configFilename = DEFAULT_CONFIGFILE;
	private boolean				debug = false;
	
	private Options				options;
	
	private Localization		localization;
	
	private Configuration		configuration;
	
	public Application() {
		info = new ApplicationInfo();
		localization = new Localization();
		options = new Options();
		configuration = new Configuration(XML_ROOT);
		
		getConfiguration().addConfiguration(Options.XML_ROOT, getOptions());
		getConfiguration().addConfiguration(Localization.XML_ROOT, getLocalization());
	}
	 
	public void initialize(Class mainClass) 
		throws ApplicationInitializationException
	{
		
		if (getView() != null)
			getView().onInitialization(mainClass);
		
		try {
			loadConfiguration(mainClass);
		} catch (ParserConfigurationException e2) {
			throw new ApplicationInitializationException("Can't load"+
					" application configuration", e2);
		} catch (SAXException e2) {
			throw new ApplicationInitializationException("Can't load"+
					" application configuration", e2);
		} catch (IOException e2) {
			throw new ApplicationInitializationException("Can't load"+
					" application configuration", e2);
		}
		
		String language;
		try {
			language = getOptions().getOptionValue("language", String.class);
		} catch (OptionNotFoundException e1) {
			throw new ApplicationInitializationException("No 'language'"+
					" option specified in configuration file", e1);
		}
		
		try {
			getLocalization().setCurrentLocalization(language);
		} catch (LocalizationException e) {
			throw new ApplicationInitializationException("Specified "+
					"language can't be found: "+language, e);
		}
		
	}
	
	public void start() {
		
		if (getView() != null)
			getView().onStart();
		
	}
	
	/**
	 * Checks if the files/dirs specified
	 * in each of the options named with
	 * the values in the array exists.
	 * If some files are not found an error
	 * for each file is displayed.
	 * 
	 * @param optionsName Array of the options' 
	 * 	names containing the files path
	 * @return False if not all files were found
	 */
	
	public boolean checkEssentials(String[] optionsName, Class mainClass) {
	
		boolean allFound = true;
		
		for (int i=0; i < optionsName.length; i++) {
			
			String file;
			
			try {
				file = getOptions().getOptionValue(optionsName[i], String.class);
			} catch (OptionNotFoundException e) {
				Logger.warning(e.getMessage());
				allFound = false;
				continue;
			}
			
			// Check if the file exists
			File essentialFile = new File(FileUtils.getJarPath(file, mainClass));
			if (!essentialFile.exists()) {
				Logger.error("Couldn't find file '"+essentialFile.getAbsolutePath()+"'");
				allFound = false;
			}
		}
		
		return allFound;
		
	}
	
	public Interaction getInteractor() {
		return interactor;
	}
	
	public void setInteractor(Interaction i) {
		interactor = i;
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public String getConfigFilename() {
		return configFilename;
	}

	public void setConfigFilename(String configFilename) {
		this.configFilename = configFilename;
	}
	
	public Localization getLocalization() {
		return localization;
	}
	
	public void setLocalization(Localization localization) {
		this.localization = localization;
	}
	
	public void loadConfiguration(Class mainClass) 
		throws ParserConfigurationException, 
			SAXException, 
			IOException 
	{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		Document doc = db.parse(new File(FileUtils.getJarPath(getConfigFilename(), mainClass)));
		
		getConfiguration().loadFromXML(doc.getFirstChild());
		
	}
	
	public void saveConfiguration(Class mainClass) throws IOException {
	
		File configFile = new File(FileUtils.getJarPath(getConfigFilename(), mainClass));
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(configFile);
		} catch (FileNotFoundException e) {
			configFile.createNewFile();
			fos = new FileOutputStream(configFile);
		}

		fos.write(getConfiguration().getXML(0).getBytes());
		fos.flush();
		fos.close();
		
	}
	
	public boolean isDebugOn() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public ApplicationInfo getInfo() {
		return info;
	}

	public void setInfo(ApplicationInfo info) {
		this.info = info;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public View getView() {
		return getView(View.class);
	}
	
	public <ViewType> ViewType getView(Class<ViewType> viewClass) {
		return viewClass.cast(view);
	}

	public void setView(View view) {
		this.view = view;
	}
	
}
