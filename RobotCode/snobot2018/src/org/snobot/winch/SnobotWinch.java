package org.snobot.winch;

import org.snobot.SmartDashboardNames;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.lib.logging.CsvLogEntry;
import org.snobot.lib.logging.CsvLogger;
import org.snobot.lib.modules.ISubsystem;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SnobotWinch implements IWinch, ISubsystem
{
    private final SpeedController mWinchMotor;
    private double mWinchSpeed = 0;
    private final IOperatorJoystick mJoystick;

    private final CsvLogEntry mSpeedLog;

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
    public SnobotWinch(SpeedController aWinchMotor, IOperatorJoystick aJoystick, CsvLogger aLogger)
    {
        mWinchMotor = aWinchMotor;
        mJoystick = aJoystick;

        mSpeedLog = new CsvLogEntry("Winch.speed");
        aLogger.addEntry(mSpeedLog);
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
        mSpeedLog.update(mWinchSpeed);
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
