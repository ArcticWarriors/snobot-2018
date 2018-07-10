package com.snobot.vision_app.app2018.messages;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TargetUpdateMessage implements IVisionMessage
{
    public static final String sMESSAGE_TYPE = "target_update";
    private static final String sLATENCY_KEY = "camera_latency_sec";
    private static final String sTARGETS_KEY = "targets";

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
        private static final String sTARGET_TYPE_KEY = "target_type";
        private static final String sANGLE_KEY = "angle";
        private static final String sDISTANCE_KEY = "distance";
        private static final String sAMBIGUOUS_KEY = "ambiguous";

        private String mTargetType;
        private double mAngle;
        private double mDistance;
        private boolean mAmbiguous;

        public TargetInfo()
        {
            this("UNKNOWN", 0, 0, false);
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
        public TargetInfo(String aTargetType, double aAngle, double aDistance, boolean aAmbiguous)
        {
            mTargetType = aTargetType;
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
                    aJson.get(sTARGET_TYPE_KEY).toString(), 
                    Double.parseDouble(aJson.get(sANGLE_KEY).toString()),
                    Double.parseDouble(aJson.get(sDISTANCE_KEY).toString()), 
                    Boolean.parseBoolean(aJson.get(sAMBIGUOUS_KEY).toString()));
        }

        @Override
        public String toString()
        {
            return "TargetInfo [mDistance=" + mDistance + ", mAngle=" + mAngle + ", mAmbiguous=" + mAmbiguous + "]";
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

        mLatencySec = Double.parseDouble(aJson.get(sLATENCY_KEY).toString());
        // mTimestamp = aCurrentTime - latency_sec;

        JSONArray targets = (JSONArray) aJson.get(sTARGETS_KEY);

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
            targetInfoJson.put(TargetInfo.sTARGET_TYPE_KEY, targetInfo.mTargetType);
            targetInfoJson.put(TargetInfo.sANGLE_KEY, targetInfo.mAngle);
            targetInfoJson.put(TargetInfo.sDISTANCE_KEY, targetInfo.mDistance);
            targetInfoJson.put(TargetInfo.sAMBIGUOUS_KEY, targetInfo.mAmbiguous);
            targetJson.put(targetInfoJson);
        }

        output.put(sLATENCY_KEY, mLatencySec);
        output.put(sTARGETS_KEY, targetJson);
        output.put(sTYPE_KEY, sMESSAGE_TYPE);

        return output;
    }

    @Override
    public String toString()
    {
        return "TargetUpdateMessage [mLatencySec=" + mLatencySec + ", mTargets=" + mTargets + "]";
    }

}
