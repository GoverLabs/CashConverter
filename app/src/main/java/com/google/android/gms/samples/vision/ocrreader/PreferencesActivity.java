package com.google.android.gms.samples.vision.ocrreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import currencyConverter.codes.CodeUtils;
import currencyConverter.codes.CurrencyCode;

public class PreferencesActivity extends AppCompatActivity {

	private Spinner nativeCurrencySpinner;
	private Spinner currentCurrencySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_preferences);

	    nativeCurrencySpinner = (Spinner) findViewById(R.id.nativeCurrencySpinner);
	    currentCurrencySpinner = (Spinner) findViewById(R.id.currentCurrencySpinner);

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			    this,
			    android.R.layout.simple_spinner_item,
			    CodeUtils.getAvailableCurrencyCodes(true)
	    );

	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    nativeCurrencySpinner.setAdapter(adapter);
	    currentCurrencySpinner.setAdapter(adapter);
    }

    public void onClickSave(View view) {
        finish();
    }
}
