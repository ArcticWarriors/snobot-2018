package org.snobot.nt.coordinategui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.snobot.nt.spline_plotter.SplineSegment;

/**
 * Serializes a 2D cubic spline into a string, to be used by the SmartDashboard.
 * 
 * @author PJ
 *
 */
public final class IdealSplineSerializer
{
    private static final Logger sLOGGER = Logger.getLogger(IdealSplineSerializer.class);

    /**
     * Constructor, private because the static functions should be used.
     */
    private IdealSplineSerializer()
    {

    }

    /**
     * De-Serializes a list of spline segments from the given string.
     * 
     * @param aString
     *            The string to de-serialize
     * 
     * @return The path to drive
     */
    public static List<SplineSegment> deserializePath(String aString)
    {
        List<SplineSegment> points = new ArrayList<SplineSegment>();
        StringTokenizer tokenizer = new StringTokenizer(aString, ",");

        while (tokenizer.hasMoreElements())
        {
            SplineSegment segment = deserializePathPoint(tokenizer);
            points.add(segment);
        }

        return points;
    }

    /**
     * Serializes a path of spline points into a string.
     * 
     * @param aPoints
     *            The list of points
     * 
     * @return The serialized string
     */
    public static String serializePath(List<SplineSegment> aPoints)
    {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < aPoints.size(); ++i)
        {
            output.append(serializePathPoint(aPoints.get(i)));
        }

        return output.toString();
    }

    /**
     * Serializes a single spline point.
     * 
     * @param aPoint
     *            The piont to serialize
     * 
     * @return The serialized point
     */
    public static String serializePathPoint(SplineSegment aPoint)
    {
        return aPoint.mLeftSidePosition + ", "
                + aPoint.mLeftSideVelocity + ", "
                + aPoint.mRightSidePosition + ", "
                + aPoint.mRightSideVelocity + ", "
                + aPoint.mRobotHeading + ","
                + aPoint.mAverageX + ","
                + aPoint.mAverageY + ",";
    }

    /**
     * De-serializes a spline point from the given string.
     * 
     * @param aTokenizer
     *            The tokenizer containing the point to parse
     * 
     * @return The de-serialized point
     */
    public static SplineSegment deserializePathPoint(StringTokenizer aTokenizer)
    {
        SplineSegment point = new SplineSegment();

        try
        {
            point.mLeftSidePosition = Double.parseDouble(aTokenizer.nextToken());
            point.mLeftSideVelocity = Double.parseDouble(aTokenizer.nextToken());
            point.mRightSidePosition = Double.parseDouble(aTokenizer.nextToken());
            point.mRightSideVelocity = Double.parseDouble(aTokenizer.nextToken());
            point.mRobotHeading = Double.parseDouble(aTokenizer.nextToken());
            point.mAverageX = Double.parseDouble(aTokenizer.nextToken());
            point.mAverageY = Double.parseDouble(aTokenizer.nextToken());
        }
        catch (Exception ex)
        {
            sLOGGER.log(Level.ERROR, ex);
        }

        return point;
    }
}
