package com.snobot.vision_app.app2018.detectors;

import java.util.Iterator;
import java.util.List;

import org.opencv.core.Mat;

import com.snobot.vision_app.app2018.IDebugLogger;
import com.snobot.vision_app.app2018.TargetLocation;
import com.snobot.vision_app.app2018.grip.GripCubeAlgorithm;

/**
 * Created by Andrew Johnson on 1/25/2018.
 */

public class CubeDetector<RawImageType> extends ADetector<RawImageType>
{

    private final GripCubeAlgorithm mGripAlgorithm;

    /**
     * Constructor.
     * 
     * @param aLogger
     *            The logging interface used for debugging
     */
    public CubeDetector(IDebugLogger aLogger)
    {
        super(aLogger);

        mGripAlgorithm = new GripCubeAlgorithm();
    }

    @Override
    public Mat process(RawImageType aRawImage, Mat aOriginalImage, long aSystemTimeNs)
    {
        mGripAlgorithm.process(aOriginalImage);

        List<FilterPair> contours = filterContours(mGripAlgorithm.findContoursOutput());
        filterTargets(contours);

        // Reported Numbers
        double distance = 0;
        double angleToThePeg = Double.NaN;

        if (mTargetInfos.size() >= 1)
        {
            Iterator<TargetLocation> targetIterator = mTargetInfos.iterator();
            TargetLocation target = targetIterator.next();

            angleToThePeg = target.getAngle();
            distance = target.getPreferredDistance();
        }

        return getImageToDisplay(aOriginalImage, mGripAlgorithm.hslThresholdOutput(), distance, angleToThePeg);
    }
}
