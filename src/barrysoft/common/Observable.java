package barrysoft.common;

public interface Observable<T> {

	public void addObserver(T observer);
	
	public void removeObserver(T observer);
	
}
