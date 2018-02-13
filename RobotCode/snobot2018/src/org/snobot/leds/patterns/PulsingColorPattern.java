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
    private final int mRed;
    private final int mGreen;
    private final int mBlue;

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
        this(aLedStrip, aNumLeds, aColor.getRed(), aColor.getGreen(), aColor.getBlue());
    }

    /**
     * Constructor.
     * 
     * @param aLedStrip
     *            The attached LED strip
     * @param aNumLeds
     *            The number of LEDs. Used to write the pattern
     * @param aRed
     *            The red component of the color, 0-255
     * @param aGreen
     *            The green component of the color, 0-255
     * @param aBlue
     *            The blue component of the color, 0-255
     */
    public PulsingColorPattern(IAddressableLedStrip aLedStrip, int aNumLeds, int aRed, int aGreen, int aBlue)
    {
        this(aLedStrip, aNumLeds, aRed, aGreen, aBlue, sDEFAULT_PERIOD);
    }

    /**
     * Constructor.
     * 
     * @param aLedStrip
     *            The attached LED strip
     * @param aNumLeds
     *            The number of LEDs. Used to write the pattern
     * @param aRed
     *            The red component of the color, 0-255
     * @param aGreen
     *            The green component of the color, 0-255
     * @param aBlue
     *            The blue component of the color, 0-255
     * @param aPeriod
     *            The period, in loops, for a full pulse cycle to take place
     */
    public PulsingColorPattern(IAddressableLedStrip aLedStrip, int aNumLeds, int aRed, int aGreen, int aBlue, int aPeriod)
    {
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;
        mRed = aRed;
        mGreen = aGreen;
        mBlue = aBlue;
        mPeriod = aPeriod;
    }

    @Override
    public boolean update()
    {
        double halfPeriod = mPeriod / 2;
        double multiplier = Math.abs((mUpdateCtr % mPeriod - halfPeriod) / halfPeriod);

        for (int i = 0; i < mNumLeds; ++i)
        {
            mLedStrip.setColor(i, (int) (mRed * multiplier), (int) (mGreen * multiplier), (int) (mBlue * multiplier));
        }

        ++mUpdateCtr;

        return true;
    }

}
