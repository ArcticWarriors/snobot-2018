package org.snobot.leds.patterns;

import java.awt.Color;

import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class SolidColorPattern implements IAddressableLedStripPattern
{
    private final IAddressableLedStrip mLedStrip;
    private final int mNumLeds;
    private final Color mColor;

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
    public SolidColorPattern(IAddressableLedStrip aLedStrip, int aNumLeds, Color aColor)
    {
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;
        mColor = aColor;
    }

    @Override
    public boolean update()
    {
        for (int i = 0; i < mNumLeds; ++i)
        {
            mLedStrip.setColor(i, mColor.getRed(), mColor.getGreen(), mColor.getBlue());
        }

        return true;
    }

}
