package currencyConverter.model;

import java.util.Arrays;
import java.util.List;

public enum CurrencyCode {

    UAH("Hryvnia", "UAH", "Ukraine", "UKR", "₴"),
    CAD("Canadian Dollar", "CAD", "Canada", "CAN", "$"),
    CNY("Yuan Renminbi", "CNY", "China", "TWN", "¥"),
    CZK("Czech Koruna", "CZK", "Czech", "CZE", "Kč"),
    DKK("Danish Krone", "DKK", " Denmark", "DNK", "Kr"),
    HUF("Forint", "HUF", "Hungary", "HUN", "F"),
    ILS("New Israeli Sheqel", "ILS", "Israel", "ISR", "NIS"),
    JPY("Yen", "JPY", "Japan", "JPN", "¥"),
    KZT("Tenge", "KZT", "Kazakhstan", "KAZ", "₸"),
    MDL("Moldovan Leu", "MDL", "Moldova", "MDA", "lei"),
    NOK("Norwegian Krone", "NOK", "Norway", "NOR", "Kr"),
    SGD("Singapore Dollar", "SGD", "Singapore", "SGP", "$"),
    SEK("Swedish Krona", "SEK", "Sweden", "SWE", "Kr"),
    CHF("Swiss Franc", "CHF", "Switzerland", "CHE", "Fr"),
    RUB("Russian Ruble", "RUB", "Russia", "RUS", "p"),
    GBP("Pound Sterling", "GBP", "United Kingdom", "GBR", "£"),
    USD("US Dollar", "USD", "United States of America", "USA", "$"),
    UZS("Uzbekistan Sum", "UZS", "Uzbekistan", "UZB", "so'm"),
    BYN("Belarusian Ruble", "BYN", "Belarus", "BLR", "p"),
    TMT("Turkmenistan New Manat", "TMT", "Turkmenistan", "TKM", "T"),
    AZN("Azerbaijanian Manat", "AZN", "Azerbaijan", "AZE", "m"),
    TRY("Turkish Lira", "TRY", "Turkish", "TUR", "₺"),
    EUR("Euro", "EUR", "European Union", "EU", "€"),
    PLZ("Zloty", "PLZ", "Poland", "POL", "zł");

    private final String name;
    private final String code;
    private final String country;
    private final String countryCode;
    private final String symbol;

    private static final List<String> europeanCountryList = Arrays
            .asList("AUT", "BEL", "DEU", "GRC", "ITA", "LTU",
                    "NLD", "IRL", "ESP", "CYP", "LVA", "LUX",
                    "MLT", "PRT", "SVN", "SVK", "FIN", "FRA", "EST");

    CurrencyCode(String name, String code, String country, String countryCode, String symbol) {
        this.name = name;
        this.code = code;
        this.country = country;
        this.countryCode = countryCode;
        this.symbol = symbol;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getCountry() {
        return this.country;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public static CurrencyCode fromCode(String code) {
        for (CurrencyCode currency : CurrencyCode.values()) {
            if (currency.code.equalsIgnoreCase(code)) {
                return currency;
            }
        }
        return null;
    }

    public static CurrencyCode fromCountryCode(String countryCode) {
        for (CurrencyCode currency : CurrencyCode.values()) {
            if (currency.countryCode.equalsIgnoreCase(countryCode)) {
                return currency;
            }
        }
        return checkEuropeCountries(countryCode);
    }

    private static CurrencyCode checkEuropeCountries(String countryCode) {
        for (String code : europeanCountryList) {
            if (code.equalsIgnoreCase(countryCode)) {
                return EUR;
            }
        }
        return null;
    }
}
