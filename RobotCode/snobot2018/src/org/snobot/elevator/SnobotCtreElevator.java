package org.snobot.elevator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.snobot.PortMappings2018;
import org.snobot.Properties2018;
import org.snobot.joystick.IOperatorJoystick;
import org.snobot.lib.logging.ILogger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class SnobotCtreElevator extends ASnobotElevator<WPI_TalonSRX>
{
    protected static final Logger sLOGGER = Logger.getLogger(SnobotCtreElevator.class);

    private static final int sDEFAULT_PID_SLOT = 0;
    private static final int sTIMEOUT = PortMappings2018.sDEFAULT_CTRE_TIMEOUT;

    private double mConversionFactor;

    /**
     * This is the constructor for the SnobotElevator.
     * 
     * @param aElevatorMotor
     *            is a SpeedController that controls the speed of the elevator
     *            motor.
     * @param aJoystick
     *            is the operator joystick.
     * @param aLogger
     *            logs the actions of the elevator in the log file.
     */
    public SnobotCtreElevator(WPI_TalonSRX aElevatorMotor, IOperatorJoystick aJoystick, ILogger aLogger)
    {
        super(aElevatorMotor, aJoystick, aLogger);

        mElevatorMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, sTIMEOUT, sTIMEOUT);
        mElevatorMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, sTIMEOUT, sTIMEOUT);
        mElevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, sTIMEOUT);
        mElevatorMotor.setSensorPhase(true);

        mElevatorMotor.config_kI(sDEFAULT_PID_SLOT, 0.0, sTIMEOUT);
        mElevatorMotor.config_kD(sDEFAULT_PID_SLOT, 0.0, sTIMEOUT);
    }

    @Override
    public void update()
    {
        super.update();

        mConversionFactor = Properties2018.sELEVATOR_CTRE_CONVERSION_FACTOR.getValue();

        mElevatorMotor.config_kF(sDEFAULT_PID_SLOT, Properties2018.sELEVATOR_CTRE_KP.getValue(), sTIMEOUT);
        mElevatorMotor.config_kP(sDEFAULT_PID_SLOT, Properties2018.sELEVATOR_CTRE_KFF.getValue(), sTIMEOUT);
        mElevatorMotor.configMotionCruiseVelocity(Properties2018.sELEVATOR_CTRE_CRUISE_VELOCITY.getValue(), sTIMEOUT);
        mElevatorMotor.configMotionAcceleration(Properties2018.sELEVATOR_CTRE_CRUISE_ACCELERATION.getValue(), sTIMEOUT);
    }

    @Override
    public boolean gotoHeight(double aHeight)
    {
        double destination = aHeight / mConversionFactor;
        sLOGGER.log(Level.INFO, "Going to : " + aHeight + " (" + destination + ")");

        double deltaHeight = aHeight - mActualHeight;
        boolean isFinished = -mHeightDeadband < deltaHeight && deltaHeight < mHeightDeadband;
        boolean isAtHeight = mDeadBandHelper.isFinished(isFinished);

        if (isAtHeight)
        {
            stop();
        }
        else
        {
            mElevatorMotor.set(ControlMode.MotionMagic, destination);
        }

        return isAtHeight;
    }

    @Override
    protected double caluculateHeight()
    {
        double rawPosition = mElevatorMotor.getSelectedSensorPosition(sDEFAULT_PID_SLOT);
        double output = rawPosition * mConversionFactor;
        sLOGGER.log(Level.INFO, "Currently at " + output + " (" + rawPosition + ")");

        return output;
    }

    @Override
    public double getSpeed()
    {
        return mElevatorMotor.getMotorOutputPercent();
    }

}
