package org.snobot.shuffleboard2018.dotstar_sim;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class DotstarData extends ComplexData<DotstarData>
{
    private final String mValues;

    public DotstarData()
    {
        this("");
    }

    public DotstarData(String aValues)
    {
        mValues = aValues;
    }

    @Override
    public Map<String, Object> asMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("Values", mValues);
        return map;
    }

    public String getValues()
    {
        return mValues;
    }
}
