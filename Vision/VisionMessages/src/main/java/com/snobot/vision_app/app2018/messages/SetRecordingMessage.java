package com.snobot.vision_app.app2018.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class SetRecordingMessage implements IVisionMessage
{
    public static final String sMESSAGE_TYPE = "record_images";

    private static final String sRECORD_KEY = "record";
    private static final String sBASE_NAME_KEY = "base_name";

    private final boolean mRecord;
    private final String mBaseName;

    /**
     * Constructor.
     * 
     * @param aJson
     *            The JSON contents
     * @throws JSONException
     *             if there is an exception
     */
    public SetRecordingMessage(JSONObject aJson) throws JSONException
    {
        this(
                (Boolean) aJson.get(sRECORD_KEY),
                (String) aJson.get(sBASE_NAME_KEY));
    }

    public SetRecordingMessage(boolean aRecord)
    {
        this(aRecord, "");
    }

    public SetRecordingMessage(
            boolean aRecord, 
            String aBaseName)
    {
        mRecord = aRecord;
        mBaseName = aBaseName;
    }

    public boolean shouldRecord()
    {
        return mRecord;
    }

    public String getBaseName()
    {
        return mBaseName;
    }

    @Override
    public JSONObject asJson() throws JSONException
    {
        JSONObject output = new JSONObject();

        output.put(sTYPE_KEY, sMESSAGE_TYPE);
        output.put(sRECORD_KEY, mRecord);
        output.put(sBASE_NAME_KEY, mBaseName);

        return output;
    }
}
