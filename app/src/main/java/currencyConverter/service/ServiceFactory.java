package currencyConverter.service;

public class ServiceFactory {

	public static ICountryProvider createCountryProvider() {
		return new CountryProvider();
	}

	public static ICurrencyConverter createCurrencyConverter() {
		return CurrencyConverter.getInstance();
	}
}
