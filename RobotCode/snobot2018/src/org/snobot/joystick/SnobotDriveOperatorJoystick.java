package org.snobot.joystick;

import org.snobot.Properties2018;
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
    private Double mRequestedHeight = null;

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

    public enum ElevatorHeights
    {
        FLOOR(Properties2018.sELEVATOR_FLOOR_HEIGHT.getValue()), EXCHANGE(Properties2018.sELEVATOR_EXCHANGE_HEIGHT.getValue()), SWITCH(
                Properties2018.sELEVATOR_SWITCH_HEIGHT.getValue()), SCALE_LOW(Properties2018.sELEVATOR_SCALE_HEIGHT_LOW.getValue()), SCALE_MID(
                        Properties2018.sELEVATOR_SCALE_HEIGHT.getValue()), SCALE_HIGH(Properties2018.sELEVATOR_SCALE_HEIGHT_HIGH.getValue());

        public final double mHeight;
        private ElevatorHeights(double aHeight)
        {
            mHeight = aHeight;
        }
    }

    @Override
    public void update()
    {
        mElevatorSpeed = mOperatorJoystick.getRawAxis(XboxButtonMap.LEFT_Y_AXIS);
        mClawButton.update(mOperatorJoystick.getRawButton(XboxButtonMap.RIGHT_TRIGGER));
        if (mOperatorJoystick.getPOV() == XboxButtonMap.D_PAD_LEFT)
        {
            mRequestedHeight = ElevatorHeights.SCALE_LOW.mHeight;
        }
        else if (mOperatorJoystick.getRawButton(XboxButtonMap.B_BUTTON))
        {
            mRequestedHeight = ElevatorHeights.SWITCH.mHeight;
        }
        else if (mOperatorJoystick.getRawButton(XboxButtonMap.A_BUTTON))
        {
            mRequestedHeight = ElevatorHeights.EXCHANGE.mHeight;
        }
        else if (mOperatorJoystick.getPOV() == XboxButtonMap.D_PAD_DOWN)
        {
            mRequestedHeight = ElevatorHeights.FLOOR.mHeight;
        }
        else if (mOperatorJoystick.getPOV() == XboxButtonMap.D_PAD_RIGHT)
        {
            mRequestedHeight = ElevatorHeights.SCALE_MID.mHeight;
        }
        else if (mOperatorJoystick.getPOV() == XboxButtonMap.D_PAD_UP)
        {
            mRequestedHeight = ElevatorHeights.SCALE_HIGH.mHeight;
        }
        else
        {
            mRequestedHeight = null;
        }
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
    public double getWinchSpeed()
    {
        return mOperatorJoystick.getRawAxis(XboxButtonMap.RIGHT_Y_AXIS);
    }

    public boolean clawOpen()
    {
        return mClawButton.getState();
    }

    @Override
    public Double currentPressed()
    {

        return mRequestedHeight;
    }

}
