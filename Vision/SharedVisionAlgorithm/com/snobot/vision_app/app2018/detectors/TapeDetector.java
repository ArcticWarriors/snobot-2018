package com.snobot.vision_app.app2018.detectors;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import com.snobot.vision_app.app2018.IDebugLogger;
import com.snobot.vision_app.app2018.TargetLocation;
import com.snobot.vision_app.app2018.grip.GripTapeAlgorithm;

/**
 * Created by Andrew Johnson on 1/25/2018.
 */


public class TapeDetector extends ADetector
{

    private final GripTapeAlgorithm mGripAlgorithm;

    /**
     * Constructor.
     * 
     * @param aLogger
     *            The logging interface used for debugging
     */
    public TapeDetector(IDebugLogger aLogger)
    {
        super(aLogger);

        mGripAlgorithm = new GripTapeAlgorithm();
    }

    @Override
    public Mat process(Mat aOriginalImage, long aSystemTimeNs)
    {
        mGripAlgorithm.process(aOriginalImage);

        List<FilterPair> contours = filterContours(mGripAlgorithm.findContoursOutput());
        filterTargets(contours);

        // Reported numbers
        double distance = 0;
        double angleToThePeg = Double.NaN;
        boolean ambgiuous = true;

        double centroidOfImageX = sIMAGE_WIDTH / 2;
        if (mTargetInfos.size() >= 2)
        {
            Iterator<TargetLocation> targetIterator = mTargetInfos.iterator();

            TargetLocation target1 = targetIterator.next();
            TargetLocation target2 = targetIterator.next();

            Rect one = Imgproc.boundingRect(target1.getContour());
            Rect two = Imgproc.boundingRect(target2.getContour());
            double centroidOfBoundingBoxOne = one.x + (one.width / 2);
            double centroidOfBoundingBoxTwo = two.x + (two.width / 2);
            double pegX = (centroidOfBoundingBoxOne + centroidOfBoundingBoxTwo) / 2;
            double pegToCenterOfImagePixels = centroidOfImageX - pegX;

            double angleToPegRadians = Math.atan((pegToCenterOfImagePixels / centroidOfImageX) * Math.tan(sHORIZONTAL_FOV_ANGLE));
            angleToThePeg = Math.toDegrees(angleToPegRadians);

            distance = (target1.getPreferredDistance() + target2.getPreferredDistance()) / 2;
            ambgiuous = false;
        }
        else if (mTargetInfos.size() == 1)
        {
            Iterator<TargetLocation> targetIterator = mTargetInfos.iterator();
            TargetLocation target = targetIterator.next();

            angleToThePeg = target.getAngle();
            distance = target.getPreferredDistance();
        }

        long currentTime = System.nanoTime();
        double latencySec = (currentTime - aSystemTimeNs) / 1e9;
        sendTargetInformation(mTargetInfos, ambgiuous, distance, angleToThePeg, latencySec);

        return getImageToDisplay(aOriginalImage, mGripAlgorithm.hslThresholdOutput(), distance, angleToThePeg);
    }

    protected void sendTargetInformation(Collection<TargetLocation> aTargetInfos, boolean aAmbigious, double aDistance, double aAngleToPeg,
            double aLatencySec)
    {
        System.out.println("Sending target info: " + aTargetInfos); // NOPMD
    }
}
