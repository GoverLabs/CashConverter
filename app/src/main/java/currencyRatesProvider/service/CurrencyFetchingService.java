package currencyRatesProvider.service;

import android.annotation.SuppressLint;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import currencyRatesProvider.converter.CurrencyRateConverter;
import currencyRatesProvider.converter.ICurrencyRateConverter;
import currencyRatesProvider.dto.CurrencyRateDTO;
import currencyRatesProvider.exception.CurrencyRateFetchingException;
import currencyRatesProvider.model.CurrencyCode;
import currencyRatesProvider.model.CurrencyModel;
import currencyRatesProvider.model.CurrencyRateModel;

public class CurrencyFetchingService implements ICurrencyFetchingService {
    public static final SimpleDateFormat DATE_PATTERN = new SimpleDateFormat("dd.MM.yyyy");
    private static final String API_PATTERN = "https://api.privatbank.ua/p24api/exchange_rates?json&date=%S";

    //For a case if today's exchange rate is not available yet
    private static final Long MILLIS_IN_ONE_DAY = 24 * 60 * 60 * 1000l;

    private final Map<CurrencyCode, CurrencyModel> exchangeRateMap;

    private ICurrencyRateConverter currencyRateConverter;

    public CurrencyFetchingService() {
        this.currencyRateConverter = new CurrencyRateConverter();
        this.exchangeRateMap = new HashMap<>();
    }

    @SuppressLint("LongLogTag")
    public Double transform(CurrencyCode from, CurrencyCode to, double value) throws CurrencyRateFetchingException {
        CurrencyModel fromModel = this.exchangeRateMap.get(from);
        CurrencyModel toModel = this.exchangeRateMap.get(to);
        if (fromModel == null || toModel == null) {
            try {
                this.updateLocalCache();
            } catch (Exception e) {
                Log.w("Error occurred while trying to fetch data", e);
                throw new CurrencyRateFetchingException(e);
            }
            fromModel = this.exchangeRateMap.get(from);
            toModel = this.exchangeRateMap.get(to);
            if (fromModel == null || toModel == null) {
                throw new CurrencyRateFetchingException("Can not fetch data for specified currencies");
            }
        }
        //Just ignore this hack please
        return Math.round(fromModel.getPurchaseRate() / toModel.getPurchaseRate() * value * 100) / 100d;
    }

    //TODO make it better
    private void updateLocalCache() throws IOException, ParseException {
        CurrencyRateDTO rateDTO = new ObjectMapper()
                .readValue(
                        new URL(
                                String.format(API_PATTERN, DATE_PATTERN.format(new Date()))
                        ), CurrencyRateDTO.class);

        if (rateDTO.getExchangeRate().isEmpty()) {
            rateDTO = new ObjectMapper()
                    .readValue(
                            new URL(
                                    String.format(API_PATTERN, DATE_PATTERN.format(new Date(System.currentTimeMillis() - MILLIS_IN_ONE_DAY)))
                            ), CurrencyRateDTO.class);
        }
        CurrencyRateModel currencyRateModel = this.currencyRateConverter.convert(rateDTO);

        for (CurrencyModel currencyModel : currencyRateModel.getExchangeRate()) {
            this.exchangeRateMap.put(currencyModel.getCurrency(), currencyModel);
        }
    }
}
