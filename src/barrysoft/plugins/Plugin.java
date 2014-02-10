package barrysoft.plugins;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import barrysoft.configuration.Configurable;
import barrysoft.configuration.Configuration;
import barrysoft.localization.Localizable;
import barrysoft.localization.Localization;
import barrysoft.logs.Logger;
import barrysoft.options.OptionNotFoundException;
import barrysoft.options.Optionalizable;
import barrysoft.options.Options;
import barrysoft.xml.XMLUtils;

public class Plugin implements PluginsInterface,
								Configurable,
								Localizable,
								Optionalizable
{
	
	public final static String XML_ROOT = "plugin";
	
	public final static String XML_OPTIONS = "options";
	public final static String XML_OPTION = "option";
	
	public final static String DEF_OPERATION = "None";
	
	private PluginInfo info = new PluginInfo();
	
	private String 	directory;
	private File 	file;
	
	private String 	status;
	private String 	operation = DEF_OPERATION;
	private int		progress;
	
	private PluginsExecutionListener 		executionListener;
	private ArrayList<PluginStatusShower> 	statusShowers;

	private Options	options;
	private Localization localization;
	private Configuration configuration;
	
	public Plugin() {
		
		statusShowers = new ArrayList<PluginStatusShower>();
		options = new Options();
		localization = new Localization();
		configuration = new Configuration(XML_ROOT);
		
		getConfiguration().addConfiguration(PluginInfo.XML_ROOT, getInfo());
		getConfiguration().addConfiguration(Options.XML_ROOT, getOptions());
		getConfiguration().addConfiguration(Localization.XML_ROOT, getLocalization());
		
	}
	
	@Override
	public boolean isEnabled() {
		try {
			return getOptions().getOptionValue("enabled", Boolean.class);
		} catch (OptionNotFoundException e) {
			getOptions().setOption("enabled", true);
			return true;
		}
	}

	@Override
	public boolean hasConfiguration() {
		return false;
	}
	
	@Override
	public void configure(JFrame owner) {
		return;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		getOptions().setOption("enabled", enabled);
	}

	public void trace(String msg) { 
		Logger.log("["+getInfo().getName()+"]: "+msg); 
	}
	
	public void debug(String msg, int level) {
		Logger.debug("["+getInfo().getName()+"]: "+msg,level);
	}
	
	public void debug(String msg) {
		debug(msg,Logger.LOWEST_VERBOSITY);
	}
	
	public void error(String error) {
		debug(error);
		errorStatusShowers(error);
	}
	
	protected void load()
			throws FileNotFoundException,
			ParserConfigurationException, 
			SAXException, 
			IOException
	{
		
		if (getFile() == null)
			throw new NullPointerException("Configuration file was null");
		
		if (!getFile().exists())
			throw new FileNotFoundException(getFile().getAbsolutePath());
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		Document doc = db.parse(file);

		getConfiguration().loadFromXML(doc.getFirstChild());
		
		loadOthers(doc);
		
	}
	
	public void loadOthers(Document doc) {}
	
	public void save() throws IOException {
		
		if (getFile() == null)
			throw new IllegalStateException("Plugin file is null");
		
		getFile().createNewFile();
		
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(getFile()));
		
		dos.write(XMLUtils.getXMLHeader().getBytes());
		dos.write(XMLUtils.openTag(getConfiguration().getXmlRoot(), 0).getBytes());
		
		dos.write(getConfiguration().getRootLessXML(1).getBytes());

		saveOthers(dos);
		
		dos.write(XMLUtils.closeTag(getConfiguration().getXmlRoot(), 0).getBytes());
		
		dos.flush();
		dos.close();
		
	}
	
	public void saveOthers(DataOutputStream dos) throws IOException {}
	
	public void updateStatus(String status, Integer step, Integer total) {
		setStatus(status);
		
		if (total > 0)
			setProgress((step * 100)/total);
		else
			setProgress(0);
		
		notifyStatusShowers();
	}
	
	public void notifyStatusShowers() {
		for (int i=0; i < statusShowers.size(); i++)
			getStatusShower(i).onStatusUpdate();
	}
	
	public void errorStatusShowers(String error) {
		for (PluginStatusShower statusShower : statusShowers)
			if (statusShower != null)
				statusShower.onError(this, error);
	}
	
	public PluginStatusShower getStatusShower(int i) {
		return statusShowers.get(i);
	}

	public void addStatusShower(PluginStatusShower statusShower) {
		statusShowers.add(statusShower);
		statusShower.onStatusUpdate();
	}
	
	public void removeStatusShower(PluginStatusShower statusShower) {
		statusShowers.remove(statusShower);
	}
	
	public void clearStatusShowers() {
		statusShowers.clear();
	}

	public PluginsExecutionListener getExecutionListener() {
		return executionListener;
	}

	public void setExecutionListener(PluginsExecutionListener executionListener) {
		this.executionListener = executionListener;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String pluginDir) {
		this.directory = pluginDir;
	}

	public File getFile() {
		return file;
	}

	protected void setFile(File pluginFile) {
		this.file = pluginFile;
	}

	@Override
	public PluginInfo getInfo() {
		return info;
	}

	protected void setInfo(PluginInfo info) {
		this.info = info;
	}

	@Override
	public Options getOptions() {
		return options;
	}
	
	@Override
	public Localization getLocalization() {
		return localization;
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

}
