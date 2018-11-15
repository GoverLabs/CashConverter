package frameProcessor.processor;

import android.content.Context;
import android.widget.TextView;

import frameProcessor.detector.TextDetector;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class FrameProcessor implements IFrameProcessor {

    private Detector<TextBlock> blockDetector;
    private volatile boolean isAvailable;

    public FrameProcessor(Context context, TextView resultView) {
        this.blockDetector = new TextRecognizer.Builder(context).build();
        this.blockDetector.setProcessor(new TextDetector(resultView));
        this.isAvailable = false;
    }

    @Override
    public boolean isAvailable() {
        return this.isAvailable;
    }

    @Override
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public boolean isOperational() {
        return this.blockDetector.isOperational();
    }

    @Override
    public void processFrame(Frame frame) {
        this.blockDetector.receiveFrame(frame);
    }

    @Override
    public void release() {
        this.blockDetector.release();
    }
}
