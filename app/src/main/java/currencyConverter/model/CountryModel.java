package currencyConverter.model;

import java.util.Date;

public class CountryModel {

	private Date date;
	private String currentCountry;

	public Date getDate() {

		return this.date;
	}

	public void setDate(Date date) {

		this.date = date;
	}

	public String getCurrentCountry() {

		return currentCountry;
	}

	public void setCurrentCountry(String currentCountry) {

		this.currentCountry = currentCountry;
	}
}
