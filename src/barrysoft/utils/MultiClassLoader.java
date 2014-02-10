package barrysoft.utils;

import java.util.Hashtable;

import barrysoft.logs.Logger;

/**
 * A simple test class loader capable of loading from
 * multiple sources, such as local files or a URL.
 *
 * This class is derived from an article by Chuck McManis
 * http://www.javaworld.com/javaworld/jw-10-1996/indepth.src.html
 * with large modifications.
 *
 * Note that this has been updated to use the non-deprecated version of
 * defineClass() -- JDM.
 *
 * @author Jack Harich - 8/18/97
 * @author John D. Mitchell - 99.03.04
 */

public abstract class MultiClassLoader extends ClassLoader {

	private Hashtable<String,Class<?>> 	classes = new Hashtable<String,Class<?>>();
	private char      	classNameReplacementChar;
	
	protected boolean   monitorOn = false;
	protected boolean   sourceMonitorOn = true;
	
	public MultiClassLoader() { }

	/**
	 * This is a simple version for external clients since they
	 * will always want the class resolved before it is returned
	 * to them.
	 */
	
	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
	    return (loadClass(className, true));
	}
	
	@Override
	public synchronized Class<?> loadClass(String className, boolean resolveIt) 
			throws ClassNotFoundException 
	{
	
	    Class<?>   result;
	    
	    Logger.debug("MultiClassLoader: Loading class: " + className 
	    				+ ", Resolving: " + resolveIt,
	    				Logger.HIGH_VERBOSITY);
	
	    try {
	    	result = loadCachedClass(className);
	    } catch (ClassNotFoundException e) {
	    	try {
	    		result = loadExternalClass(className, resolveIt);
	    	} catch (ClassNotFoundException e2) {
	            result = super.findSystemClass(className);
	            
	        	Logger.debug("MultiClassLoader: returning system class (in CLASSPATH).",
	        			Logger.HIGH_VERBOSITY);
	    	}
	    }
	    
        return result;
        
	}
	
	public Class<?> loadCachedClass(String className)
		throws ClassNotFoundException
	{
		Class<?>   	result;
	    result = classes.get(className);
	    
	    if (result == null)
	    	throw new ClassNotFoundException("Can't find cached class: "+className);
	    	
    	Logger.debug("MultiClassLoader: Returning cached result.", 
    			Logger.HIGH_VERBOSITY);
    	
        return result;
	}
	
	public Class<?> loadExternalClass(String className, boolean resolveIt) 
		throws ClassNotFoundException 
	{
		
	    byte[]  	classBytes = loadClassBytes(className);
	    Class<?>   	result;
	    
	    if (classBytes == null)
	    	throw new ClassNotFoundException("Can't load external class: "+className);
	    
	    result = defineClass(className, classBytes, 0, classBytes.length);
	    
	    if (result == null)
	        throw new ClassFormatError();
	
	    if (resolveIt) 
	    	resolveClass(result);
	
	    // Done
	    classes.put(className, result);
	    
	    Logger.debug("MultiClassLoader: Returning newly loaded class.",
	    		Logger.HIGH_VERBOSITY);
    
	    return result;
	    
	}
	
	/**
	 * This optional call allows a class name such as
	 * "COM.test.Hello" to be changed to "COM_test_Hello",
	 * which is useful for storing classes from different
	 * packages in the same retrival directory.
	 * In the above example the char would be '_'.
	 */
	
	public void setClassNameReplacementChar(char replacement) {
	    classNameReplacementChar = replacement;
	}

	protected abstract byte[] loadClassBytes(String className);
	
	protected String formatClassName(String className) {
	    if (classNameReplacementChar == '\u0000') {
	        // '/' is used to map the package to the path
	        return className.replace('.', '/') + ".class";
	    } else {
	        // Replace '.' with custom char, such as '_'
	        return className.replace('.',
	            classNameReplacementChar) + ".class";
	    }
	}

}
