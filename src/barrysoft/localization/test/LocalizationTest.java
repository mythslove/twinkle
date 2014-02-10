package barrysoft.localization.test;

import java.util.Hashtable;

import org.junit.Test;

import barrysoft.localization.Localization;

public class LocalizationTest {
	
	@Test
	public void testLocalization() {
		
		Hashtable<String, String> words = new Hashtable<String, String>();
		
		words.put("WORD1", "This is %1 faulty.");
		
		Localization localization = new Localization();
		localization.addLocalization("Italian", words);
		localization.setCurrentLocalization("Italian");
		
		System.out.println(localization.getLocalized("WORD1", "NOT"));
		
	}
	
}
