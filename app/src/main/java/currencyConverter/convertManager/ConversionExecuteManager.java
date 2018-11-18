package currencyConverter.convertManager;
import currencyConverter.service.CountryProvider;

public class ConversionExecuteManager {

    public static double parseRecognizedStringToDouble(String _recognizedString)
    {
        String [] masOfStrings = _recognizedString.split("");

        String resultValue = "";

        for(int i = 1; i < masOfStrings.length; i++)
        {
            if (isNumber(masOfStrings[i]))
                resultValue += masOfStrings[i];

            if( masOfStrings[i].equals(".") || masOfStrings[i].equals(","))
                resultValue += ".";
        }

        return Double.parseDouble(resultValue);
    }

    public static boolean isNumber(String str)
    {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }
}
