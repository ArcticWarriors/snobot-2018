package org.snobot.shuffleboard2018.coordinategui;

import java.util.HashMap;
import java.util.Map;

import org.snobot.SmartDashboardNames;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class CoordinateData extends ComplexData<CoordinateData>
{
    private final double mX;
    private final double mY;
    private final double mAngle;

    public CoordinateData()
    {
        this(0, 0, 0);
    }

    /**
     * Constructor.
     * 
     * @param aMap
     *            The map potentially containing the coordinate data
     */
    public CoordinateData(Map<String, Object> aMap)
    {
        this((Double) aMap.getOrDefault(SmartDashboardNames.sX_POSITION, 0.0),
                (Double) aMap.getOrDefault(SmartDashboardNames.sY_POSITION, 0.0),
                (Double) aMap.getOrDefault(SmartDashboardNames.sORIENTATION, 0.0));
    }

    /**
     * Constructor.
     * 
     * @param aX
     *            The X position
     * @param aY
     *            The Y position
     * @param aAngle
     *            The angle of the robot, relative to north
     */
    public CoordinateData(double aX, double aY, double aAngle)
    {
        mX = aX;
        mY = aY;
        mAngle = aAngle;
    }

    @Override
    public Map<String, Object> asMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put(SmartDashboardNames.sX_POSITION, mX);
        map.put(SmartDashboardNames.sY_POSITION, mY);
        map.put(SmartDashboardNames.sORIENTATION, mAngle);
        return map;
    }

    public double getX()
    {
        return mX;
    }

    public double getY()
    {
        return mY;
    }

    public double getAngle()
    {
        return mAngle;
    }
}
