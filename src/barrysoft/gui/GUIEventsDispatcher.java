package barrysoft.gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.Vector;

public class GUIEventsDispatcher<Type> {
	
	private final Vector<Component> targets =
		new Vector<Component>();
	
	public void addTarget(Component target)
	{
		targets.add(target);
	}
	
	public void removeTarget(Component target)
	{
		targets.remove(target);
	}
	
	public void dispatch(Type event)
	{
		dispatch(new GUIEvent<Type>(event, new Object[] {}));
	}
	
	public void dispatch(Type event, Object... data)
	{
		dispatch(new GUIEvent<Type>(event, data));
	}
	
	public void dispatch(int id, Type event, Object... data)
	{
		dispatch(new GUIEvent<Type>(id, event, data));
	}
	
	public void dispatch(GUIEvent<Type> event)
	{
		EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		
		for (Component target : targets)
		{
			GUIEvent<Type> eventCopy = event.copy();
			eventCopy.setSource(target);
			
			eventQueue.postEvent(eventCopy);
		}		
	}

}
