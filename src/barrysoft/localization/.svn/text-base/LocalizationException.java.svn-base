package barrysoft.localization;

public class LocalizationException extends RuntimeException {
	
	private static final long serialVersionUID = 1401667646395412941L;

	public LocalizationException(String tag) {
		super(String.format("Can't find localized value for '%s'",tag));
	}
	
	public LocalizationException(String tag, Throwable cause) {
		super(String.format("Can't find localized value for '%s'",tag), cause);
	}
	
}
