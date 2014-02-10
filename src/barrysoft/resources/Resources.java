package barrysoft.resources;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import barrysoft.utils.RuntimeInfo;

public abstract class Resources {
	
	private String 	resourcesPath;
	private File 	configurationDirectory;
	private File 	workingDirectory;
	
	public Resources(Class<?> mainClass, String resourcesPath)
	{
		this(mainClass, resourcesPath, null);
	}
	
	public Resources(Class<?> mainClass, String resourcesPath, File configDir) 
	{
		if (RuntimeInfo.isRunningFromJar(mainClass))
			setWorkingDirectory(RuntimeInfo.getJarDirectory(mainClass));
		else
			setWorkingDirectory(new File("."));
		
		if (configDir == null)
			configDir = getWorkingDirectory();
		
		setConfigurationDirectory(configDir);
		setResourcesPath(resourcesPath);
	}
	
	/**
	 * Gets a File instance for an external
	 * file (not in the classpath).
	 * 
	 * @param name The relative path of the file
	 * 
	 * @param workingDir The directory to use as
	 * 					working directory.
	 * 
	 * @return A File instance of the requested
	 * 			file whether it exists or not
	 */
	
	public File getExternalFile(File workingDir, String name)
	{
		return new File(workingDir + File.separator + name);
	}
	
	/**
	 * Gets a File instance for an external
	 * file (not in the classpath), relative
	 * to the current working directory.
	 * 
	 * @param name The relative path of the file
	 * 
	 * @return A File instance of the requested
	 * 			file whether it exists or not
	 * 
	 * @see #getWorkingDirectory()
	 */
	
	public File getExternalFile(String name)
	{
		return getExternalFile(getWorkingDirectory(), name);
	}
	
	/**
	 * Gets a File instance for an external
	 * file (not in the classpath) in the 
	 * current configuration directory.
	 * 
	 * @param name The relative path of the file
	 * 
	 * @return A File instance of the requested
	 * 			file whether it exists or not
	 * 
	 * @see #getConfigurationDirectory()
	 */
	
	public File getExternalConfigFile(String name)
	{
		return getExternalFile(getConfigurationDirectory(), name);
	}
	
	/**
	 * Gets the URL to an icon image.
	 * 
	 * @param name The name of the icon without ".png"
	 * 
	 * @return An URL to the icon if it exists or
	 * 			null otherwise
	 */
	
	public URL getIconURL(String name)
	{
		return getInternalFileURL(getResourcesPath(), name+".png");
	}
	
	/**
	 * Converts a classpath to the
	 * corresponding URL path.
	 * 
	 * @param path	The package path of the file
	 * 				(like <code>com.package.name</code>)
	 * 
	 * @param name	The name of the file
	 * 
	 * @return The composed path
	 */
	
	public static String buildInternalPath(String path, String name, boolean absolute)
	{
		return (absolute ? "/" : "") + path.replaceAll("\\.", "\\/") + "/" + name;
	}
	
	public String buildInternalPath(String name, boolean absolute)
	{
		return (absolute ? "/" : "") + getResourcesPath().replaceAll("\\.", "\\/") + "/" + name;
	}
	
	/**
	 * Gets the InputStream of an internal
	 * file (in the classpath).
	 *  
	 * @param path	The package path of the file
	 * 				(like <code>com.package.name</code>)
	 * 
	 * @param name	The name of the file
	 * 
	 * @return An input stream for the desired file
	 */
	
	public InputStream getInternalFile(String path, String name)
	{
		return getClass().getResourceAsStream(buildInternalPath(path, name, true));
	}
	
	public InputStream getInternalFile(String name)
	{
		return getInternalFile(getResourcesPath(), name);
	}
	
	/**
	 * Gets the URL of an internal
	 * file (in the classpath).
	 * 
	 * @param path	The package path of the file
	 * 				(like <code>com.package.name</code>)
	 * 
	 * @param name	The name of the file
	 * 
	 * @return The URL to the desired file
	 */
	
