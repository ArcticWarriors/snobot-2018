package org.snobot.leds.patterns;

import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class TestPattern implements IAddressableLedStripPattern
{
    private final IAddressableLedStrip mLedStrip;
    private final int mNumLeds;

    private int mUpdateCtr;
    private int mRed;
    private int mGreen;
    private int mBlue;

    private CurrentColor mCurrentColor;

    private enum CurrentColor
    {
        Red, Green, Blue
    }

    /**
     * Constructor.
     * 
     * @param aLedStrip
     *            The attached LED strip
     * @param aNumLeds
     *            The number of LEDs. Used to write the pattern
     */
    public TestPattern(IAddressableLedStrip aLedStrip, int aNumLeds)
    {
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;

        mCurrentColor = CurrentColor.Red;
        updateLatchedColors();
    }

    @Override
    public boolean update()
    {
        int index = mUpdateCtr % mNumLeds;

        for (int i = 0; i < mNumLeds; ++i)
        {
            if (i == index)
            {
                mLedStrip.setColor(i, mRed, mGreen, mBlue);
            }
            else
            {
                mLedStrip.setColor(i, 0, 0, 0);
            }
        }
        
        ++mUpdateCtr;
        
        if ((mUpdateCtr % mNumLeds) == 0)
        {
            CurrentColor[] values = CurrentColor.values();
            mCurrentColor = values[(mCurrentColor.ordinal() + 1) % values.length];
            updateLatchedColors();
        }

        return false;
    }

    private void updateLatchedColors()
    {
        switch (mCurrentColor)
        {
        case Blue:
            mRed = mGreen = 0;
            mBlue = 255;
            break;
        case Green:
            mRed = mBlue = 0;
            mGreen = 255;
            break;
        case Red:
            mGreen = mBlue = 0;
            mRed = 255;
            break;
        default:
            break;

        }
    }

}
