/*
 * JSubsGetter
 * 
 * "$Id: LoggerObj.java 74 2009-09-29 19:50:38Z Mk $"
 * 
 * Revision: $Revision: 74 $
 * Author: $Author: Mk $
 * Revision date: $Date: 2009-09-29 21:50:38 +0200 (Tue, 29 Sep 2009) $
 * Modified by: $LastChangedBy: Mk $
 * Last Modified: $LastChangedDate: 2009-09-29 21:50:38 +0200 (Tue, 29 Sep 2009) $
 * Source: $URL: svn://localhost/jsubsgetter/branches/JSubsGetterGUI/com/jsubsgetter/barrysoft/logs/LoggerObj.java $
 * 
 */

package barrysoft.logs;

public interface LoggerObj {

	void critical(String cri, int status);
	void error(String err);
	void warning(String warn);
	void debug(String dbg);
	void notice(String not);
	void log(String str);
	void log(String str, boolean ret);
	void logToFile(String filename, String str);
	void logToFile(String filename, String str, boolean append);
	boolean isDebug();
	void setDebug(boolean debug);
	
}
