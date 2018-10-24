package currencyConverter.repository;

import currencyConverter.exception.CountryFethchingException;
import currencyConverter.model.CountryModel;

public interface ICountryRepository {

	void create(CountryModel model) throws CountryFethchingException;

	void update(CountryModel model) throws CountryFethchingException;

	CountryModel load();

	void clear();
}
