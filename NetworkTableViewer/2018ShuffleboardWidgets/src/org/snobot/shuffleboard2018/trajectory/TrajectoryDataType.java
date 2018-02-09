package org.snobot.shuffleboard2018.trajectory;

import java.util.Map;
import java.util.function.Function;

import org.snobot.SmartDashboardNames;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

public class TrajectoryDataType extends ComplexDataType<TrajectoryData>
{
    private static final String NAME = SmartDashboardNames.sSPLINE_NAMESPACE;

    public TrajectoryDataType()
    {
        super(NAME, TrajectoryData.class);
    }

    @Override
    public Function<Map<String, Object>, TrajectoryData> fromMap()
    {
        return map ->
        {
            return new TrajectoryData(map);
        };
    }

    @Override
    public TrajectoryData getDefaultValue()
    {
        return new TrajectoryData();
    }
}
