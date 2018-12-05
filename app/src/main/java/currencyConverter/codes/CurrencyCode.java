package currencyConverter.codes;

public enum CurrencyCode {

	Undefined("NULL"),

	UAH("UAH"),
	CAD("CAD"),
	CNY("CHY"),
	CZK("CZK"),
	DKK("DKK"),
	HUF("HUF"),
	ILS("ILS"),
	JPY("JPY"),
	KZT("KZT"),
	MDL("MDL"),
	NOK("NOK"),
	SGD("SGD"),
	SEK("SEK"),
	CHF("CHF"),
	RUB("RUB"),
	GBP("GBP"),
	USD("USD"),
	UZS("UZS"),
	BYN("BYN"),
	TMT("TMT"),
	AZN("AZN"),
	TRY("TRY"),
	EUR("EUR"),
	GEL("GEL"),
	PLN("PLZ") // NOTE Privatbank API is using PLZ, not PLN code
	;

	String stringISO;

    /**
     * Constructs currency code from string format
     *
     * @param stringISO currency code in ISO 4217 format
     */
    CurrencyCode(String stringISO) {
	    this.stringISO = stringISO;
    }

    /**
     * Convert currency code to ISO 4217 format
     */
    public String toStringISO() {
    	return stringISO;
    }

	@Override
	public String toString() { return toStringISO(); }

	public static CurrencyCode fromStringISO(String stringISO) {
		CurrencyCode code = CurrencyCode.Undefined;

		try {
			code = CurrencyCode.valueOf(stringISO.toUpperCase());
		} catch (IllegalArgumentException e) {
			// Ignore unsupported codes
		}

		return code;
	}
}
