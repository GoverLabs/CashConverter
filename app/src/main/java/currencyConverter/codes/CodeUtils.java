package currencyConverter.codes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import currencyConverter.exception.CurrencyCodeFetchingException;

public class CodeUtils {

    public static CurrencyCode getCurrencyFromCountry(CountryCode countryCode) throws CurrencyCodeFetchingException {
        List<com.neovisionaries.i18n.CurrencyCode> currencyCodeList =
            com.neovisionaries.i18n.CurrencyCode.getByCountryIgnoreCase( countryCode.toStringISO() );

		com.neovisionaries.i18n.CurrencyCode originalCode= currencyCodeList.get(0);

		if(originalCode != null) {
			return new CurrencyCode( originalCode.getName() );
		} else {
			throw new CurrencyCodeFetchingException();
		}
    }

    public static List<String> getAvailableCurrencyCodes(boolean sortByName) {
    	ArrayList<String> codeList = new ArrayList<String>();

	    for (com.neovisionaries.i18n.CurrencyCode currency : com.neovisionaries.i18n.CurrencyCode.values()) {
		    codeList.add(currency.getName());
	    }

	    if(sortByName) {
		    Collections.sort(codeList);
	    }

	    return codeList;
    }
}
