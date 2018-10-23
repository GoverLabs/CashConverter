package currencyConverter.model;

public class CurrencyModel {

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
