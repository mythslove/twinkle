package barrysoft.options;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import barrysoft.utils.ReflectionUtils;
import barrysoft.xml.XMLUtils;
import barrysoft.xml.XMLizable;

public class Option<T> implements XMLizable {
	
	public final static String XML_ROOT = "option";
	
	private String 			name;
	private T 				value;
	
	public Option() {}
	
	public Option(String name, T value) {
		setName(name);
		setValue(value);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}

	public String getXML(int indentation) {
		return getXML(indentation, null);
	}
	
	public String getXML(int indentation, String id) {
		
		Vector<String> attributes = new Vector<String>();
		
		if (id != null)
			attributes.add(String.format("id=\"%s\"", id));
		
		attributes.add(String.format("name=\"%s\"", getName()));
		attributes.add(String.format("type=\"%s\"",
				getValue().getClass().getCanonicalName()));
		
		return XMLUtils.element(XML_ROOT, getValue().toString(), 
								(String[]) attributes.toArray(new String[attributes
										.size()]), indentation);
		
	}

	@SuppressWarnings("unchecked")
	public void loadFromXML(Node node)
	{
		
		if (!node.getNodeName().equals(XML_ROOT))
			throw new IllegalArgumentException("Wrong node passed to an ApplicationOption: "+
					node.getNodeName());
		
		NamedNodeMap attributes = node.getAttributes();
		
		if (attributes == null)
			throw new IllegalArgumentException("No node attributes found while loading option.");
		
		setName(attributes.getNamedItem("name").getNodeValue());
		
		String className = attributes.getNamedItem("type").getNodeValue();

		String value;
		
		try {
			value = node.getFirstChild().getNodeValue();
		} catch (NullPointerException e) {
			value = "";
		}
		
		Class<?> optClass = null;
		
		try {
			
			optClass = Option.class.getClassLoader().loadClass(className);
			Constructor<?> c = ReflectionUtils.getConstructorFor(optClass, String.class);			
			setValue((T)c.newInstance(new Object[] {value}));
			
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Can't load option value for: "+getName(), e);
		} catch (InstantiationException e) {
			throw new RuntimeException("Can't load option value for: "+getName(), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Can't load option value for: "+getName(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Can't load option value for: "+getName(), e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Can't load option value for: "+getName(), e);
		}
		
	}
		
}
