package userData;

import java.io.Serializable;

import currencyConverter.codes.CountryCode;
import currencyConverter.codes.CurrencyCode;

public class UserData implements Serializable {

	public CountryCode currentCountry;
	public CurrencyCode nativeCurrency;
	public CurrencyCode currentCurrency;

	public boolean isAutodetectionEnabled;

	public UserData() {
		this.currentCountry = CountryCode.Undefined;
		this.nativeCurrency = CurrencyCode.Undefined;
		this.currentCurrency = CurrencyCode.Undefined;
		this.isAutodetectionEnabled = true;
	}
}
