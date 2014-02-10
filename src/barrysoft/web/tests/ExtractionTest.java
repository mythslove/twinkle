package barrysoft.web.tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import barrysoft.web.ExtractionJob;
import barrysoft.web.ExtractionStep;
import barrysoft.web.Extractor;
import barrysoft.web.ParserRule;
import barrysoft.web.ParserRuleParam;

public class ExtractionTest extends TestCase {
	
	public void testExtract() {
		
		Extractor search = new Extractor();
		
		search.getParser().setName("ITSA Search");
		
		ParserRule inr = new ParserRule("(?i)(?s).*?<a href=\"([^<>]+)\">\\s[series]</a>.*?");
		inr.setQuickRule(" [series]<");
		inr.setGroupName("season link", 0);
		inr.addParam(new ParserRuleParam("series", "How I Met Your Mother"));
		
		search.getParser().addRule(inr);
		try {
			search.getDownloader().setUrl("http://www.italiansubs.net/index.php?option=com_remository");
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
		
		Extractor season = new Extractor();
		
		season.getParser().setName("ITSA Season");
		
		ParserRule outr = new ParserRule("(?i)(?s).*?<a href=\"([^<>]+)\">\\s[season]</a>.*?");
		outr.setQuickRule(" [season]<");
		outr.setGroupName("episodes link", 0);
		//outr.addParam(new ParserRuleParam("season", "Stagione 2"));
		
		season.getParser().addRule(outr);
		
		Extractor episode = new Extractor();
		
		episode.getParser().setName("ITSA Episode");
		
		outr = new ParserRule("(?i)(?s).*?<a href=\"([^<>]+)\">[episode]</a>.*?");
		outr.setQuickRule("[episode]<");
		outr.setGroupName("subtitle link", 0);
		//outr.addParam(new ParserRuleParam("episode", "How I Met Your Mother 2x06"));
		
		episode.getParser().addRule(outr);
		
		ExtractionJob job = new ExtractionJob("Get ITSA subtitle link");
		
		ExtractionStep estep = new ExtractionStep("Get season's page", search, season);
		estep.connect("season link", ExtractionStep.URL_BINDING);
		job.addStep(estep);
		
		estep = new ExtractionStep("Get subtitle's page", season, episode);
		estep.connect("episodes link", ExtractionStep.URL_BINDING);
		job.addStep(estep);
		
		try {
			job.execute();
		} catch (IllegalStateException e) {
			fail(e.getMessage());
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		String[] link;
		try {
			link = episode.getParser().getRule(0).getResults("subtitle link");
			assertEquals(1, link.length);
			System.out.println(link[0]);
		} catch (IndexOutOfBoundsException e) {
			fail(e.getMessage());
		} catch (NoSuchElementException e) {
			fail(e.getMessage());
		}
		
		System.out.println(job.getXML(0));
	}

}
