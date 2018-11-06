package currencyConverter.converter;

import java.text.ParseException;

import currencyConverter.dto.CurrencyRateDTO;
import currencyConverter.model.CurrencyRate;

public interface ICurrencyRateConverter {

    CurrencyRate convert(CurrencyRateDTO currencyRateDTO) throws ParseException;
}
