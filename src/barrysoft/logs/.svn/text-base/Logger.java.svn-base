/*
 * JSubsGetter
 * 
 * "$Id: Logger.java 74 2009-09-29 19:50:38Z Mk $"
 * 
 * Revision: $Revision: 74 $
 * Author: $Author: Mk $
 * Revision date: $Date: 2009-09-29 21:50:38 +0200 (Tue, 29 Sep 2009) $
 * Modified by: $LastChangedBy: Mk $
 * Last Modified: $LastChangedDate: 2009-09-29 21:50:38 +0200 (Tue, 29 Sep 2009) $
 * Source: $URL: svn://localhost/jsubsgetter/branches/JSubsGetterGUI/com/jsubsgetter/barrysoft/logs/Logger.java $
 * 
 */

package barrysoft.logs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger{

	private static LoggerObj currentLogger;
	private static int	verboseLevel;
	
	public final static int LOWEST_VERBOSITY = 0;
	public final static int LOW_VERBOSITY = 1;
	public final static int MEDIUM_VERBOSITY = 2;
	public final static int HIGH_VERBOSITY = 3;
	public final static int HIGHEST_VERBOSITY = 4;
	
	public static void setLogger(LoggerObj logger) {
		Logger.currentLogger = logger;
	}
	
	public static LoggerObj getLogger() {
		return Logger.currentLogger;
	}
	
	public static void critical(String cri, int status) {
		Logger.currentLogger.critical(cri, status);
	}
	
	public static void error(String err) {
		Logger.currentLogger.error(err);
	}
	
	public static void warning(String warn) {
		Logger.currentLogger.warning(warn);
	}
	
	public static void notice(String not) {
		Logger.currentLogger.notice(not);
	}
	
	
	public static void debug(String dbg, int level) {
		if (level <= getVerboseLevel())
			Logger.currentLogger.debug(dbg);
	}
	
	public static void debug(String dbg) {
		Logger.debug(dbg,LOWEST_VERBOSITY);
	}
	
	public static void log(String str) {
		Logger.currentLogger.log(str);
	}
	
	public static void log(String str, boolean ret) {
		Logger.currentLogger.log(str,ret);
	}
	
	public static void logToFile(String filename, String str) {
		logToFile(filename,str,false);
	}
	
	public static void logToFile(String filename, String str, boolean append) {
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter(filename,append));
	        out.write(str);
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isDebug() {
		return Logger.currentLogger.isDebug();
	}

	public static void setDebug(boolean debug) {
		Logger.currentLogger.setDebug(debug);
	}

	public static int getVerboseLevel() {
		return verboseLevel;
	}

	public static void setVerboseLevel(int verboseLevel) {
		Logger.verboseLevel = verboseLevel;
	}
	
}
