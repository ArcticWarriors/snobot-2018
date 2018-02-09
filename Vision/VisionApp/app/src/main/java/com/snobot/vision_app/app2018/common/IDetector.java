package com.snobot.vision_app.app2018.common;

import org.opencv.core.Mat;

/**
 * Created by Andrew Johnson on 1/25/2018.
 */

public interface IDetector {
    Mat process(Mat aMat, long aSystemTimeNs);
}
