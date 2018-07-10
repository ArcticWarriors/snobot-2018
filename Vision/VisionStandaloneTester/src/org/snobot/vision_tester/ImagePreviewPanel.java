package org.snobot.vision_tester;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.snobot.vision_tester.utils.OpenCvUtilities;

public class ImagePreviewPanel extends JPanel
{
    private JLabel mOriginalImageLabel;
    private JLabel mThresholdImageLabel;

    public ImagePreviewPanel()
    {
        initComponents();
    }
    
    public void onCalculation(Mat aOriginal, Mat aPostThreshold)
    {
        mOriginalImageLabel.setIcon(new ImageIcon(OpenCvUtilities.matToImage(aOriginal)));
        mThresholdImageLabel.setIcon(new ImageIcon(OpenCvUtilities.matToImage(aPostThreshold)));
    }

    /**
     * Gets the original image.
     * 
     * @return The original image
     */
    public BufferedImage getOriginalImage()
    {
        BufferedImage output = null;

        Icon icon = mOriginalImageLabel.getIcon();
        if (icon != null)
        {
            output = (BufferedImage) ((ImageIcon) icon).getImage();
        }

        return output;
    }

    private void initComponents()
    {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]
        { 400, 400 };
        gridBagLayout.rowHeights = new int[]
        { 0, 0 };
        gridBagLayout.columnWeights = new double[]
        { 1.0, 1.0 };
        gridBagLayout.rowWeights = new double[]
        { 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        mOriginalImageLabel = new JLabel();

        GridBagConstraints gbc_originalImageLabel = new GridBagConstraints();
        gbc_originalImageLabel.anchor = GridBagConstraints.NORTHWEST;
        gbc_originalImageLabel.insets = new Insets(0, 0, 0, 5);
        gbc_originalImageLabel.gridx = 0;
        gbc_originalImageLabel.gridy = 0;
        add(mOriginalImageLabel, gbc_originalImageLabel);
        mThresholdImageLabel = new JLabel();
        GridBagConstraints gbc_thresholdImageLabel = new GridBagConstraints();
        gbc_thresholdImageLabel.anchor = GridBagConstraints.NORTHWEST;
        gbc_thresholdImageLabel.gridx = 1;
        gbc_thresholdImageLabel.gridy = 0;
        add(mThresholdImageLabel, gbc_thresholdImageLabel);
    }
}
