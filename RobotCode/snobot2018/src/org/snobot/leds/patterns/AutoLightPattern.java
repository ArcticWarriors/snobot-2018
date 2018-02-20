package org.snobot.leds.patterns;

import java.awt.Color;

import org.snobot.autonomous.AutonSelectionType;
import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

public class AutoLightPattern implements IAddressableLedStripPattern
{
    private final IAddressableLedStrip mLedStrip;
    private final int mNumLeds;
    private final int mNumLedsToFillPerSide;
    private final Color mPlanAColor;
    private final Color mPlanBColor;
    private final Color mPlanDefaultColor;

    private AutonSelectionType mAutoLight = AutonSelectionType.Default;

    /**
     * Auto Light Pattern Arguments.
     * 
     * @param aLedStrip
     *            the LED strip
     * @param aNumLeds
     *            the number of LEDs
     * @param aLedsToFillPerSide
     *            the number of LEDs to consume for this pattern
     * @param aPlanAColor
     *            the color to draw for "plan a"
     * @param aPlanBColor
     *            the color to draw for "plan b"
     * @param aPlanDefaultColor
     *            the color to draw for when the original plans have been
     *            altered (and can't be used)
     */
    public AutoLightPattern(IAddressableLedStrip aLedStrip, int aNumLeds, int aLedsToFillPerSide, Color aPlanAColor, Color aPlanBColor,
            Color aPlanDefaultColor)
    {
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;
        mNumLedsToFillPerSide = aLedsToFillPerSide;

        mPlanAColor = aPlanAColor;
        mPlanBColor = aPlanBColor;
        mPlanDefaultColor = aPlanDefaultColor;
    }

    @Override
    public boolean update()
    {
        Color color;

        switch (mAutoLight)
        {
        case PlanA:
        {
            color = mPlanAColor;
            break;
        }
        case PlanB:
        {
            color = mPlanBColor;
            break;
        }

        case Default:
        default:
        {
            color = mPlanDefaultColor;
            break;
        }
        }

        for (int i = 0; i < mNumLedsToFillPerSide; ++i)
        {
            mLedStrip.setColor(i, color.getRed(), color.getGreen(), color.getBlue());
            mLedStrip.setColor(mNumLeds - 1 - i, color.getRed(), color.getGreen(), color.getBlue());
        }

        return false;

    }

    public void setAutoState(AutonSelectionType aSelection)
    {
        mAutoLight = aSelection;
    }

}
