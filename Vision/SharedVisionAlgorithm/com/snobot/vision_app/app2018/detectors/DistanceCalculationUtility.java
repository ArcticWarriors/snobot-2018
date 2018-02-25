package com.snobot.vision_app.app2018.detectors;

import org.opencv.core.MatOfPoint;

import com.snobot.vision_app.app2018.TargetLocation;

public final class DistanceCalculationUtility
{
    public static final int sIMAGE_WIDTH = 640;
    public static final int sIMAGE_HEIGHT = 480;
    public static final double sHORIZONTAL_FOV_ANGLE = Math.toRadians(62.69 / 2);
    public static final double sVERTICAL_FOV_ANGLE = Math.toRadians(49.48 / 2);

    private DistanceCalculationUtility()
    {

    }

    /**
     * Calculates distance based on the seen targets size, the known size of the
     * image, and trig.
     * 
     * @param aTargetWidth
     *            The width of the target, in inches
     * @param aTargetHeight
     *            The height of the target, in inches
     * @param aBoxWidth
     *            The width of the bounding box, in pixels
     * @param aBoxHeight
     *            The height of the bounding box, in pixels
     * @param aBoxX
     *            The leftmost position of the bounding box
     * @param aContour
     *            A countour to put in the target location
     * @return The target location
     */
    public static TargetLocation calculate(
            double aTargetWidth, double aTargetHeight,
            double aBoxWidth, double aBoxHeight, double aBoxX,
            MatOfPoint aContour)
    {
        double aspectRatio = aBoxWidth * 1.0 / aBoxHeight;

        double distanceFromHorz = (aTargetWidth * sIMAGE_WIDTH) / (2 * aBoxWidth * Math.tan(sHORIZONTAL_FOV_ANGLE));
        double distanceFromVert = (aTargetHeight * sIMAGE_HEIGHT) / (2 * aBoxHeight * Math.tan(sVERTICAL_FOV_ANGLE));

        // Calculate the angle by calculating how far off the center the
        // bounding box is. Assume that the ratio of pixels to angle is
        // linear, meaning that it is off by the FOV
        double xCentroid = aBoxX + aBoxWidth / 2;
        double distanceFromCenterPixel = xCentroid - sIMAGE_WIDTH / 2;
        double percentOffCenter = distanceFromCenterPixel / sIMAGE_WIDTH * 100;
        double yawAngle = percentOffCenter * sHORIZONTAL_FOV_ANGLE;

        return new TargetLocation(aContour, yawAngle, distanceFromHorz, distanceFromVert, aspectRatio);
    }
}
