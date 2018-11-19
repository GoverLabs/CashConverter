package frameProcessor.processor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;

public class FrameProcessor extends Detector<TextBlock> implements IFrameProcessor {

    private Detector<TextBlock> blockDetector;
    private volatile boolean isAvailable;

    public FrameProcessor(Context context) {
        this.blockDetector = new TextRecognizer.Builder(context).build();
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
    public void setProcessor(Processor<TextBlock> processor) {
        this.blockDetector.setProcessor(processor);
    }

    @Override
    public void receiveFrame(Frame frame) {
        if (this.isAvailable) {
            this.blockDetector.receiveFrame(this.cropFrame(frame));
        }
    }

    private Frame cropFrame(Frame frame) {
        int width = frame.getMetadata().getWidth();
        int height = frame.getMetadata().getHeight();
        int right = width - width / 3;
        int left = width / 3;
        int bottom = height - height / 6;
        int top = height / 6;
        YuvImage yuvImage = new YuvImage(frame.getGrayscaleImageData().array(), ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(left, top, right, bottom), 100, byteArrayOutputStream);
        byte[] jpegArray = byteArrayOutputStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.length);

        return new Frame.Builder()
                .setBitmap(bitmap)
                .setRotation(frame.getMetadata().getRotation())
                .build();
    }

}
