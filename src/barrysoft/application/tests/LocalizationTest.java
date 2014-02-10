package barrysoft.application.tests;

import java.util.Hashtable;

import junit.framework.TestCase;
import barrysoft.localization.Localization;

public class LocalizationTest extends TestCase {

	public void testLocalization() {
		
		Localization localization = new Localization();
		
		Hashtable<String, String> values = new Hashtable<String, String>();
		
		values.put("TEST1", "This is a string[br]yeah");
		
		localization.addLocalization("English", values);
		localization.setCurrentLocalization("English");
		
		String t = localization.getLocalized("TEST1");
		
		if (t.lastIndexOf('\n') == -1)
			fail("Can't find new line");

		
	}
	
}
