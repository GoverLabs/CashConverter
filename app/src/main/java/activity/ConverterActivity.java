/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import activity.camera.CameraSourcePreview;
import activity.camera.GraphicOverlay;

import com.goverlabs.converter.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import java.io.IOException;

import currencyConverter.codes.CurrencyCode;
import currencyConverter.exception.CurrencyRateFetchingException;
import currencyConverter.service.ICurrencyConverter;
import currencyConverter.service.ServiceFactory;
import frameProcessor.detector.NumberDetector;
import frameProcessor.processor.FrameProcessor;
import frameProcessor.processor.IFrameProcessor;
import listeners.AnonymousListener;
import listeners.CameraButtonListener;
import userData.UserData;
import userData.repository.IUserDataRepository;
import userData.repository.UserDataRepository;

public final class ConverterActivity extends AppCompatActivity {
    private static final String TAG = "PriceConverter";

    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private LinearLayout layoutButtons;
    private EditText editboxPrice;
    private Detector<TextBlock> frameProcessor;

    private UserData userData;
    private IUserDataRepository userDataRepository;

    private PermissionValidator permissionValidator;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        ContextSingleton.init(getApplicationContext());

        setContentView(R.layout.act_main);

        preview = (CameraSourcePreview) findViewById(R.id.preview);
        graphicOverlay = (GraphicOverlay) findViewById(R.id.graphicOverlay);
        layoutButtons = (LinearLayout) findViewById(R.id.layoutButtons);
		editboxPrice = (EditText) findViewById(R.id.editboxPrice);

        final TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
        final ImageView cameraUnCaptureView = (ImageView) findViewById(R.id.cameraCanvas1);
        this.frameProcessor = new FrameProcessor(this.getApplicationContext());

        NumberDetector numberDetector = new NumberDetector();
        numberDetector.setOnNumberDetectedListener(new AnonymousListener() {

            @Override
            public void onEvent(final double result) {
                textViewResult.post(new Runnable() {
                    public void run() {
                        textViewResult.setText(convertDetectedPrice(result));
                        ((IFrameProcessor) frameProcessor).setAvailability(false);
                        cameraUnCaptureView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        this.frameProcessor.setProcessor(numberDetector);

        editboxPrice.addTextChangedListener(
		        new TextWatcher() {
			        @Override
			        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			        @Override
			        public void onTextChanged(CharSequence s, int start, int before, int count) {}

			        @Override
			        public void afterTextChanged(Editable s) {
			        	double result = Double.parseDouble(s.toString());
				        textViewResult.setText(convertDetectedPrice(result));
			        }
		        }
        );

		userDataRepository = new UserDataRepository();
	    userData = userDataRepository.load();

	    if(userData == null) {
	    	userData = new UserData();
	    	userDataRepository.create(userData);
	    }

	    this.permissionValidator = new PermissionValidator(this, graphicOverlay);
	    if(permissionValidator.arePermissionGranted()) {
		    initCameraSource();
	    }

        final Button button = (Button) findViewById(R.id.buttonConvert);
        button.setOnClickListener(new CameraButtonListener(
                (IFrameProcessor) this.frameProcessor, cameraUnCaptureView, textViewResult));

        ServiceFactory.createCurrencyConverter().initialize();
    }

    @SuppressLint("InlinedApi")
    private void initCameraSource() {
        if (!this.frameProcessor.isOperational()) {
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        this.cameraSource =
                new CameraSource.Builder(getApplicationContext(), this.frameProcessor)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedPreviewSize(1280, 1280)
                        .setRequestedFps(2.f)
                        .setAutoFocusEnabled(true)
                        .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (preview != null) {
            preview.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preview != null) {
            preview.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    	if(permissionValidator.onPermissionRequestResult(requestCode, permissions, grantResults)) {
            this.initCameraSource();
            return;
        }

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.no_permissions)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (cameraSource != null) {
            try {
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        intent.putExtra("EXTRA_USER_DATA", this.userData);
	    startActivityForResult(intent, 1);
    }

    public void onClickEditPrice(View view) {
        layoutButtons.setVisibility(View.GONE);
        editboxPrice.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        layoutButtons.setVisibility(View.VISIBLE);
        editboxPrice.setVisibility(View.GONE);
    }

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        UserData newData = (UserData) dataIntent.getSerializableExtra("EXTRA_USER_DATA");

        userData = newData;
        userDataRepository.update(newData);
	}

	private String convertDetectedPrice(double price) {
		CurrencyCode sourceCurrency = userData.currentCurrency;
		CurrencyCode targetCurrency = userData.nativeCurrency;

		ICurrencyConverter converter = ServiceFactory.createCurrencyConverter();

		double convertedPrice = 0.0;
		try {
			convertedPrice = converter.convert(sourceCurrency, targetCurrency, price);
		} catch (CurrencyRateFetchingException e) {
		    // TODO add error message

			return "";
		}

		return String.format(
				"%s %.2f => %s %.2f"
			,   sourceCurrency.toStringISO()
			,   price
			,   targetCurrency.toStringISO()
			,   convertedPrice
		);
	}
}
