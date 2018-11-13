package currencyConverter.codes;

public class CurrencyCode {

    private com.neovisionaries.i18n.CurrencyCode code;

    /**
     * Constructs currency code from string format
     *
     * @param stringISO currency code in ISO 4217 format
     */
    public CurrencyCode(String stringISO) {
        this.code = com.neovisionaries.i18n.CurrencyCode.getByCodeIgnoreCase(stringISO);
    }

    /**
     * Convert currency code to ISO 4217 format
     */
    public String toStringISO() {
        return this.code.getName();
    }
}
