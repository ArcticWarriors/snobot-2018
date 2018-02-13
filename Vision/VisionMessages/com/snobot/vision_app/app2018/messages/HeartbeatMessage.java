package com.snobot.vision_app.app2018.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class HeartbeatMessage implements IVisionMessage
{
    public static final String sMESSAGE_TYPE = "heartbeat";

    @Override
    public JSONObject asJson() throws JSONException
    {
        JSONObject output = new JSONObject();

        output.put("type", sMESSAGE_TYPE);

        return output;
    }
}
