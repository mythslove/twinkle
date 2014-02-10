package barrysoft.application.views;

import barrysoft.application.Application;

public abstract class View<AppType extends Application> {
	
	private AppType parent;
	
	public View(AppType parent) {
		setParent(parent);
	}

	public AppType getParent() {
		return parent;
	}

	public void setParent(AppType parent) {
		this.parent = parent;
	}
	
	public abstract void onInitialization(Class mainClass);
	
	public abstract void onInitialized(Class mainClass);
	
	public abstract void onStart();
	
}
