package org.snobot.shuffleboard2018.game_data;

import java.util.Map;
import java.util.function.Function;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

public final class FmsInfoDataType extends ComplexDataType<FmsInfoData>
{
    private static final String NAME = "FMSInfo";
    
    public FmsInfoDataType()
    {
        super(NAME, FmsInfoData.class);
    }

    @Override
    public Function<Map<String, Object>, FmsInfoData> fromMap()
    {
        return map ->
        {
            return new FmsInfoData(
                    (String) map.getOrDefault("GameSpecificMessage", ""), 
                    (Boolean) map.getOrDefault("IsRedAlliance", false), 
                    ((Number) map.getOrDefault("MatchNumber", -1)).intValue());
        };
    }

    @Override
    public FmsInfoData getDefaultValue()
    {
        return new FmsInfoData();
    }


}
