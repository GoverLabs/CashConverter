package currencyConverter.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.model.CurrencyRateModel;

public class CurrencyFileBasedRepository implements ICurrencyRateModelRepository {

    private static final String FILE_NAME = "CurrencyRateModel.json";

    @Override
    public CurrencyRateModel create(CurrencyRateModel model) throws CurrencyRateFetchingException {
        try {
            if (model != null) {
                File file = new File(FILE_NAME);
                if (!file.exists()) {
                    file.createNewFile();
                }
                new ObjectMapper().writeValue(file, model);
            }
            return model;
        } catch (IOException e) {
            throw new CurrencyRateFetchingException(e);
        }
    }

    @Override
    public CurrencyRateModel update(CurrencyRateModel model) throws CurrencyRateFetchingException {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        return this.create(model);
    }

    @Override
    public CurrencyRateModel load() {
        CurrencyRateModel currencyRateModel = null;
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try {
                currencyRateModel = new ObjectMapper().readValue(file, CurrencyRateModel.class);
            } catch (IOException e) {
                //Just ignore it
            }
        }
        return currencyRateModel;
    }

    @Override
    public void clear() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
}
