package barrysoft.application.views;

import java.util.Vector;

import barrysoft.application.Application;
import barrysoft.application.ApplicationInfo;
import barrysoft.interaction.InteractionTextual;
import barrysoft.logs.Logger;
import barrysoft.logs.LoggerTextual;
import barrysoft.utils.StringUtils;

public class TextualView<AppType extends Application> extends View<AppType> {
	
	private Vector<MenuChoice> 	menu = new Vector<MenuChoice>();
	private Vector<MenuChoice> 	menuDisplay = new Vector<MenuChoice>();
	
	private boolean busy = false;
	
	public TextualView(AppType parent) {
		super(parent);
		
		Logger.setLogger(new LoggerTextual());
		Logger.setVerboseLevel(Logger.LOWEST_VERBOSITY);
		
		getParent().setInteractor(new InteractionTextual());
	}
	
	public void printHeader() {
		String text;
		
		text = getParent().getInfo().getSoftwareName()+" v"+getParent().getInfo().getVersion();
		
		if (!getParent().getInfo().getBuildNumber().isEmpty())
			text += " build "+getParent().getInfo().getBuildNumber();
		
		if (!getParent().getInfo().getTimeStamp().isEmpty())
			text += " ("+getParent().getInfo().getTimeStamp()+")";
		
		Logger.log(StringUtils.getSeparator("-", text));
		Logger.log(text);
		Logger.log(StringUtils.getSeparator("-", text));
		Logger.log("");
	}
	
	public void addMenuItem(String text, MenuChoice<AppType> choice) {
		choice.setText(text);
		addMenuItem(choice);
	}
	
	
	public void addMenuItem(MenuChoice<AppType> choice) {
		menu.add(choice);
		menuDisplay.add(choice);
	}
	
	public void addMenuSeparator() {
		menuDisplay.add(new MenuChoice<AppType>("#") {
			@Override
			public boolean execute(AppType app, TextualView<AppType> view) {
				return true;
			}
			
		});
	}
	
	public void printMenu() {
		
		Logger.log(StringUtils.repeatString("=", 40));
		Logger.log("\nMenu:\n");
		
		int count = 0;
		
		for (int i=0; i < menuDisplay.size(); i++) {
			if (menuDisplay.get(i).getText().equals("#"))
				Logger.log("   "+StringUtils.repeatString("-", 20));
			else {
				Logger.log("   "+(++count)+") "+menuDisplay.get(i).getText());
			}
		}
		
		Logger.log("");
		Logger.log(StringUtils.repeatString("=", 40));
		Logger.log("");
	}
	
	public MenuChoice getMenuChoice() throws InvalidMenuChoiceException {
		
		int selected = 0;
		
		try {
			selected = getParent().getInteractor().inputInteger("Select an action");
		} catch (NumberFormatException nfe) {
			throw new InvalidMenuChoiceException("Please enter a numerical value");
		}
		
		if (selected < 1 || selected > getMenuItemsCount())
			throw new InvalidMenuChoiceException(selected);
		
		return menu.get(selected-1);
	}
	
	@SuppressWarnings("unchecked")
	public void doMenu() {
		
		MenuChoice m;
		
		// Display the menu
		printMenu();
		
		// Get the user input and retry if not valid
		while(true) {
			try {
				m = getMenuChoice();
				Logger.debug("Choice: "+m.getText(),Logger.HIGH_VERBOSITY);
				
				// Execute the desired action
				if (!m.execute(getParent(), this)) {
					Logger.error("Operation failed.");
				}
				
				while (isBusy());
			} catch(InvalidMenuChoiceException ic) {
				printError(ic.getMessage());
			}
		}
		
	}
	
	public int getMenuItemsCount() {
		return menu.size();
	}
	
	public void printError(String error) {
		printResponse("Error, "+error);
	}
	
	public void printResponse(String msg) {
		Logger.log(" "+msg);
	}
	
	public void printList(String[] list, int indentation) {
		for (int i=0; i < list.length; i++) {
			printResponse(StringUtils.repeatString(" ", indentation)+"-"+list[i]);
		}
	}


	@Override
	public void onInitialization(Class mainClass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		printHeader();
		doMenu();
	}

	@Override
	public void onInitialized(Class mainClass) {
		// TODO Auto-generated method stub
		
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

}
