package barrysoft.application;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class ApplicationWindowGrid extends ApplicationWindow {

	private static final long serialVersionUID = 374374612021477311L;

	public ApplicationWindowGrid(String name, String title) {
		super(name, title);
	}
	
	public ApplicationWindowGrid(String name, String title, int w, int h) {
		super(name, title,w ,h);
	}
	
	@Override
	public void createGui() {
		super.createGui();
		
		Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
	}
	
	public void addComponent(Component c, int x, int y) {
		addComponent(c,x,y,1,1);
	}
	
	public void addComponent(Component c, int x, int y, int w, int h) {
		addComponent(c,x,y,w,h,GridBagConstraints.HORIZONTAL, GridBagConstraints.NONE,0 ,0);
	}
	
	public void addComponent(Component c, int x, int y, int w, int h, 
							 int fill, int anchor, int padx, int pady) {
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = fill;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.insets = new Insets(5,5,5,5);
		gbc.ipadx = padx;
		gbc.ipady = pady;
		
		getContentPane().add(c,gbc);
		
	}

}
