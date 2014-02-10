package barrysoft.twinkle.tests.updater;

import java.io.File;

import junit.framework.TestCase;
import barrysoft.twinkle.restarter.RestarterOSX;

public class RestarterOSXTest extends TestCase {
	
	private final static String[] paths = {
		"/Applications/JSubsGetter.app/Contents/Resources/JSubsGetter.jar",
		"./asdas/JSubsGetter.app/Contents/Resources/JSubsGetter.jar",
		"JSubsGetter.app/Contents/Resources/JSubsGetter.jar"
	};
	
	public void testAppBundleParse()
	{
		for (String path : paths)
			System.out.println(RestarterOSX.getInstance().getAppBundle(new File(path)));
	}

}
