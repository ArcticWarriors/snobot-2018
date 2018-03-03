package org.snobot.leds.patterns;

import java.awt.Color;

import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

import edu.wpi.first.wpilibj.DriverStation;

public class GameDataPattern implements IAddressableLedStripPattern
{
    private final IAddressableLedStrip mLedStrip;
    private final int mNumLeds;
    private final Color mOurColor;
    private final Color mTheirColor;

    /**
     * Constructor.
     * 
     * 
     * @param aLedStrip
     *            The attached LED strip
     * @param aNumLeds
     *            The number of LEDs. Used to write the pattern
     * @param aOurColor
     *            The color to animate our scoring platforms
     * @param aTheirColor
     *            The color to animate their scoring platforms
     */
    public GameDataPattern(IAddressableLedStrip aLedStrip, int aNumLeds, Color aOurColor, Color aTheirColor)
    {
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;
        mOurColor = aOurColor;
        mTheirColor = aTheirColor;
    }

    @Override
    public boolean update()
    {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData == null || gameData.length() != 3)
        {
            colorPortion(0, mNumLeds, Color.black);
            return false;
        }

        Color rightSwitch = gameData.charAt(0) == 'R' ? mOurColor : mTheirColor;
        Color rightScale = gameData.charAt(1) == 'R' ? mOurColor : mTheirColor;
        colorPortion(0, mNumLeds * .25, rightSwitch);
        colorPortion(mNumLeds * .25, mNumLeds * .5, rightScale);

        Color leftSwitch = gameData.charAt(0) == 'L' ? mOurColor : mTheirColor;
        Color leftScale = gameData.charAt(1) == 'L' ? mOurColor : mTheirColor;
        colorPortion(mNumLeds * .5, mNumLeds * .75, leftSwitch);
        colorPortion(mNumLeds * .75, mNumLeds * 1, leftScale);

        return false;
    }

    private void colorPortion(double aStart, double aEnd, Color aColor)
    {
        for (int i = (int) aStart; i < (int) aEnd; ++i)
        {
            mLedStrip.setColor(i, aColor.getRed(), aColor.getGreen(), aColor.getBlue());
        }
    }

}
