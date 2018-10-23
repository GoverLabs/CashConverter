package currencyConverter.repository;

import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.model.CurrencyRateModel;

public interface ICurrencyRateModelRepository {

    CurrencyRateModel create(CurrencyRateModel model) throws CurrencyRateFetchingException;
    CurrencyRateModel update(CurrencyRateModel model) throws CurrencyRateFetchingException;
    CurrencyRateModel load();
    void clear();
}
