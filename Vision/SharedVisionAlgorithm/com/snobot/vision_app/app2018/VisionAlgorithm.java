package com.snobot.vision_app.app2018;

import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;

import com.snobot.vision_app.app2018.detectors.CubeDetector;
import com.snobot.vision_app.app2018.detectors.DisplayType;
import com.snobot.vision_app.app2018.detectors.IDetector;
import com.snobot.vision_app.app2018.detectors.ITensorFlowDetectorFactory;
import com.snobot.vision_app.app2018.detectors.PortalDetector;
import com.snobot.vision_app.app2018.detectors.TapeDetector;

public class VisionAlgorithm<RawImageType>
{
    private final Map<CameraMode, IDetector<RawImageType>> mDetectors;
    private IDetector<RawImageType> mActiveDetector;
    private CameraMode mMode;

    /**
     * Constructor.
     * 
     * @param aDebugLogger
     *            The debug log abstraction
     * @param aTfFactory
     *            Factory for creating a tensorflow algorithm
     */
    public VisionAlgorithm(IDebugLogger aDebugLogger, ITensorFlowDetectorFactory<RawImageType> aTfFactory)
    {
        mDetectors = new HashMap<>();
        mDetectors.put(CameraMode.SwitchTape, new TapeDetector<RawImageType>(aDebugLogger));
        mDetectors.put(CameraMode.Cube, new CubeDetector<RawImageType>(aDebugLogger));
        mDetectors.put(CameraMode.Exchange, new PortalDetector<RawImageType>(aDebugLogger));
        mDetectors.put(CameraMode.Tensorflow, new PortalDetector<RawImageType>(aDebugLogger));
        mDetectors.put(CameraMode.Tensorflow, aTfFactory.createDetector(aDebugLogger));

        mMode = CameraMode.SwitchTape;
        mActiveDetector = mDetectors.get(mMode);
    }

    public Mat processImage(RawImageType aRawImage, Mat aMat, long aSystemTimeNs)
    {
        return mActiveDetector.process(aRawImage, aMat, aSystemTimeNs);
    }

    public void setFilterParameters(FilterParams aFilterParams)
    {
        mActiveDetector.setFilterParams(aFilterParams);
    }

    public void setCameraMode(CameraMode aMode)
    {
        mMode = aMode;
        mActiveDetector = mDetectors.get(aMode);
    }

    public CameraMode getCameraMode()
    {
        return mMode;
    }

    /**
     * Sets the display type for all of the detectors.
     * 
     * @param aDisplayType
     *            The display type
     */
    public void setDisplayType(DisplayType aDisplayType)
    {
        for (IDetector<RawImageType> detector : mDetectors.values())
        {
            detector.setDisplayType(aDisplayType);
        }
    }

}
