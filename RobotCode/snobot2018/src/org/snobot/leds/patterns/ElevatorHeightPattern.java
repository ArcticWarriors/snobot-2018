package org.snobot.leds.patterns;

import java.awt.Color;

import org.snobot.Properties2018;
import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class ElevatorHeightPattern implements IAddressableLedStripPattern
{
    private final IAddressableLedStrip mLedStrip;
    private final int mNumUsableLeds;
    private final int mTopLeds;
    private final Color mActualColor;
    private final Color mDesiredColor;
    private final Color mAtHeightColor;

    private double mActualHeight;
    private double mDesiredHeight;
    private boolean mIsFinished;

    /**
     * Constructor.
     * 
     * @param aLedStrip
     *            The attached LED strip
     * @param aNumLeds
     *            The number of LEDs. Used to write the pattern
     * @param aActualColor
     *            The color to draw the "actual height" with
     * @param aDesiredColor
     *            The color to draw the "desired height" with
     * @param aAtHeightColor
     *            The color to draw when the elevator is at the right height
     */
    public ElevatorHeightPattern(IAddressableLedStrip aLedStrip, int aNumLeds, int aTopLeds, Color aActualColor, Color aDesiredColor,
            Color aAtHeightColor)
    {
        mLedStrip = aLedStrip;
        mTopLeds = aTopLeds;
        mNumUsableLeds = aNumLeds - 2 * mTopLeds;
        mActualColor = aActualColor;
        mDesiredColor = aDesiredColor;
        mAtHeightColor = aAtHeightColor;
    }

    @Override
    public boolean update()
    {
        if (mIsFinished)
        {
            drawColor(mNumUsableLeds, mAtHeightColor);
        }
        else
        {
            double maxHeight = Properties2018.sELEVATOR_MAX_HEIGHT.getValue();

            int desiredLeds = (int) (mDesiredHeight / maxHeight * mNumUsableLeds);
            int actualLeds = (int) (mActualHeight / maxHeight * mNumUsableLeds);

            desiredLeds = Math.min(mNumUsableLeds, Math.max(0, desiredLeds));
            actualLeds = Math.min(mNumUsableLeds, Math.max(0, actualLeds));

            drawColor(mNumUsableLeds, Color.black);

            if (desiredLeds < actualLeds)
            {
                drawColor(actualLeds, mActualColor);
                drawColor(desiredLeds, mDesiredColor);
            }
            else
            {
                drawColor(desiredLeds, mDesiredColor);
                drawColor(actualLeds, mActualColor);
            }
        }

        return true;
    }

    private void drawColor(int aLedCount, Color aColor)
    {

        for (int i = 0; i < aLedCount; ++i)
        {
            mLedStrip.setColor(i + mTopLeds, aColor.getRed(), aColor.getGreen(), aColor.getBlue());
        }
    }

    /**
     * Sets the heights of the elevator.
     * 
     * @param aActual
     *            The actual height of the elevator
     * @param aDesired
     *            The desired height of the elevator
     * @param aWithinDeadband
     *            If the height is within the deadband
     */
    public void setHeights(double aActual, double aDesired, boolean aWithinDeadband)
    {
        mActualHeight = aActual;
        mDesiredHeight = aDesired;
        mIsFinished = aWithinDeadband;
    }

}
