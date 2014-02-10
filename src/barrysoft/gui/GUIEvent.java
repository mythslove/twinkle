package barrysoft.gui;

import java.awt.AWTEvent;

public class GUIEvent<Type> extends AWTEvent {
	
	private static final long serialVersionUID = -4527481163765534107L;
	
	public static final int EVENT_ID = AWTEvent.RESERVED_ID_MAX + 11;
	
	private final Type				type;
	private final Object[]			data;
	
	public GUIEvent(Type type, Object[] data)
	{
		this(new Object(), EVENT_ID, type, data);
	}
	
	public GUIEvent(int id, Type type, Object[] data)
	{
		this(new Object(), id, type, data);
	}
	
	public GUIEvent(Object source, int id, Type type, Object[] data)
	{
		super(source, id);
		this.data = data;
		this.type = type;
	}
	
	public Object[] getData()
	{
		return data;
	}
	
	public <T> T getDataItem(int index, Class<T> clazz)
	{
		if (data.length <= index)
		{
			throw new IllegalArgumentException("Data item out of bounds: "
					+index+"/"+data.length);
		}
		
		return clazz.cast(data[index]);
	}
	
	public Type getType()
	{
		return type;
	}
	
	public GUIEvent<Type> copy()
	{
		GUIEvent<Type> eventCopy = new GUIEvent<Type>(getSource(), getID(), type, data);
		
		return eventCopy;
	}

}
