package org.snobot.drivetrain;

import org.snobot.Properties2018;
import org.snobot.joystick.IDriveJoystick;
import org.snobot.lib.logging.ILogger;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class SnobotStandardDriveTrain extends ASnobotDrivetrain<SpeedController>
{
    private final Encoder mLeftDriveEncoder;
    private final Encoder mRightDriveEncoder;

    /**
     * This class is where all the motors stuff are set so that they work.
     * 
     * @param aLeftMotor
     *            The left motor of the drive train
     * @param aRightMotor
     *            The right motor of the drive train
     * @param aLeftDriveEncoder
     *            The left motor encoder
     * @param aRightDriveEncoder
     *            The right motor encoder
     * @param aDriverJoystick
     *            The driver joystick
     * @param aLogger
     *            The logger
     * 
     */
    public SnobotStandardDriveTrain(
            SpeedController aLeftMotor, SpeedController aRightMotor, 
            Encoder aLeftDriveEncoder, Encoder aRightDriveEncoder, 
            Gyro aGyro,
            IDriveJoystick aDriverJoystick, ILogger aLogger)
    {
        super(aLeftMotor, aRightMotor, aGyro, aDriverJoystick, aLogger);
        mLeftDriveEncoder = aLeftDriveEncoder;
        mRightDriveEncoder = aRightDriveEncoder;

    }

    @Override
    public void update()
    {
        mLeftDriveEncoder.setDistancePerPulse(Properties2018.sLEFT_ENCODER_DIST_PER_PULSE.getValue());
        mRightDriveEncoder.setDistancePerPulse(Properties2018.sRIGHT_ENCODER_DIST_PER_PULSE.getValue());

        mLeftMotorDistance = mLeftDriveEncoder.getDistance();
        mRightMotorDistance = mRightDriveEncoder.getDistance();

    }

    @Override
    public void resetEncoders()
    {
        mLeftMotorDistance = 0;
        mRightMotorDistance = 0;

        mLeftDriveEncoder.reset();
        mRightDriveEncoder.reset();
    }
}
