package org.snobot.vision_tester.utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public final class OpenCvUtilities
{
    private OpenCvUtilities()
    {

    }

    /**
     * Resizes an image if it is too big for the tester.
     * 
     * @param aImage
     *            The image
     * @return The resized image, if necessary
     */
    public static BufferedImage resizeImage(BufferedImage aImage) // NOPMD
    {

        if (aImage.getWidth() > 1280)
        {
            double scaleFactor = 640.0 / aImage.getWidth();

            BufferedImage after = new BufferedImage((int) (scaleFactor * aImage.getWidth()), (int) (scaleFactor * aImage.getHeight()),
                    BufferedImage.TYPE_3BYTE_BGR);
            AffineTransform at = new AffineTransform();
            at.scale(scaleFactor, scaleFactor);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            after = scaleOp.filter(aImage, after);

            aImage = after;
        }

        return aImage;
    }

    /**
     * Converts a java image to an openCV image.
     * 
     * @param aOriginalImage
     *            The image to convert
     * @return The converted image
     */
    public static Mat imageToMat(BufferedImage aOriginalImage)
    {
        BufferedImage currentImage = aOriginalImage;

        byte[] pixels = ((DataBufferByte) currentImage.getRaster().getDataBuffer()).getData();
        Mat matImage = new Mat(aOriginalImage.getHeight(), aOriginalImage.getWidth(), CvType.CV_8UC3);
        matImage.put(0, 0, pixels);

        return matImage;
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
