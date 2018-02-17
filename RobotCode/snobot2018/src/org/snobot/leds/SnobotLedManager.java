package org.snobot.leds;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.snobot.leds.patterns.PulsingColorPattern;
import org.snobot.leds.patterns.SolidColorPattern;
import org.snobot.leds.patterns.TestPattern;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SnobotLedManager implements ILedManager
{
    private final IAddressableLedStrip mLedStrip;
    private final SendableChooser<SnobotLedPatterns> mPatternChooser;
    private final Map<SnobotLedPatterns, IAddressableLedStripPattern> mPatternMap;

    private final IAddressableLedStripPattern mDefaultPattern;

    /**
     * Constructor.
     */
    public SnobotLedManager()
    {
        int numLeds = 144;
        mLedStrip = new DostarLedStrip(numLeds, SPI.Port.kOnboardCS0);
        mPatternChooser = new SendableChooser<>();

        for (SnobotLedPatterns pattern : SnobotLedPatterns.values())
        {
            mPatternChooser.addObject(pattern.toString(), pattern);
        }

        SmartDashboard.putData("LED Patterns", mPatternChooser);

        // mDefaultPattern = new SolidColorPattern(mLedStrip, numLeds, 0, 0, 0);
        mDefaultPattern = new TestPattern(mLedStrip, numLeds);

        mPatternMap = new HashMap<>();
        mPatternMap.put(SnobotLedPatterns.Off, mDefaultPattern);
        mPatternMap.put(SnobotLedPatterns.TestPattern, new TestPattern(mLedStrip, numLeds));

        mPatternMap.put(SnobotLedPatterns.SolidRed, new SolidColorPattern(mLedStrip, numLeds, Color.red));
        mPatternMap.put(SnobotLedPatterns.SolidBlue, new SolidColorPattern(mLedStrip, numLeds, Color.blue));
        mPatternMap.put(SnobotLedPatterns.SolidOrange, new SolidColorPattern(mLedStrip, numLeds, Color.orange));

        mPatternMap.put(SnobotLedPatterns.PulsingRed, new PulsingColorPattern(mLedStrip, numLeds, Color.red));
        mPatternMap.put(SnobotLedPatterns.PulsingBlue, new PulsingColorPattern(mLedStrip, numLeds, Color.blue));
        mPatternMap.put(SnobotLedPatterns.PulsingOrange, new PulsingColorPattern(mLedStrip, numLeds, Color.orange));
    }

    @Override
    public void update()
    {
        SnobotLedPatterns selectedPattern = mPatternChooser.getSelected();
        IAddressableLedStripPattern patternRunner = mPatternMap.get(selectedPattern);
        if (patternRunner == null)
        {
            patternRunner = mDefaultPattern;
        }

        patternRunner.update();
        mLedStrip.updateStrip();
    }

}
