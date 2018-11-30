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
    private CurrencyCode(String stringISO) {
	    this.stringISO = stringISO;
    }

    /**
     * Convert currency code to ISO 4217 format
     */
    public String toStringISO() {
    	return stringISO;
    }

//	/**
//	 * Constructs country code from string format
//	 *
//	 * @param numeric country code in numeric format
//	 */
//	public CurrencyCode(int numeric) {
//	}

	@Override
	public String toString() { return toStringISO(); }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        CurrencyCode that = (CurrencyCode) o;
//        return this.code.getNumeric() == that.code.getNumeric();
//    }

//    @Override
//    public int hashCode() {
//        return 114 ^ 12 << 3 + this.code.getNumeric();
//    }

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
