package org.snobot.shuffleboard2018.auto_widget;

import java.util.Map;
import java.util.function.Function;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

public class AutonDataType extends ComplexDataType<AutonData>
{
    private static final String NAME = "AutonWidget";

    public AutonDataType()
    {
        super(NAME, AutonData.class);
    }

    @Override
    public Function<Map<String, Object>, AutonData> fromMap()
    {
        return map ->
        {
            return new AutonData(map);
        };
    }

    @Override
    public AutonData getDefaultValue()
    {
        return new AutonData();
    }

}
