package com.snobot.vision_app.app2018.messages;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TargetUpdateMessage implements IVisionMessage
{
    public static final String sMESSAGE_TYPE = "target_update";

    private final List<TargetInfo> mTargets;
    private final double mLatencySec;

    /**
     * Information on a single vision target.
     * 
     * @author PJ
     *
     */
    public static class TargetInfo
    {
        private double mAngle;
        private double mDistance;
        private boolean mAmbiguous;

        public TargetInfo()
        {
            this(0, 0, false);
        }

        /**
         * Constructor.
         * 
         * @param aAngle
         *            The angle to the target, relative to north
         * @param aDistance
         *            The distance away from the target, in inches
         * @param aAmbiguous
         *            True if the target is ambiguous
         */
        public TargetInfo(double aAngle, double aDistance, boolean aAmbiguous)
        {
            mAngle = aAngle;
            mDistance = aDistance;
            mAmbiguous = aAmbiguous;
        }

        /**
         * Constructor.
         * 
         * @param aJson
         *            The received JSON object
         */
        public TargetInfo(JSONObject aJson) throws JSONException
        {
            this(
                    Double.parseDouble(aJson.get("angle").toString()),
                    Double.parseDouble(aJson.get("distance").toString()),
                    Boolean.parseBoolean(aJson.get("ambiguous").toString()));
        }
    }

    /**
     * Constructor.
     * 
     * @param aTargets
     *            The targets detected
     * @param aLatencySec
     *            The latency during the processing chain
     */
    public TargetUpdateMessage(List<TargetInfo> aTargets, double aLatencySec)
    {
        mTargets = aTargets;
        mLatencySec = aLatencySec;
    }

    /**
     * Constructor.
     * 
     * @param aJson
     *            The received JSON message
     * @param aCurrentTime
     *            The current time, in seconds
     */
    public TargetUpdateMessage(JSONObject aJson, double aCurrentTime) throws JSONException
    {
        mTargets = new ArrayList<>();

        mLatencySec = Double.parseDouble(aJson.get("camera_latency_sec").toString());
        // mTimestamp = aCurrentTime - latency_sec;

        JSONArray targets = (JSONArray) aJson.get("targets");

        for (int i = 0; i < targets.length(); ++i)
        {
            JSONObject targetJson = targets.getJSONObject(i);
            mTargets.add(new TargetInfo(targetJson));
        }
    }

    /**
     * Gets the latency of the processing chain.
     * 
     * @return The latency in seconds
     */
    public double getLatency()
    {
        return mLatencySec;
    }

    /**
     * Gets the valid targets detected by the app.
     * 
     * @return The valid targets
     */
    public List<TargetInfo> getTargets()
    {
        return mTargets;
    }

    @Override
    public JSONObject asJson() throws JSONException
    {
        JSONObject output = new JSONObject();

        JSONArray targetJson = new JSONArray();

        for (TargetInfo targetInfo : mTargets)
        {
            JSONObject targetInfoJson = new JSONObject();
            targetInfoJson.put("angle", targetInfo.mAngle);
            targetInfoJson.put("distance", targetInfo.mDistance);
            targetInfoJson.put("ambiguous", targetInfo.mAmbiguous);
            targetJson.put(targetInfoJson);
        }

        output.put("camera_latency_sec", mLatencySec);
        output.put("targets", targetJson);
        output.put("type", sMESSAGE_TYPE);

        return output;
    }

}
