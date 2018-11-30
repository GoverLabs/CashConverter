package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import java.util.Comparator;

import currencyConverter.codes.CountryCode;
import currencyConverter.codes.CurrencyCode;
import currencyConverter.exception.CountryFethchingException;
import currencyConverter.service.ICountryProvider;
import currencyConverter.service.ServiceFactory;
import userData.UserData;

public class PreferencesActivity extends AppCompatActivity {

	private Spinner currentCountrySpinner;
	private Spinner currentCurrencySpinner;

	private UserData userData;

	private ArrayAdapter<CurrencyCode> currencyAdapter;
	private ArrayAdapter<CountryCode> countryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_preferences);

	    Spinner nativeCurrencySpinner = (Spinner) findViewById(R.id.nativeCurrencySpinner);
		currentCountrySpinner = (Spinner) findViewById(R.id.currentCountrySpinner);
	    currentCurrencySpinner = (Spinner) findViewById(R.id.currentCurrencySpinner);
	    CheckBox currencyAutoDetectCheckBox = (CheckBox) findViewById(R.id.checkBox);

	    currencyAdapter = new ArrayAdapter<>(
			    this,
			    android.R.layout.simple_spinner_item,
			    CurrencyCode.values()
	    );
	    currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    currencyAdapter.sort(
            new Comparator<CurrencyCode>() {
			    @Override
			    public int compare(CurrencyCode lhs, CurrencyCode rhs) {
				    return lhs.toString().compareToIgnoreCase( rhs.toString() );
			    }
	    });

	    nativeCurrencySpinner.setAdapter(currencyAdapter);
	    currentCurrencySpinner.setAdapter(currencyAdapter);

	    countryAdapter = new ArrayAdapter<>(
			    this,
			    android.R.layout.simple_spinner_item,
			    CountryCode.values()
	    );
	    countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    countryAdapter.sort(
		    new Comparator<CountryCode>() {
			    @Override
			    public int compare(CountryCode lhs, CountryCode rhs) {
				    return lhs.toString().compareToIgnoreCase( rhs.toString() );
			    }
		    }
	    );

	    currentCountrySpinner.setAdapter(countryAdapter);

	    currencyAutoDetectCheckBox.setOnCheckedChangeListener(
			    new CompoundButton.OnCheckedChangeListener() {
				    @Override
				    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

						currentCurrencySpinner.setEnabled(!isChecked);
						currentCountrySpinner.setEnabled(!isChecked);
						userData.setAutodetectionEnabled(isChecked);

						if(isChecked) {
							onAutoDetectionEnabled();
					    } else {
							onAutoDetectionDisabled();
					    }
				    }
			    }
	    );

	    nativeCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			    userData.setNativeCurrency(currencyAdapter.getItem(position));
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {

		    }
	    });

	    currentCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			    updateCurrentCountry(countryAdapter.getItem(position));
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {

		    }
	    });

	    currentCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			    updateCurrentCurrency(currencyAdapter.getItem(position));
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {

		    }
	    });

	    userData = getIntent().getParcelableExtra("EXTRA_USER_DATA");

	    if(userData.isAutodetectionEnabled()) {
		    currencyAutoDetectCheckBox.setChecked(true);
		    onAutoDetectionEnabled();
	    } else {
		    currencyAutoDetectCheckBox.setChecked(false);
		    onAutoDetectionDisabled();
	    }

	    nativeCurrencySpinner.setSelection(currencyAdapter.getPosition(userData.getNativeCurrency()));
	    currentCurrencySpinner.setSelection(currencyAdapter.getPosition(userData.getCurrentCurrency()));
	    currentCountrySpinner.setSelection(countryAdapter.getPosition(userData.getCurrentCountry()));
    }

    private void onAutoDetectionEnabled() {
		ICountryProvider countryProvider = ServiceFactory.createCountryProvider();

		try {
			CountryCode currentCountry = countryProvider.getCurrentCountry(getApplicationContext());
			updateCurrentCountry(currentCountry);
		} catch (CountryFethchingException e) {
			e.printStackTrace();
		}
	}

	private void onAutoDetectionDisabled() {

	}

    public void onClickSave(View view) {
	    Intent intent = new Intent();
	    intent.putExtra("EXTRA_USER_DATA", this.userData);
	    setResult(RESULT_OK, intent);
	    finish();
    }

    private void updateCurrentCurrency(CurrencyCode code) {
	    currentCurrencySpinner.setSelection(currencyAdapter.getPosition(code));
	    userData.setCurrentCurrency(code);
    }

    private void updateCurrentCountry(CountryCode code) {
	    currentCountrySpinner.setSelection(countryAdapter.getPosition(code));
	    userData.setCurrentCountry(code);

	    updateCurrentCurrency(code.getCurrency());
    }
}
