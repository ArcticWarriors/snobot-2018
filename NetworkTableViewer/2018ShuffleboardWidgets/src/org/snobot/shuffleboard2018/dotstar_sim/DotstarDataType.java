package org.snobot.shuffleboard2018.dotstar_sim;

import java.util.Map;
import java.util.function.Function;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

public class DotstarDataType extends ComplexDataType<DotstarData>
{
    private static final String NAME = "LedSimulator";

    public DotstarDataType()
    {
        super(NAME, DotstarData.class);
    }

    @Override
    public Function<Map<String, Object>, DotstarData> fromMap()
    {
        return map ->
        {
            return new DotstarData((String) map.getOrDefault("Values", ""));
        };
    }

    @Override
    public DotstarData getDefaultValue()
    {
        return new DotstarData();
    }
}
