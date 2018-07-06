package org.snobot.positioner;

import org.snobot.SmartDashboardNames;
import org.snobot.drivetrain.IDriveTrain;
import org.snobot.lib.Utilities;
import org.snobot.lib.logging.CsvLogEntry;
import org.snobot.lib.logging.CsvLogger;
import org.snobot.lib.modules.ISubsystem;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
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
    private final NetworkTable mPositionTable;

    private final Timer mTimer;
    private final IDriveTrain mDriveTrain;

    protected final CsvLogEntry mXLog;
    protected final CsvLogEntry mYLog;
    protected final CsvLogEntry mAngleLog;
    protected final CsvLogEntry mSpeedLog;

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
    public Positioner(IDriveTrain aDriveTrain, CsvLogger aLogger)
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
        mStartAngle = 0;

        mPositionTable = NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sROBOT_POSITION_TABLE);
        mPositionTable.getEntry(".type").setString(SmartDashboardNames.sROBOT_POSITION_TABLE);

        mTimer.start();
        
        mXLog = new CsvLogEntry("Positioner.X");
        mYLog = new CsvLogEntry("Positioner.Y");
        mAngleLog = new CsvLogEntry("Positioner.Angle");
        mSpeedLog = new CsvLogEntry("Positioner.Speed");

        aLogger.addEntry(mXLog);
        aLogger.addEntry(mYLog);
        aLogger.addEntry(mAngleLog);
        aLogger.addEntry(mSpeedLog);
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
        mTotalDistance = (mDriveTrain.getRightDistance() + mDriveTrain.getLeftDistance()) / 2;
        double deltaDistance = mTotalDistance - mLastDistance;
        mXPosition += deltaDistance * Math.sin(orientationRadians);
        mYPosition += deltaDistance * Math.cos(orientationRadians);
        // System.out.println("Positioner " + mTotalDistance + " " +
        // mDriveTrain.getRightDistance() + " " +
        // mDriveTrain.getLeftDistance());

        // Update
        double dt = mTimer.get() - mLastTime;
        mSpeed = dt == 0 ? 0 : deltaDistance;
        mLastTime = mTimer.get();
        mLastDistance = mTotalDistance;

        mXLog.update(mXPosition);
        mYLog.update(mYPosition);
        mAngleLog.update(mOrientation);
        mSpeedLog.update(mSpeed);
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
        double boundedAngle = Utilities.boundAngle0to360Degrees(mOrientation);

        SmartDashboard.putNumber(SmartDashboardNames.sX_POSITION, mXPosition);
        SmartDashboard.putNumber(SmartDashboardNames.sY_POSITION, mYPosition);
        SmartDashboard.putNumber(SmartDashboardNames.sORIENTATION, boundedAngle);
        SmartDashboard.putNumber(SmartDashboardNames.sSPEED, mSpeed);

        mPositionTable.getEntry(SmartDashboardNames.sX_POSITION).setDouble(mXPosition);
        mPositionTable.getEntry(SmartDashboardNames.sY_POSITION).setDouble(mYPosition);
        mPositionTable.getEntry(SmartDashboardNames.sORIENTATION).setDouble(boundedAngle);
    }

    @Override
    public void stop()
    {
        // Nothing
    }

}
