package barrysoft.plugins;

import java.io.File;

/**
 * This class is throwed by <code>PluginManager</code>
 * when there has been an error while trying to load
 * a plugin.
 * 
 * @author Daniele Rapagnani
 *
 */

public class PluginLoadingException extends Exception {
	
	private static final long serialVersionUID = 2710752053185583800L;

	/**
	 * Creates an instance of this exception.
	 * 
	 * @param f	The plugin file that couldn't be
	 * 				loaded
	 * @param cause Why the loading process failed
	 */
	
	public PluginLoadingException(File f, Throwable cause) {
		super(String.format("Can't load plugin from file: %s", 
				f.getAbsoluteFile()), cause);
	}
	
}
