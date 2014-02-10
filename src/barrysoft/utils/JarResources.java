package barrysoft.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import barrysoft.logs.Logger;

/**
 * JarResources: JarResources maps all resources included in a
 * Zip or Jar file. Additionaly, it provides a method to extract one
 * as a blob.
 */
public final class JarResources {

    // jar resource mapping tables
    private Hashtable<String, Integer> 	htSizes;
    private Hashtable<String, byte[]> 	htJarContents;

    // a jar file
    private String jarFileName;

    /**
     * Creates a JarResources.<br>
     * It extracts all resources from a Jar
     * into an internal hashtable, keyed by resource names.
     * 
     * @param jarFileName a jar or zip file
     * 
     * @throws IOException 
     */
    
    public JarResources(String jarFileName) throws IOException {
		
    	htSizes = new Hashtable<String, Integer>();
    	htJarContents = new Hashtable<String, byte[]>();
    	
    	this.jarFileName=jarFileName;
    	
		init();
	}

    /**
     * Extracts a jar resource as a blob.
     * 
     * @param name a resource name.
     * 
     * @return The requested resource as a byte
     * 			array.
     * 
     * @throws NotFoundException If the requeste
     * 				resource could not be found
     */
    
    public byte[] getResource(String name) throws MissingResourceException {
    	
    	byte[] resource = htJarContents.get(name);
    	
    	if (resource == null)
    		throw new MissingResourceException("Can't find requested resource", name, "jar");
    	
    	return resource;
	}

    /** 
     * Initializes the internal hash tables 
     * with the Jar resource files. 
     *  
     * @throws IOException 
     */
    
    private void init() throws IOException {
    	
		try {
			
			// extracts just sizes only. 
		    ZipFile zf=new ZipFile(jarFileName);
		    Enumeration<? extends ZipEntry> e=zf.entries();
		    
		    while (e.hasMoreElements()) {
		    	
				ZipEntry ze=e.nextElement();
		
				Logger.debug(dumpZipEntry(ze), Logger.HIGH_VERBOSITY);
		
				htSizes.put(ze.getName(),new Integer((int)ze.getSize()));
			}
		    
		    zf.close();
	
		    // extract resources and put them into the hashtable.
		    FileInputStream fis=new FileInputStream(jarFileName);
		    BufferedInputStream bis=new BufferedInputStream(fis);
		    ZipInputStream zis=new ZipInputStream(bis);
		    ZipEntry ze=null;
		    
		    while ((ze=zis.getNextEntry())!=null) {
			
		    	if (ze.isDirectory())
		    		continue;
	
		    	Logger.debug("ze.getName()="+ze.getName()+
						       ","+"getSize()="+ze.getSize(),
						       Logger.HIGH_VERBOSITY);
	
				int size=(int)ze.getSize();
				
				// -1 means unknown size.
				if (size==-1)
				    size=htSizes.get(ze.getName()).intValue();
		
				byte[] b=new byte[size];
				int rb=0;
				int chunk=0;
				
				while ((size - rb) > 0) {
				    chunk=zis.read(b,rb,size - rb);
				    
				    if (chunk==-1)
				    	break;
				    
				    rb+=chunk;
				}
		
				// add to internal resource hashtable
				htJarContents.put(ze.getName(),b);
		
				Logger.debug(ze.getName()+"  rb="+rb+
							",size="+size+
							",csize="+ze.getCompressedSize(),
							Logger.HIGH_VERBOSITY);
			}
		    
		    zis.close();
		    
		} catch (NullPointerException e) {
			return;
		}
	}

	/**
	 * Dumps a zip entry into a string.
	 * 
	 * @param ze a ZipEntry
	 * 
	 * @return String representation of the zip
	 */
    
	private String dumpZipEntry(ZipEntry ze) {
		
		StringBuffer sb=new StringBuffer();
		
		if (ze.isDirectory())
		    sb.append("d ");
		else
			sb.append("f ");
	
		if (ze.getMethod()==ZipEntry.STORED)
		    sb.append("stored   ");
		else
		    sb.append("defalted ");
	
		sb.append(ze.getName());
		sb.append("\t");
		sb.append(""+ze.getSize());
		
		if (ze.getMethod()==ZipEntry.DEFLATED)
		    sb.append("/"+ze.getCompressedSize());
	
		return (sb.toString());
	}

}
