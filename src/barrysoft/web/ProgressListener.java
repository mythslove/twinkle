package barrysoft.web;

public interface ProgressListener<T> {
	
	void progressStart(ProgressEvent<T> event);
	
	void progressUpdate(ProgressEvent<T> event);
	
	void progressFinish(ProgressEvent<T> event);

}
