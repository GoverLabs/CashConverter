package currencyConverter.converter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import currencyConverter.dto.CurrencyDTO;
import currencyConverter.dto.CurrencyRateDTO;
import currencyConverter.codes.CurrencyCode;
import currencyConverter.model.Currency;
import currencyConverter.model.CurrencyRate;

public class CurrencyRateConverter implements ICurrencyRateConverter {

    @Override
    public CurrencyRate convert(CurrencyRateDTO currencyRateDTO) throws ParseException {
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setDate(System.currentTimeMillis());
        currencyRate.setExchangeRate(this.convertCurrencyModel(currencyRateDTO.getExchangeRate()));
        return currencyRate;
    }

    private List<Currency> convertCurrencyModel(List<CurrencyDTO> currencyDTOList) {
        List<Currency> currencyList = new ArrayList<>();
        for (CurrencyDTO currencyDTO : currencyDTOList) {
            Currency currency = new Currency();
            if (currencyDTO.getCurrency() != null) {
                currency.setCurrency(new CurrencyCode(currencyDTO.getCurrency()));
            }
            if (currencyDTO.getPurchaseRateNB() != null) {
                currency.setRate(Double.parseDouble(currencyDTO.getPurchaseRateNB()));
            }
            currencyList.add(currency);
        }
        return currencyList;
    }
}
