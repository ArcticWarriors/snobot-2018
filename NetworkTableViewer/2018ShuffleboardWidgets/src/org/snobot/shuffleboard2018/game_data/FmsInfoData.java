package org.snobot.shuffleboard2018.game_data;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class FmsInfoData extends ComplexData<FmsInfoData>
{
    private final String mGameSpecificMessage;
    private final boolean mIsRedAlliance;
    private final int mMatchNumber;

    public FmsInfoData()
    {
        this("", false, -1);
    }

    public FmsInfoData(String aGameSpecificMessage, boolean aIsRedAlliance, int aMatchNumber)
    {
        this.mGameSpecificMessage = aGameSpecificMessage;
        this.mIsRedAlliance = aIsRedAlliance;
        this.mMatchNumber = aMatchNumber;
    }

    @Override
    public Map<String, Object> asMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("GameSpecificMessage", mGameSpecificMessage);
        map.put("IsRedAlliance", mIsRedAlliance);
        map.put("MatchNumber", mMatchNumber);
        return map;
    }

    public String getGameSpecificMessage()
    {
        return mGameSpecificMessage;
    }

    public boolean isIsRedAlliance()
    {
        return mIsRedAlliance;
    }

    public int getMatchNumber()
    {
        return mMatchNumber;
    }

}
