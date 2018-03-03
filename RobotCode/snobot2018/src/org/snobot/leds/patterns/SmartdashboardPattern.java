package org.snobot.leds.patterns;

import org.snobot.SmartDashboardNames;
import org.snobot.leds.IAddressableLedStrip;
import org.snobot.leds.IAddressableLedStripPattern;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartdashboardPattern implements IAddressableLedStripPattern
{
    private final IAddressableLedStrip mLedStrip;
    private final int mNumLeds;

    /**
     * Constructor.
     * 
     * @param aLedStrip
     *            The LED strip
     * @param aNumLeds
     *            The number of LED's to animate
     */
    public SmartdashboardPattern(IAddressableLedStrip aLedStrip, int aNumLeds)
    {
        mLedStrip = aLedStrip;
        mNumLeds = aNumLeds;

        SmartDashboard.putNumber(SmartDashboardNames.sLED_SD_PATTERN_RED, 0);
        SmartDashboard.putNumber(SmartDashboardNames.sLED_SD_PATTERN_GREEN, 0);
        SmartDashboard.putNumber(SmartDashboardNames.sLED_SD_PATTERN_BLUE, 0);
    }

    @Override
    public boolean update()
    {
        int red = (int) SmartDashboard.getNumber(SmartDashboardNames.sLED_SD_PATTERN_RED, 0);
        int green = (int) SmartDashboard.getNumber(SmartDashboardNames.sLED_SD_PATTERN_GREEN, 0);
        int blue = (int) SmartDashboard.getNumber(SmartDashboardNames.sLED_SD_PATTERN_BLUE, 0);

        for (int i = 0; i < mNumLeds; ++i)
        {
            mLedStrip.setColor(i, red, green, blue);
        }

        return false;
    }

}
