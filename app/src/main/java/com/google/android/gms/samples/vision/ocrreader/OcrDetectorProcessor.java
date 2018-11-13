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

import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import java.text.DecimalFormat;

/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 * TODO: Make this implement Detector.Processor<TextBlock> and add text to the GraphicOverlay
 */
public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<OcrGraphic> graphicOverlay;
    private TextView resultTextView;
    private ImageView noTextDetectedView;
    private ImageView textDetectedView;

    private static DecimalFormat df2 = new DecimalFormat(".##");

    OcrDetectorProcessor(
            GraphicOverlay<OcrGraphic> ocrGraphicOverlay,
            TextView resultText,
            ImageView noTextDetectedView,
            ImageView textDetectedView) {
        this.graphicOverlay = ocrGraphicOverlay;
        this.resultTextView = resultText;
        this.noTextDetectedView = noTextDetectedView;
        this.textDetectedView = textDetectedView;
    }

    @Override
    public void release() {
        graphicOverlay.clear();
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        graphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        if (items.size() > 0) {

            if (this.textDetectedView.getVisibility() != View.VISIBLE) {
                this.noTextDetectedView.setVisibility(View.INVISIBLE);
                this.textDetectedView.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < items.size(); ++i) {
                TextBlock item = items.valueAt(i);

                //TODO update with new converter logic
                if (item != null && item.getValue() != null) {
                    Log.d("Processor", "Text detected! " + item.getValue());
                    String[] number = item.getValue().split("\\D+");

                    if (number.length == 1) {
                        final double value = Double.parseDouble(number[0]) / 26.5;

                        resultTextView.post(new Runnable() {
                            public void run() {
                                resultTextView.setText("$ " + df2.format(value));
                            }
                        });
                    }
                }
            }
        } else {
            if (this.noTextDetectedView.getVisibility() != View.VISIBLE) {
                this.textDetectedView.setVisibility(View.INVISIBLE);
                this.noTextDetectedView.setVisibility(View.VISIBLE);
            }
        }
    }
}
