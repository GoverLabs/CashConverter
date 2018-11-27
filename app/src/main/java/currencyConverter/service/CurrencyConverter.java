package currencyConverter.service;

import java.util.concurrent.Executors;

import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.codes.CurrencyCode;

public class CurrencyConverter implements ICurrencyConverter {

    private static ICurrencyConverter instance;

    private ICurrencyRatesProvider currencyRatesProvider;

    private CurrencyConverter() {
        this.currencyRatesProvider = new CurrencyRatesProvider();
    }

    public static ICurrencyConverter getInstance() {
        if (instance == null) {
            instance = new CurrencyConverter();
        }
        return instance;
    }

    @Override
    public Double convert(CurrencyCode from, CurrencyCode to, double value) throws CurrencyRateFetchingException {
        Double fromRate = this.currencyRatesProvider.getCurrencyRate(from);
        Double toRate = this.currencyRatesProvider.getCurrencyRate(to);
        return fromRate / toRate * value;
    }

    @Override
    public void initialize() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    CurrencyConverter.this.currencyRatesProvider.initialize();
                } catch (Exception e) {
                    //Just Ignore It
                }
            }
        });
    }
}
