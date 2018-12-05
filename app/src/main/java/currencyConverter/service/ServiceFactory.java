package currencyConverter.service;

public class ServiceFactory {

	public static ICountryProvider createCountryProvider() {
		return CountryProvider.getInstance();
	}

	public static ICurrencyConverter createCurrencyConverter() {
		return CurrencyConverter.getInstance();
	}
}
