package org.snobot.joystick;

import org.snobot.lib.logging.ILogger;
import org.snobot.lib.ui.XboxButtonMap;

import edu.wpi.first.wpilibj.Joystick;

public class SnobotDriveOperatorJoystick implements IOperatorJoystick
{
    private final Joystick mOperatorJoystick;
    private final ILogger mLogger;
    private double mElevatorSpeed;

    public SnobotDriveOperatorJoystick(Joystick aElevatorJoystick, ILogger aLogger)
    {
        mOperatorJoystick = aElevatorJoystick;
        mLogger = aLogger;
    }

    @Override
    public void update()
    {
        mElevatorSpeed = mOperatorJoystick.getRawAxis(XboxButtonMap.LEFT_Y_AXIS);

    }

    @Override
    public void initializeLogHeaders()
    {
        mLogger.addHeader("ElevatorJoystickSpeed");

    }

    @Override
    public void updateLog()
    {
        mLogger.updateLogger(mElevatorSpeed);

    }

    @Override
    public void updateSmartDashboard()
    {
        // Nothing Right Now

    }

    @Override
    public double getElevatorSpeed()
    {

        return mElevatorSpeed;
    }

}
