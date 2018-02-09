package org.snobot.shuffleboard2018.coordinategui;

import java.util.HashMap;
import java.util.Map;

import org.snobot.SmartDashboardNames;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class GoToPositionData extends ComplexData<GoToPositionData>
{
    private final String mStart;
    private final String mEnd;

    public GoToPositionData()
    {
        this("", "");
    }

    public GoToPositionData(Map<String, Object> aMap)
    {
        this((String) aMap.getOrDefault(SmartDashboardNames.sGO_TO_POSITION_START, ""),
                (String) aMap.getOrDefault(SmartDashboardNames.sGO_TO_POSITION_END, ""));
    }

    public GoToPositionData(String aStart, String aEnd)
    {
        mStart = aStart;
        mEnd = aEnd;
    }

    @Override
    public Map<String, Object> asMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put(SmartDashboardNames.sGO_TO_POSITION_START, mStart);
        map.put(SmartDashboardNames.sGO_TO_POSITION_END, mEnd);
        return map;
    }

    public String getStart()
    {
        return mStart;
    }

    public String getEnd()
    {
        return mEnd;
    }

}
