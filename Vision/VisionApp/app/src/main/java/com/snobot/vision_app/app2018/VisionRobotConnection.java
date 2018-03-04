package com.snobot.vision_app.app2018;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.List;

import com.snobot.vision_app.app2018.broadcastReceivers.RobotConnectionStatusBroadcastReceiver;
import com.snobot.vision_app.app2018.messages.IterateDisplayImageMessage;
import com.snobot.vision_app.app2018.messages.SetRecordingMessage;
import com.snobot.vision_app.app2018.messages.HeartbeatMessage;
import com.snobot.vision_app.app2018.messages.TargetUpdateMessage;
import com.snobot.vision_app.utils.RobotConnection;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PJ on 11/24/2016.
 */

public class VisionRobotConnection extends RobotConnection {

    private static final String sTAG = "RobotConnection";

    private final IVisionActivity mCameraActivity;

    public interface IVisionActivity
    {
        void useCamera(int aCameraId);

        void iterateDisplayType();

        void setRecording(boolean aRecord, String aBaseName);
    }


    public VisionRobotConnection(IVisionActivity aCameraActivity) {
        super();
        mCameraActivity = aCameraActivity;
    }

    @Override
    public void handleMessage(String message) {

        try
        {
            JSONObject jsonObject = new JSONObject(message);
            String type = (String) jsonObject.get("type");

            if(HeartbeatMessage.sMESSAGE_TYPE.equals(type))
            {
                mLastHeartbeatReceiveTime = System.currentTimeMillis();
            }
            else if (SetRecordingMessage.sMESSAGE_TYPE.equals(type))
            {
                Log.i(sTAG, message);
                SetRecordingMessage recordingMessage = new SetRecordingMessage(jsonObject);
                mCameraActivity.setRecording(
                        recordingMessage.shouldRecord(),
                        recordingMessage.getBaseName());
            }
            else if (IterateDisplayImageMessage.sMESSAGE_TYPE.equals(type))
            {
                Log.i(sTAG, message);
                mCameraActivity.iterateDisplayType();
            }
            else
            {
                Log.e(sTAG, "Parsing unknown messages: " + message);
            }
        }
        catch(Exception e)
        {
            Log.e(sTAG, "Couldn't parse message" + message, e);
        }
    }


    @Override
    protected void onRobotConnected() {
        Intent i = new Intent(RobotConnectionStatusBroadcastReceiver.ACTION_ROBOT_CONNECTED);
        ((Context) mCameraActivity).sendBroadcast(i);
    }

    @Override
    protected void onRobotDisconnected() {
        Intent i = new Intent(RobotConnectionStatusBroadcastReceiver.ACTION_ROBOT_DISCONNECTED);
        ((Context) mCameraActivity).sendBroadcast(i);
    }

    @Override
    public void sendHeartbeatMessage()
    {
        try
        {
            send(new HeartbeatMessage().asJson());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void sendVisionUpdate(List<TargetUpdateMessage.TargetInfo> aTargets, double aLatencySec)
    {
        try
        {
            JSONObject message = new TargetUpdateMessage(aTargets, aLatencySec).asJson();
//            Log.i(sTAG, message.toString());
            send(message);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    protected void send(JSONObject aJson)
    {
        String message = aJson.toString() + "\n";
        send(ByteBuffer.wrap(message.getBytes()));
    }
}
