package com.snobot.vision_app.app2018.detectors;

import org.opencv.core.Mat;

/**
 * Created by Andrew Johnson on 1/25/2018.
 */

public interface IDetector
{
    Mat process(Mat aOriginalImage, long aSystemTimeNs);
}
