package activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.goverlabs.converter.R;

public class PermissionValidator {

	// Permission request codes need to be < 256
	private static int RC_PERMISSION_CODE = 2;

	Activity activity;
	View view;

	public PermissionValidator(Activity activity, View view) {
		this.activity = activity;
		this.view = view;
	}

	public boolean arePermissionGranted() {
		boolean permissionsGranted = true;

		permissionsGranted &= checkPermission(Manifest.permission.CAMERA);
		permissionsGranted &= checkPermission(Manifest.permission.INTERNET);
		permissionsGranted &= checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
		permissionsGranted &= checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

		return permissionsGranted;
	}

	public boolean onPermissionRequestResult(
		int requestCode, String[] permissions, int[] grantResults
	) {
		if (requestCode != RC_PERMISSION_CODE) {
			return false;
		}

		for (int result : grantResults) {
			if(result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}

		return true;
	}

	private boolean checkPermission(String permission) {
		int rc = ActivityCompat.checkSelfPermission(activity, permission);
		if (rc != PackageManager.PERMISSION_GRANTED) {

			final String[] permissions = new String[] {permission};

			if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
				ActivityCompat.requestPermissions(activity, permissions, RC_PERMISSION_CODE);
				return false;

			} else {
				View.OnClickListener listener = new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						ActivityCompat.requestPermissions(activity, permissions,
								RC_PERMISSION_CODE);
					}
				};

				Snackbar.make(view, R.string.permission_request,
						Snackbar.LENGTH_INDEFINITE)
						.setAction(R.string.ok, listener)
						.show();

				return false;
			}
		}

		return true;
	}
}
