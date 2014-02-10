package barrysoft.application.views;

import java.util.Vector;

import barrysoft.application.Application;
import barrysoft.application.ApplicationWindow;
import barrysoft.interaction.InteractionSwing;
import barrysoft.logs.Logger;
import barrysoft.logs.LoggerSwingSimple;

public class SwingView<AppType extends Application> extends View<AppType> {
	
	private Vector<ApplicationWindow> windows = new Vector<ApplicationWindow>();
	
	public SwingView(AppType parent) {
		super(parent);
		
		Logger.setLogger(new LoggerSwingSimple());
		Logger.setVerboseLevel(Logger.LOWEST_VERBOSITY);
		
		getParent().setInteractor(new InteractionSwing());
	}
	
	public void addWindow(ApplicationWindow w) {
		windows.add(w);
	}
	
	public void showWindow(String name) {
		for (int i=0; i<windows.size(); i++) {
			if (windows.get(i).getWindowName().equals(name)) {
				windows.get(i).setVisible(true);
			}
		}
	}
	
	public void hideWindow(String name) {
		for (int i=0; i<windows.size(); i++) {
			if (windows.get(i).getWindowName().equals(name)) {
				windows.get(i).setVisible(false);
			}
		}
	}

	@Override
	public void onInitialization(Class mainClass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitialized(Class mainClass) {
		// TODO Auto-generated method stub
		
	}

}
