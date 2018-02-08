package org.snobot.drivetrain;

import org.snobot.joystick.IDriveJoystick;
import org.snobot.lib.logging.ILogger;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 * Drivetrain using CAN controllers.
 *
 * @author Jeff
 */
public class SnobotCanDriveTrain extends ASnobotDrivetrain<WPI_TalonSRX>
{
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
            Gyro aGyro,
            IDriveJoystick aDriverJoystick, ILogger aLogger)
    {
        super(aLeftMotor, aRightMotor, aGyro, aDriverJoystick, aLogger);

        mLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        mRightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
    }

    @Override
    public void update()
    {
        mRightMotorDistance = mRightMotor.getSelectedSensorPosition(0);
        mLeftMotorDistance = mLeftMotor.getSelectedSensorPosition(0);
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
