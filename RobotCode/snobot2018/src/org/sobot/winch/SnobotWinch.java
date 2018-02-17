package org.sobot.winch;

import org.snobot.SmartDashboardNames;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.lib.logging.ILogger;
import org.snobot.lib.modules.ISubsystem;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SnobotWinch implements IWinch, ISubsystem
{
    private final SpeedController mWinchMotor;
    private double mWinchSpeed = 0;
    private final IOperatorJoystick mJoystick;
    private final ILogger mLogger;

    /**
     * This is the constructor for the SnobotWinch.
     * 
     * @param aWinchMotor
     *            is the SpeedController that controls the winch motor.
     * @param aJoystick
     *            is the operator joystick.
     * @param aLogger
     *            logs the actions of the winch.
     */
    public SnobotWinch(SpeedController aWinchMotor, IOperatorJoystick aJoystick, ILogger aLogger)
    {
        mWinchMotor = aWinchMotor;
        mJoystick = aJoystick;
        mLogger = aLogger;
    }

    @Override
    public void setMotorSpeed(double aSpeed)
    {
        if (aSpeed < 0)
        {
            stop();
        }
        else
        {
            mWinchMotor.set(aSpeed);
        }
    }

    @Override
    public void control()
    {
        this.setMotorSpeed(mWinchSpeed);
    }

    @Override
    public void stop()
    {
        this.setMotorSpeed(0);
    }

    @Override
    public void update()
    {
        mWinchSpeed = mJoystick.getWinchSpeed();
    }

    @Override
    public void initializeLogHeaders()
    {
        mLogger.addHeader("WinchSpeed");
    }

    @Override
    public void updateLog()
    {
        mLogger.updateLogger(mWinchSpeed);
    }

    @Override
    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber(SmartDashboardNames.sWINCH_SPEED, mWinchSpeed);
    }

    @Override
    public double getWinchSpeed()
    {
        return mWinchMotor.get();
    }
}
