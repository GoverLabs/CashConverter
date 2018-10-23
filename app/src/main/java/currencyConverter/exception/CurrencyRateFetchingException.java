package currencyConverter.exception;

public class CurrencyRateFetchingException extends Exception {

    public CurrencyRateFetchingException(Throwable throwable) {
        super(throwable);
    }

    public CurrencyRateFetchingException(String detailMessage) {
        super(detailMessage);
    }
}
