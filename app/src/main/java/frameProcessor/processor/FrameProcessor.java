package frameProcessor.processor;

import android.content.Context;
import android.util.SparseArray;
import android.widget.TextView;

import frameProcessor.detector.TextDetector;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class FrameProcessor extends Detector<TextBlock> implements IFrameProcessor {

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
    public void release() {
        this.blockDetector.release();
    }

    @Override
    public SparseArray<TextBlock> detect(Frame frame) {
        return this.isAvailable ? this.blockDetector.detect(frame) : new SparseArray<TextBlock>();
    }

    @Override
    public void receiveFrame(Frame frame) {
        if (isAvailable)
            this.blockDetector.receiveFrame(frame);
    }
}
