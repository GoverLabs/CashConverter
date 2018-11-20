package currencyConverter.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import currencyConverter.exception.CountryFethchingException;
import currencyConverter.model.CountryModel;
import currencyConverter.codes.CountryCode;
import currencyConverter.repository.CountryRepository;
import currencyConverter.repository.ICountryRepository;

class CountryProvider implements ICountryProvider {

	private static final Integer SAVED_RATES_TTL_DAYS = 3;

    private ICountryRepository countryRepository;

    private CountryCode currentCountry;

    CountryProvider() {
    	this.countryRepository = new CountryRepository();
	}

    @Override
    public CountryCode getCurrentCountry(Context context) throws CountryFethchingException {

		if(!this.isCacheAvailable()) {
			CountryModel countryModel = this.countryRepository.load();
			if(countryModel == null) {
				countryModel = fetchCurrentCountry(context);
				this.countryRepository.create(countryModel);
			} else if (!this.isCacheActual(countryModel)) {
				countryModel = fetchCurrentCountry(context);
				this.countryRepository.update(countryModel);
			}
			this.updateLocalCache(countryModel);
		}

		return currentCountry;
    }

    private boolean isCacheAvailable() {
    	return this.currentCountry != null;

	}

	private boolean isCacheActual(CountryModel countryModel) {
		Date lastUpdateDate = countryModel.getDate();
		Calendar ttlCalendar = Calendar.getInstance();
		ttlCalendar.add(Calendar.DAY_OF_YEAR, -SAVED_RATES_TTL_DAYS);
		return lastUpdateDate.after(ttlCalendar.getTime());
	}

	private CountryModel fetchCurrentCountry(Context context) throws CountryFethchingException {
		String countryCode = getCountryByGeolocation(context);

		if(countryCode == null) {
			countryCode = getCountryBySimCardInfo(context);
		}

		if(countryCode == null) {
			throw new CountryFethchingException();
		}

		CountryModel countryModel = new CountryModel();
		countryModel.setCurrentCountry(new CountryCode(countryCode));
		countryModel.setDate(new Date());

		return countryModel;
	}

    private String getCountryByGeolocation(Context context) throws CountryFethchingException {

        final LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (
            locationManager != null &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ) {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location != null) {
                final Geocoder gcd = new Geocoder(context, Locale.getDefault());
                final int maximumResultsCount = 1;

                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(),
                            location.getLongitude(), maximumResultsCount);

                    if (addresses != null && !addresses.isEmpty()) {
                        String country = addresses.get(0).getCountryCode();
                        if (country != null) {
                            return country;
                        }
                    }
                } catch (Exception e) {
                    throw new CountryFethchingException(e);
                }
            }
        }

        return null;
    }

    private String getCountryBySimCardInfo(Context context) {

        final TelephonyManager tm =
             (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {

            final String networkCountry = tm.getNetworkCountryIso();

            if (networkCountry != null && networkCountry.length() == 2) {

                return networkCountry.toLowerCase(Locale.US);
            }
        }
        return null;
    }

	private void updateLocalCache(CountryModel countryModel) {
		this.currentCountry = countryModel.getCurrentCountry();
	}

}
