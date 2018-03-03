package org.snobot.leds.patterns;

import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class SmoothRainbow implements IAddressableLedStripPattern
{
    private final IAddressableLedStrip mLedStrip;
    private final int mNumLeds;
    private final double mPeriod;

    private int mUpdateCtr;

    /**
     * Constructor.
     * 
     * @param aLedStrip
     *            The LED strip
     * @param aNumLeds
     *            The number of LEDS to talk to
     * @param aPeriod
     *            The period for the animation. Bigger number means longer
     *            transition
     */
    public SmoothRainbow(IAddressableLedStrip aLedStrip, int aNumLeds, double aPeriod)
    {
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;
        mPeriod = aPeriod;
    }

    @Override
    public boolean update()
    {
        for (int i = 0; i < mNumLeds; i++)
        {
            double hue = Math.abs(mUpdateCtr % mPeriod - mPeriod / 2) / (mPeriod / 2);
            mLedStrip.setLEDColorHSL(i, hue, 1, 0.5);
        }

        ++mUpdateCtr;

        return false;
    }

}
