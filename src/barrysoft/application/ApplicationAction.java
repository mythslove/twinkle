package barrysoft.application;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import barrysoft.logs.Logger;

public abstract class ApplicationAction<AppType extends Application> implements Action {

	private AppType application;
	private boolean enabled = true;
	
	private String name;
	private String shortDesc;
	
	public ApplicationAction(AppType application, String name, String desc) {
		setApplication(application);
		setName(name);
		setShortDesc(desc);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	public Object getValue(String key) {
		
		if (key.equals(Action.NAME))
			return getName();
		else if (key.equals(Action.SHORT_DESCRIPTION))
			return getShortDesc();
		else
			return null;
		
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub

	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	public void setEnabled(boolean b) {
		enabled = b;
	}

	public void actionPerformed(ActionEvent e) {
		Logger.debug(String.format("'%s' has been pressed", getName()));
	}

	public AppType getApplication() {
		return application;
	}

	public void setApplication(AppType application) {
		this.application = application;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
