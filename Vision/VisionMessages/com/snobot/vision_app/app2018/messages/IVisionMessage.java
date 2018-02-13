package com.snobot.vision_app.app2018.messages;

import org.json.JSONException;
import org.json.JSONObject;

public interface IVisionMessage
{
    JSONObject asJson() throws JSONException;
}
