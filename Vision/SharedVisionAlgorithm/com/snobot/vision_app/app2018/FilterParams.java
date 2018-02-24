package com.snobot.vision_app.app2018;

/**
 * Created by Andrew Johnson on 1/25/2018.
 */

public class FilterParams
{
    public double mMinArea = 0.0;
    public double mMinPerimeter = 0.0;

    public double mMinWidth = 0.0;
    public double mMaxWidth = 0.0;

    public double mMinHeight = 0.0;
    public double mMaxHeight = 0.0;

    public double mMinContoursSolidity = 0;
    public double mMaxContoursSolidity = 0;

    public double mMaxVertices = 0.0;
    public double mMinVertices = 0.0;

    public double mMinRatio = 0.0;
    public double mMaxRatio = 0.0;

    @Override
    public String toString()
    {
        return "FilterParams{" + "mMinArea=" + mMinArea + ", mMinPerimeter=" + mMinPerimeter + ", mMinWidth=" + mMinWidth + ", mMaxWidth=" + mMaxWidth
                + ", mMinHeight=" + mMinHeight + ", mMaxHeight=" + mMaxHeight + ", mMinContoursSolidity=" + mMinContoursSolidity
                + ", mMaxContoursSolidity=" + mMaxContoursSolidity + ", mMaxVertices=" + mMaxVertices + ", mMinVertices=" + mMinVertices
                + ", mMinRatio=" + mMinRatio + ", mMaxRatio=" + mMaxRatio + '}';
    }
}
