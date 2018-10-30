package currencyConverter.service;

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

public class CountryProvider implements ICountryProvider {

    @Override
    public String getCurrentCountry(Context context) throws CountryFethchingException {

        String countryCode = getCountryByGeolocation(context);

        if(countryCode != null) {

            return countryCode;
        }
        else {
            countryCode = getCountryBySimCardInfo(context);
        }

        if(countryCode == null) {
            throw new CountryFethchingException();
        }

        return null;
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
}
