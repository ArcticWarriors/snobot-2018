package org.snobot.leds.patterns;

import java.awt.Color;

import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class PercentageCompletePattern implements IAddressableLedStripPattern
{
    private static final Color sDEFAULT_COLOR = Color.black;

    private final IAddressableLedStrip mLedStrip;
    private final int mNumUsableLeds;
    private final int mTopLeds;
    private final Color mColor;

    private double mPercentageComplete;

    /**
     * Constructor.
     * 
     * @param aLedStrip
     *            The attached LED strip
     * @param aNumLeds
     *            The number of LEDs. Used to write the pattern
     * @param aColor
     *            The color to draw
     */
    public PercentageCompletePattern(IAddressableLedStrip aLedStrip, int aNumLeds, int aTopLeds, Color aColor)
    {
        mLedStrip = aLedStrip;
        mTopLeds = aTopLeds;
        mNumUsableLeds = aNumLeds - 2 * mTopLeds;
        mColor = aColor;
    }

    @Override
    public boolean update()
    {

        drawColor(0, mNumUsableLeds, sDEFAULT_COLOR);

        int halfLeds = mNumUsableLeds / 2;
        int ledsToColor = (int) (halfLeds * mPercentageComplete);

        drawColor(halfLeds - ledsToColor, ledsToColor, mColor);
        drawColor(halfLeds, ledsToColor, mColor);

        return true;
    }

    private void drawColor(int aStart, int aLedCount, Color aColor)
    {
        for (int i = aStart; i < (aStart + aLedCount); ++i)
        {
            mLedStrip.setColor(i + mTopLeds, aColor.getRed(), aColor.getGreen(), aColor.getBlue());
        }
    }

    /**
     * Sets the percentage of completion (0-1).
     * 
     * @param aPercentage
     *            The percenatage
     */
    public void setPercentageComplete(double aPercentage)
    {
        mPercentageComplete = aPercentage;
    }

}
