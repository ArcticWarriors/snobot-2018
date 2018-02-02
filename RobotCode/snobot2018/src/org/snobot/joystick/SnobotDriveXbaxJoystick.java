package org.snobot.joystick;

import org.snobot.lib.logging.ILogger;
import org.snobot.lib.ui.XboxButtonMap;

import edu.wpi.first.wpilibj.Joystick;

public class SnobotDriveXbaxJoystick implements IDriveJoystick
{
    private final Joystick mJoystick;
    private final ILogger mLogger;

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
    public SnobotDriveXbaxJoystick(Joystick aJoystick, ILogger aLogger)
    {
        mJoystick = aJoystick;
        mLogger = aLogger;

    }

    @Override
    public void update()
    {
        mLeftSpeed = -mJoystick.getRawAxis(XboxButtonMap.LEFT_Y_AXIS);
        mRightSpeed = -mJoystick.getRawAxis(XboxButtonMap.RIGHT_Y_AXIS);

    }

    @Override
    public void initializeLogHeaders()
    {
        mLogger.addHeader("XbaxLeftJoystickSpeed");
        mLogger.addHeader("XbaxRightJoystickSpeed");
    }

    @Override
    public void updateLog()
    {
        mLogger.updateLogger(mLeftSpeed);
        mLogger.updateLogger(mRightSpeed);
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

}
