package org.snobot.leds.patterns;

import java.awt.Color;

import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class ChasingColorPattern implements IAddressableLedStripPattern
{
    private static final int sDEFAULT_PERIOD = 100;

    private final Color mBaseColor;

    private final boolean mRedFactor;
    private final boolean mGreenFactor;
    private final boolean mBlueFactor;

    private final IAddressableLedStrip mLedStrip;
    private final int mNumLeds;

    private int mUpdateCtr;

    public ChasingColorPattern(IAddressableLedStrip aLedStrip, int aNumLeds, Color aBaseColor,
            boolean aRedFactor, boolean aGreenFactor, boolean aBlueFactor)
    {
        this(aLedStrip, aNumLeds, aBaseColor, aRedFactor, aGreenFactor, aBlueFactor,
                sDEFAULT_PERIOD);
    }

    /**
     * Chasing color Arguments.
     * 
     * @param aLedStrip
     *            the Led Strip
     * @param aNumLeds
     *            the number of LEDs
     * @param aBaseColor
     *            The base color to draw a sweep for
     * @param aRedFactor
     *            multiplies red by the multiplier
     * @param aGreenFactor
     *            multiplies green by the multiplier
     * @param aBlueFactor
     *            multiplies blue by the multiplier
     * @param aPeriod
     *            the frequency of the ChasingLights
     */
    public ChasingColorPattern(IAddressableLedStrip aLedStrip, int aNumLeds, Color aBaseColor,
            boolean aRedFactor, boolean aGreenFactor, boolean aBlueFactor, 
            int aPeriod)
    {
        mBaseColor = aBaseColor;
        mRedFactor = aRedFactor;
        mGreenFactor = aGreenFactor;
        mBlueFactor = aBlueFactor;
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;
    }

    @Override
    public boolean update()
    {
        final double width = 1.0; // bigger means wider color strips
        final double period = 4.0; // bigger means slower cycle
        final double edgeSharpness = 1.0; // bigger means less blurred edges between stripe colors

        for (int aLedIndex = 0; aLedIndex < mNumLeds; aLedIndex++)
        {
            double multiplier = Math.min(255, Math.max(0, 76.5 + edgeSharpness * 255 * Math.sin(aLedIndex / width + mUpdateCtr / period)));
            multiplier /= 255.0;

            int red = mBaseColor.getRed();
            int green = mBaseColor.getGreen();
            int blue = mBaseColor.getBlue();

            if (mRedFactor)
            {
                red *= multiplier;
            }
            if (mGreenFactor)
            {
                green = (int) (green * multiplier);
            }
            if (mBlueFactor)
            {
                blue *= multiplier;
            }
                        
            mLedStrip.setColor(aLedIndex, red, green, blue);
        }

        ++mUpdateCtr;
        return true;
    }
}
