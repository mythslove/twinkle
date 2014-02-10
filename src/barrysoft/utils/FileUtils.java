package barrysoft.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import barrysoft.logs.Logger;

public class FileUtils {
	
	public static final String[] SIZE_NAMES = {
		"bytes", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"
	};
	
	public static String getCurrendDir() {
		File f = new File(".");
		try {
			return f.getCanonicalPath();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String bytesToSize(long bytes) {
		return bytesToSize(bytes, 2);
	}
	
	public static String bytesToSize(long bytes, int precision) {
		
		long start;
		for (start = SIZE_NAMES.length-1; start > 0; start--)
		{
			if (bytes >= Math.pow(2, 10 * start))
				break;
		}

		float size = ((float)(bytes / Math.pow(2, 10 * start)));
		
		return String.format("%."+precision+"f %s", size, SIZE_NAMES[(int)start]);
	}
	
	public static String getJarPath(String file) {
		return getJarPath(file, FileUtils.class);
	}
	
	public static String getJarPath(String file, Class<?> mainClass) {
		
		try {
			
			String jarDir = mainClass.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			
			if (jarDir == null)
				jarDir = mainClass.getResource("/.").getPath();

			File f = new File(jarDir);
			
			if (!f.isDirectory())
				jarDir = f.getParentFile().getAbsolutePath();
			else
				jarDir = f.getAbsolutePath();
		
			return OSSpecific.formatDirectory(jarDir)+file;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean fileExists(String filename) {
		File f = new File(filename);
		return f.exists();
	}

	public static boolean copyFile(String srcFileName, String targetFileName) {
		
		boolean fileCopied = false;
		
		InputStream in = null;
		OutputStream out = null;
		
		try {
			
			in = new FileInputStream(srcFileName);
			File newFile = new File(targetFileName);
			out = new FileOutputStream(targetFileName);
			byte[] buffer = new byte[2048];
			
			while (true) {
				synchronized (buffer) {
					int length = in.read(buffer);
					if (length != -1) {
						out.write(buffer, 0, length);
					}
					else
						break;
				}
			}

			fileCopied = newFile.exists();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		finally {
			try{
				if (in != null) { in.close();}
				if (out != null) { out.close(); }
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return fileCopied;
	}
	
	public static boolean moveFile(String srcFileName, String targetFileName) {
		
		File f = new File(srcFileName);
		
		if (!f.renameTo(new File(targetFileName))) {
			
			Logger.debug("Couldn't move the file to "+targetFileName,Logger.LOW_VERBOSITY);
			Logger.debug("Brute-force copy will be done",Logger.LOW_VERBOSITY);
			
			if (!FileUtils.copyFile(srcFileName,targetFileName)) {
				Logger.error("File couldn't be copied to "+targetFileName);
				return false;
			}
			
			if (!f.delete()) {
				Logger.error("Can't delete file "+f.getName());
				return false;
			}
			
		}
		
		return true;
		
	}
			
	public static boolean deleteFile(String filename) {
		File f = new File(filename);
		return f.delete();
	}
	
	public static void getFileLines(String filename, Vector<String> container) 
	throws FileNotFoundException, IOException
	{
			if (container == null)
				return;
			
			File f = new File(filename);
				
			FileReader fr = new FileReader(f);
		    BufferedReader brd = new BufferedReader(fr);
		    
		    String line = "";
		    while (line != null) {
		    	line = brd.readLine();
		    	
		    	if (line != null)
		    		container.add(line);
		    }
		    brd.close();
	}
}
