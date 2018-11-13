package currencyConverter.model;

import currencyConverter.codes.CurrencyCode;

public class Currency {

    private CurrencyCode currency;
    private Double rate;

    public CurrencyCode getCurrency() {
        return this.currency;
    }

    public void setCurrency(CurrencyCode currency) {
        this.currency = currency;
    }

    public Double getRate() {
        return this.rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
