package com.snobot.vision_app.app2018.detectors;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.snobot.vision_app.app2018.FilterParams;
import com.snobot.vision_app.app2018.IDebugLogger;
import com.snobot.vision_app.app2018.TargetComparators;
import com.snobot.vision_app.app2018.TargetLocation;

/**
 * Created by PJ on 2/20/2017.
 */

public abstract class ADetector<RawImageType> implements IDetector<RawImageType>
{

    protected static final double sTARGET_WIDTH = 2;
    protected static final double sTARGET_HEIGHT = 5;

    protected static final Point sCENTER_LINE_START = new Point(DistanceCalculationUtility.sIMAGE_WIDTH / 2, 0);
    protected static final Point sCENTER_LINE_END = new Point(DistanceCalculationUtility.sIMAGE_WIDTH / 2, DistanceCalculationUtility.sIMAGE_HEIGHT);

    protected static final Scalar sCENTER_LINE_COLOR = new Scalar(0, 255, 0);
    protected static final Scalar sBLACK_COLOR = new Scalar(0, 0, 0);
    protected static final Scalar sWHITE_COLOR = new Scalar(255, 255, 255);

    protected static final Scalar[] sCONTOUR_COLORS = new Scalar[]
    {
        new Scalar(0, 0, 255),
        new Scalar(255, 0, 0),
        new Scalar(0, 255, 255)
    };


    protected static final DecimalFormat sDF = new DecimalFormat("0.0000");

    protected DisplayType mDisplayType;
    protected FilterParams mFilterParams;

    // Saved for speed
    protected List<FilterPair> mRealTargets;
    protected List<FilterPair> mFilteredTargets;
    protected Set<TargetLocation> mTargetInfos;

    protected final IDebugLogger mLogger;

    protected static final Map<FilterResult, Scalar> sFILTERED_COLORS;

    static
    {
        sFILTERED_COLORS = new HashMap<>();
        sFILTERED_COLORS.put(FilterResult.BadWidth, new Scalar(255, 255, 255));
        sFILTERED_COLORS.put(FilterResult.BadHeight, new Scalar(190, 190, 190));
        sFILTERED_COLORS.put(FilterResult.BadVertices, new Scalar(130, 130, 130));
        sFILTERED_COLORS.put(FilterResult.BadArea, sBLACK_COLOR);
        sFILTERED_COLORS.put(FilterResult.BadPerimeter, sBLACK_COLOR);
        sFILTERED_COLORS.put(FilterResult.BadSolidarity, sBLACK_COLOR);
        sFILTERED_COLORS.put(FilterResult.BadAspectRatio, sBLACK_COLOR);
    }

    protected enum FilterResult
    {
        Success, BadWidth, BadHeight, BadVertices, BadArea, BadPerimeter, BadSolidarity, BadAspectRatio
    }

    /**
     * Constructor.
     * 
     * @param aLogger
     *            Logger interface for debugging
     */
    public ADetector(IDebugLogger aLogger)
    {
        mLogger = aLogger;
        mDisplayType = DisplayType.MarkedUpImage;
        mFilterParams = new FilterParams();

        mRealTargets = new ArrayList<>();
        mFilteredTargets = new ArrayList<>();
        mTargetInfos = new TreeSet<>(new TargetComparators.AspectRatioComparator());
    }

    public void setDisplayType(DisplayType aDisplayType)
    {
        mDisplayType = aDisplayType;
    }

    @Override
    public void setFilterParams(FilterParams aFilterParams)
    {
        mFilterParams = aFilterParams;
    }

    @Override
    public FilterParams getFilterParams()
    {
        return mFilterParams;
    }
    
    protected void filterTargets(List<FilterPair> aContours)
    {
        mTargetInfos.clear();
        mFilteredTargets.clear();

        for (int i = 0; i < aContours.size(); ++i)
        {
            FilterPair filterPair = aContours.get(i);

            if (filterPair.mResult != FilterResult.Success)
            {
                mFilteredTargets.add(filterPair);
                continue;
            }

            MatOfPoint contour = filterPair.mContour;
            Rect rect = Imgproc.boundingRect(contour);

            TargetLocation location = DistanceCalculationUtility.calculate(
                    sTARGET_WIDTH, sTARGET_HEIGHT, 
                    rect.width, rect.height, rect.x, contour);
            mTargetInfos.add(location);
        }
    }

    protected Mat getImageToDisplay(Mat aOriginalImage, Mat aHslImage, double aDistance, double aAngleToThePeg)
    {

        Mat displayImage;

        switch (mDisplayType)
        {
        case PostThreshold:
        {
            displayImage = new Mat();
            Imgproc.cvtColor(aHslImage, displayImage, Imgproc.COLOR_GRAY2RGB);
            break;
        }
        case MarkedUpImage:
        {
            displayImage = getMarkedUpPegImage(aOriginalImage, mTargetInfos, mFilteredTargets, aDistance, aAngleToThePeg);
            break;
        }
        case OriginalImage:
        default: // Intentional fallthrough
        {
            displayImage = aOriginalImage;
            break;
        }
        }
        
        return displayImage;
    }

