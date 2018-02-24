package com.snobot.vision_app.app2018;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

/**
 * Created by PJ on 2/19/2017.
 */

public class VisionAlgorithmPreferences
{
    private static final int sDEFAULT_HUE_MIN = 27;
    private static final int sDEFAULT_HUE_MAX = 96;
    private static final int sDEFAULT_SAT_MIN = 164;
    private static final int sDEFAULT_SAT_MAX = 255;
    private static final int sDEFAULT_LUM_MIN = 110;
    private static final int sDEFAULT_LUM_MAX = 255;

    private static final int sDEFAULT_FILTER_WIDTH_MIN = 10;
    private static final int sDEFAULT_FILTER_WIDTH_MAX = 100;
    private static final int sDEFAULT_FILTER_HEIGHT_MIN = 30;
    private static final int sDEFAULT_FILTER_HEIGHT_MAX = 480;
    private static final int sDEFAULT_FILTER_VERTICES_MIN = 0;
    private static final int sDEFAULT_FILTER_VERTICES_MAX = 200;
    private static final float sDEFAULT_FILTER_RATIO_MIN = .3f;
    private static final float sDEFAULT_FILTER_RATIO_MAX = 1f;



    private Pair<Integer, Integer> mHueRange;
    private Pair<Integer, Integer> mSatRange;
    private Pair<Integer, Integer> mLumRange;

    private Pair<Integer, Integer> mFilterWidthRange;
    private Pair<Integer, Integer> mFilterHeightRange;
    private Pair<Integer, Integer> mFilterVerticesRange;
    private Pair<Float, Float> mFilterRatioRange;

    private String mKeyPrefix;
    private SharedPreferences mPreferences;

    public VisionAlgorithmPreferences(String aKeyPrefix, Context aContext)
    {
        mKeyPrefix = aKeyPrefix;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(aContext);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.commit();

        setHueThreshold(new Pair<>(
                mPreferences.getInt(mKeyPrefix + "HueMin", sDEFAULT_HUE_MIN),
                mPreferences.getInt(mKeyPrefix + "HueMax", sDEFAULT_HUE_MAX)));

        setSatThreshold(new Pair<>(
                mPreferences.getInt(mKeyPrefix + "SatMin", sDEFAULT_SAT_MIN),
                mPreferences.getInt(mKeyPrefix + "SatMax", sDEFAULT_SAT_MAX)));

        setLumThreshold(new Pair<>(
                mPreferences.getInt(mKeyPrefix + "LumMin", sDEFAULT_LUM_MIN),
                mPreferences.getInt(mKeyPrefix + "LumMax", sDEFAULT_LUM_MAX)));

        setFilterWidthRange(new Pair<>(
                mPreferences.getInt(mKeyPrefix + "FilterWidthMin", sDEFAULT_FILTER_WIDTH_MIN),
                mPreferences.getInt(mKeyPrefix + "FilterWidthMax", sDEFAULT_FILTER_WIDTH_MAX)));

        setFilterHeightRange(new Pair<>(
                mPreferences.getInt(mKeyPrefix + "FilterHeightMin", sDEFAULT_FILTER_HEIGHT_MIN),
                mPreferences.getInt(mKeyPrefix + "FilterHeightMax", sDEFAULT_FILTER_HEIGHT_MAX)));

        setFilterVerticesRange(new Pair<>(
                mPreferences.getInt(mKeyPrefix + "FilterVerticesMin", sDEFAULT_FILTER_VERTICES_MIN),
                mPreferences.getInt(mKeyPrefix + "FilterVerticesMax", sDEFAULT_FILTER_VERTICES_MAX)));

        setFilterRatioRange(new Pair<>(
                mPreferences.getFloat("FilterRatioMin", sDEFAULT_FILTER_RATIO_MIN),
                mPreferences.getFloat("FilterRatioMax", sDEFAULT_FILTER_RATIO_MAX)));
    }

