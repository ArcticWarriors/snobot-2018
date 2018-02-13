package org.snobot.vision_tester;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Mat;

public final class OpenCvUtilities
{
    private OpenCvUtilities()
    {

    }

    /**
     * Converts an opencv Mat to a java image.
     * 
     * @param aMat
     *            The mat to convert
     * @return The converted image
     */
    public static BufferedImage matToImage(Mat aMat)
    {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (aMat.channels() > 1)
        {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = aMat.channels() * aMat.cols() * aMat.rows();
        byte[] buffer = new byte[bufferSize];
        aMat.get(0, 0, buffer); // get all the pixels
        BufferedImage image = new BufferedImage(aMat.cols(), aMat.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
}