	public URL getInternalFileURL(String path, String name)
	{
		return getInternalFileURL(path, name, true);
	}
	
	/**
	 * Gets the URL of an internal
	 * file (in the classpath).
	 * 
	 * @param path	The package path of the file
	 * 				(like <code>com.package.name</code>)
	 * 
	 * @param name	The name of the file
	 * 
	 * @param absolute	True if the path should be absolute
	 * 
	 * @return The URL to the desired file
	 */
	
	public URL getInternalFileURL(String path, String name, boolean absolute)
	{
		return getClass().getResource(buildInternalPath(path, name, absolute));
	}
	
	/**
	 * Gets the URL of an internal file
	 * (in the classpath) that is located
	 * in the default directory (CONF_DIR)
	 * 
	 * @param name	The name of the file
	 * 
	 * @param absolute	True if the path should be absolute
	 * 
	 * @return The URL to the desired file
	 */
	
	public URL getInternalFileURL(String name, boolean absolute)
	{
		return getInternalFileURL(getResourcesPath(), name, absolute);
	}
	
	/**
	 * <p>
	 * Returns the path in the classpath
	 * where all resources are located.
	 * </p>
	 * 
	 * @return The current resources path
	 * @see #setResourcesPath(String)
	 */

	public String getResourcesPath()
	{
		return resourcesPath;
	}
	
	/**
	 * <p>
	 * Sets the path in the classpath
	 * where all resources are located.
	 * </p><p>
	 * The path is expressed with the package
	 * notation.
	 * </p><p>
	 * For example a valid resource path could be:</p>
	 * <blockquote>
	 * <code>com.package.resources</code> 
	 * </blockquote>
	 * 
	 * @param resourcePath the resource path to be set
	 * @see #setResourcesPath(String)
	 */

	public void setResourcesPath(String resourcesPath)
	{
		this.resourcesPath = resourcesPath;
	}
	
	/**
	 * Returns the current configuration
	 * path.
	 * 
	 * @return The current configuration path
	 * @see #setConfigurationDirectory(File)
	 */

	public File getConfigurationDirectory()
	{
		return configurationDirectory;
	}
	
	/**
	 * <p>
	 * Sets the current configuration directory.
	 * </p><p>
	 * A configuration directory is used to store
	 * all user related configuration files.<br>
	 * It is usually located inside the current user's 
	 * home directory so that her configuration 
	 * will be preserved even if the application is 
	 * removed and reinstalled.
	 * </p><p>
	 * If the specified directory doesn't exist
	 * it will be created.
	 * </p>
	 * 
	 * @param configurationDirectory The configuration
	 * 			directory to be used, including the
	 * 			user's home directory path (if so desired)
	 */

	public void setConfigurationDirectory(File configurationDirectory)
	{
		if (configurationDirectory == null)
			return;
		
		if (!configurationDirectory.exists())
			configurationDirectory.mkdir();
		
		this.configurationDirectory = configurationDirectory;
	}
	
	/**
	 * Returns the current working directory.
	 * 
	 * @return The current working directory
	 * @see #setWorkingDirectory(File)
	 */

	public File getWorkingDirectory()
	{
		return workingDirectory;
	}
	
	/**
	 * <p>
	 * Sets the current working directory.
	 * </p><p>
	 * This should be set according to the
	 * way the application was executed.
	 * If the application has been executed
	 * using the java command than the current
	 * directory can be retrived with:
	 * <blockquote><code>
	 * new File(".")
	 * </code></blockquote>
	 * If the application has been executed
	 * from a jar file, other means must be used
	 * to extract the current path.
	 * </p>
	 * 
	 * @param workingDirectory The working directory
	 * 		path to be set
	 */

	public void setWorkingDirectory(File workingDirectory)
	{
		this.workingDirectory = workingDirectory;
	}

}
