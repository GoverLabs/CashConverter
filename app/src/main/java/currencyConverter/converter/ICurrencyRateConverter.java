package currencyConverter.converter;

import java.text.ParseException;

import currencyConverter.dto.CurrencyRateDTO;
import currencyConverter.model.CurrencyRateModel;

public interface ICurrencyRateConverter {

    CurrencyRateModel convert(CurrencyRateDTO currencyRateDTO) throws ParseException;
}
