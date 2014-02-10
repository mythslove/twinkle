/*
 * JSubsGetter
 * 
 * "$Id: OSSpecific.java 65 2009-09-28 03:00:56Z Mk $"
 * 
 * Revision: $Revision: 65 $
 * Author: $Author: Mk $
 * Revision date: $Date: 2009-09-28 05:00:56 +0200 (Mon, 28 Sep 2009) $
 * Modified by: $LastChangedBy: Mk $
 * Last Modified: $LastChangedDate: 2009-09-28 05:00:56 +0200 (Mon, 28 Sep 2009) $
 * Source: $URL: svn://localhost/jsubsgetter/branches/JSubsGetterGUI/com/jsubsgetter/barrysoft/utils/OSSpecific.java $
 * 
 */

package barrysoft.utils;

public class OSSpecific {

	public static String formatDirectory(String dir) {
		
		if (System.getProperty("os.name").contains("Windows")) {
			if (!dir.endsWith("\\")) dir = dir + "\\";
		} else {
			if (!dir.endsWith("/")) dir = dir + "/";
		}
		
		return dir;
		
	}
	
	public static boolean isOSX() {
		return System.getProperty("os.name").contains("Mac OS X");
	}
	
	public static boolean isWindows() {
		return System.getProperty("os.name").contains("Windows");
	}
	
	public static boolean isLinux() {
		return System.getProperty("os.name").contains("Linux");
	}
	
}
