package barrysoft.web;

public class ProgressEvent<T> {
	
	private final T 		source;
	private final String	operation;
	private final int		current;
	private final int		total;
	
	public ProgressEvent(T source)
	{
		this(source, null, 0, -1);
	}
	
	public ProgressEvent(T source, int current)
	{
		this(source, null, current, -1);
	}
	
	public ProgressEvent(T source, String operation, int current, int total)
	{
		this.source = source;
		this.operation = operation;
		this.current = current;
		this.total = total;
	}

	public int getCurrent() {
		return current;
	}

	public String getOperation() {
		return operation;
	}

	public T getSource() {
		return source;
	}

	public int getTotal() {
		return total;
	}

}
