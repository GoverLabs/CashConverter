package currencyConverter.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import activity.ContextSingleton;
import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.model.CurrencyRate;

public class CurrencyFileBasedRepository implements ICurrencyRateModelRepository {

    private static final String FILE_NAME = "CurrencyRate.json";

    @Override
    public CurrencyRate create(CurrencyRate model) throws CurrencyRateFetchingException {
        try {
            if (model != null) {
                File file = new File(ContextSingleton.getInstance().getContext().getFilesDir(), FILE_NAME);

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
    public CurrencyRate update(CurrencyRate model) throws CurrencyRateFetchingException {
        File file = new File(ContextSingleton.getInstance().getContext().getFilesDir(), FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        return this.create(model);
    }

    @Override
    public CurrencyRate load() {
        CurrencyRate currencyRate = null;
        File file = new File(ContextSingleton.getInstance().getContext().getFilesDir(), FILE_NAME);
        if (file.exists()) {
            try {
                currencyRate = new ObjectMapper().readValue(file, CurrencyRate.class);
            } catch (IOException e) {
                //Just ignore it
            }
        }
        return currencyRate;
    }

    @Override
    public void clear() {
        File file = new File(ContextSingleton.getInstance().getContext().getFilesDir(), FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
}
