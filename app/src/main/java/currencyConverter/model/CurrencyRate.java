package currencyConverter.model;

import java.util.List;

public class CurrencyRate {

    private long date;
    private List<Currency> exchangeRate;

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<Currency> getExchangeRate() {
        return this.exchangeRate;
    }

    public void setExchangeRate(List<Currency> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
