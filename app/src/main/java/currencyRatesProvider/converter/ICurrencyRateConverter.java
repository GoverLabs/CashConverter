package currencyRatesProvider.converter;

import java.text.ParseException;

import currencyRatesProvider.dto.CurrencyRateDTO;
import currencyRatesProvider.model.CurrencyRateModel;

public interface ICurrencyRateConverter {

    CurrencyRateModel convert(CurrencyRateDTO currencyRateDTO) throws ParseException;
}
