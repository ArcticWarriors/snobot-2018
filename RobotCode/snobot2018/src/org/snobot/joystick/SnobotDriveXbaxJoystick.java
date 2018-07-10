package org.snobot.joystick;

import org.snobot.lib.logging.CsvLogEntry;
import org.snobot.lib.logging.CsvLogger;
import org.snobot.lib.ui.XboxButtonMap;

import edu.wpi.first.wpilibj.Joystick;

public class SnobotDriveXbaxJoystick implements IDriveJoystick
{
    private final Joystick mJoystick;
    private final CsvLogEntry mLeftSpeedLog;
    private final CsvLogEntry mRightSpeedLog;

    // Driving Stuff
    private double mRightSpeed;
    private double mLeftSpeed;


    /**
     * This is where the joystick gets set up so that it works.
     * 
     * @param aJoystick
     *            The Joystick
     * @param aLogger
     *            The logger
     */
    public SnobotDriveXbaxJoystick(Joystick aJoystick, CsvLogger aLogger)
    {
        mJoystick = aJoystick;

        mLeftSpeedLog = new CsvLogEntry("DrivetrainJoystick.LeftJoystick");
        mRightSpeedLog = new CsvLogEntry("DrivetrainJoystick.RightJoystick");

        aLogger.addEntry(mLeftSpeedLog);
        aLogger.addEntry(mRightSpeedLog);
    }

    @Override
    public void update()
    {
        mLeftSpeed = -mJoystick.getRawAxis(XboxButtonMap.LEFT_Y_AXIS);
        mRightSpeed = -mJoystick.getRawAxis(XboxButtonMap.RIGHT_Y_AXIS);

        mLeftSpeedLog.update(mLeftSpeed);
        mRightSpeedLog.update(mRightSpeed);
    }

    @Override
    public void updateSmartDashboard()
    {
        // Nothing to do
    }

    @Override
    public double getRightspeed()
    {
        return mRightSpeed;
    }

    @Override
    public double getLeftspeed()
    {
        return mLeftSpeed;
    }

    @Override
    public boolean isSuperSlowMode()
    {
        return mJoystick.getRawButton(XboxButtonMap.RB_BUTTON);
    }

    @Override
    public boolean isSlowMode()
    {
        return mJoystick.getRawButton(XboxButtonMap.LB_BUTTON);
    }

}
