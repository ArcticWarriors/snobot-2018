package com.snobot.vision_app.app2018.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class IterateDisplayImageMessage implements IVisionMessage
{
    public static final String sMESSAGE_TYPE = "iterate_display_image";

    @Override
    public JSONObject asJson() throws JSONException
    {
        JSONObject output = new JSONObject();

        output.put("type", sMESSAGE_TYPE);

        return output;
    }
}
