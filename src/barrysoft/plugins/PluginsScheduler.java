package barrysoft.plugins;

import java.util.Stack;

public class PluginsScheduler <PluginType extends Plugin> implements PluginsExecutionListener<PluginType> {

	public static final int	DEFAULT_POOL_SIZE = 5;
	
	private Stack<PluginsRunner> queue = new Stack<PluginsRunner>();
	private int poolSize = DEFAULT_POOL_SIZE;
	private PluginsExecutionListener<PluginType> execList = null;
	
	private boolean firstExecution = true;
	
	private int runningThreads = 0;
	
	public PluginsScheduler() {}
	
	public PluginsScheduler(int poolSize) {
		setPoolSize(poolSize);
	}
	
	public void queuePlugin(PluginsRunner p) {
		queue.add(p);
		executeNextFill();
	}
	
	public void executeNextCount(int count) {
		for (int i=0; i < count; i++)
			executeNext();
	}
	
	public void executeNext() {
		if (queue.isEmpty())			
			return;
		
		if (runningThreads < getPoolSize()) {
			
			PluginsRunner p = queue.pop();
			new Thread(p).start();
	
			//First thread executed
			if (firstExecution) {
				
				if (getExecList() != null)
					getExecList().tasksStarted();
				
				firstExecution = false;
			}
			
			runningThreads++;
		}
		
	}
	
	public int getQueuedCount() { 
		return queue.size(); 
	}
	
	public int getRunningCount() {
		return runningThreads;
	} 
	
	public int getToProcessCount() {
		return getQueuedCount()+getRunningCount()-1;
	}
	
	public int getLeftCount(int total) {
		return (total - getToProcessCount());
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public PluginsExecutionListener<PluginType> getExecList() {
		return execList;
	}

	public void setExecList(PluginsExecutionListener<PluginType> execList) {
		this.execList = execList;
	}

	public void pluginDone(PluginsRunner<PluginType> p) {
		if (getExecList() != null)
			getExecList().pluginDone(p);
		
		runningThreads--;
		
		if (queue.isEmpty() && runningThreads == 0) {
			if (getExecList() != null)
				getExecList().tasksEnded();
			
			firstExecution = true;
		}
		
		executeNextFill();
	}
	
	public void executeNextFill() {
		if (queue.isEmpty()) return;
		
		if (runningThreads < getPoolSize()) {		
			int emptySlots = (getPoolSize() - runningThreads);
			executeNextCount(Math.min(emptySlots, queue.size()));
		}
	}

	public void tasksEnded() {
		// TODO Auto-generated method stub
		
	}

	public void tasksStarted() {
		// TODO Auto-generated method stub
		
	}

}
