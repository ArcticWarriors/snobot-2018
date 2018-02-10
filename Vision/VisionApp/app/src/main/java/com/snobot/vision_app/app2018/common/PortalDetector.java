package com.snobot.vision_app.app2018.common;

import org.opencv.core.Mat;

import java.util.Collection;

/**
 * Created by Andrew Johnson on 1/25/2018.
 */

public class PortalDetector extends BaseJavaAlgorithm implements IDetector {
    @Override
    public Mat process(Mat aMat, long aSystemTimeNs) {
        return null;
    }

    @Override
    protected void sendTargetInformation(Collection<TargetLocation> targetInfos, boolean aAmbigious, double aDistance, double aAngleToPeg, double aLatencySec) {

    }
}
