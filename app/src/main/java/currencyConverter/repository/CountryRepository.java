package currencyConverter.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import currencyConverter.exception.CountryFethchingException;
import currencyConverter.model.CountryModel;

public class CountryRepository implements ICountryRepository {

	private static final String FILE_NAME = "CountryModel.json";

	@Override
	public void create(CountryModel model) throws CountryFethchingException {

		try {
			if (model != null) {
				File file = new File(FILE_NAME);

				if (!file.exists()) {

					file.createNewFile();
					new ObjectMapper().writeValue(file, model);
				}
				else {
					throw new CountryFethchingException();
				}
			}

		} catch (IOException e) {
			throw new CountryFethchingException(e);
		}
	}

	@Override
	public void update(CountryModel model) throws CountryFethchingException {
		clear();
		create(model);
	}

	@Override
	public CountryModel load() {

		CountryModel model = null;

		File file = new File(FILE_NAME);
		if (file.exists()) {

			try {
				model = new ObjectMapper().readValue(file, CountryModel.class);
			} catch (IOException e) {
				//Just ignore it
			}
		}

		return model;
	}

	@Override
	public void clear() {
		File file = new File(FILE_NAME);

		if (file.exists()) {
			file.delete();
		}
	}
}
