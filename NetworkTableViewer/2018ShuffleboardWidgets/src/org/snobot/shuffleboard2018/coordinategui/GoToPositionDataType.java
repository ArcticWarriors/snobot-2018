package org.snobot.shuffleboard2018.coordinategui;

import java.util.Map;
import java.util.function.Function;

import org.snobot.SmartDashboardNames;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

public class GoToPositionDataType extends ComplexDataType<GoToPositionData>
{
    private static final String NAME = SmartDashboardNames.sGO_TO_POSITION_TABLE_NAME;

    public GoToPositionDataType()
    {
        super(NAME, GoToPositionData.class);
    }

    @Override
    public Function<Map<String, Object>, GoToPositionData> fromMap()
    {
        return map ->
        {
            return new GoToPositionData(map);
        };
    }

    @Override
    public GoToPositionData getDefaultValue()
    {
        return new GoToPositionData();
    }

}
