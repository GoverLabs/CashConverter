package currencyConverter.service;

import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.codes.CurrencyCode;

interface ICurrencyRatesProvider {

    Double getCurrencyRate(CurrencyCode currencyCode) throws CurrencyRateFetchingException;
}
