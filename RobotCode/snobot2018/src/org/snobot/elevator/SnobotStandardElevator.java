package org.snobot.elevator;

import org.snobot.Properties2018;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.leds.ILedManager;
import org.snobot.lib.logging.ILogger;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class SnobotStandardElevator extends ASnobotElevator<SpeedController>
{
    private final Encoder mElevatorEncoder;
    private final double mKp;

    /**
     * This is the constructor for the SnobotElevator.
     * 
     * @param aElevatorMotor
     *            is a SpeedController that controls the speed of the elevator
     *            motor.
     * @param aElevatorEncoder
     *            measures the distance the elevator has moved.
     * @param aJoystick
     *            is the operator joystick.
     * @param aLogger
     *            logs the actions of the elevator in the log file.
     */
    public SnobotStandardElevator(ILedManager aLedManager, SpeedController aElevatorMotor, Encoder aElevatorEncoder, IOperatorJoystick aJoystick,
            ILogger aLogger)
    {
        super(aLedManager, aElevatorMotor, aJoystick, aLogger);

        mElevatorEncoder = aElevatorEncoder;
        mKp = Properties2018.sELEVATOR_K_P.getValue();
    }

    @Override
    protected double caluculateHeight()
    {
        return mElevatorEncoder.getDistance();
    }

    @Override
    public boolean gotoHeight(double aHeight)
    {
        double deltaHeight = aHeight - mActualHeight;

        boolean isFinished = -mHeightDeadband < deltaHeight && deltaHeight < mHeightDeadband;
        boolean isAtHeight = mDeadBandHelper.isFinished(isFinished);

        mLedManager.setElevatorError(mActualHeight, aHeight, isFinished);

        if (isAtHeight)
        {
            stop();
        }
        else
        {
            setMotorSpeed(deltaHeight * mKp);
        }
        return isAtHeight;
    }

    @Override
    public double getSpeed()
    {
        return mElevatorMotor.get();
    }

    @Override
    public void resetEncoders()
    {
        mActualHeight = 0;
        mElevatorEncoder.reset();
    }
}
