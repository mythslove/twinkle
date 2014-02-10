package barrysoft.plugins;

public abstract class PluginsRunner <PluginType extends Plugin> implements Runnable {
	
	private PluginType 			currentPlugin;
	private Thread				thread;
	
	private int					current;
	private int					total;
	
	private String				taskName;
	
	public PluginsRunner(PluginType plugin, int current, int total, String taskName) {
		this(plugin, current, total);
		setTaskName(taskName);
	}
	
	public PluginsRunner(PluginType plugin, int current, int total) {
		this.currentPlugin = plugin;
		this.current = current;
		this.total = total;
	}
	
	@Override
	public abstract void run();
	
	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public PluginType getCurrentPlugin() {
		return currentPlugin;
	}

	public void setCurrentPlugin(PluginType currentPlugin) {
		this.currentPlugin = currentPlugin;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
