package org.snobot.leds;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.snobot.autonomous.AutonSelectionType;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.leds.patterns.AutoLightPattern;
import org.snobot.leds.patterns.ChasingColorPattern;
import org.snobot.leds.patterns.ClawLightPattern;
import org.snobot.leds.patterns.ElevatorHeightPattern;
import org.snobot.leds.patterns.PulsingColorPattern;
import org.snobot.leds.patterns.SolidColorPattern;
import org.snobot.leds.patterns.TestPattern;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SnobotLedManager implements ILedManager
{
    private static final int NUM_LEDS = 144;
    private static final int NUM_LEDS_TOP_PORTION = 14;

    private static final Color SNOBOT_BLUE = Color.blue;
    private static final Color SNOBOT_ORANGE = Color.orange;

    private final RobotBase mRobot;
    private final IOperatorJoystick mOperatorJoystick;

    private final IAddressableLedStrip mLedStrip;
    private final SendableChooser<SnobotLedPatterns> mPatternChooser;
    private final Map<SnobotLedPatterns, IAddressableLedStripPattern> mPatternMap;

    private final IAddressableLedStripPattern mDefaultPattern;

    private final ClawLightPattern mClawLight;
    private final AutoLightPattern mAutoLight;
    private final ElevatorHeightPattern mElevatorHeight;

    /**
     * Constructor.
     */
    public SnobotLedManager(RobotBase aRobot, IOperatorJoystick aOperatorJoystick)
    {
        mRobot = aRobot;
        mOperatorJoystick = aOperatorJoystick;
        mLedStrip = new DostarLedStrip(NUM_LEDS, SPI.Port.kOnboardCS0);
        mPatternChooser = new SendableChooser<>();

        for (SnobotLedPatterns pattern : SnobotLedPatterns.values())
        {
            mPatternChooser.addObject(pattern.toString(), pattern);
        }

        SmartDashboard.putData("LED Patterns", mPatternChooser);

        mDefaultPattern = new SolidColorPattern(mLedStrip, NUM_LEDS, Color.black);
        mClawLight = new ClawLightPattern(mLedStrip, NUM_LEDS, NUM_LEDS_TOP_PORTION, Color.red, Color.green);
        mAutoLight = new AutoLightPattern(mLedStrip, NUM_LEDS, NUM_LEDS_TOP_PORTION, Color.red, Color.green, Color.white);
        mElevatorHeight = new ElevatorHeightPattern(mLedStrip, NUM_LEDS, NUM_LEDS_TOP_PORTION, SNOBOT_BLUE, SNOBOT_ORANGE, new Color(0, 100, 0));

        mPatternMap = new HashMap<>();
        mPatternMap.put(SnobotLedPatterns.Off, mDefaultPattern);
        mPatternMap.put(SnobotLedPatterns.TestPattern, new TestPattern(mLedStrip, NUM_LEDS));

        mPatternMap.put(SnobotLedPatterns.SolidRed, new SolidColorPattern(mLedStrip, NUM_LEDS, Color.red));
        mPatternMap.put(SnobotLedPatterns.SolidBlue, new SolidColorPattern(mLedStrip, NUM_LEDS, Color.blue));
        mPatternMap.put(SnobotLedPatterns.SolidOrange, new SolidColorPattern(mLedStrip, NUM_LEDS, Color.orange));

        mPatternMap.put(SnobotLedPatterns.PulsingRed, new PulsingColorPattern(mLedStrip, NUM_LEDS, Color.red));
        mPatternMap.put(SnobotLedPatterns.PulsingBlue, new PulsingColorPattern(mLedStrip, NUM_LEDS, Color.blue));
        mPatternMap.put(SnobotLedPatterns.PulsingOrange, new PulsingColorPattern(mLedStrip, NUM_LEDS, Color.orange));
        
        mPatternMap.put(SnobotLedPatterns.ChasingBlue, new ChasingColorPattern(mLedStrip, NUM_LEDS, new Color(26, 152, 255), false, true, false));
        mPatternMap.put(SnobotLedPatterns.ChasingRed, new ChasingColorPattern(mLedStrip, NUM_LEDS, new Color(255, 152, 26), false, true, false));

        mPatternMap.put(SnobotLedPatterns.AutoLight, mAutoLight);
        mPatternMap.put(SnobotLedPatterns.ClawLight, mClawLight);
        mPatternMap.put(SnobotLedPatterns.PercentError, mElevatorHeight);

    }



    @Override
    public void update()
    {
        if (mOperatorJoystick.useLedChooser())
        {
            updateLedsFromChooser();
        }
        else
        {
            updateLedsFromRobotState();
        }
        mLedStrip.updateStrip();
    }

    private void updateLedsFromRobotState()
    {
        if (mRobot.isAutonomous())
        {
            mAutoLight.update();
        }
        else
        {
            mClawLight.update();
        }

        mElevatorHeight.update();
    }

    private void updateLedsFromChooser()
    {
        SnobotLedPatterns selectedPattern = mPatternChooser.getSelected();
        IAddressableLedStripPattern patternRunner = mPatternMap.get(selectedPattern);
        if (patternRunner == null)
        {
            patternRunner = mDefaultPattern;
        }

        patternRunner.update();
    }

    @Override
    public void setClawState(boolean aOpen)
    {
        mClawLight.setClawState(aOpen);
    }



    @Override
    public void setAutoSelection(AutonSelectionType aSelection)
    {

        mAutoLight.setAutoState(aSelection);

    }

    @Override
    public void setElevatorError(double aActual, double aDesired, boolean aWithinDeadband)
    {
        mElevatorHeight.setHeights(aActual, aDesired, aWithinDeadband);
    }

}
