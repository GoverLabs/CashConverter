package listeners;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import frameProcessor.processor.IFrameProcessor;

public class CameraButtonListener implements View.OnClickListener {

    private IFrameProcessor frameProcessor;
    private ImageView cameraUnCaptureView;
    private TextView textViewResult;

    public CameraButtonListener(IFrameProcessor frameProcessor, ImageView cameraUnCaptureView, TextView textViewResult) {
        this.frameProcessor = frameProcessor;
        this.cameraUnCaptureView = cameraUnCaptureView;
        this.textViewResult = textViewResult;
    }

    @Override
    public void onClick(View view) {
        if (!this.frameProcessor.isAvailable()) {
            this.frameProcessor.setAvailability(true);
            this.setCaptureView();
        } else {
            this.frameProcessor.setAvailability(false);
            this.setUnCaptureView();
        }
    }

    private void setCaptureView() {
        this.cameraUnCaptureView.setVisibility(View.INVISIBLE);
    }

    private void setUnCaptureView() {
        this.cameraUnCaptureView.setVisibility(View.VISIBLE);
        textViewResult.setText("");
    }
}
