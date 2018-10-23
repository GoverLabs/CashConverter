package currencyConverter.model;

import java.util.List;

public class CurrencyRateModel {

    private long date;
    private List<CurrencyModel> exchangeRate;

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<CurrencyModel> getExchangeRate() {
        return this.exchangeRate;
    }

    public void setExchangeRate(List<CurrencyModel> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
