package barrysoft.interaction;

public interface Interaction {

	String  inputString(String prompt);
	Integer inputInteger(String prompt);
	
	boolean	askQuestion(String prompt);
	int		askChoice(String prompt, String[] choices, int initial);
	
}
