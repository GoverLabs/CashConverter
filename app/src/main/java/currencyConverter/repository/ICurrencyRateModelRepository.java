package currencyConverter.repository;

import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.model.CurrencyRate;

public interface ICurrencyRateModelRepository {

    CurrencyRate create(CurrencyRate model) throws CurrencyRateFetchingException;
    CurrencyRate update(CurrencyRate model) throws CurrencyRateFetchingException;
    CurrencyRate load();
    void clear();
}
