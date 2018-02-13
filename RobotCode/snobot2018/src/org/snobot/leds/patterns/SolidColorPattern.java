package org.snobot.leds.patterns;

import java.awt.Color;

import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class SolidColorPattern implements IAddressableLedStripPattern
{
    private final IAddressableLedStrip mLedStrip;
    private final int mNumLeds;
    private final int mRed;
    private final int mGreen;
    private final int mBlue;

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
    public SolidColorPattern(IAddressableLedStrip aLedStrip, int aNumLeds, int aRed, int aGreen, int aBlue)
    {
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;
        mRed = aRed;
        mGreen = aGreen;
        mBlue = aBlue;
    }

    @Override
    public boolean update()
    {
        for (int i = 0; i < mNumLeds; ++i)
        {
            mLedStrip.setColor(i, mRed, mGreen, mBlue);
        }

        return true;
    }

}
