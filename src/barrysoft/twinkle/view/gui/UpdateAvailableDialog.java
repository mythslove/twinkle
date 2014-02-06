package barrysoft.twinkle.view.gui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import barrysoft.gui.GUIEvent;
import barrysoft.resources.ResourcesManager;
import barrysoft.twinkle.UpdateRequest;
import barrysoft.twinkle.UpdateVersion;
import barrysoft.twinkle.view.UpdaterEventType;

public class UpdateAvailableDialog extends JDialog 
{
	private static final long serialVersionUID = -7287483142094325248L;
	
	private static final Dimension MIN_SIZE = new Dimension(600, 400);

	private final JLabel		icon = new JLabel();
	private final JLabel		subtitle = new JLabel();
	private final JLabel		versionInfo = new JLabel();
	private final JTextPane 	releaseNotes = new JTextPane();
	private final JCheckBox		automaticallyDownload = new JCheckBox();	
	
	private UpdateVersion version;
	private UpdateRequest source;
	
	public UpdateAvailableDialog(Action install, Action skipVersion)
	{
		//TODO: Localization
		
		setMinimumSize(MIN_SIZE);
		setTitle("Software Update");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		enableEvents(GUIEvent.EVENT_ID);
		
		releaseNotes.setContentType("text/html");
		releaseNotes.setEditable(false);
		releaseNotes.setOpaque(true);
		releaseNotes.setBackground(Color.white);
		
		icon.setIcon(new ImageIcon(ResourcesManager.getResources().
				getIconURL("software-update-available")));
		
		JPanel contentPanel = new JPanel(new GridBagLayout());
		
		JPanel topPanel = new JPanel(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.WEST;
		g.insets = new Insets(10, 0, 0, 0);
		g.gridy = 0;
		topPanel.add(subtitle, g);
		
		g.insets = new Insets(0, 0, 0, 0);
		g.gridy = 1;
		topPanel.add(versionInfo, g);
		 
		g.insets = new Insets(10, 0, 0, 0);
		g.gridy = 2;
		topPanel.add(new JLabel("<html><b><small>Release notes:</small></b></html>"),
				g);
		
		JPanel bottomPanel = new JPanel(new GridBagLayout());
	
		automaticallyDownload.setText("Automatically download and install updates next time");
		
		automaticallyDownload.setSelected(false);
				
		GridBagConstraints g2 = new GridBagConstraints();
		g2.fill = GridBagConstraints.HORIZONTAL;
		g2.insets = new Insets(0, 0, 10, 0);
		g2.gridwidth = 2;
		g2.gridx = 0;
		g2.gridy = 0;
		g2.gridwidth = 3;
		g2.weightx = 100;
		
		bottomPanel.add(automaticallyDownload, g2);
		
		JButton skipVersionButton = new JButton(skipVersion);
		skipVersionButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		
		GridBagConstraints g3 = new GridBagConstraints();
		g3.anchor = GridBagConstraints.WEST;
		g3.gridx = 0;
		g3.gridy = 1;
		bottomPanel.add(skipVersionButton, g3);
		
		JButton remaindLaterButton = new JButton("Remind me later");
		remaindLaterButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		
		g3.anchor = GridBagConstraints.EAST;
		g3.gridx = 2;
		bottomPanel.add(remaindLaterButton, g3);
		
		g3.anchor = GridBagConstraints.EAST;
		g3.gridx = 3;
		bottomPanel.add(new JButton(install), g3);
		
		GridBagConstraints g4 = new GridBagConstraints();
		g4.anchor = GridBagConstraints.WEST;
		g4.insets = new Insets(10, 10, 5, 10);
		g4.gridy = 0;
		
		contentPanel.add(topPanel, g4);
		
		g4.anchor = GridBagConstraints.CENTER;
		g4.fill = GridBagConstraints.BOTH;
		g4.insets = new Insets(5, 10, 5, 10);
		g4.gridy = 1;
		g4.weightx = 100;
		g4.weighty = 100;
		contentPanel.add(new JScrollPane(releaseNotes), 
				g4);
		
		g4.anchor = GridBagConstraints.SOUTH;
		g4.fill = GridBagConstraints.HORIZONTAL;
		g4.insets = new Insets(5, 10, 10, 10);
		g4.gridy = 2;
		g4.weightx = 0;
		g4.weighty = 0;
		contentPanel.add(bottomPanel, g4);
		
		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints g5 = new GridBagConstraints();
		g5.anchor = GridBagConstraints.NORTHWEST;
		g5.fill = GridBagConstraints.NONE;
		g5.insets = new Insets(10, 10, 10, 10);
		g5.gridx = 0;
		getContentPane().add(icon, g5);

		GridBagConstraints g6 = new GridBagConstraints();
		g6.anchor = GridBagConstraints.CENTER;
		g6.fill = GridBagConstraints.BOTH;
		g6.insets = new Insets(0, 0, 0, 0);
		g6.weightx = 100;
		g6.weighty = 100;
		g6.gridx = 1;
		getContentPane().add(contentPanel, g6);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		pack();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void processEvent(AWTEvent event)
	{
		if (event instanceof GUIEvent)
		{
			GUIEvent<UpdaterEventType> ue = (GUIEvent<UpdaterEventType>)event;
			
			switch(ue.getType()) {
			case NEW_VERSION_FOUND:				
				UpdateVersion version = ue.getDataItem(0, UpdateVersion.class);
				UpdateRequest source = ue.getDataItem(1, UpdateRequest.class);
				
				setUpdateVersion(version, source);
				break;
				
			case CHECKING_COMPLETED:
				setVisible(true);
				break;
				
			case DOWNLOAD_STARTED:
				setVisible(false);
				dispose();
				break;
			
			case UPDATE_COMPLETED:
				setVisible(false);
				dispose();
				break;
				
			case ERROR_OCCURRED:
				setVisible(false);
				dispose();
				break;
				
			default:
				new RuntimeException("Invalid type: "+ue.getType().toString());
			}
		}
		else
		{
			super.processEvent(event);
		}
	}
	
	public boolean isAlwaysDownload()
	{
		return automaticallyDownload.isSelected();
	}
	
	public void setUpdateVersion(UpdateVersion version, UpdateRequest source)
	{
		this.version = version;
		this.source = source;
		
		subtitle.setText(String.format(
			"<html><b>A new version of %s is available!</b></html>",
			source.getApplicationInfo().getSoftwareName()));
		
		String newVersion;
		String currentVersion = source.getApplicationInfo().getVersion();
		
		if (version.getShortVersion() == null) {
			newVersion = version.getVersion();
		} else {
			newVersion = version.getShortVersion() + " ("+version.getVersion()+")";
			currentVersion += " ("+source.getComparableVersion()+")";
		}
		
		versionInfo.setText(String.format(
			"<html>%s %s is now available; you have version %s.<br>Do you wish to update now?</html>",
			source.getApplicationInfo().getSoftwareName(),
			newVersion,
			currentVersion));
		
		try {
			releaseNotes.setPage(version.getReleaseNotesLink());
		} catch (IOException e) {
			releaseNotes.setText("<html><h1>Error while opening the page</h1></html>");
		}
	}

	public UpdateRequest getUpdateSource()
	{
		return source;
	}

	public UpdateVersion getUpdateVersion()
	{
		return version;
	}

	public static void main(String... args){

		Action mockInstallAction = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Do nothing, this is just for testing
			}
		};
		mockInstallAction.putValue(Action.NAME, "Install");

		Action mockSkipVersionAction = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Do nothing, this is just for testing
			}
		};
		mockSkipVersionAction.putValue(Action.NAME, "Skip this version");

		UpdateAvailableDialog dialog = new UpdateAvailableDialog(mockInstallAction, mockSkipVersionAction);
		dialog.setVisible(true);
	}

}
