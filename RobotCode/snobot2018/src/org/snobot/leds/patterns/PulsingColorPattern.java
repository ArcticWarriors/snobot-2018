package org.snobot.leds.patterns;

import java.awt.Color;

import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class PulsingColorPattern implements IAddressableLedStripPattern
{
    private static final int sDEFAULT_PERIOD = 100; // two second cycle time

    private final IAddressableLedStrip mLedStrip;
    private final double mPeriod;
    private final int mNumLeds;
    private final Color mColor;

    private int mUpdateCtr;

    /**
     * Constructor.
     * 
     * @param aLedStrip
     *            The attached LED strip
     * @param aNumLeds
     *            The number of LEDs. Used to write the pattern
     * @param aColor
     *            The color to pulse
     */
    public PulsingColorPattern(IAddressableLedStrip aLedStrip, int aNumLeds, Color aColor)
    {
        this(aLedStrip, aNumLeds, aColor, sDEFAULT_PERIOD);
    }

    /**
     * Constructor.
     * 
     * @param aLedStrip
     *            The attached LED strip
     * @param aNumLeds
     *            The number of LEDs. Used to write the pattern
     * @param aColor
     *            The color to draw
     * @param aPeriod
     *            The period, in loops, for a full pulse cycle to take place
     */
    public PulsingColorPattern(IAddressableLedStrip aLedStrip, int aNumLeds, Color aColor, int aPeriod)
    {
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;
        mColor = aColor;
        mPeriod = aPeriod;
    }

    @Override
    public boolean update()
    {
        double halfPeriod = mPeriod / 2;
        double multiplier = Math.abs((mUpdateCtr % mPeriod - halfPeriod) / halfPeriod);

        for (int i = 0; i < mNumLeds; ++i)
        {
            mLedStrip.setColor(i, 
                    (int) (mColor.getRed() * multiplier), 
                    (int) (mColor.getGreen() * multiplier),
                    (int) (mColor.getBlue() * multiplier));
        }

        ++mUpdateCtr;

        return true;
    }

}
