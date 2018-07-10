package com.snobot.vision_app.app2018.detectors;

import org.opencv.core.Mat;

import com.snobot.vision_app.app2018.FilterParams;

public class NullDetector<RawImageTypeT> implements IDetector<RawImageTypeT>
{

    @Override
    public Mat process(RawImageTypeT aRawImage, Mat aOriginalImage, long aSystemTimeNs)
    {
        return aOriginalImage;
    }

    @Override
    public void setFilterParams(FilterParams aFilterParams)
    {
        // Nothing to do
    }

    @Override
    public FilterParams getFilterParams()
    {
        return null;
    }

    @Override
    public void setDisplayType(DisplayType aDisplayType)
    {
        // Nothing to do
    }

}
