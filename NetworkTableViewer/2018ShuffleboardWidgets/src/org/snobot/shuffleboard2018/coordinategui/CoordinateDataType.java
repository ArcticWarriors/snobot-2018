package org.snobot.shuffleboard2018.coordinategui;

import java.util.Map;
import java.util.function.Function;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

public class CoordinateDataType extends ComplexDataType<CoordinateData>
{
    private static final String NAME = "RobotPosition";

    public CoordinateDataType()
    {
        super(NAME, CoordinateData.class);
    }

    @Override
    public Function<Map<String, Object>, CoordinateData> fromMap()
    {
        return map ->
        {
            return new CoordinateData(map);
        };
    }

    @Override
    public CoordinateData getDefaultValue()
    {
        return new CoordinateData();
    }
}
