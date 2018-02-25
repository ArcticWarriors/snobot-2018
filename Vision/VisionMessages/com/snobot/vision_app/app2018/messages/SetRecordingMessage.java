package com.snobot.vision_app.app2018.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class SetRecordingMessage implements IVisionMessage
{
    public static final String sMESSAGE_TYPE = "record_images";

    private static final String sRECORD_KEY = "record";
    private static final String sMATCH_TYPE_KEY = "match_type";
    private static final String sMATCH_NUMBER_KEY = "match_number";
    private static final String sMATCH_MODE_KEY = "match_mode";

    private final boolean mRecord;
    private final String mMatchType;
    private final String mMatchNumber;
    private final String mMatchMode;

    public SetRecordingMessage(JSONObject aJson) throws JSONException
    {
        this(
                (Boolean) aJson.get(sRECORD_KEY),
                (String) aJson.get(sMATCH_TYPE_KEY), 
                (String) aJson.get(sMATCH_NUMBER_KEY), 
                (String) aJson.get(sMATCH_MODE_KEY));
    }

    public SetRecordingMessage(boolean aRecord)
    {
        this(aRecord, "", "", "");
    }
    public SetRecordingMessage(
            boolean aRecord, 
            String aMatchType, 
            String aMatchNumber, 
            String aMatchMode)
    {
        mRecord = aRecord;
        mMatchType = aMatchType;
        mMatchNumber = aMatchNumber;
        mMatchMode = aMatchMode;
    }

    public boolean shouldRecord()
    {
        return mRecord;
    }

    public String getMatchType()
    {
        return mMatchType;
    }

    public String getMatchMode()
    {
        return mMatchMode;
    }

    public String getMatchNumber()
    {
        return mMatchNumber;
    }

    @Override
    public JSONObject asJson() throws JSONException
    {
        JSONObject output = new JSONObject();

        output.put(sTYPE_KEY, "record_images");
        output.put(sRECORD_KEY, mRecord);
        output.put(sMATCH_TYPE_KEY, mMatchType);
        output.put(sMATCH_NUMBER_KEY, mMatchNumber);
        output.put(sMATCH_MODE_KEY, mMatchMode);

        return output;
    }
}
