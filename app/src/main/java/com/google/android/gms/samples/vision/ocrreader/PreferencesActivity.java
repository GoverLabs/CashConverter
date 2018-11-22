package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import currencyConverter.codes.CodeUtils;
import currencyConverter.codes.CountryCode;
import currencyConverter.codes.CurrencyCode;
import currencyConverter.exception.CountryFethchingException;
import currencyConverter.exception.CurrencyCodeFetchingException;
import currencyConverter.service.ICountryProvider;
import currencyConverter.service.ServiceFactory;
import userData.UserData;

public class PreferencesActivity extends AppCompatActivity {

	private Spinner nativeCurrencySpinner;
	private Spinner currentCountrySpinner;
	private Spinner currentCurrencySpinner;
	private CheckBox currencyAutoDetectCheckBox;

	private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_preferences);

	    nativeCurrencySpinner = (Spinner) findViewById(R.id.nativeCurrencySpinner);
		currentCountrySpinner = (Spinner) findViewById(R.id.currentCountrySpinner);
	    currentCurrencySpinner = (Spinner) findViewById(R.id.currentCurrencySpinner);
	    currencyAutoDetectCheckBox = (CheckBox) findViewById(R.id.checkBox);

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			    this,
			    android.R.layout.simple_spinner_item,
			    CodeUtils.getAvailableCurrencyCodes(true)
	    );

	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    nativeCurrencySpinner.setAdapter(adapter);
	    currentCurrencySpinner.setAdapter(adapter);

	    currencyAutoDetectCheckBox.setOnCheckedChangeListener(
			    new CompoundButton.OnCheckedChangeListener() {
				    @Override
				    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

						currentCurrencySpinner.setEnabled(isChecked);
						currentCountrySpinner.setEnabled(isChecked);
						userData.setAutodetectionEnabled(isChecked);

						if(isChecked) {
							onAutoDetectionEnabled();
					    } else {
							onAutoDetectionDisabled();
					    }
				    }
			    }
	    );

//		nativeCurrencySpinner.setOnItemClickListener(
//				new AdapterView.OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//					}
//				}
//		);
//
//		currentCountrySpinner.setOnItemClickListener(
//				new AdapterView.OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//					}
//				}
//		);
//
//		currentCurrencySpinner.setOnItemClickListener(
//				new AdapterView.OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//					}
//				}
//		);

	    this.userData = (UserData) getIntent().getParcelableExtra("EXTRA_USER_DATA");

	    if(this.userData.isAutodetectionEnabled()) {
		    onAutoDetectionEnabled();
	    } else {
		    onAutoDetectionDisabled();
	    }
    }

    private void onAutoDetectionEnabled() {
		ICountryProvider countryProvider = ServiceFactory.createCountryProvider();

		try {
			CountryCode currentCountry = countryProvider.getCurrentCountry(getApplicationContext());
			CurrencyCode currencyCode = CodeUtils.getCurrencyFromCountry(currentCountry);

			userData.setCurrentCountry(currentCountry);
			userData.setCurrentCurrency(currencyCode);
		} catch (CountryFethchingException e) {
			e.printStackTrace();
		} catch (CurrencyCodeFetchingException e) {
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
}
