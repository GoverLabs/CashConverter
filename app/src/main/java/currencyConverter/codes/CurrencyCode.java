package currencyConverter.codes;

public class CurrencyCode {

    private com.neovisionaries.i18n.CurrencyCode code;

    CurrencyCode(com.neovisionaries.i18n.CurrencyCode code) {
        this.code = code;
    }

    public com.neovisionaries.i18n.CurrencyCode getCode() {
        return this.code;
    }

    public void setCode(com.neovisionaries.i18n.CurrencyCode code) {
        this.code = code;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyCode that = (CurrencyCode) o;
        return this.code.getNumeric() == that.code.getNumeric();
    }

    @Override
    public int hashCode() {
        return 114 ^ 12 << 3 + this.code.getNumeric();
    }
}
