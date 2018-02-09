package com.snobot.vision_app.app2018.java_algorithm;

import com.snobot.vision_app.app2018.VisionAlgorithmPreferences;
import com.snobot.vision_app.app2018.VisionRobotConnection;

import org.opencv.core.Mat;

/**
 * Created by PJ on 2/8/2018.
 */

public class JavaVisionAlgorithm
{
    public JavaVisionAlgorithm(VisionRobotConnection mRobotConnection, VisionAlgorithmPreferences mPreferences) {
    }

    public void setDisplayType(DisplayType displayType) {
        
    }

    public void setCameraDirection(int cameraDirection) {
    }

    public void setRecording(boolean aRecord, String aName) {
    }

    public void iterateDisplayType() {
    }

    public void setRobotConnected(boolean robotConnected) {
    }

    public Mat processImage(Mat originalMat, long aSystemTimeNs) {
        return null;
    }

    public enum DisplayType {MarkedUpImage}
}
