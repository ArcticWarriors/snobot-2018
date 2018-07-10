package org.snobot.vision_tester;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.yaml.snakeyaml.Yaml;

import com.snobot.vision_app.app2018.CameraMode;
import com.snobot.vision_app.app2018.FilterParams;
import com.snobot.vision_app.app2018.detectors.DisplayType;

public class TestConfig
{
    public List<String> mFiles;
    public CameraMode mCameraMode;
    public FilterParams mFilterParams;
    public DisplayType mDisplayType;
    public String mTensorflowGraphFile;
    public String mTensorflowLabelsFile;

    /**
     * Constructor.
     */
    public TestConfig()
    {
        mFiles = new ArrayList<>();
        mCameraMode = CameraMode.SwitchTape;
        mDisplayType = DisplayType.MarkedUpImage;
        mFilterParams = new FilterParams();
        mTensorflowGraphFile = null;
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

    public String getTensorflowGraphFile()
    {
        return mTensorflowGraphFile;
    }

    public void setTensorflowGraphFile(String aTensorflowGraphFile)
    {
        this.mTensorflowGraphFile = aTensorflowGraphFile;
    }

    public String getTensorflowLabelsFile()
    {
        return mTensorflowLabelsFile;
    }

    public void setTensorflowLabelsFile(String aTensorflowLabelsFile)
    {
        this.mTensorflowLabelsFile = aTensorflowLabelsFile;
    }

    /**
     * Helper class to load the configuration.
     * 
     * @param aConfigFile
     *            The config file
     * @return The config from the file
     */
    public static TestConfig loadConfig(String aConfigFile)
    {
        TestConfig output = new TestConfig();

        try (FileInputStream stream = new FileInputStream(aConfigFile))
        {
            Yaml yaml = new Yaml();
            output = (TestConfig) yaml.load(stream);
        }
        catch (IOException ex)
        {
            LogManager.getLogger(TestConfig.class).log(Level.ERROR, ex);
        }

        return output;
    }

}
