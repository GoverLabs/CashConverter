package currencyRatesProvider.converter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import currencyRatesProvider.dto.CurrencyDTO;
import currencyRatesProvider.dto.CurrencyRateDTO;
import currencyRatesProvider.model.CurrencyCode;
import currencyRatesProvider.model.CurrencyModel;
import currencyRatesProvider.model.CurrencyRateModel;
import currencyRatesProvider.service.CurrencyFetchingService;

public class CurrencyRateConverter implements ICurrencyRateConverter {

    @Override
    public CurrencyRateModel convert(CurrencyRateDTO currencyRateDTO) throws ParseException {
        CurrencyRateModel currencyRateModel = new CurrencyRateModel();
        currencyRateModel.setBaseCurrency(CurrencyCode.valueOf(currencyRateDTO.getBaseCurrencyLit()));
        currencyRateModel.setDate(CurrencyFetchingService.DATE_PATTERN.parse(currencyRateDTO.getDate()));
        currencyRateModel.setExchangeRate(this.convertCurrencyModel(currencyRateDTO.getExchangeRate()));
        return currencyRateModel;
    }

    private List<CurrencyModel> convertCurrencyModel(List<CurrencyDTO> currencyDTOList) {
        List<CurrencyModel> currencyModelList = new ArrayList<>();
        for (CurrencyDTO currencyDTO : currencyDTOList) {
            CurrencyModel currencyModel = new CurrencyModel();
            if (currencyDTO.getBaseCurrency() != null) {
                currencyModel.setBaseCurrency(CurrencyCode.valueOf(currencyDTO.getBaseCurrency()));
            }
            if (currencyDTO.getCurrency() != null) {
                currencyModel.setCurrency(CurrencyCode.valueOf(currencyDTO.getCurrency()));
            }
            if (currencyDTO.getPurchaseRateNB() != null) {
                currencyModel.setPurchaseRate(Double.parseDouble(currencyDTO.getPurchaseRateNB()));
            }
            if (currencyDTO.getSaleRateNB() != null) {
                currencyModel.setSaleRate(Double.parseDouble(currencyDTO.getSaleRateNB()));
            }
            currencyModelList.add(currencyModel);
        }
        return currencyModelList;
    }
}
