package barrysoft.application.tests;

import static org.junit.Assert.fail;

import java.util.Hashtable;

import org.junit.Test;

import barrysoft.localization.Localization;

public class LocalizationTest {

	@Test
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
