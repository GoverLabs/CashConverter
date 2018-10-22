package currencyRatesProvider.model;

import java.util.Date;
import java.util.List;

public class CurrencyRateModel {

    private Date date;
    private CurrencyCode baseCurrency;
    private List<CurrencyModel> exchangeRate;

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CurrencyCode getBaseCurrency() {
        return this.baseCurrency;
    }

    public void setBaseCurrency(CurrencyCode baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public List<CurrencyModel> getExchangeRate() {
        return this.exchangeRate;
    }

    public void setExchangeRate(List<CurrencyModel> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
