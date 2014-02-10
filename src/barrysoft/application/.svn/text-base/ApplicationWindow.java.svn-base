package barrysoft.application;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

public class ApplicationWindow extends JFrame implements WindowListener, ComponentListener {
	
	private static final long serialVersionUID = 8814818584355097379L;
	
	private String		windowName = ""; 
	private JMenuBar	menuBar = null;
	
	public ApplicationWindow(String name, String title) {
		this(name,title,0,0);
	}
	
	public ApplicationWindow(String name, String title, int w, int h) {
		setWindowName(name);
		initialize(w, h, title);
		addComponentListener(this);
		addWindowListener(this);
	}
	
	public void initialize(int w, int h, String title) {
		
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
        setSize(w, h);
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
	}
	
	public void createGui() {
		
		Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        
	}
	
	public void createMenu() {
		
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
	}
	
	public void addMenu(JMenu menu) {
		if (menuBar != null)
			menuBar.add(menu);
	}
	
	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void componentResized(ComponentEvent e) {
		pack();
		
	}

	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

}
