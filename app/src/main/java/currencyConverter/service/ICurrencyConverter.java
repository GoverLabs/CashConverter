package currencyConverter.service;

import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.model.CurrencyCode;

public interface ICurrencyConverter {

    String convert(CurrencyCode from, CurrencyCode to, double value) throws CurrencyRateFetchingException;
}
