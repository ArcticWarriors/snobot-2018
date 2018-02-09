package org.snobot.shuffleboard2018.trajectory;

import java.util.HashMap;
import java.util.Map;

import org.snobot.SmartDashboardNames;
import org.snobot.shuffleboard2018.dotstar_sim.DotstarData;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class TrajectoryData extends ComplexData<DotstarData>
{
    private final String mIdealSpline;
    private final String mMeasuredSpline;
    private final String mSplineWaypoints;

    public TrajectoryData()
    {
        this("", "", "");
    }

    /**
     * Constructor.
     * 
     * @param aMap
     *            A data map, created from the widget
     */
    public TrajectoryData(Map<String, Object> aMap)
    {
        this(
                (String) aMap.getOrDefault(SmartDashboardNames.sSPLINE_IDEAL_POINTS, ""),
                (String) aMap.getOrDefault(SmartDashboardNames.sSPLINE_REAL_POINT, ""),
                (String) aMap.getOrDefault(SmartDashboardNames.sSPLINE_WAYPOINTS, ""));
    }

    /**
     * Constructor.
     * 
     * @param aIdealSpline
     *            A string representing the serialized ideal spline
     * @param aMeasuredSpline
     *            A string representing the serialized measured spline
     * @param aSplineWaypoints
     *            A string representing the serialized waypoints
     */
    public TrajectoryData(
            String aIdealSpline,
            String aMeasuredSpline,
            String aSplineWaypoints)
    {
        mIdealSpline = aIdealSpline;
        mMeasuredSpline = aMeasuredSpline;
        mSplineWaypoints = aSplineWaypoints;
    }

    @Override
    public Map<String, Object> asMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put(SmartDashboardNames.sSPLINE_IDEAL_POINTS, mIdealSpline);
        map.put(SmartDashboardNames.sSPLINE_REAL_POINT, mMeasuredSpline);
        map.put(SmartDashboardNames.sSPLINE_WAYPOINTS, mSplineWaypoints);
        return map;
    }

    public String getIdealSpline()
    {
        return mIdealSpline;
    }

    public String getMeasuredSpline()
    {
        return mMeasuredSpline;
    }

    public String getSplineWaypoints()
    {
        return mSplineWaypoints;
    }

}
