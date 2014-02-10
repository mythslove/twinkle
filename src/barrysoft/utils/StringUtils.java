package barrysoft.utils;

import java.text.DecimalFormat;

public class StringUtils {
	
	private static final String[] tensNames = {
	    "",
	    " ten",
	    " twenty",
	    " thirty",
	    " forty",
	    " fifty",
	    " sixty",
	    " seventy",
	    " eighty",
	    " ninety"
	  };

	  private static final String[] numNames = {
	    "",
	    " one",
	    " two",
	    " three",
	    " four",
	    " five",
	    " six",
	    " seven",
	    " eight",
	    " nine",
	    " ten",
	    " eleven",
	    " twelve",
	    " thirteen",
	    " fourteen",
	    " fifteen",
	    " sixteen",
	    " seventeen",
	    " eighteen",
	    " nineteen"
	  };
	  
		private static final String[] ordinals = {
		    "",
		    "first",
		    "second",
		    "third",
		    "fourth",
		    "fifth",
		    "sixth",
		    "seventh",
		    "eighth",
		    "ninth",
		    "tenth",
		    "eleventh",
		    "twelfth",
		    "thirteenth",
		    "fourteenth",
		    "fifteenth",
		    "sixteenth",
		    "seventeenth",
		    "eighteenth",
		    "nineteenth",
		    "twentieth"
		  };
	  
	public static String capitalizeFirstLetters ( String s ) {

	    for (int i = 0; i < s.length(); i++) {

	        if (i == 0) {
	            // Capitalize the first letter of the string.
	            s = String.format( "%s%s",
	                         Character.toUpperCase(s.charAt(0)),
	                         s.substring(1) );
	        }

	        // Is this character a non-letter or non-digit?  If so
	        // then this is probably a word boundary so let's capitalize
	        // the next character in the sequence.
	        if (!Character.isLetterOrDigit(s.charAt(i))) {
	            if (i + 1 < s.length()) {
	                s = String.format( "%s%s%s",
	                             s.subSequence(0, i+1),
	                             Character.toUpperCase(s.charAt(i + 1)),
	                             s.substring(i+2) );
	            }
	        }

	    }

	    return s;

	}
	
	public static String camelCaseToNormal(String camelCaseText, boolean capitalize)
	{
		String normalText = new String();
		
		for (int i=0; i < camelCaseText.length(); i++)
		{
			if (Character.isUpperCase(camelCaseText.charAt(i)))
				normalText += " " + Character.toLowerCase(camelCaseText.charAt(i));
			else
				normalText += camelCaseText.charAt(i);
		}
		
		if (capitalize)
			normalText = capitalizeFirstLetters(normalText);
		
		return normalText;
	}
	
	public static String getPath( String s ) {
		
		return s.replaceAll("(.+)"+System.getProperty("file.separator")+".+", "$1");
		
	}
	
	public static String getFilename( String s ) {
		
		return s.replaceAll(".*"+System.getProperty("file.separator")+"(.+)", "$1");
		
	}
	
	public static String replaceFileExtension( String s, String ext ) {
		
		return StringUtils.getFilename(s).replaceAll("(.+)\\..+", "$1").trim() + "."+ext;
	}
	
