package barrysoft.interaction;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InteractionTextual implements Interaction {
	
	public String inputString(String prompt) {
		
		InputStreamReader fin = new InputStreamReader(System.in);
		BufferedReader tastiera = new BufferedReader(fin);
		
		System.out.print(prompt+": ");
		
		try {
			
			String str = tastiera.readLine();
			return str;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public Integer inputInteger(String prompt) throws NumberFormatException {
		String str = inputString(prompt);
		return (Integer.parseInt(str));
	}
	
	public boolean askQuestion(String prompt) {
		
		String str = null;
		
		do {
			str = inputString(prompt+" [y/n]");
		} while (!str.equalsIgnoreCase("y") && !str.equalsIgnoreCase("n"));
		
		return (str.equalsIgnoreCase("y") ? true : false);
	}

	public int askChoice(String prompt, String[] choices, int initial) {
		
		System.out.println();
		
		for (int i=0; i < choices.length; i++)
			System.out.println(String.format("%d) %s", i+1, choices[i]));
		
		System.out.println();
		
		try {
			return inputInteger(prompt+" ["+(initial+1)+"]") - 1;
		} catch (NumberFormatException e) {
			return initial;
		}
		
	}

}
