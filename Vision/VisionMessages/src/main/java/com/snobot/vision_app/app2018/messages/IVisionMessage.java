package com.snobot.vision_app.app2018.messages;

import org.json.JSONException;
import org.json.JSONObject;

public interface IVisionMessage
{
    static String sTYPE_KEY = "type";

    JSONObject asJson() throws JSONException;
}
