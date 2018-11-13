package com.google.android.gms.samples.vision.ocrreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.internal.client.FrameMetadataParcel;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class SelectedAreaTextRecognizer extends Detector<TextBlock> {

    private TextRecognizer textRecognizer;

    public SelectedAreaTextRecognizer(Context context) {
        this.textRecognizer = new TextRecognizer.Builder(context).build();
    }

    @Override
    public SparseArray<TextBlock> detect(Frame frame) {
        if (frame == null) {
            throw new IllegalArgumentException("No frame supplied.");
        } else {
            Bitmap bitmap;
            if (frame.getBitmap() != null) {
                bitmap = frame.getBitmap();
            } else {
                FrameMetadataParcel frameMetadataParcel = FrameMetadataParcel.zzc(frame);
                Frame.Metadata metadata = frame.getMetadata();
                bitmap = this.convertToBitmap(frame.getGrayscaleImageData(), metadata.getFormat(), frameMetadataParcel.width, frameMetadataParcel.height);
            }
            //If we split screen on 9 same parts, a text will be recognized only in the central one
            int splitX = bitmap.getWidth() / 3;
            int splitY = bitmap.getHeight() / 3;
            Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, splitX, splitY, bitmap.getWidth() - splitX * 2, bitmap.getHeight() - splitY * 2);
            return this.textRecognizer.detect(new Frame.Builder().setBitmap(croppedBitmap).build());
        }
    }

    //From Google's lib
    private Bitmap convertToBitmap(ByteBuffer var1, int var2, int var3, int var4) {
        byte[] var5;
        if (var1.hasArray() && var1.arrayOffset() == 0) {
            var5 = var1.array();
        } else {
            var5 = new byte[var1.capacity()];
            var1.get(var5);
        }

        ByteArrayOutputStream var6 = new ByteArrayOutputStream();
        YuvImage var7 = new YuvImage(var5, var2, var3, var4, (int[]) null);
        var7.compressToJpeg(new Rect(0, 0, var3, var4), 100, var6);
        byte[] var8 = var6.toByteArray();
        return BitmapFactory.decodeByteArray(var8, 0, var8.length);
    }
}
