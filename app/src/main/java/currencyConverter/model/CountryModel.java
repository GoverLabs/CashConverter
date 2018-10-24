package currencyConverter.model;

public class CountryModel {

	private long date;
	private String currentCountry;
	private String homeCountry;

	public long getDate() {

		return this.date;
	}

	public void setDate(long date) {

		this.date = date;
	}

	public String getCurrentCountry() {

		return currentCountry;
	}

	public void setCurrentCountry(String currentCountry) {

		this.currentCountry = currentCountry;
	}

	public String getHomeCountry() {
		return homeCountry;
	}

	public void setHomeCountry(String homeCountry) {
		this.homeCountry = homeCountry;
	}
}
