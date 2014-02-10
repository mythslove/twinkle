/*
 * JSubsGetter
 * 
 * "$Id: InteractionTextual.java 18 2009-09-16 02:26:59Z Mk $"
 * 
 * Revision: $Revision: 18 $
 * Author: $Author: Mk $
 * Revision date: $Date: 2009-09-16 04:26:59 +0200 (Wed, 16 Sep 2009) $
 * Modified by: $LastChangedBy: Mk $
 * Last Modified: $LastChangedDate: 2009-09-16 04:26:59 +0200 (Wed, 16 Sep 2009) $
 * Source: $URL: svn://localhost/jsubsgetter/branches/JSubsGetterGUI/com/jsubsgetter/barrysoft/interaction/InteractionTextual.java $
 * 
 */

package barrysoft.interaction;

import javax.swing.JOptionPane;

import barrysoft.logs.Logger;

public class InteractionSwing implements Interaction {
	
	public String inputString(String prompt) {
		return JOptionPane.showInputDialog(prompt);		
	}
	
	public Integer inputInteger(String prompt) {
		String str = inputString(prompt);
		return (new Integer(str));
	}
	
	public boolean askQuestion(String prompt) {
		return (JOptionPane.showConfirmDialog(null, prompt, "Question", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION ? true : false);
	}

	public int askChoice(String prompt, String[] choices, int initial) {
		if (initial < 0 || initial >= choices.length) {
			Logger.warning("Wrong initial value passed to ask choice: "+initial+", max is "+choices.length);
			return -1;
		}
		
		return JOptionPane.showOptionDialog(null, prompt, "Please select an option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[initial]);
	}
	
}
