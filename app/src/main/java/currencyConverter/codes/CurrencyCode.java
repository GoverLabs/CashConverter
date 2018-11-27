package currencyConverter.codes;

public class CurrencyCode {

    private com.neovisionaries.i18n.CurrencyCode code;

    CurrencyCode(com.neovisionaries.i18n.CurrencyCode code) {
        this.code = code;
    }

    public CurrencyCode() {
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

	/**
	 * For SR only
	 */
    public com.neovisionaries.i18n.CurrencyCode getCode() {
        return this.code;
    }

	/**
	 * Constructs country code from string format
	 *
	 * @param numeric country code in numeric format
	 */
	public CurrencyCode(int numeric) {
		this.code = com.neovisionaries.i18n.CurrencyCode.getByCode(numeric);
	}

	@Override
	public String toString() { return toStringISO(); }

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
