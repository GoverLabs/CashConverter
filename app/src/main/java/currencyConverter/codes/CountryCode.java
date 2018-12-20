package currencyConverter.codes;

import java.util.Locale;

public enum CountryCode {

	Undefined("NULL", CurrencyCode.Undefined),

	UA("UA", CurrencyCode.UAH),

	CA("CA", CurrencyCode.CAD),

	CN("CN", CurrencyCode.CNY),

	CZ("CZ", CurrencyCode.CZK),

	DK("DK", CurrencyCode.DKK),
	FO("FO", CurrencyCode.DKK),
	GL("GL", CurrencyCode.DKK),

	HU("HU", CurrencyCode.HUF),

	IL("IL", CurrencyCode.ILS),

	JP("JP", CurrencyCode.JPY),

	KZ("KZ", CurrencyCode.KZT),

	MD("MD", CurrencyCode.MDL),

	NO("NO", CurrencyCode.NOK),

	SG("SG", CurrencyCode.SGD),

	SE("SE", CurrencyCode.SEK),

	CH("CH", CurrencyCode.CHF),
	LI("LI", CurrencyCode.CHF),

	RU("RU", CurrencyCode.RUB),

	GB("GB", CurrencyCode.GBP),

	EC("EC", CurrencyCode.USD),
	GU("GU", CurrencyCode.USD),
	HI("HI", CurrencyCode.USD),
	PA("PA", CurrencyCode.USD),
	PR("PR", CurrencyCode.USD),
	SV("SV", CurrencyCode.USD),
	US("US", CurrencyCode.USD),

	UZ("UZ", CurrencyCode.UZS),

	BY("BY", CurrencyCode.BYN),

	TM("TM", CurrencyCode.TMT),

	AZ("AZ", CurrencyCode.AZN),

	TR("TR", CurrencyCode.TRY),

	// TODO add EUR
	IT("IT", CurrencyCode.EUR),

	GE("GE", CurrencyCode.GEL),

	PL("PL", CurrencyCode.PLN)
	;

	String stringISO;
	CurrencyCode correspondingCurrency;

    /**
     * Constructs country code from string format
     *
     * @param stringISO country code in ISO 3166-1 alpha-2 format
     * @param currency countries native currency
     */
    CountryCode(String stringISO, CurrencyCode currency) {
    	this.stringISO = stringISO;
    	this.correspondingCurrency = currency;
    }

    /**
     * Convert country code to ISO 3166-1 alpha-2 format
     */
    public String toStringISO() {
        return this.stringISO;
    }

	@Override
	public String toString() { return toStringISO(); }

	public CurrencyCode getCurrency() { return this.correspondingCurrency; }

	public static CountryCode fromStringISO(String stringISO) {
		CountryCode code = CountryCode.Undefined;

    	try {
		    code = CountryCode.valueOf(stringISO.toUpperCase(Locale.ENGLISH));
	    } catch (IllegalArgumentException e) {
		    // Ignore unsupported codes
	    }

	    return code;
	}
}