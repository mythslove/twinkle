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

public class LoggerTextual implements LoggerObj {

	private boolean Debug = true;

	@Override
	public void critical(String cri, int status) {
		this.log("[Critical Error] "+cri);
		System.exit(status);
	}
	
	@Override
	public void error(String err) {
		this.log("[Error] "+err);
	}
	
	@Override
	public void warning(String warn) {
		this.log("[Warning] "+warn);
	}
	
	@Override
	public void notice(String not) {
		this.log("[Notice] "+not);
	}
	
	@Override
	public void debug(String dbg) {
		if (!this.Debug) return;
		this.log("[Debug] "+dbg);
	}
	
	@Override
	public void log(String str) {
		this.log(str,true);
	}
	
	@Override
	public void log(String str, boolean ret) {
		System.out.print(str + (ret ? "\n" : ""));
	}
	
	@Override
	public void logToFile(String filename, String str) {
		logToFile(filename,str,false);
	}
	
	@Override
	public void logToFile(String filename, String str, boolean append) {

	}
	
	@Override
	public boolean isDebug() {
		return Debug;
	}

	@Override
	public void setDebug(boolean debug) {
		Debug = debug;
	}
	
}
