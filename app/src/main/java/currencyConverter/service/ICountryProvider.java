package currencyConverter.service;

import android.content.Context;

import currencyConverter.codes.CountryCode;
import currencyConverter.exception.CountryFethchingException;

public interface ICountryProvider {

    CountryCode getCurrentCountry() throws CountryFethchingException;
}
