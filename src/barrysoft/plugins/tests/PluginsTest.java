package barrysoft.plugins.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

import org.junit.Test;

import barrysoft.logs.Logger;
import barrysoft.logs.LoggerTextual;
import barrysoft.options.OptionNotFoundException;
import barrysoft.plugins.Plugin;
import barrysoft.plugins.PluginLoadingException;
import barrysoft.plugins.PluginsManager;
import barrysoft.utils.FileUtils;

public class PluginsTest {
	
	public static final String pluginsDir = "plugins";
	public static final String pluginSpec = "plugin1.spi";
	
	@Test
	public void testLoadPlugin() {
		
		Logger.setLogger(new LoggerTextual());
		
		PluginsManager<TestPlugin> pm = new PluginsManager<TestPlugin>();
		
		File pluginFile = new File(FileUtils.getJarPath(pluginsDir+"/"+pluginSpec, pm.getClass()));
		
		try {
			pm.addPlugin(pluginFile);
		} catch (PluginLoadingException e) {
			fail(String.format("%s, Cause: (%s) %s", e.getMessage(), 
					e.getCause().getClass().getCanonicalName(),
					e.getCause().getMessage()));
		}
		
		Plugin p = pm.getPlugin(0);
		
		assertEquals("Test Plugin 1", p.getInfo().getName());
		assertEquals(p.getClass().getCanonicalName(), p.getInfo().getJarClass());
		assertEquals("test_plugin.jar", p.getInfo().getJarName());
		assertEquals("Test Type", p.getInfo().getType());
		assertEquals("Plugins Testing Unit", p.getInfo().getHostApplication());
		assertEquals("1.0", p.getInfo().getVersion());
		assertEquals("Daniele Rapagnani", p.getInfo().getAuthor());
		assertEquals("This plugin does nothing and is only used for testing purposes",
					p.getInfo().getDescription());
		
		p.getOptions().setOption("regex", "(?i).*?<h href=\"bla\">.*?");
		
		try {
			for (int i=1; i <= 4; i++)
				assertEquals("test"+i, p.getOptions().getOptionValue("option"+i, String.class));
		} catch (OptionNotFoundException e) {
			fail(e.getMessage());
		}
		
		Vector<String> v = new Vector<String>();
		
		for (int i=0; i < 20; i++) {
			String uuid = UUID.randomUUID().toString();
			
			v.add(uuid);
			
			p.getOptions().setOption("uuid"+(i+1), uuid);
		}
		
		try {
			p.save();
		} catch (IOException e1) {
			fail(e1.getMessage());
		}
		
		pm = new PluginsManager<TestPlugin>();
		
		try {
			pm.addPlugin(pluginFile);
		} catch (PluginLoadingException e) {
			fail(e.getMessage());
		}
		
		p = pm.getPlugin(0);
		
		try {
			for (int i=0; i < 20; i++)
				assertEquals(v.get(i), p.getOptions().getOptionValue("uuid"+(i+1), String.class));
		} catch (OptionNotFoundException e) {
			fail(e.getMessage());
		}
		
		try {
			assertEquals("(?i).*?<h href=\"bla\">.*?",p.getOptions().getOptionValue("regex", String.class));
		} catch (OptionNotFoundException e) {
			fail(e.getMessage());
		}
		
		assertEquals(TestPlugin.EXTERNAL, ((Testable)p).test());
		
	}
	
	@Test
	public void testLoadPlugins() {
		
		Logger.setLogger(new LoggerTextual());
		
		PluginsManager<TestPlugin> pm = new PluginsManager<TestPlugin>();
		pm.setPluginsDirectory(FileUtils.getJarPath(pluginsDir, getClass()));
		
		pm.loadPlugins();
		
		assertEquals(1, pm.getPluginsCount());
				
	}
	
}
