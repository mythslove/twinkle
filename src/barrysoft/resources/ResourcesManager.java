package barrysoft.resources;

public class ResourcesManager {
	
	private static Resources currentResources;
	
	public static void setResources(Resources currentResources)
	{		
		ResourcesManager.currentResources = currentResources;
	}
	
	public static Resources getResources()
	{
		if (currentResources == null)
		{
			throw new RuntimeException("No resources have been " +
					"set for this ResourceManager.");
		}
		
		return currentResources;
	}

}
