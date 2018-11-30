package currencyConverter.service;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import currencyConverter.converter.CurrencyRateConverter;
import currencyConverter.converter.ICurrencyRateConverter;
import currencyConverter.dto.CurrencyRateDTO;
import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.codes.CurrencyCode;
import currencyConverter.model.Currency;
import currencyConverter.model.CurrencyRate;
import currencyConverter.repository.CurrencyFileBasedRepository;
import currencyConverter.repository.ICurrencyRateModelRepository;

class CurrencyRatesProvider implements ICurrencyRatesProvider {
    private static final SimpleDateFormat DATE_PATTERN = new SimpleDateFormat("dd.MM.yyyy");
    private static final String API_PATTERN = "https://api.privatbank.ua/p24api/exchange_rates?json&date=%s";
    private static final Integer SAVED_RATES_TTL_DAYS = 3;

    private final Map<CurrencyCode, Currency> exchangeRateMap;

    private ICurrencyRateConverter currencyRateConverter;
    private ICurrencyRateModelRepository currencyRateModelRepository;

    CurrencyRatesProvider() {
        this.currencyRateConverter = new CurrencyRateConverter();
        this.currencyRateModelRepository = new CurrencyFileBasedRepository();
        this.exchangeRateMap = new HashMap<>();
    }

    @Override
    public Double getCurrencyRate(CurrencyCode currencyCode) throws CurrencyRateFetchingException {
        if (!this.isCacheAvailable()) {
            this.initialize();
        }
        Currency currency = this.exchangeRateMap.get(currencyCode);
        if (currency == null) {
            throw new CurrencyRateFetchingException("Can not fetch data");
        }
        return currency.getRate();
    }

    @Override
    public void initialize() throws CurrencyRateFetchingException {
        if (!this.isCacheAvailable()) {
            CurrencyRate currencyRate = this.currencyRateModelRepository.load();
            if (currencyRate == null) {
                currencyRate = this.currencyRateModelRepository.create(this.fetchLatestAvailableRates());
            } else if (!this.isActual(currencyRate)) {
                currencyRate = this.currencyRateModelRepository.update(this.fetchLatestAvailableRates());
            }
            this.updateLocalCache(currencyRate);
        }
    }

    private boolean isCacheAvailable() {
        return !this.exchangeRateMap.isEmpty();
    }

    private boolean isActual(CurrencyRate currencyRate) {
        Date lastUpdateDate = new Date(currencyRate.getDate());
        Calendar ttlCalendar = Calendar.getInstance();
        ttlCalendar.add(Calendar.DAY_OF_YEAR, -SAVED_RATES_TTL_DAYS);
        return lastUpdateDate.after(ttlCalendar.getTime());
    }

    private CurrencyRate fetchLatestAvailableRates() throws CurrencyRateFetchingException {
        try {
            Calendar calendar = Calendar.getInstance();
            CurrencyRateDTO rateDTO = this.fetchRatesForDate(calendar.getTime());
            if (rateDTO.getExchangeRate().isEmpty()) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                rateDTO = this.fetchRatesForDate(calendar.getTime());
                if (rateDTO.getExchangeRate().isEmpty()) {
                    throw new CurrencyRateFetchingException("Can not fetch data");
                }
            }
            return this.currencyRateConverter.convert(rateDTO);
        } catch (Exception e) {
            Log.w("Can not fetch data", e);
            throw new CurrencyRateFetchingException(e);
        }
    }

    private CurrencyRateDTO fetchRatesForDate(Date date) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL apiURL = new URL(String.format(API_PATTERN, DATE_PATTERN.format(date)));
        return objectMapper.readValue(apiURL, CurrencyRateDTO.class);
    }

    private void updateLocalCache(CurrencyRate currencyRate) {
        for (Currency currency : currencyRate.getExchangeRate()) {
            if (currency != null && currency.getCurrency() != null ) {
                this.exchangeRateMap.put(currency.getCurrency(), currency);
            }
        }
    }
}
