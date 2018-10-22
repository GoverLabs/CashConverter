package currencyRatesProvider.model;

public class CurrencyModel {

    private CurrencyCode baseCurrency;
    private CurrencyCode currency;
    private Double saleRate;
    private Double purchaseRate;

    public CurrencyCode getBaseCurrency() {
        return this.baseCurrency;
    }

    public void setBaseCurrency(CurrencyCode baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyCode getCurrency() {
        return this.currency;
    }

    public void setCurrency(CurrencyCode currency) {
        this.currency = currency;
    }

    public Double getSaleRate() {
        return this.saleRate;
    }

    public void setSaleRate(Double saleRate) {
        this.saleRate = saleRate;
    }

    public Double getPurchaseRate() {
        return this.purchaseRate;
    }

    public void setPurchaseRate(Double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }
}
