package currencyConverter.service;

import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.model.CurrencyCode;

public class CurrencyConverter implements ICurrencyConverter {

    private static final String RESULT_FORMAT_PATTERN = "%.2f %s";

    private ICurrencyRatesProvider currencyRatesProvider;

    public CurrencyConverter() {
        this.currencyRatesProvider = new CurrencyRatesProvider();
    }

    @Override
    public String convert(CurrencyCode from, CurrencyCode to, double value) throws CurrencyRateFetchingException {
        Double fromRate = this.currencyRatesProvider.getCurrencyRate(from);
        Double toRate = this.currencyRatesProvider.getCurrencyRate(to);
        return String.format(RESULT_FORMAT_PATTERN, fromRate / toRate * value, to.getSymbol()) ;
    }
}
