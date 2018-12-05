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
package com.google.android.gms.samples.vision.ocrreader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.CameraSourcePreview;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import java.io.IOException;

import currencyConverter.codes.CurrencyCode;
import currencyConverter.exception.CurrencyRateFetchingException;
import activity.ContextSingleton;
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

/**
 * Activity for the Ocr Detecting app.  This app detects text and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and contents of each TextBlock.
 */
public final class OcrCaptureActivity extends AppCompatActivity {
    private static final String TAG = "OcrCaptureActivity";

    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private LinearLayout layoutButtons;
    private EditText editboxPrice;
    private Detector<TextBlock> frameProcessor;

    private UserData userData;
    private IUserDataRepository userDataRepository;

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

		userDataRepository = new UserDataRepository();
	    userData = userDataRepository.load();

	    if(userData == null) {
	    	userData = new UserData();
	    	userDataRepository.create(userData);
	    }

	    // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            initCameraSource();
        } else {
            requestCameraPermission();
        }

        final Button button = (Button) findViewById(R.id.buttonConvert);
        if (button != null) {
            button.setOnClickListener(new CameraButtonListener(
                    (IFrameProcessor) this.frameProcessor, cameraUnCaptureView, textViewResult));
        }
        ServiceFactory.createCurrencyConverter().initialize();
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(graphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     * <p>
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
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

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (preview != null) {
            preview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preview != null) {
            preview.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            this.initCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
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
			Snackbar.make(graphicOverlay, "Oh fuck, something is wrong",
					Snackbar.LENGTH_LONG)
					.show();

			return "";
		}

		return String.format(
				"%s %f => %s %f"
			,   sourceCurrency.toStringISO()
			,   price
			,   targetCurrency.toStringISO()
			,   convertedPrice
		);
	}
}
