package currencyConverter.model;

import java.util.Date;
import currencyConverter.codes.CountryCode;

public class CountryModel {

	private Date date;
	private CountryCode currentCountry;

	public Date getDate() {

		return this.date;
	}

	public void setDate(Date date) {

		this.date = date;
	}

	public CountryCode getCurrentCountry() {

		return currentCountry;
	}

	public void setCurrentCountry(CountryCode currentCountry) {

		this.currentCountry = currentCountry;
	}
}
