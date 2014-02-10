package barrysoft.logs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import barrysoft.application.ApplicationDebugDisplayer;
import barrysoft.utils.OSSpecific;

public class LoggerSwing implements LoggerObj {

	private boolean debug;
	private ApplicationDebugDisplayer debugFrame;
	
	public LoggerSwing() {
		this(null);
	}
	
	public LoggerSwing(ApplicationDebugDisplayer d) {
		debugFrame = d;
		
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void debug(String dbg) {
		if (!this.debug) return;
		this.log("[Debug] "+dbg);
	}

	public void critical(String cri, int status) {
		JOptionPane.showOptionDialog(null, cri, "Critical Error", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE,null,new Object[]{"Exit"},"Exit");
		System.exit(status);
	}
	
	public void error(String err) {
		if (OSSpecific.isOSX()) {
			JOptionPane.showOptionDialog(null, err, "Error occured", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{"Ok"},"Ok");
		} else {
			JOptionPane.showMessageDialog(null, err, "Error occured", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean isDebug() {
		// TODO Auto-generated method stub
		return debug;
	}

	public void log(String str) {
		log(str,true);
	}

	public void log(String str, boolean ret) {
		// TODO Auto-generated method stub
		if (this.debugFrame != null) {
			this.debugFrame.addMessage(str);
		}
		System.out.println(str);
	}
	

	public void logToFile(String filename, String str, boolean append) {
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter(filename,append));
		    out.write(str);
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		debug("Logfile saved to: "+filename);
	}

	public void logToFile(String filename, String str) {
		logToFile(filename,str,false);
	}

	public void notice(String not) {
		if (OSSpecific.isOSX()) {
			JOptionPane.showOptionDialog(null, not, "Notice", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Ok"},"Ok");
		} else {
			JOptionPane.showMessageDialog(null, not, "Notice", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void setDebug(boolean debug) {
		// TODO Auto-generated method stub
		this.debug = debug;
	}

	public void warning(String warn) {
		// TODO Auto-generated method stub
		if (OSSpecific.isOSX()) {
			JOptionPane.showOptionDialog(null, warn, "Warning", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{"Ok"},"Ok");
		} else {
			JOptionPane.showMessageDialog(null, warn, "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	public ApplicationDebugDisplayer getDebugFrame() {
		return debugFrame;
	}

	public void setDebugFrame(ApplicationDebugDisplayer debugFrame) {
		this.debugFrame = debugFrame;
	}

}
