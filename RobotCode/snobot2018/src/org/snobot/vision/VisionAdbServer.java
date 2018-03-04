package org.snobot.vision;

import java.nio.ByteBuffer;

import org.apache.log4j.Level;
import org.json.JSONObject;
import org.snobot.Properties2018;
import org.snobot.lib.adb.IAdbBridge;
import org.snobot.lib.adb.NativeAdbBridge;
import org.snobot.lib.external_connection.RobotConnectionServer;

import com.snobot.vision_app.app2018.messages.HeartbeatMessage;
import com.snobot.vision_app.app2018.messages.IterateDisplayImageMessage;
import com.snobot.vision_app.app2018.messages.SetRecordingMessage;
import com.snobot.vision_app.app2018.messages.TargetUpdateMessage;

public class VisionAdbServer extends RobotConnectionServer
{
    private static final String sAPP_PACKAGE = "org.snobot.visionapp";
    private static final String sAPP_MAIN_ACTIVITY = "com.snobot.vision_app.app2017.SnobotVisionGLActivity";

    private static final double sTIMEOUT_PERIOD = 1.1; // Based on how often the App sends the heartbeat

    private TargetUpdateMessage mLatestTargetUpdate;
    private boolean mFreshImage;

    /**
     * Constructor.
     * 
     * @param aAppBindPort
     *            The port the app is binding to
     * @param aAppMjpegBindPort
     *            The port the MJPEG server is binding to
     * @param aAppForwardedMjpegBindPort
     *            The port to forward the MJPEG stream to
     */
    public VisionAdbServer(int aAppBindPort, int aAppMjpegBindPort, int aAppForwardedMjpegBindPort)
    {
        super(aAppBindPort, sTIMEOUT_PERIOD);

        IAdbBridge adbBridge = new NativeAdbBridge(Properties2018.sADB_PATH.getValue(), sAPP_PACKAGE, sAPP_MAIN_ACTIVITY, true);

        adbBridge.reversePortForward(aAppBindPort, aAppBindPort);
        adbBridge.portForward(aAppMjpegBindPort, aAppForwardedMjpegBindPort);

        mFreshImage = false;
    }

    @Override
    public void handleMessage(String aMessage, double aTimestamp)
    {
        Level logLevel = Level.DEBUG;

        try
        {
            JSONObject jsonObject = new JSONObject(aMessage);

            String type = (String) jsonObject.get("type");

            if (HeartbeatMessage.sMESSAGE_TYPE.equals(type))
            {
                send(new HeartbeatMessage().asJson());
            }
            else if (TargetUpdateMessage.sMESSAGE_TYPE.equals(type))
            {
                mLatestTargetUpdate = new TargetUpdateMessage(jsonObject, getTimestamp());
                mFreshImage = true;
            }
            else
            {
                sLOGGER.error("Unknown message " + type);
                logLevel = Level.ERROR;
            }
        }
        catch (Exception ex)
        {
            sLOGGER.error("Error parsing message, incoming='" + aMessage + "' - " + ex);
        }

        sLOGGER.log(logLevel, aMessage);
    }

    @Override
    public double getTimestamp()
    {
        return System.currentTimeMillis() * 1e-3;
    }

    @Override
    public void onConnected()
    {
        sLOGGER.info("App connected");
    }

    @Override
    public void onDisconnected()
    {
        sLOGGER.warn("App disconnected");
    }

    protected void send(JSONObject aObject)
    {
        String message = aObject.toString() + "\n";
        send(ByteBuffer.wrap(message.getBytes()));
    }

    public void sendStartRecordingMessage(String aMatchType, String aMatchNumber, String aMatchMode)
    {
        String baseName = aMatchType + "_" + aMatchNumber + "_" + aMatchMode;
        send(new SetRecordingMessage(true, baseName).asJson());
    }

    public void sendStopRecordingMessage()
    {
        send(new SetRecordingMessage(false).asJson());
    }

    public void iterateShownImage()
    {
        send(new IterateDisplayImageMessage().asJson());
    }

    public TargetUpdateMessage getLatestTargetUpdate()
    {
        return mLatestTargetUpdate;
    }

    /**
     * Momentary flag to check if the server has received an image since the
     * last check.
     * 
     * @return True if there has been a new image since the last check
     */
    public boolean hasFreshImage()
    {
        boolean output = mFreshImage;
        mFreshImage = false;
        return output;
    }

}
