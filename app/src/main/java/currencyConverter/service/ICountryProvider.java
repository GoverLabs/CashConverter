package currencyConverter.service;

import android.content.Context;

import currencyConverter.exception.CountryFethchingException;

public interface ICountryProvider {

    String getCurrentCountry(Context context) throws CountryFethchingException;
}
