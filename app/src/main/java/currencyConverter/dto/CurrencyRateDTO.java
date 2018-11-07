package currencyConverter.dto;

import java.util.List;

public class CurrencyRateDTO {

    private String date;
    private String bank;
    private String baseCurrency;
    private String baseCurrencyLit;
    private List<CurrencyDTO> exchangeRate;

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBaseCurrency() {
        return this.baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrencyLit() {
        return this.baseCurrencyLit;
    }

    public void setBaseCurrencyLit(String baseCurrencyLit) {
        this.baseCurrencyLit = baseCurrencyLit;
    }

    public List<CurrencyDTO> getExchangeRate() {
        return this.exchangeRate;
    }

    public void setExchangeRate(List<CurrencyDTO> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
