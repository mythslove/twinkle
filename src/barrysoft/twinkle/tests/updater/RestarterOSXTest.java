package barrysoft.twinkle.tests.updater;

import java.io.File;

import org.junit.Test;

import barrysoft.twinkle.restarter.RestarterOSX;

public class RestarterOSXTest {
	
	private final static String[] paths = {
		"/Applications/JSubsGetter.app/Contents/Resources/JSubsGetter.jar",
		"./asdas/JSubsGetter.app/Contents/Resources/JSubsGetter.jar",
		"JSubsGetter.app/Contents/Resources/JSubsGetter.jar"
	};
	
	@Test
	public void testAppBundleParse()
	{
		for (String path : paths)
			System.out.println(RestarterOSX.getInstance().getAppBundle(new File(path)));
	}

}
