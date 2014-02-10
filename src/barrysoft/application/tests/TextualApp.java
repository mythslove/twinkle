package barrysoft.application.tests;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import barrysoft.Version;
import barrysoft.application.Application;
import barrysoft.application.ApplicationInfo;
import barrysoft.application.ApplicationInitializationException;
import barrysoft.application.views.MenuChoice;
import barrysoft.application.views.TextualView;

public class TextualApp extends Application {

	ApplicationInfo info;
	
	public TextualApp() {
		
		info = new ApplicationInfo();
		
		setInfo(info);
		
		TextualView<TextualApp> tv = new TextualView<TextualApp>(this);
		
		tv.addMenuItem(new MenuChoice<TextualApp>("A menu item") {
			@Override
			public boolean execute(TextualApp app, TextualView view) {
			
				doStuff();
				
				view.printResponse("Menu was choosen, ok!!");
				
				return true;
			}		
		});
		
		setView(tv);
		
		try {
			initialize(TextualApp.class);
		} catch (ApplicationInitializationException e) {
			e.printStackTrace();
		}
		
		start();
		
	}
	
	public void doStuff() {
		System.out.println("Done some stuff...");
	}

}
