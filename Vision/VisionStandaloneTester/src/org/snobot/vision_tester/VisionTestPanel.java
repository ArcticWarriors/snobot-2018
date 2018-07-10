package org.snobot.vision_tester;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.snobot.vision_tester.java_algorithm.StandaloneVisionAlgorithm;
import org.snobot.vision_tester.utils.OpenCvUtilities;

public class VisionTestPanel extends JPanel
{
    private final StandaloneVisionAlgorithm mVisionAlgorithm;
    private ImagePreviewPanel mImagePreview;

    /**
     * Constructor.
     * 
     * @param aTestConfig
     *            The test parameters used
     */
    public VisionTestPanel(TestConfig aTestConfig)
    {
        mVisionAlgorithm = new StandaloneVisionAlgorithm(aTestConfig);
        initComponents();
    }


    private void initComponents()
    {
        setLayout(new BorderLayout());

        mImagePreview = new ImagePreviewPanel();
        add(mImagePreview, BorderLayout.CENTER);
    }

    /**
     * Processes an image. Converts it to a Mat first.
     * 
     * @param aOriginalImage
     *            The image
     */
    public void processImage(BufferedImage aOriginalImage)
    {
        try
        {
            Mat matImage = OpenCvUtilities.imageToMat(aOriginalImage);
            Mat output = mVisionAlgorithm.processImage(aOriginalImage);
            mImagePreview.onCalculation(matImage, output);
        }
        catch (Exception ex)
        {
            Logger.getLogger(VisionTestPanel.class).log(Level.ERROR, ex);
        }
    }
}
