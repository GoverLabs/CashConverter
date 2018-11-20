package userData;

import android.os.Parcel;
import android.os.Parcelable;

import currencyConverter.codes.CountryCode;
import currencyConverter.codes.CurrencyCode;

public class UserData implements Parcelable {

	private CountryCode currentCountry;

	private CurrencyCode nativeCurrency;
	private CurrencyCode currentCurrency;

	private boolean isAutoDetectionEnabled;

	public UserData() {

	}

	protected UserData(Parcel in) {
	}

	public static final Creator<UserData> CREATOR = new Creator<UserData>() {
		@Override
		public UserData createFromParcel(Parcel in) {
			return new UserData(in);
		}

		@Override
		public UserData[] newArray(int size) {
			return new UserData[size];
		}
	};

	public CountryCode getCurrentCountry() {
		return currentCountry;
	}

	public void setCurrentCountry(CountryCode currentCountry) {
		this.currentCountry = currentCountry;
	}

	public CurrencyCode getNativeCurrency() {
		return nativeCurrency;
	}

	public void setNativeCurrency(CurrencyCode nativeCurrency) {
		this.nativeCurrency = nativeCurrency;
	}

	public CurrencyCode getCurrentCurrency() {
		return currentCurrency;
	}

	public void setCurrentCurrency(CurrencyCode currentCurrency) {
		this.currentCurrency = currentCurrency;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	public boolean isAutoDetectionEnabled() {
		return isAutoDetectionEnabled;
	}

	public void setAutoDetectionEnabled(boolean autoDetectionEnabled) {
		isAutoDetectionEnabled = autoDetectionEnabled;
	}

	// TODO implement parcing
}
