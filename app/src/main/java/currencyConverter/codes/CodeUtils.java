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

    public static List<CurrencyCode> getAvailableCurrencyCodes(boolean sortByName) {
    	ArrayList<CurrencyCode> codeList = new ArrayList<CurrencyCode>();

	    for (com.neovisionaries.i18n.CurrencyCode currency : com.neovisionaries.i18n.CurrencyCode.values()) {
		    codeList.add(new CurrencyCode(currency));
	    }

//	    if(sortByName) {
//		    Collections.sort(codeList);
//	    }

	    return codeList;
    }

	public static List<CountryCode> getAvailableCountryCodes(boolean sortByName) {
		ArrayList<CountryCode> codeList = new ArrayList<CountryCode>();

		for (com.neovisionaries.i18n.CountryCode country : com.neovisionaries.i18n.CountryCode.values()) {
			codeList.add(new CountryCode(country.getAlpha2()));
		}

//	    if(sortByName) {
//		    Collections.sort(codeList);
//	    }

		return codeList;
	}
}
