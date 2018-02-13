package org.snobot.vision_tester;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import com.snobot.vision_app.app2018.IDebugLogger;
import com.snobot.vision_app.app2018.detectors.ADetector;
import com.snobot.vision_app.app2018.detectors.CubeDetector;
import com.snobot.vision_app.app2018.detectors.PortalDetector;
import com.snobot.vision_app.app2018.detectors.TapeDetector;

public class VisionTestPanel extends JPanel
{
    public enum CameraMode
    {
        SwitchTape, Cube, Exchange
    }

    protected BufferedImage mCurrentImage;

    protected CameraMode mCameraMode;
    protected Map<CameraMode, ADetector> mAlgorithms;

    private ImagePreviewPanel mImagePreview;

    /**
     * Constructor.
     * 
     * @param aTestConfig
     *            The test parameters used
     */
    public VisionTestPanel(TestConfig aTestConfig)
    {
        IDebugLogger logger = new IDebugLogger()
        {

            @Override
            public void debug(String aMessage)
            {
                System.out.println(aMessage); // NOPMD
            }
        };

        mAlgorithms = new HashMap<>();
        mAlgorithms.put(CameraMode.SwitchTape, new TapeDetector(logger));
        mAlgorithms.put(CameraMode.Cube, new CubeDetector(logger));
        mAlgorithms.put(CameraMode.Exchange, new PortalDetector(logger));

        mCameraMode = aTestConfig.mCameraMode;
        mAlgorithms.get(mCameraMode).setFilterParams(aTestConfig.mFilterParams);
        mAlgorithms.get(mCameraMode).setDisplayType(aTestConfig.mDisplayType);

        initComponents();
    }

    /**
     * Processes an image. Converts it to a Mat first.
     * 
     * @param aOriginalImage
     *            The image
     */
    public void processImage(BufferedImage aOriginalImage)
    {
        mCurrentImage = aOriginalImage;

        if (aOriginalImage != null)
        {
            byte[] pixels = ((DataBufferByte) mCurrentImage.getRaster().getDataBuffer()).getData();
            Mat matImage = new Mat(aOriginalImage.getHeight(), aOriginalImage.getWidth(), CvType.CV_8UC3);
            matImage.put(0, 0, pixels);

            try
            {
                processImage(matImage);
            }
            catch (Exception ex)
            {
                ex.printStackTrace(); // NOPMD
            }
        }
    }

    /**
     * Processes an image and updates the icons.
     * 
     * @param aOriginalImage
     *            The image
     */
    public void processImage(Mat aOriginalImage)
    {
        Mat displayImage = mAlgorithms.get(mCameraMode).process(aOriginalImage, 0);

        mImagePreview.onCalculation(aOriginalImage, displayImage);
    }

    private void initComponents()
    {
        setLayout(new BorderLayout());

        mImagePreview = new ImagePreviewPanel();
        add(mImagePreview, BorderLayout.CENTER);
    }
}
