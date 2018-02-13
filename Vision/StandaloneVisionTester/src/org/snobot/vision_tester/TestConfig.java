package org.snobot.vision_tester;

import java.util.ArrayList;
import java.util.List;

import org.snobot.vision_tester.VisionTestPanel.CameraMode;

import com.snobot.vision_app.app2018.FilterParams;
import com.snobot.vision_app.app2018.detectors.ADetector.DisplayType;

public class TestConfig
{

    public List<String> mFiles;
    public CameraMode mCameraMode;
    public FilterParams mFilterParams;
    public DisplayType mDisplayType;

    /**
     * Constructor.
     */
    public TestConfig()
    {
        mFiles = new ArrayList<>();
        mCameraMode = CameraMode.SwitchTape;
        mDisplayType = DisplayType.MarkedUpImage;
        mFilterParams = new FilterParams();
    }

    public List<String> getFiles()
    {
        return mFiles;
    }

    public void setFiles(List<String> aFiles)
    {
        this.mFiles = aFiles;
    }

    public CameraMode getCameraMode()
    {
        return mCameraMode;
    }

    public void setCameraMode(CameraMode aCameraMode)
    {
        this.mCameraMode = aCameraMode;
    }

    public FilterParams getFilterParams()
    {
        return mFilterParams;
    }

    public void setFilterParams(FilterParams aFilterParams)
    {
        this.mFilterParams = aFilterParams;
    }

    public DisplayType getDisplayType()
    {
        return mDisplayType;
    }

    public void setDisplayType(DisplayType aDisplayType)
    {
        this.mDisplayType = aDisplayType;
    }

}
