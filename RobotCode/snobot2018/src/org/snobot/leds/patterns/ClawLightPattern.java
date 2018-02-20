package org.snobot.leds.patterns;

import java.awt.Color;

import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class ClawLightPattern implements IAddressableLedStripPattern
{

    private final IAddressableLedStrip mLedStrip;
    private final int mNumLeds;
    private final int mNumLedsToFillPerSide;
    private final Color mOpenColor;
    private final Color mClosedColor;
    private boolean mClawOpen;

    /**
     * Constructor.
     * 
     * @param aLedStrip
     *            The Strip
     * @param aNumLeds
     *            The number of LEDs in the strip
     * @param aLedsToFillPerSide
     *            The number of LEDS this pattern should light on the ends of
     *            the strip
     * @param aOpenColor
     *            The color to draw if the claw is open
     * @param aClosedColor
     *            The color to draw if the claw is closed
     */
    public ClawLightPattern(IAddressableLedStrip aLedStrip, int aNumLeds, int aLedsToFillPerSide, Color aOpenColor, Color aClosedColor)
    {
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;
        mNumLedsToFillPerSide = aLedsToFillPerSide;
        mOpenColor = aOpenColor;
        mClosedColor = aClosedColor;
    }

    public void setClawState(boolean aOpen)
    {
        mClawOpen = aOpen;
    }

    @Override
    public boolean update()
    {
        Color color = mClawOpen ? mOpenColor : mClosedColor;

        for (int i = 0; i < mNumLedsToFillPerSide; ++i)
        {
            mLedStrip.setColor(i, color.getRed(), color.getGreen(), color.getBlue());
            mLedStrip.setColor(mNumLeds - 1 - i, color.getRed(), color.getGreen(), color.getBlue());
        }
        

        return true;
    }

}
