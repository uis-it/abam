package no.uis.abam.util;

@Deprecated
public class NumberValidator {

	public static final String ERROR_NOT_NUMBER = "Input must be a number.";
	public static final String ERROR_INPUT_TO_LONG = "Input is to long.";
	public static final String ERROR_NUMBER_TO_HIGH = "Input is to high";
	public static final String DEFAULT_ERROR_MESSAGE = "";

	public static final int DEFAULT_MAX_LENGTH = 3;
	public static final int DEFAULT_HIGHEST_ALLOWED_NUMBER = 3;
	
	private static String[] allowedIntervalSymbols = {"-", "<", ">", "/", "\\"};
	
	private static int maxLength = DEFAULT_MAX_LENGTH;
	private static int maxValue = DEFAULT_HIGHEST_ALLOWED_NUMBER;	
	private static int[] numbersParsedFromString;
		
	private static String stringToValidate;
	private static String errorMessage = "";
	
	private static String[] inputSplitted;
	
	public NumberValidator() {
					
	}
	
	public static boolean isValid(String stringToValidate) {
		clearErrorMessage();
		NumberValidator.stringToValidate = stringToValidate;
		splitInputString();
		if (!inputIsToLong() && inputIsNumber() && inputIsInRange()) {		
			return true;
		}
		return false;
	}
	
	private static void clearErrorMessage() {
		errorMessage = DEFAULT_ERROR_MESSAGE;		
	}
	
	private static void splitInputString() {
		for (String splitSymbol : allowedIntervalSymbols) {
			if (stringToValidate.contains(splitSymbol)) {				
				inputSplitted = stringToValidate.split(splitSymbol);
				numbersParsedFromString = new int[inputSplitted.length];
				return;
			}
		}
		inputSplitted = new String[]{stringToValidate};
		numbersParsedFromString = new int[inputSplitted.length];
	}
	
	private static boolean inputIsToLong() {
		if (stringToValidate.length() > maxLength) {
			errorMessage = ERROR_INPUT_TO_LONG;
			return true;
		}		
		return false;
	}
	
	private static boolean inputIsNumber() {		
		for (int i = 0; i < inputSplitted.length; i++) {
			try {
				numbersParsedFromString[i] = Integer.parseInt(inputSplitted[i]);
			} catch (NumberFormatException e) {
				if (!inputSplitted[i].isEmpty()) {
					errorMessage = ERROR_NOT_NUMBER;
					return false;
				}				
			}
		}
		return true;
	}
	
	private static boolean inputIsInRange() {
		for (int i = 0; i < numbersParsedFromString.length; i++) {
			if (numbersParsedFromString[i] > maxValue) {
				errorMessage = ERROR_NUMBER_TO_HIGH;
				return false;
			}
		}
		return true;
	}
	
	public static String getErrorMessage() {
		return errorMessage;
	}
	
	public static void setMaxLength(int maxLength) {
		NumberValidator.maxLength = maxLength;
	}

	public static void setMaxValue(int maxValue) {
		NumberValidator.maxValue = maxValue;
	}

}