    public void setHueThreshold(Pair<Integer, Integer> aThreshold)
    {
        mHueRange = aThreshold;
        saveIntRange(mKeyPrefix + "Hue", mHueRange);
    }

    public void setSatThreshold(Pair<Integer, Integer> aThreshold)
    {
        mSatRange = aThreshold;
        saveIntRange(mKeyPrefix + "Sat", mSatRange);
    }

    public void setLumThreshold(Pair<Integer, Integer> aThreshold)
    {
        mLumRange = aThreshold;
        saveIntRange(mKeyPrefix + "Lum", mLumRange);
    }

    public void setFilterWidthRange(Pair<Integer, Integer> aFilterWidthRange) {
        mFilterWidthRange = aFilterWidthRange;
        saveIntRange(mKeyPrefix + "FilterWidth", mFilterWidthRange);
    }

    public void setFilterHeightRange(Pair<Integer, Integer> aFilterHeightRange) {
        mFilterHeightRange = aFilterHeightRange;
        saveIntRange(mKeyPrefix + "FilterHeight", mFilterHeightRange);
    }

    public void setFilterVerticesRange(Pair<Integer, Integer> aFilterVerticesRange) {
        mFilterVerticesRange = aFilterVerticesRange;
        saveIntRange(mKeyPrefix + "FilterVertices", mFilterVerticesRange);
    }

    public void setFilterRatioRange(Pair<Float, Float> aFilterRatioRange) {
        mFilterRatioRange = aFilterRatioRange;
        saveFloatRange(mKeyPrefix + "FilterRatio", mFilterRatioRange);
    }

    private void saveIntRange(String aName, Pair<Integer, Integer> aRange)
    {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(aName + "Min", aRange.first);
        editor.putInt(aName + "Max", aRange.second);
        editor.apply();
    }

    private void saveFloatRange(String aName, Pair<Float, Float> aRange)
    {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putFloat(aName + "Min", aRange.first);
        editor.putFloat(aName + "Max", aRange.second);
        editor.apply();
    }

    public Pair<Integer, Integer> getHueThreshold() {
        return mHueRange;
    }

    public Pair<Integer, Integer> getSatThreshold() {
        return mSatRange;
    }

    public Pair<Integer, Integer> getLumThreshold() {
        return mLumRange;
    }

    public Pair<Integer, Integer> getFilterWidthThreshold() {
        return mFilterWidthRange;
    }

    public Pair<Integer, Integer> getFilterHeightThreshold() {
        return mFilterHeightRange;
    }

    public Pair<Integer, Integer> getFilterVerticesThreshold() {
        return mFilterVerticesRange;
    }

    public Pair<Float, Float> getFilterRatioRange() {
        return mFilterRatioRange;
    }

    public void restoreHslDefaults() {

        mHueRange = new Pair<>(sDEFAULT_HUE_MIN, sDEFAULT_HUE_MAX);
        mSatRange = new Pair<>(sDEFAULT_SAT_MIN, sDEFAULT_SAT_MAX);
        mLumRange = new Pair<>(sDEFAULT_LUM_MIN, sDEFAULT_LUM_MAX);

        setHueThreshold(mHueRange);
        setSatThreshold(mSatRange);
        setLumThreshold(mLumRange);
    }

    public void restoreFilterDefaults()
    {
        setFilterWidthRange(new Pair<>(sDEFAULT_FILTER_WIDTH_MIN, sDEFAULT_FILTER_WIDTH_MAX));
        setFilterHeightRange(new Pair<>(sDEFAULT_FILTER_HEIGHT_MIN, sDEFAULT_FILTER_HEIGHT_MAX));
        setFilterVerticesRange(new Pair<>(sDEFAULT_FILTER_VERTICES_MIN, sDEFAULT_FILTER_VERTICES_MAX));
        setFilterRatioRange(new Pair<>(sDEFAULT_FILTER_RATIO_MIN, sDEFAULT_FILTER_RATIO_MAX));
    }


}
