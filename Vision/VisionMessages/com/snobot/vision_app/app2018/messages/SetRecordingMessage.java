package com.snobot.vision_app.app2018.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class SetRecordingMessage implements IVisionMessage
{
    public static final String sMESSAGE_TYPE = "record_images";

    private boolean mRecord;
    private String mName;

    public SetRecordingMessage(JSONObject aJson) throws JSONException
    {
        this((Boolean) aJson.get("record"), (String) aJson.get("name"));
    }

    public SetRecordingMessage(boolean aRecord, String aName)
    {
        mRecord = aRecord;
        mName = aName;
    }

    public boolean shouldRecord()
    {
        return mRecord;
    }

    public String getName()
    {
        return mName;
    }

    @Override
    public JSONObject asJson() throws JSONException
    {
        JSONObject output = new JSONObject();

        output.put("type", "record_images");
        output.put("record", mRecord);
        output.put("name", mName);

        return output;
    }
}
