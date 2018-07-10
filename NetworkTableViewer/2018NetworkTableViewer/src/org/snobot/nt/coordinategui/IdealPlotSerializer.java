package org.snobot.nt.coordinategui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snobot.nt.path_plotter.PathSetpoint;

/**
 * Class able to serialize and de-serialize a list of points into a string. Used
 * to send data to/from the SmartDashboard
 * 
 * @author PJ
 *
 */
public final class IdealPlotSerializer
{
    private static final Logger sLOGGER = LogManager.getLogger(IdealPlotSerializer.class);

    /**
     * Constructor, private because they should be using the static functions.
     */
    private IdealPlotSerializer()
    {

    }

    /**
     * De-serializes a path from the SmartDashboard.
     * 
     * @param aString
     *            The string to de-serialize
     * 
     * @return The list of constructed path setpoints
     */
    public static List<PathSetpoint> deserializePath(String aString)
    {
        List<PathSetpoint> points = new ArrayList<PathSetpoint>();
        StringTokenizer tokenizer = new StringTokenizer(aString, ",");

        while (tokenizer.hasMoreElements())
        {
            PathSetpoint point = deserializePathPoint(tokenizer);
            points.add(point);
        }

        return points;
    }

    /**
     * Serializes the given path to a string, so that it can be deserialized
     * later.
     * 
     * @param aPoints
     *            The path to serialize
     * 
     * @return The string of serialized points
     */
    public static String serializePath(List<PathSetpoint> aPoints)
    {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < aPoints.size(); ++i)
        {
            output.append(serializePathPoint(aPoints.get(i)));
        }

        return output.toString();
    }

    /**
     * Serializes a single path setpoint.
     * 
     * @param aPoint
     *            The point to serialize
     * 
     * @return The point, serialized into a string
     */
    public static String serializePathPoint(PathSetpoint aPoint)
    {
        return aPoint.mPosition + "," + aPoint.mVelocity + ",";
    }

    /**
     * De-serializes a single path point.
     * 
     * @param aTokenizer
     *            The tokenizer containing the point to deserialize
     * 
     * @return The de-serialized point
     */
    public static PathSetpoint deserializePathPoint(StringTokenizer aTokenizer)
    {
        PathSetpoint point = new PathSetpoint();

        try
        {
            point.mPosition = Double.parseDouble(aTokenizer.nextToken());
            point.mVelocity = Double.parseDouble(aTokenizer.nextToken());
        }
        catch (Exception ex)
        {
            sLOGGER.log(Level.ERROR, ex);
        }

        return point;
    }
}
