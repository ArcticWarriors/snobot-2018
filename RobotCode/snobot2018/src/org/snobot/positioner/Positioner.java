package org.snobot.positioner;

import org.snobot.SmartDashboardNames;
import org.snobot.drivetrain.IDriveTrain;
import org.snobot.lib.Utilities;
import org.snobot.lib.logging.ILogger;
import org.snobot.lib.modules.ISubsystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Monitors the robot's X/Y-position, orientation, total distance traveled,
 * change in distance traveled, and speed.
 * 
 * @author Andrew and Brayden
 *
 */
public class Positioner implements ISubsystem, IPositioner
{
    private final Timer mTimer;
    private final IDriveTrain mDriveTrain;
    private final ILogger mLogger;

    private double mXPosition;
    private double mYPosition;
    private double mOrientation;
    private double mTotalDistance;

    private double mLastDistance;
    private double mLastTime;
    private double mSpeed;
    private double mStartAngle;

    /**
     * Creates a new Positioner object.
     * 
     * @param aDriveTrain
     *            The DriveTrain to use.
     * @param aLogger
     *            The robot's Logger.
     */
    public Positioner(IDriveTrain aDriveTrain, ILogger aLogger)
    {
        mXPosition = 0;
        mYPosition = 0;
        mOrientation = 0;
        mTotalDistance = 0;
        mLastDistance = 0;
        mLastTime = 0;
        mSpeed = 0;
        mDriveTrain = aDriveTrain;
        mTimer = new Timer();
        mLogger = aLogger;
        mStartAngle = 0;
    }

    /**
     * Starts timer and adds headers to Logger.
     */
    @Override
    public void initializeLogHeaders()
    {
        mTimer.start();

        mLogger.addHeader("X-coordinate");
        mLogger.addHeader("Y-coordinate");
        mLogger.addHeader("Orientation");
        mLogger.addHeader("Speed");
    }

    /**
     * Calculates the robot's current X/Y-position, orientation, distance
     * traveled, and speed.
     */
    @Override
    public void update()
    {
        // Orientation
        mOrientation = mDriveTrain.getHeading() + mStartAngle;
        double orientationRadians = Math.toRadians(mOrientation);

        // ChangeInDistance and X/Y
        // mDriveTrain.getLeftDistance()
        mTotalDistance = mDriveTrain.getRightDistance();
        double deltaDistance = mTotalDistance - mLastDistance;
        mXPosition += deltaDistance * Math.sin(orientationRadians);
        mYPosition += deltaDistance * Math.cos(orientationRadians);
        // System.out.println("Positioner " + mTotalDistance + " " +
        // mDriveTrain.getRightDistance() + " " +
        // mDriveTrain.getLeftDistance());

        // Update
        mSpeed = deltaDistance / (mTimer.get() - mLastTime);
        mLastTime = mTimer.get();
        mLastDistance = mTotalDistance;
    }

    @Override
    public double getXPosition()
    {
        return mXPosition;
    }

    @Override
    public double getYPosition()
    {
        return mYPosition;
    }

    @Override
    public double getOrientationDegrees()
    {
        return mOrientation;
    }

    @Override
    public double getOrientationRadians()
    {
        return Math.toRadians(mOrientation);
    }

    @Override
    public double getTotalDistance()
    {
        return mTotalDistance;
    }

    @Override
    public void setPosition(double aX, double aY, double aAngle)
    {
        mDriveTrain.resetEncoders();
        mXPosition = aX;
        mYPosition = aY;
        mStartAngle = aAngle;
        mDriveTrain.resetHeading();
        mDriveTrain.resetEncoders();

        mTotalDistance = 0;
        mLastDistance = 0;
        mLastTime = mTimer.get();
    }

    @Override
    public void control()
    {
        // Nothing
    }

    /**
     * Puts the robot's X/Y-position, orientation, and speed on the
     * SmartDashboard.
     */
    @Override
    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber(SmartDashboardNames.sX_POSITION, mXPosition);
        SmartDashboard.putNumber(SmartDashboardNames.sY_POSITION, mYPosition);
        SmartDashboard.putNumber(SmartDashboardNames.sORIENTATION, Utilities.boundAngle0to360Degrees(mOrientation));
        SmartDashboard.putNumber(SmartDashboardNames.sSPEED, mSpeed);
    }

    /**
     * Sends the robot's X/Y-position, orientation, total distance traveled,
     * change in distance traveled, and speed to the logger.
     */
    @Override
    public void updateLog()
    {
        mLogger.updateLogger(mXPosition);
        mLogger.updateLogger(mYPosition);
        mLogger.updateLogger(mOrientation);
        mLogger.updateLogger(mSpeed);
    }

    @Override
    public void stop()
    {
        // Nothing
    }

}
