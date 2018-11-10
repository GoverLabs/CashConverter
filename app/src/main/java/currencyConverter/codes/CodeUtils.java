package currencyConverter.codes;

import java.util.List;

import currencyConverter.codes.CountryCode;
import currencyConverter.codes.CurrencyCode;

public class CodeUtils {

    public static CurrencyCode getCurrencyFromCountry(CountryCode countryCode) {
        List<com.neovisionaries.i18n.CurrencyCode> currencyCodeList =
            com.neovisionaries.i18n.CurrencyCode.getByCountryIgnoreCase( countryCode.toStringISO() );

        return new CurrencyCode( currencyCodeList.get(0).getName() );
    }
}
