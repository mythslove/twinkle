package barrysoft.configuration;

import barrysoft.xml.XMLizable;

public class ConfigurationEntry {
	
	private XMLizable 	object;
	private String		id;
	private String		tag;
	private boolean		readOnly;
	
	public ConfigurationEntry(XMLizable object, String tag) {
		this(object, tag, null, false);
	}
	
	public ConfigurationEntry(XMLizable object, String tag, String id) {
		this(object, tag, id, false);
	}
	
	public ConfigurationEntry(XMLizable object, String tag, String id, boolean readOnly) {
		this.object = object;
		this.tag = tag;
		this.id = id;
		this.readOnly = readOnly;
	}

	public XMLizable getObject() {
		return object;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public String getTag() {
		return tag;
	}

	public String getId() {
		return id;
	}

}
