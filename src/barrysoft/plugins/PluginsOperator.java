package barrysoft.plugins;

import java.util.Vector;

/**
 * This class is used to represent
 * an object which does some kind
 * of operation with plugins.<br>
 * Common things that are needed
 * to do such kind of task are wrapped
 * inside this class.
 * 
 * @author Daniele Rapagnani
 *
 */

public abstract class PluginsOperator<PluginType extends Plugin> 
										implements PluginsExecutionListener<PluginType> 
{
	
	private Vector<PluginStatusShower> statusShowers = 
		new Vector<PluginStatusShower>();
	
	private PluginsManager<PluginType> pluginsManager;
	private PluginsScheduler<PluginType> pluginsScheduler;

	public PluginsOperator(PluginsManager<PluginType> pluginsManager,
			PluginsScheduler<PluginType> pluginsScheduler) 
	{
		setPluginsManager(pluginsManager);
		setPluginsScheduler(pluginsScheduler);
	}
	
	protected Vector<PluginStatusShower> getStatusShowers() {
		return statusShowers;
	}
	
	public void removeFromShowers(PluginType plugin) {
		for (PluginStatusShower statusShower: getStatusShowers())
			if (statusShower != null)
				statusShower.removePlugin(plugin);
	}
	
	public void removeShowersFrom(PluginType plugin) {
		for (PluginStatusShower statusShower: getStatusShowers())
			if (statusShower != null)
				plugin.removeStatusShower(statusShower);
	}

	public void addStatusShower(PluginStatusShower statusShower) {
		statusShowers.add(statusShower);
	}	

	public PluginsManager<PluginType> getPluginsManager() {
		return pluginsManager;
	}

	public void setPluginsManager(PluginsManager<PluginType> pluginsManager) {
		this.pluginsManager = pluginsManager;
	}

	public PluginsScheduler<PluginType> getPluginsScheduler() {
		return pluginsScheduler;
	}

	public void setPluginsScheduler(PluginsScheduler<PluginType> pluginsScheduler) {
		this.pluginsScheduler = pluginsScheduler;
		this.pluginsScheduler.setExecList(this);
	}

	public abstract void pluginDone(PluginsRunner<PluginType> p);

	public abstract void tasksEnded();

	public abstract void tasksStarted();

}
