package org.snobot.vision_tester.java_algorithm;

import java.awt.image.BufferedImage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.snobot.vision_tester.TestConfig;
import org.snobot.vision_tester.utils.OpenCvUtilities;

import com.snobot.vision_app.app2018.IDebugLogger;
import com.snobot.vision_app.app2018.VisionAlgorithm;
import com.snobot.vision_app.app2018.detectors.IDetector;
import com.snobot.vision_app.app2018.detectors.ITensorFlowDetectorFactory;

public class StandaloneVisionAlgorithm
{
    private static final Logger sLOGGER = LogManager.getLogger(StandaloneVisionAlgorithm.class);
    private final VisionAlgorithm<BufferedImage> mVisionAlgorithm;

    /**
     * Constructor.
     * 
     * @param aTestConfig
     *            The test parameters used
     */
    public StandaloneVisionAlgorithm(TestConfig aTestConfig)
    {
        IDebugLogger logger = new IDebugLogger()
        {

            @Override
            public void debug(Class<?> aClass, String aMessage)
            {
                sLOGGER.log(Level.DEBUG, aMessage);
            }

            @Override
            public void info(Class<?> aClass, String aMessage)
            {
                sLOGGER.log(Level.INFO, aMessage);
            }
        };

        ITensorFlowDetectorFactory<BufferedImage> tfFactory = new ITensorFlowDetectorFactory<BufferedImage>()
        {

            @Override
            public IDetector<BufferedImage> createDetector(IDebugLogger aLogger)
            {
                if (aTestConfig.mTensorflowGraphFile == null)
                {
                    sLOGGER.log(Level.INFO, "No TensorFlow graph defined");
                    return new IDetector.NullDetector<BufferedImage>();
                }
                try
                {
                    return new TensorflowAlgorithm(aTestConfig.mTensorflowGraphFile, aTestConfig.mTensorflowLabelsFile);
                }
                catch (Exception ex)
                {
                    sLOGGER.log(Level.ERROR, "Could not load tensorflow graph/labels", ex);
                }
                return new IDetector.NullDetector<BufferedImage>();
            }
        };

        mVisionAlgorithm = new VisionAlgorithm<BufferedImage>(logger, tfFactory);

        mVisionAlgorithm.setCameraMode(aTestConfig.mCameraMode);
        mVisionAlgorithm.setDisplayType(aTestConfig.mDisplayType);
        mVisionAlgorithm.setFilterParameters(aTestConfig.mFilterParams);
    }

    /**
     * Processes an image. Converts it to a Mat first.
     * 
     * @param aOriginalImage
     *            The image
     * @return The processed, marked up image
     */
    public Mat processImage(BufferedImage aOriginalImage)
    {

        if (aOriginalImage != null)
        {
            Mat matImage = OpenCvUtilities.imageToMat(aOriginalImage);
            return mVisionAlgorithm.processImage(aOriginalImage, matImage, 0);
        }
        
        return null;
    }
}