    private Mat getMarkedUpPegImage(Mat aOriginal, Collection<TargetLocation> aTargetInfos, List<FilterPair> aFilteredTargets, double aDistance,
                                    double aOverallAngle)
    {
        Mat displayImage = new Mat();
        aOriginal.copyTo(displayImage);

        String overallInformation;
        if (Double.isNaN(aOverallAngle))
        {
            overallInformation = "No angle detected";
        }
        else
        {
            overallInformation = "Dist: " + sDF.format(aDistance) + ", Angle: " + sDF.format(aOverallAngle);
        }
        Imgproc.line(displayImage, sCENTER_LINE_START, sCENTER_LINE_END, sCENTER_LINE_COLOR, 1);
        Imgproc.putText(displayImage, overallInformation, new Point(20, 20), Core.FONT_HERSHEY_COMPLEX, .6, sWHITE_COLOR);

        int ctr = 0;
        for (TargetLocation targetInfo : aTargetInfos)
        {
            String textToDisplay = "Dist. " + sDF.format(targetInfo.getPreferredDistance()) + " Angle: " + sDF.format(targetInfo.getAngle());

            Scalar contourColor = sCONTOUR_COLORS[ctr % sCONTOUR_COLORS.length];
            Imgproc.drawContours(displayImage, Arrays.asList(targetInfo.getContour()), 0, contourColor, 3);
            Imgproc.putText(displayImage, textToDisplay, new Point(20, 20 * ctr + 50), Core.FONT_HERSHEY_COMPLEX, .6, contourColor);
            ++ctr;
        }

        for (FilterPair targetInfo : aFilteredTargets)
        {
            Scalar contourColor = sFILTERED_COLORS.get(targetInfo.mResult);
            Imgproc.drawContours(displayImage, Arrays.asList(targetInfo.mContour), 0, contourColor, 3);
        }

        if (aTargetInfos.isEmpty())
        {
            Imgproc.putText(displayImage, "No image detected", new Point(20, 50), Core.FONT_HERSHEY_COMPLEX, .6, sBLACK_COLOR);
        }

        return displayImage;
    }

    protected static class FilterPair
    {
        protected MatOfPoint mContour;
        protected FilterResult mResult;

        public FilterPair(MatOfPoint aContour, FilterResult aResult)
        {
            mContour = aContour;
            mResult = aResult;
        }
    }

    /**
     * Filters out contours that do not meet certain criteria.
     *
     * @param aInputContours
     *            is the input list of contours
     */
    protected List<FilterPair> filterContours(List<MatOfPoint> aInputContours)
    {
        final MatOfInt hull = new MatOfInt();
        mRealTargets.clear();
        // operation
        for (int i = 0; i < aInputContours.size(); i++)
        {
            mLogger.debug(getClass(), "Checking contour " + i);
            FilterResult filterResult = FilterResult.Success;

            final MatOfPoint contour = aInputContours.get(i);
            final Rect bb = Imgproc.boundingRect(contour);

            if (bb.width < mFilterParams.mMinWidth || bb.width > mFilterParams.mMaxWidth)
            {
                filterResult = FilterResult.BadWidth;
                mLogger.debug(getClass(), "  Bad Width " + bb.width + "(" + mFilterParams.mMinWidth + ", " + mFilterParams.mMaxWidth + ")");
            }

            if (bb.height < mFilterParams.mMinHeight || bb.height > mFilterParams.mMaxHeight)
            {
                filterResult = FilterResult.BadHeight;
                mLogger.debug(getClass(), "  Bad Height " + bb.height + "(" + mFilterParams.mMinHeight + ", " + mFilterParams.mMaxHeight + ")");
            }

            final double area = Imgproc.contourArea(contour);
            if (area < mFilterParams.mMinArea)
            {
                filterResult = FilterResult.BadArea;
                mLogger.debug(getClass(), "  Bad Area " + area + "(" + mFilterParams.mMinArea + ")");
            }

            double perimeter = Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true);
            if (perimeter < mFilterParams.mMinPerimeter)
            {
                filterResult = FilterResult.BadPerimeter;
                mLogger.debug(getClass(), "  Bad Perimeter " + perimeter + "(" + mFilterParams.mMinPerimeter + ")");
            }

            Imgproc.convexHull(contour, hull);
            MatOfPoint mopHull = new MatOfPoint();
            mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
            for (int j = 0; j < hull.size().height; j++)
            {
                int index = (int) hull.get(j, 0)[0];
                double[] point = new double[]
                { contour.get(index, 0)[0], contour.get(index, 0)[1] };
                mopHull.put(j, 0, point);
            }
            final double solid = 100 * area / Imgproc.contourArea(mopHull);
            if (solid < mFilterParams.mMinContoursSolidity || solid > mFilterParams.mMaxContoursSolidity)
            {
                filterResult = FilterResult.BadSolidarity;
                mLogger.debug(getClass(), "  Bad Solidarity " + solid + "(" + mFilterParams.mMinContoursSolidity + ", " + mFilterParams.mMaxContoursSolidity + ")");
            }
            if (contour.rows() < mFilterParams.mMinVertices || contour.rows() > mFilterParams.mMaxVertices)
            {
                filterResult = FilterResult.BadVertices;
                mLogger.debug(getClass(), "  Bad Vertices " + contour.rows() + "(" + mFilterParams.mMinVertices + ", " + mFilterParams.mMaxVertices + ")");
            }
            final double ratio = bb.width / (double) bb.height;
            if (ratio < mFilterParams.mMinRatio || ratio > mFilterParams.mMaxRatio)
            {
                filterResult = FilterResult.BadAspectRatio;
                mLogger.debug(getClass(), "  Bad Aspect Ratio " + ratio + "(" + mFilterParams.mMinRatio + ", " + mFilterParams.mMaxRatio + ")");
            }
            mRealTargets.add(new FilterPair(contour, filterResult));
        }

        return mRealTargets;
    }
}
