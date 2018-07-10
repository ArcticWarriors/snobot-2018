package com.snobot.vision_app.app2018.detectors.tf;

public class Detection
{
    public final String mClassificationName;
    public final float mConfidence;
    public final BoudingBox mBoundingBox;
    public final double mAngle;
    public final double mDistanceFromHoriz;
    public final double mDistanceFromVert;
    public final double mAspectRatio;

    /**
     * Constructor.
     * 
     * @param aClassificationName
     *            The name of the target from the classifier
     * @param aConfidence
     *            The confidence the image matches the classification
     * @param aBoundingBox
     *            The bounding box, in pixels
     * @param aAngle
     *            The angle in degrees
     * @param aDistanceFromHoriz
     *            The distance calculated from the horizontal reference points
     * @param aDistanceFromVert
     *            The distance calculated from the vertical reference points
     * @param aAspectRatio
     *            The aspect ratio
     */
    public Detection(String aClassificationName, float aConfidence, final BoudingBox aBoundingBox,
            double aAngle, double aDistanceFromHoriz, double aDistanceFromVert, double aAspectRatio)
    {
        mClassificationName = aClassificationName;
        mConfidence = aConfidence;
        mBoundingBox = aBoundingBox;

        mAngle = aAngle;
        mDistanceFromHoriz = aDistanceFromHoriz;
        mDistanceFromVert = aDistanceFromVert;
        mAspectRatio = aAspectRatio;
    }


    @Override
    public String toString()
    {
        return "Detection [mClassificationName=" + mClassificationName + ", mConfidence=" + mConfidence + ", mBoundingBox=" + mBoundingBox
                + ", mAngle=" + mAngle + ", mDistanceFromHoriz=" + mDistanceFromHoriz + ", mDistanceFromVert=" + mDistanceFromVert + ", mAspectRatio="
                + mAspectRatio + "]";
    }


    public static class BoudingBox
    {
        private final double mLeft;
        private final double mTop;
        private final double mRight;
        private final double mBottom;

        /**
         * Constructor.
         * 
         * @param aTop
         *            The normalized location of the top of the bounding box
         * @param aLeft
         *            The normalized location of the left of the bounding box
         * @param aBottom
         *            The normalized location of the bottom of the bounding box
         * @param aRight
         *            The normalized location of the right of the bounding box
         */
        public BoudingBox(double aTop, double aLeft, double aBottom, double aRight)
        {
            mLeft = aLeft;
            mTop = aTop;
            mRight = aRight;
            mBottom = aBottom;
        }

        @Override
        public String toString()
        {
            return "BoudingBox [mLeft=" + mLeft + ", mTop=" + mTop + ", mRight=" + mRight + ", mBottom=" + mBottom + "]";
        }

        public double getLeft()
        {
            return mLeft;
        }

        public double getTop()
        {
            return mTop;
        }

        public double getRight()
        {
            return mRight;
        }

        public double getBottom()
        {
            return mBottom;
        }

    }

}
