package userData;

import currencyConverter.codes.CountryCode;
import currencyConverter.codes.CurrencyCode;

public class UserData implements Parcelable {

	public CountryCode currentCountry;
	public CurrencyCode nativeCurrency;
	public CurrencyCode currentCurrency;

	public boolean isAutodetectionEnabled;

	public UserData() {
		this.currentCountry = CountryCode.Undefined;
		this.nativeCurrency = CurrencyCode.Undefined;
		this.currentCurrency = CurrencyCode.Undefined;
		this.isAutodetectionEnabled = true;
	}

	protected UserData(Parcel in) {
		this.currentCountry = CountryCode.values()[in.readInt()];
		this.nativeCurrency = CurrencyCode.values()[in.readInt()];
		this.currentCurrency = CurrencyCode.values()[in.readInt()];
		this.autodetectionEnabled = ( in.readByte() != 0 );
	}

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

	public boolean isAutodetectionEnabled() {
		return autodetectionEnabled;
	}

	public void setAutodetectionEnabled(boolean autodetectionEnabled) {
		this.autodetectionEnabled = autodetectionEnabled;
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(currentCountry.ordinal());
		dest.writeInt(nativeCurrency.ordinal());
		dest.writeInt(currentCurrency.ordinal());
		dest.writeByte((byte) (autodetectionEnabled ? 1 : 0));
	}
}
