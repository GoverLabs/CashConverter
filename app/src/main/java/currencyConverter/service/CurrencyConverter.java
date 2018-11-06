package currencyConverter.service;

import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.model.CurrencyCode;

public class CurrencyConverter implements ICurrencyConverter {

    private ICurrencyRatesProvider currencyRatesProvider;

    public CurrencyConverter() {
        this.currencyRatesProvider = new CurrencyRatesProvider();
    }

    @Override
    public Double convert(CurrencyCode from, CurrencyCode to, double value) throws CurrencyRateFetchingException {
        Double fromRate = this.currencyRatesProvider.getCurrencyRate(from);
        Double toRate = this.currencyRatesProvider.getCurrencyRate(to);
        return fromRate / toRate * value ;
    }
}
