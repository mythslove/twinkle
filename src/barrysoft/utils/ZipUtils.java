package barrysoft.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import barrysoft.logs.Logger;

public class ZipUtils {

    public static ArrayList<String> unzipArchive(File archive, File outputDir) {
    	
    	ArrayList<String> list = new ArrayList<String>();
    	
        try {
            ZipFile zipfile = new ZipFile(archive);
            for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                list.add(unzipEntry(zipfile, entry, outputDir));
            }
        } catch (Exception e) {
            Logger.error("Error while extracting file " + archive);
        }
        
        return list;
    }

    private static String unzipEntry(ZipFile zipfile, ZipEntry entry, File outputDir) throws IOException {

        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return "";
        }

        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()){
            createDir(outputFile.getParentFile());
        }

        Logger.debug("Extracting: " + entry);
        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {
            copy(inputStream, outputStream);
        } finally {
            outputStream.close();
            inputStream.close();
        }
        
        return entry.toString();
    }

    private static void createDir(File dir) {
        Logger.debug("Creating dir "+dir.getName());
        if(!dir.mkdirs()) throw new RuntimeException("Can not create dir "+dir);
    }
    
    public static void copy(InputStream in, OutputStream out)
    						throws IOException
    {
	    if (in == null)
	    	throw new NullPointerException("InputStream is null!");
	    
	    if (out == null)
	    	throw new NullPointerException("OutputStream is null");

	    byte[] buf = new byte[1024];
	    
	    int len;
	    while ((len = in.read(buf)) > 0) {
	    	out.write(buf, 0, len);
	    }
	    
	    in.close();
	    out.close();
    }
}

