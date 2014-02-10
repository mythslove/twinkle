package barrysoft.application.views;

import barrysoft.application.Application;

public abstract class MenuChoice<Type extends Application> {
	private String text = "No text";
	
	public MenuChoice() {}
	public MenuChoice(String text) {
		setText(text);
	}
	
	public void	setText(String text) { this.text = text; }
	public String getText() { return text; }
	public abstract boolean execute(Type app, TextualView<Type> view);
}
