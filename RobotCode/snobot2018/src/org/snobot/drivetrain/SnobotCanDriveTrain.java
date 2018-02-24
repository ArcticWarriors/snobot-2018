package org.snobot.drivetrain;

import org.snobot.PortMappings2018;
import org.snobot.joystick.IDriveJoystick;
import org.snobot.lib.logging.ILogger;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * Drivetrain using CAN controllers.
 *
 * @author Jeff
 */
public class SnobotCanDriveTrain extends ASnobotDrivetrain<WPI_TalonSRX>
{
    private static final double sWHEEL_CIRCUMFERANCE = 6 * Math.PI;
    private static final double sTICKS_PER_INCH = sWHEEL_CIRCUMFERANCE / 4096.0;

    private static final int sDEFAULT_PID_SLOT = 0;
    private static final int sTIMEOUT = PortMappings2018.sDEFAULT_CTRE_TIMEOUT;
    
    private static final double sRIGHT_MULTIPLIER = RobotBase.isSimulation() ? 1 : -1;

    /**
     * This class is where all the motors stuff are set so that they work.
     * 
     * @param aLeftMotor
     *            The left motor of the drive train
     * @param aRightMotor
     *            The right motor of the drive train
     * @param aDriverJoystick
     *            The driver joystick
     * @param aLogger
     *            The logger
     * 
     */
    public SnobotCanDriveTrain(
            WPI_TalonSRX aLeftMotor, WPI_TalonSRX aRightMotor,
            SnobotADXRS450_Gyro aGyro,
            IDriveJoystick aDriverJoystick, ILogger aLogger)
    {
        super(aLeftMotor, aRightMotor, aGyro, aDriverJoystick, aLogger);

        mLeftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, sTIMEOUT);
        mLeftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, sTIMEOUT);
        mLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        mLeftMotor.setSensorPhase(true);
        mLeftMotor.config_kF(sDEFAULT_PID_SLOT, 0.2, sTIMEOUT);
        mLeftMotor.config_kP(sDEFAULT_PID_SLOT, 0.2, sTIMEOUT);
        mLeftMotor.config_kI(sDEFAULT_PID_SLOT, 0, sTIMEOUT);
        mLeftMotor.config_kD(sDEFAULT_PID_SLOT, 0, sTIMEOUT);
        mLeftMotor.configMotionCruiseVelocity(15000, sTIMEOUT);
        mLeftMotor.configMotionAcceleration(6000, sTIMEOUT);

        mRightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, sTIMEOUT);
        mRightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, sTIMEOUT);
        mRightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        mRightMotor.setSensorPhase(true);
        mRightMotor.config_kF(sDEFAULT_PID_SLOT, 0.2, sTIMEOUT);
        mRightMotor.config_kP(sDEFAULT_PID_SLOT, 0.2, sTIMEOUT);
        mRightMotor.config_kI(sDEFAULT_PID_SLOT, 0, sTIMEOUT);
        mRightMotor.config_kD(sDEFAULT_PID_SLOT, 0, sTIMEOUT);
        mRightMotor.configMotionCruiseVelocity(15000, sTIMEOUT);
        mRightMotor.configMotionAcceleration(6000, sTIMEOUT);
    }

    @Override
    public void update()
    {
        mRightMotorDistance = mRightMotor.getSelectedSensorPosition(sDEFAULT_PID_SLOT) * sTICKS_PER_INCH * sRIGHT_MULTIPLIER;
        mLeftMotorDistance = mLeftMotor.getSelectedSensorPosition(sDEFAULT_PID_SLOT) * sTICKS_PER_INCH;
    }

    @Override
    public void resetEncoders()
    {
        mLeftMotorDistance = 0;
        mRightMotorDistance = 0;

        mRightMotor.setSelectedSensorPosition(0, 0, 0);
        mLeftMotor.setSelectedSensorPosition(0, 0, 0);
    }

}
