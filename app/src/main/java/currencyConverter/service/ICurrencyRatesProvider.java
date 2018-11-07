package currencyConverter.service;

import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.model.CurrencyCode;

interface ICurrencyRatesProvider {

    Double getCurrencyRate(CurrencyCode currencyCode) throws CurrencyRateFetchingException;
}
