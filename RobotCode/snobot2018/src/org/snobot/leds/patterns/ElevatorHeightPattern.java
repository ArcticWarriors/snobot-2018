package org.snobot.leds.patterns;

import java.awt.Color;

import org.snobot.Properties2018;
import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class ElevatorHeightPattern implements IAddressableLedStripPattern
{
    private static final Color sDEFAULT_COLOR = Color.black;

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
            drawColor(0, mNumUsableLeds, mAtHeightColor);
        }
        else
        {
            double maxHeight = Properties2018.sELEVATOR_MAX_HEIGHT.getValue();

            int halfLeds = mNumUsableLeds / 2;

            int ledsForDesiredHeight = (int) (mDesiredHeight / maxHeight * halfLeds);
            int ledsForActualHeight = (int) (mActualHeight / maxHeight * halfLeds);

            ledsForDesiredHeight = Math.min(halfLeds, Math.max(0, ledsForDesiredHeight));
            ledsForActualHeight = Math.min(halfLeds, Math.max(0, ledsForActualHeight));

            drawColor(0, mNumUsableLeds, sDEFAULT_COLOR);

            if (ledsForDesiredHeight < ledsForActualHeight)
            {
                drawColor(halfLeds - ledsForActualHeight, ledsForActualHeight, mActualColor);
                drawColor(halfLeds, ledsForActualHeight, mActualColor);

                drawColor(halfLeds - ledsForDesiredHeight, ledsForDesiredHeight, mDesiredColor);
                drawColor(halfLeds, ledsForDesiredHeight, mDesiredColor);
            }
            else
            {
                drawColor(halfLeds - ledsForDesiredHeight, ledsForDesiredHeight, mDesiredColor);
                drawColor(halfLeds, ledsForDesiredHeight, mDesiredColor);
                
                drawColor(halfLeds - ledsForActualHeight, ledsForActualHeight, mActualColor);
                drawColor(halfLeds, ledsForActualHeight, mActualColor);
            }
        }

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
