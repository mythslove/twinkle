/*
 * JSubsGetter
 * 
 * "$Id: LoggerTextual.java 74 2009-09-29 19:50:38Z Mk $"
 * 
 * Revision: $Revision: 74 $
 * Author: $Author: Mk $
 * Revision date: $Date: 2009-09-29 21:50:38 +0200 (Tue, 29 Sep 2009) $
 * Modified by: $LastChangedBy: Mk $
 * Last Modified: $LastChangedDate: 2009-09-29 21:50:38 +0200 (Tue, 29 Sep 2009) $
 * Source: $URL: svn://localhost/jsubsgetter/branches/JSubsGetterGUI/com/jsubsgetter/barrysoft/logs/LoggerTextual.java $
 * 
 */

package barrysoft.logs;

public class EmptyLogger implements LoggerObj {

	private boolean Debug = true;

	public void critical(String cri, int status) {
		System.exit(status);
	}
	
	public void error(String err) {
		//
	}
	
	public void warning(String warn) {
		//
	}
	
	public void notice(String not) {
		//
	}
	
	public void debug(String dbg) {
		//
	}
	
	public void log(String str) {
		//
	}
	
	public void log(String str, boolean ret) {
		//
	}
	
	public void logToFile(String filename, String str) {
		logToFile(filename,str,false);
	}
	
	public void logToFile(String filename, String str, boolean append) {

	}
	
	public boolean isDebug() {
		return Debug;
	}

	public void setDebug(boolean debug) {
		Debug = debug;
	}
	
}
