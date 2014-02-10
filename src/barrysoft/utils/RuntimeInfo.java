package barrysoft.utils;

import java.io.File;

public class RuntimeInfo {
	
	/**
	 * Gets the directory where the jar is located.
	 * 
	 * @return The directory where the jar is located.
	 */
	
	public static File getJarDirectory(Class<?> clazz)
	{
		return (new File(clazz.getProtectionDomain().
				getCodeSource().getLocation().getPath())).getParentFile();
	}
	
	/**
	 * Check if the current instance of the application
	 * is running from a jar package.
	 * 
	 * @return True if running from a jar package, false
	 * 			otherwise
	 */
	
	public static boolean isRunningFromJar(Class<?> clazz)
	{
		String className = clazz.getName().replace('.', '/');
		String classJar = clazz.getResource("/" + className + ".class").toString();
		
		return classJar.startsWith("jar:");
	}

}
