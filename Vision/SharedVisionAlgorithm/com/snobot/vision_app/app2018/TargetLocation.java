package com.snobot.vision_app.app2018;

import org.opencv.core.MatOfPoint;

/**
 * Created by Andrew Johnson on 1/26/2018.
 */

public class TargetLocation
{
    private final MatOfPoint mContour;
    private final double mAngle;
    private final double mDistanceFromHoriz;
    private final double mDistanceFromVert;
    private final double mAspectRatio;

    /**
     * Constructor.
     * 
     * @param aContour
     *            The contour this is wrapping
     * @param aAngle
     *            The angle from the camera lens
     * @param aDistanceFromHoriz
     *            The distance, as calculated by the horizontal size
     * @param aDistanceFromVert
     *            The distance, as calculated by the vertical size
     * @param aAspectRatio
     *            The aspect ratio
     */
    public TargetLocation(MatOfPoint aContour, double aAngle, double aDistanceFromHoriz, double aDistanceFromVert, double aAspectRatio)
    {
        mContour = aContour;
        mAngle = aAngle;
        mDistanceFromHoriz = aDistanceFromHoriz;
        mDistanceFromVert = aDistanceFromVert;
        mAspectRatio = aAspectRatio;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(mAngle);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mAspectRatio);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((mContour == null) ? 0 : mContour.hashCode());
        temp = Double.doubleToLongBits(mDistanceFromHoriz);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mDistanceFromVert);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    @SuppressWarnings({"PMD.IfStmtsMustUseBraces", "PMD.SimplifyBooleanReturns"})
    public boolean equals(Object aOther)
    {
        if (this == aOther)
            return true;
        if (aOther == null)
            return false;
        if (getClass() != aOther.getClass())
            return false;
        TargetLocation other = (TargetLocation) aOther;
        if (Double.doubleToLongBits(mAngle) != Double.doubleToLongBits(other.mAngle))
            return false;
        if (Double.doubleToLongBits(mAspectRatio) != Double.doubleToLongBits(other.mAspectRatio))
            return false;
        if (mContour == null)
        {
            if (other.mContour != null)
                return false;
        }
        else if (!mContour.equals(other.mContour))
            return false;
        if (Double.doubleToLongBits(mDistanceFromHoriz) != Double.doubleToLongBits(other.mDistanceFromHoriz))
            return false;
        if (Double.doubleToLongBits(mDistanceFromVert) != Double.doubleToLongBits(other.mDistanceFromVert))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "TapeLocation [mAngle=" + mAngle + ", mDistanceFromHoriz=" + mDistanceFromHoriz + ", mDistanceFromVert="
                + mDistanceFromVert + ", mAspectRatio=" + mAspectRatio + "]";
    }

    public MatOfPoint getContour()
    {
        return mContour;
    }

    public double getAngle()
    {
        return mAngle;
    }

    public double getAspectRatio()
    {
        return mAspectRatio;
    }

    public double getPreferredDistance()
    {
        return mDistanceFromVert;
    }
}
