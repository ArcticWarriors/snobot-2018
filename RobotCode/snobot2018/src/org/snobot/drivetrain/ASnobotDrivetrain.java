package org.snobot.drivetrain;

import org.snobot.Properties2018;
import org.snobot.SmartDashboardNames;
import org.snobot.joystick.IDriveJoystick;
import org.snobot.lib.logging.CsvLogEntry;
import org.snobot.lib.logging.CsvLogger;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class ASnobotDrivetrain<SpeedControllerType extends SpeedController> implements IDriveTrain
{
    protected final IDriveJoystick mDriverJoystick;

    protected final SpeedControllerType mLeftMotor;
    protected final SpeedControllerType mRightMotor;
    protected final DifferentialDrive mRobotDrive;
    protected final SnobotADXRS450_Gyro mGyro;

    protected double mRightMotorDistance;
    protected double mLeftMotorDistance;
    protected double mLeftMotorSpeed;
    protected double mRightMotorSpeed;

    protected final CsvLogEntry mLeftDistanceLog;
    protected final CsvLogEntry mRightDistanceLog;
    protected final CsvLogEntry mLeftSpeedLog;
    protected final CsvLogEntry mRightSpeedLog;

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
            SnobotADXRS450_Gyro aGyro,
            IDriveJoystick aDriverJoystick, CsvLogger aLogger)
    {
        mLeftMotor = aLeftMotor;
        mRightMotor = aRightMotor;
        mGyro = aGyro;
        mDriverJoystick = aDriverJoystick;
        mRobotDrive = new DifferentialDrive(aLeftMotor, aRightMotor);
        mRobotDrive.setSafetyEnabled(false);

        mLeftDistanceLog = new CsvLogEntry("Drivetrain.LeftEncoderDistance");
        mRightDistanceLog = new CsvLogEntry("Drivetrain.RightEncoderDistance");
        mLeftSpeedLog = new CsvLogEntry("Drivetrain.LeftMotorSpeed");
        mRightSpeedLog = new CsvLogEntry("Drivetrain.RightMotorSpeed");

        aLogger.addEntry(mLeftDistanceLog);
        aLogger.addEntry(mRightDistanceLog);
        aLogger.addEntry(mLeftSpeedLog);
        aLogger.addEntry(mRightSpeedLog);
    }

    @Override
    public void control()
    {
        double leftSpeed = mDriverJoystick.getLeftspeed();
        double rightSpeed = mDriverJoystick.getRightspeed();

        if (mDriverJoystick.isSuperSlowMode())
        {
            leftSpeed *= Properties2018.sDRIVE_SUPER_SLOW_MULTIPLIER.getValue();
            rightSpeed *= Properties2018.sDRIVE_SUPER_SLOW_MULTIPLIER.getValue();
        }
        else if (mDriverJoystick.isSlowMode())
        {
            leftSpeed *= Properties2018.sDRIVE_SLOW_MULTIPLIER.getValue();
            rightSpeed *= Properties2018.sDRIVE_SLOW_MULTIPLIER.getValue();
        }

        setLeftRightSpeed(leftSpeed, rightSpeed);
    
    }

    @Override
    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber(SmartDashboardNames.sLEFT_DRIVE_ENCODER_DISTANCE, mLeftMotorDistance);
        SmartDashboard.putNumber(SmartDashboardNames.sRIGHT_DRIVE_ENCODER_DISTANCE, mRightMotorDistance);
        SmartDashboard.putBoolean(SmartDashboardNames.sGYRO_DETECTED, isGyroConnected());
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

    public boolean isGyroConnected()
    {
        return mGyro.isGyroConnected();
    }
}
