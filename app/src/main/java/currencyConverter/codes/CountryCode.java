package currencyConverter.codes;

import java.util.Currency;

public class CountryCode {

    private com.neovisionaries.i18n.CountryCode code;

    /**
     * Constructs country code from string format
     *
     * @param stringISO country code in ISO 3166-1 alpha-2 format
     */
    public CountryCode(String stringISO) {
        this.code = com.neovisionaries.i18n.CountryCode.getByCodeIgnoreCase(stringISO);
    }

    /**
     * Convert country code to ISO 3166-1 alpha-2 format
     */
    public String toStringISO() {
        return this.code.getAlpha2();
    }

    /**
     * For SR only
     */
    public com.neovisionaries.i18n.CountryCode getCode() {
        return this.code;
    }

	/**
	 * Constructs country code from string format
	 *
	 * @param numeric country code in numeric format
	 */
	public CountryCode(int numeric) {
		this.code = com.neovisionaries.i18n.CountryCode.getByCode(numeric);
	}

	@Override
	public String toString() { return this.code.getName(); }
}