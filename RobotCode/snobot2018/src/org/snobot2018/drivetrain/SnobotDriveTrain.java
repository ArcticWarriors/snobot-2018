package org.snobot2018.drivetrain;

import org.snobot.Properties2018;
import org.snobot.SmartDashboardNames;
import org.snobot.lib.logging.ILogger;
import org.snobot2018.joystick.IDriveJoystick;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SnobotDriveTrain implements IDriveTrain 
{
    private final Encoder mLeftDriveEncoder;
    private final Encoder mRightDriveEncoder;

    protected final SpeedController mLeftMotor;
    protected final SpeedController mRightMotor;

    protected final ILogger mLogger;

    protected double mRightMotorDistance;
    protected double mLeftMotorDistance;

    protected double mLeftMotorSpeed;
    protected double mRightMotorSpeed;

    protected final DifferentialDrive mRobotDrive;

    protected final IDriveJoystick mDriverJoystick;


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
    public SnobotDriveTrain(
            SpeedController aLeftMotor, 
            SpeedController aRightMotor, 
            Encoder aLeftDriveEncoder, 
            Encoder aRightDriveEncoder, IDriveJoystick aDriverJoystick, ILogger aLogger)
    {
        mLeftMotor = aLeftMotor;
        mRightMotor = aRightMotor;
        mLogger = aLogger;
        mDriverJoystick = aDriverJoystick;
        mLeftDriveEncoder = aLeftDriveEncoder;
        mRightDriveEncoder = aRightDriveEncoder;
        mRobotDrive = new DifferentialDrive(aLeftMotor, aRightMotor);

    }

    @Override
    public void control()
    {
        setLeftRightSpeed(mDriverJoystick.getLeftspeed(), mDriverJoystick.getRightspeed());

    }

    @Override
    public void stop()
    {
        setLeftRightSpeed(0, 0);
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
    public void initializeLogHeaders()
    {

        mLogger.addHeader("LeftEncoderDistance");
        mLogger.addHeader("RightEncoderDistance");
        mLogger.addHeader("LeftMotorSpeed");
        mLogger.addHeader("RightMotorSpeed");
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
    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber(SmartDashboardNames.sLEFT_DRIVE_ENCODER_DISTANCE, mLeftMotorDistance);
        SmartDashboard.putNumber(SmartDashboardNames.sRIGHT_DRIVE_ENCODER_DISTANCE, mRightMotorDistance);
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
    public void restEncoders()
    {
        mLeftDriveEncoder.reset();
        mRightDriveEncoder.reset();
    }


}
