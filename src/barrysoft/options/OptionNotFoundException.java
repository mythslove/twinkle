package barrysoft.options;

public class OptionNotFoundException extends Exception {
	
	private static final long serialVersionUID = -1889433222239548761L;
	
	public OptionNotFoundException(String name, Class clazz)
	{
		super(String.format("Can't find option with name '%s' of class '%s'",
				name, clazz.getSimpleName()));
	}

}
