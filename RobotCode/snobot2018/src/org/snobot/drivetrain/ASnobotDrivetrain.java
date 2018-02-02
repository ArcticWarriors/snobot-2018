package org.snobot.drivetrain;

import org.snobot.SmartDashboardNames;
import org.snobot.joystick.IDriveJoystick;
import org.snobot.lib.logging.ILogger;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class ASnobotDrivetrain<SpeedControllerType extends SpeedController> implements IDriveTrain
{
    protected final ILogger mLogger;
    protected final IDriveJoystick mDriverJoystick;

    protected final SpeedControllerType mLeftMotor;
    protected final SpeedControllerType mRightMotor;
    protected final DifferentialDrive mRobotDrive;
    private final Gyro mGyro;

    protected double mRightMotorDistance;
    protected double mLeftMotorDistance;
    protected double mLeftMotorSpeed;
    protected double mRightMotorSpeed;

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
    public ASnobotDrivetrain(
            SpeedControllerType aLeftMotor, SpeedControllerType aRightMotor,
            Gyro aGyro,
            IDriveJoystick aDriverJoystick, ILogger aLogger)
    {
        mLeftMotor = aLeftMotor;
        mRightMotor = aRightMotor;
        mLogger = aLogger;
        mGyro = aGyro;
        mDriverJoystick = aDriverJoystick;
        mRobotDrive = new DifferentialDrive(aLeftMotor, aRightMotor);
    }

    @Override
    public void initializeLogHeaders()
    {
        mLogger.addHeader("LeftEncoderDistance");
        mLogger.addHeader("RightEncoderDistance");
        mLogger.addHeader("LeftMotorSpeed");
        mLogger.addHeader("RightMotorSpeed");
    }

    @Override
    public void control()
    {
        setLeftRightSpeed(mDriverJoystick.getLeftspeed(), mDriverJoystick.getRightspeed());
    
    }

    @Override
    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber(SmartDashboardNames.sLEFT_DRIVE_ENCODER_DISTANCE, mLeftMotorDistance);
        SmartDashboard.putNumber(SmartDashboardNames.sRIGHT_DRIVE_ENCODER_DISTANCE, mRightMotorDistance);
    }

    @Override
    public void updateLog()
    {
        mLogger.updateLogger(mLeftMotorDistance);
        mLogger.updateLogger(mRightMotorDistance);
        mLogger.updateLogger(mLeftMotorSpeed);
        mLogger.updateLogger(mRightMotorSpeed);
    }

    @Override
    public double getRightDistance()
    {
        return mRightMotorDistance;
    }

    @Override
    public double getLeftDistance()
    {
        return mLeftMotorDistance;
    }

    @Override
    public void setLeftRightSpeed(double aLeftSpeed, double aRightSpeed)
    {
        mLeftMotorSpeed = aLeftSpeed;
        mRightMotorSpeed = aRightSpeed;
        mRobotDrive.tankDrive(mLeftMotorSpeed, mRightMotorSpeed);
    }

    @Override
    public double getLeftMotorSpeed()
    {
        return mLeftMotorSpeed;
    }

    @Override
    public double getRightMotorSpeed()
    {
        return mRightMotorSpeed;
    }

    @Override
    public void stop()
    {
        setLeftRightSpeed(0, 0);
    }

    @Override
    public void resetHeading()
    {
        mGyro.reset();
    }

    @Override
    public double getHeading()
    {
        return mGyro.getAngle();
    }

}
