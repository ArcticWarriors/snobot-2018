package org.snobot.shuffleboard2018.path;

import java.util.HashMap;
import java.util.Map;

import org.snobot.SmartDashboardNames;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class PathData extends ComplexData<PathData>
{
    private final String mIdealPath;
    private final String mMeasuredPathPoint;

    public PathData()
    {
        this("", "");
    }

    /**
     * Constructor.
     * 
     * @param aMap
     *            A data map, created from the widget
     */
    public PathData(Map<String, Object> aMap)
    {
        this((String) aMap.getOrDefault(SmartDashboardNames.sPATH_IDEAL_POINTS, ""),
                (String) aMap.getOrDefault(SmartDashboardNames.sPATH_POINT, ""));
    }

    /**
     * Constructor.
     * 
     * @param aIdealPath
     *            A string representing the serialized ideal spline
     * @param aMeasuredPoint
     *            A string representing the serialized measured spline
     */
    public PathData(String aIdealPath, String aMeasuredPoint)
    {
        mIdealPath = aIdealPath;
        mMeasuredPathPoint = aMeasuredPoint;
    }

    @Override
    public Map<String, Object> asMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put(SmartDashboardNames.sPATH_IDEAL_POINTS, mIdealPath);
        map.put(SmartDashboardNames.sPATH_POINT, mMeasuredPathPoint);
        return map;
    }

    public String getIdealPath()
    {
        return mIdealPath;
    }

    public String getMeasuredPathPoint()
    {
        return mMeasuredPathPoint;
    }
}
