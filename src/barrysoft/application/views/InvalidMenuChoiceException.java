package barrysoft.application.views;

public class InvalidMenuChoiceException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidMenuChoiceException(String reason) {
		super("Invalid menu choice: "+reason);
	}
	
	public InvalidMenuChoiceException(int number) {
		super("Invalid menu choice: "+number);
	}
		
}
