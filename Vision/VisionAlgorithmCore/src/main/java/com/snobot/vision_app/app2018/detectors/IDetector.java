package com.snobot.vision_app.app2018.detectors;

import org.opencv.core.Mat;

import com.snobot.vision_app.app2018.FilterParams;

/**
 * Created by Andrew Johnson on 1/25/2018.
 */

public interface IDetector<RawImageTypeT>
{
    Mat process(RawImageTypeT aRawImage, Mat aOriginalImage, long aSystemTimeNs);

    void setFilterParams(FilterParams aFilterParams);

    FilterParams getFilterParams();

    void setDisplayType(DisplayType aDisplayType);

    public class NullDetector<RawImageType> implements IDetector<RawImageType>
    {

        @Override
        public Mat process(RawImageType aRawImage, Mat aOriginalImage, long aSystemTimeNs)
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
}
