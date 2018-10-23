package currencyConverter.converter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import currencyConverter.dto.CurrencyDTO;
import currencyConverter.dto.CurrencyRateDTO;
import currencyConverter.model.CurrencyCode;
import currencyConverter.model.CurrencyModel;
import currencyConverter.model.CurrencyRateModel;

public class CurrencyRateConverter implements ICurrencyRateConverter {

    @Override
    public CurrencyRateModel convert(CurrencyRateDTO currencyRateDTO) throws ParseException {
        CurrencyRateModel currencyRateModel = new CurrencyRateModel();
        currencyRateModel.setDate(System.currentTimeMillis());
        currencyRateModel.setExchangeRate(this.convertCurrencyModel(currencyRateDTO.getExchangeRate()));
        return currencyRateModel;
    }

    private List<CurrencyModel> convertCurrencyModel(List<CurrencyDTO> currencyDTOList) {
        List<CurrencyModel> currencyModelList = new ArrayList<>();
        for (CurrencyDTO currencyDTO : currencyDTOList) {
            CurrencyModel currencyModel = new CurrencyModel();
            if (currencyDTO.getCurrency() != null) {
                currencyModel.setCurrency(CurrencyCode.fromCode(currencyDTO.getCurrency()));
            }
            if (currencyDTO.getPurchaseRateNB() != null) {
                currencyModel.setRate(Double.parseDouble(currencyDTO.getPurchaseRateNB()));
            }
            currencyModelList.add(currencyModel);
        }
        return currencyModelList;
    }
}