	public static String convertNumberToWord(long number) {
	    // 0 to 999 999 999 999
		if (number == 0) { return "zero"; }
	
		String snumber = Long.toString(number);
	
		// pad with "0"
		String mask = "000000000000";
		DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(number);
	
		// XXXnnnnnnnnn 
		int billions = Integer.parseInt(snumber.substring(0,3));
		// nnnXXXnnnnnn
		int millions  = Integer.parseInt(snumber.substring(3,6)); 
		// nnnnnnXXXnnn
		int hundredThousands = Integer.parseInt(snumber.substring(6,9)); 
		// nnnnnnnnnXXX
		int thousands = Integer.parseInt(snumber.substring(9,12));    
	
		String tradBillions;
		switch (billions) {
		case 0:
		  tradBillions = "";
		  break;
		case 1 :
		  tradBillions = convertLessThanOneThousand(billions) 
		  + " billion ";
		  break;
		default :
		  tradBillions = convertLessThanOneThousand(billions) 
		  + " billion ";
		}
		String result =  tradBillions;
	
		String tradMillions;
		switch (millions) {
		case 0:
		  tradMillions = "";
		  break;
		case 1 :
		  tradMillions = convertLessThanOneThousand(millions) 
		  + " million ";
		  break;
		default :
		  tradMillions = convertLessThanOneThousand(millions) 
		  + " million ";
		}
		result =  result + tradMillions;
	
		String tradHundredThousands;
		switch (hundredThousands) {
		case 0:
		  tradHundredThousands = "";
		  break;
		case 1 :
		  tradHundredThousands = "one thousand ";
		  break;
		default :
		  tradHundredThousands = convertLessThanOneThousand(hundredThousands) 
		  + " thousand ";
		}
		result =  result + tradHundredThousands;
	
		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands);
		result =  result + tradThousand;
	
		// remove extra spaces!
		return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}
	
	private static String convertLessThanOneThousand(int number) {
	    
		String soFar;
	
	    if (number % 100 < 20){
	      soFar = numNames[number % 100];
	      number /= 100;
	    }
	    else {
	      soFar = numNames[number % 10];
	      number /= 10;
	
	      soFar = tensNames[number % 10] + soFar;
	      number /= 10;
	    }
	    
	    if (number == 0) return soFar;
	    return numNames[number] + " hundred" + soFar;
	}

	public static String getOrdinalFor(int value) {
		 int hundredRemainder = value % 100;
		 int tenRemainder = value % 10;
		 if(hundredRemainder - tenRemainder == 10) {
		  return "th";
		 }
		 
		 switch (tenRemainder) {
		  case 1:
		   return "st";
		  case 2:
		   return "nd";
		  case 3:
		   return "rd";
		  default:
		   return "th";
		 }
	}

	public static String getOrdinalString(int value) {
		if (value < ordinals.length) return ordinals[value];
		else return convertNumberToWord(value)+getOrdinalFor(value);
	}
	
	public static String repeatString(String str, int times) {
		String r = new String("");
		
		for (int i=0; i < times; i++)
			r += str;
		
		return r;
	}
	
	public static String getSeparator(String separator, String text) {
		return repeatString(separator,text.length()/separator.length());
	}
	
	public static String stripHTML(String str) {
		str = str.replaceAll("<br\\s*/*>","\n");
		//str = str.replaceAll("\\<.*?>.*?\\</.*?>", "");
		str = str.replaceAll("&#\\d+?;","");
		str = str.replaceAll("\\&.+?\\;", "");
		str = str.replaceAll("\\<.*?>","");
		
		return str;
	}
	
	public static String encodeXML(String str) {
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		
		return str;
	}
	
	public static String sanitizeForRegex(String str) {
		
		str = str.replaceAll("[^\\\\]\\(", "\\\\(");
		str = str.replaceAll("[^\\\\]\\)", "\\\\)");
		str = str.replaceAll("[^\\\\]\\[", "\\\\[");
		str = str.replaceAll("[^\\\\]\\]", "\\\\]");
		str = str.replaceAll("[^\\\\]\\.", "\\\\.");
		str = str.replaceAll("[^\\\\]\\*", "\\\\*");
		str = str.replaceAll("[^\\\\]\\?", "\\\\?");
		str = str.replaceAll("[^\\\\]\\$", "\\\\$");
		str = str.replaceAll("[^\\\\]\\^", "\\\\^");
		str = str.replaceAll("[^\\\\]\\+", "\\\\+");
		str = str.replaceAll("[^\\\\]\\-", "\\\\-");
		
		return str;
	}
}
