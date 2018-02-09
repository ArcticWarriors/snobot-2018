package org.snobot.shuffleboard2018.path;

import java.util.Map;
import java.util.function.Function;

import org.snobot.SmartDashboardNames;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

public class PathDataType extends ComplexDataType<PathData>
{
    private static final String NAME = SmartDashboardNames.sPATH_NAMESPACE;

    public PathDataType()
    {
        super(NAME, PathData.class);
    }

    @Override
    public Function<Map<String, Object>, PathData> fromMap()
    {
        return map ->
        {
            return new PathData(map);
        };
    }

    @Override
    public PathData getDefaultValue()
    {
        return new PathData();
    }
}
