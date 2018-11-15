package frameProcessor.processor;

import com.google.android.gms.vision.Frame;

public interface IFrameProcessor {

    boolean isAvailable();
    void setAvailability(boolean isAvailable);
    boolean isOperational();
    void processFrame(Frame frame);
    void release();
}
