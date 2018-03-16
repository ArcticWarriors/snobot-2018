package org.snobot.leds;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.snobot.SmartDashboardNames;
import org.snobot.autonomous.AutonSelectionType;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.leds.patterns.AutoLightPattern;
import org.snobot.leds.patterns.ChasingColorPattern;
import org.snobot.leds.patterns.ClawLightPattern;
import org.snobot.leds.patterns.ElevatorHeightPattern;
import org.snobot.leds.patterns.GameDataPattern;
import org.snobot.leds.patterns.PercentageCompletePattern;
import org.snobot.leds.patterns.PulsingColorPattern;
import org.snobot.leds.patterns.SmartdashboardPattern;
import org.snobot.leds.patterns.SmoothRainbow;
import org.snobot.leds.patterns.SolidColorPattern;
import org.snobot.leds.patterns.TestPattern;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SnobotLedManager implements ILedManager
{
    private static final int NUM_LEDS = 144;
    private static final int NUM_LEDS_TOP_PORTION = 3;

    private static final Color RAW_RED = Color.red;
    private static final Color RAW_GREEN = Color.green;
    private static final Color RAW_BLUE = Color.blue;
    private static final Color RAW_ORANGE = Color.orange;

    private static final Color SNOBOT_BLUE = Color.blue;
    private static final Color SNOBOT_ORANGE = Color.orange;

    private final RobotBase mRobot;
    private final IOperatorJoystick mOperatorJoystick;

    private final IAddressableLedStrip mLedStrip;
    private final SendableChooser<SnobotLedPatterns> mPatternChooser;
    private final Map<SnobotLedPatterns, IAddressableLedStripPattern> mPatternMap;

    private final IAddressableLedStripPattern mDefaultPattern;

    private final GameDataPattern mGameDataPattern;
    private final ClawLightPattern mClawLight;
    private final AutoLightPattern mAutoLight;
    private final ElevatorHeightPattern mElevatorHeight;
    private final PercentageCompletePattern mTrajectoryPattern;

    /**
     * Constructor.
     */
    public SnobotLedManager(RobotBase aRobot, IOperatorJoystick aOperatorJoystick)
    {
        mRobot = aRobot;
        mOperatorJoystick = aOperatorJoystick;
        mLedStrip = new DostarLedStrip(NUM_LEDS, SPI.Port.kMXP);
        mPatternChooser = new SendableChooser<>();

        for (SnobotLedPatterns pattern : SnobotLedPatterns.values())
        {
            if (pattern == SnobotLedPatterns.Off)
            {
                mPatternChooser.addDefault(pattern.toString(), pattern);
            }
            else
            {
                mPatternChooser.addObject(pattern.toString(), pattern);
            }
        }

        SmartDashboard.putData(SmartDashboardNames.sLED_PATTERN_CHOOSER, mPatternChooser);

        mDefaultPattern = new SolidColorPattern(mLedStrip, NUM_LEDS, Color.black);
        mClawLight = new ClawLightPattern(mLedStrip, NUM_LEDS, NUM_LEDS_TOP_PORTION, RAW_RED, RAW_GREEN);
        mAutoLight = new AutoLightPattern(mLedStrip, NUM_LEDS, NUM_LEDS_TOP_PORTION, RAW_RED, RAW_GREEN, Color.white);
        mElevatorHeight = new ElevatorHeightPattern(mLedStrip, NUM_LEDS, NUM_LEDS_TOP_PORTION, SNOBOT_BLUE, SNOBOT_ORANGE, new Color(0, 100, 0));
        mTrajectoryPattern = new PercentageCompletePattern(mLedStrip, NUM_LEDS, NUM_LEDS_TOP_PORTION, SNOBOT_BLUE);
        mGameDataPattern = new GameDataPattern(mLedStrip, NUM_LEDS, SNOBOT_BLUE, SNOBOT_ORANGE);

        mPatternMap = new HashMap<>();
        mPatternMap.put(SnobotLedPatterns.Off, mDefaultPattern);
        mPatternMap.put(SnobotLedPatterns.TestPattern, new TestPattern(mLedStrip, NUM_LEDS));
        mPatternMap.put(SnobotLedPatterns.SmoothRainbow, new SmoothRainbow(mLedStrip, NUM_LEDS, 200));
        mPatternMap.put(SnobotLedPatterns.SmartDashboardPattern, new SmartdashboardPattern(mLedStrip, NUM_LEDS));
        
        mPatternMap.put(SnobotLedPatterns.SolidRed, new SolidColorPattern(mLedStrip, NUM_LEDS, RAW_RED));
        mPatternMap.put(SnobotLedPatterns.SolidGreen, new SolidColorPattern(mLedStrip, NUM_LEDS, RAW_GREEN));
        mPatternMap.put(SnobotLedPatterns.SolidBlue, new SolidColorPattern(mLedStrip, NUM_LEDS, RAW_BLUE));
        mPatternMap.put(SnobotLedPatterns.SolidOrange, new SolidColorPattern(mLedStrip, NUM_LEDS, RAW_ORANGE));

        mPatternMap.put(SnobotLedPatterns.PulsingRed, new PulsingColorPattern(mLedStrip, NUM_LEDS, RAW_RED));
        mPatternMap.put(SnobotLedPatterns.PulsingGreen, new PulsingColorPattern(mLedStrip, NUM_LEDS, RAW_GREEN));
        mPatternMap.put(SnobotLedPatterns.PulsingBlue, new PulsingColorPattern(mLedStrip, NUM_LEDS, RAW_BLUE));
        mPatternMap.put(SnobotLedPatterns.PulsingOrange, new PulsingColorPattern(mLedStrip, NUM_LEDS, RAW_ORANGE));
        
        mPatternMap.put(SnobotLedPatterns.ChasingBlue, new ChasingColorPattern(mLedStrip, NUM_LEDS, new Color(26, 152, 255), false, true, false));
        mPatternMap.put(SnobotLedPatterns.ChasingRed, new ChasingColorPattern(mLedStrip, NUM_LEDS, new Color(255, 152, 26), false, true, false));

        mPatternMap.put(SnobotLedPatterns.AutoLight, mAutoLight);
        mPatternMap.put(SnobotLedPatterns.ClawLight, mClawLight);
        mPatternMap.put(SnobotLedPatterns.PercentError, mTrajectoryPattern);
        mPatternMap.put(SnobotLedPatterns.ElevatorPosition, mElevatorHeight);

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
        if (mRobot.isDisabled())
        {
            if(DriverStation.getInstance().isFMSAttached())
            {
                mGameDataPattern.update();
            }
            else
            {
                mPatternMap.get(SnobotLedPatterns.TestPattern);
            }
        }
        else if (mRobot.isAutonomous())
        {
            mAutoLight.update();
            mTrajectoryPattern.update();
        }
        else
        {
            mClawLight.update();
            mElevatorHeight.update();
        }

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

    @Override
    public void setTrajectoryPercentageComplete(double aPercentage)
    {
        mTrajectoryPattern.setPercentageComplete(aPercentage);
    }

}
