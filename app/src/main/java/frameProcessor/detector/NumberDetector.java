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
package frameProcessor.detector;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import listeners.AnonymousListener;

public class NumberDetector implements Detector.Processor<TextBlock> {

    private AnonymousListener listener;

    public NumberDetector() {
    }

    @Override
    public void release() {
        //Just do nothing in this case
    }

    public void setOnNumberDetectedListener(AnonymousListener listener) {
        this.listener = listener;
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                Log.d("Processor", "Text detected! " + item.getValue());
                String text = item.getValue().replaceAll("[^0-9?!.]", "");
                if (text.length() > 0) {
                    this.listener.onEvent(text + "--> " + String.valueOf(Double.parseDouble(text) / 28));
                }
            }
        }
    }
}
