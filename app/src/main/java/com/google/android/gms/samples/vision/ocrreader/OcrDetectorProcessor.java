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
import android.widget.TextView;

import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import java.text.DecimalFormat;

import currencyConverter.service.CountryProvider;


/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private TextView resultTextView;

    private static DecimalFormat df2 = new DecimalFormat(".##");

    OcrDetectorProcessor( TextView resultText ) {
        resultTextView = resultText;
    }

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {

        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                Log.d("Processor", "Text detected! " + item.getValue());

                String text = item.getValue();

                String[] number = text.split("\\D+");

                if(number.length == 1 )
                {
                    final double value = Double.parseDouble(number[0]) / 26.5;

                    resultTextView.post(new Runnable() {
                        public void run() {
                            resultTextView.setText("$ " + df2.format(value));
                        }
                    });
                }
            }
        }
    }
}
