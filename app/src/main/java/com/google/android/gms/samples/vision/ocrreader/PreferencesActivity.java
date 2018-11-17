package com.google.android.gms.samples.vision.ocrreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import java.util.List;

import currencyConverter.codes.CodeUtils;
import currencyConverter.codes.CurrencyCode;
import currencyConverter.service.CountryProvider;

public class PreferencesActivity extends AppCompatActivity {

	private Spinner nativeCurrencySpinner;
	private Spinner currentCurrencySpinner;
	private CheckBox currencyAutoDetectCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_preferences);

	    nativeCurrencySpinner = (Spinner) findViewById(R.id.nativeCurrencySpinner);
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
					    if(isChecked) {
						    currentCurrencySpinner.setEnabled(false);
					    }
					    else {
						    currentCurrencySpinner.setEnabled(true);
					    }
				    }
			    }
	    );
    }

    public void onClickSave(View view) {
        finish();
    }
}
