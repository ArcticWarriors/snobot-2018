package org.snobot.joystick;

import org.snobot.lib.logging.ILogger;
import org.snobot.lib.ui.ToggleButton;
import org.snobot.lib.ui.XboxButtonMap;

import edu.wpi.first.wpilibj.Joystick;

public class SnobotDriveOperatorJoystick implements IOperatorJoystick
{
    private final Joystick mOperatorJoystick;
    private final ILogger mLogger;
    private double mElevatorSpeed;
    private final ToggleButton mClawButton;

    /**
     * The joystick constructor.
     * 
     * @param aElevatorJoystick
     *            The joystick for the elevator.
     * @param aLogger
     *            Logs the elevator status.
     */
    public SnobotDriveOperatorJoystick(Joystick aElevatorJoystick, ILogger aLogger)
    {
        mOperatorJoystick = aElevatorJoystick;
        mLogger = aLogger;
        mClawButton = new ToggleButton(false);
    }

    @Override
    public void update()
    {
        mElevatorSpeed = mOperatorJoystick.getRawAxis(XboxButtonMap.LEFT_Y_AXIS);
        mClawButton.update(mOperatorJoystick.getRawButton(XboxButtonMap.RIGHT_TRIGGER));
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

    @Override
    public boolean clawOpen()
    {
        return mClawButton.getState();
    }

}
