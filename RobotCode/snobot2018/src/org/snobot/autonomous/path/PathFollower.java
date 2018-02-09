package org.snobot.autonomous.path;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.snobot.SmartDashboardNames;
import org.snobot.lib.motion_profile.ISetpointIterator;
import org.snobot.lib.motion_profile.IdealPlotSerializer;
import org.snobot.lib.motion_profile.PathSetpoint;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Follows an ideal path generated elsewhere.
 * 
 * @author Andrew
 * 
 */
public class PathFollower
{
    private static final edu.wpi.first.networktables.NetworkTable sPATH_NETWORK_TABLE = NetworkTableInstance.getDefault()
            .getTable(SmartDashboardNames.sPATH_NAMESPACE);

    protected static final Logger sLOGGER = Logger.getLogger(PathFollower.class);

    private final ISetpointIterator mSetpointIterator;
    private final double mKv;
    private final double mKa;
    private final double mKp;

    private int mPathPoint;

    private final NetworkTableEntry mIdealTableEntry;
    private final NetworkTableEntry mMeasuredTableEntry;

    //private double mLastError;
    private double mLastPosition;

    /**
     * Follows the path.
     * 
     * @param aSetpointIterator
     *            Setpoint iterator to follow points
     * @param aKV
     *            The multiplier for Velocity
     * @param aKA
     *            the multiplier for acceleration
     * @param aKP
     *            the multiplier for position
     */
    public PathFollower(ISetpointIterator aSetpointIterator, double aKV, double aKA, double aKP)
    {
        mSetpointIterator = aSetpointIterator;
        mKv = aKV;
        mKa = aKA;
        mKp = aKP;

        sPATH_NETWORK_TABLE.getEntry(".type").setString(SmartDashboardNames.sPATH_NAMESPACE);
        mIdealTableEntry = sPATH_NETWORK_TABLE.getEntry(SmartDashboardNames.sPATH_IDEAL_POINTS);
        mMeasuredTableEntry = sPATH_NETWORK_TABLE.getEntry(SmartDashboardNames.sPATH_POINT);
    }

    public void init()
    {
        mIdealTableEntry.setString(IdealPlotSerializer.serializePath(mSetpointIterator.getIdealPath()));
    }

    /**
     * Calculates the Motor Speed.
     * 
     * @param aCurrPosition
     *            gets the current position
     * @return the current position
     */
    public double calcMotorSpeed(double aCurrPosition)
    {
        if (mSetpointIterator.isFinished())
        {
            return 0;
        }
        else
        {
            double dt = .02;
            double velocity = (aCurrPosition - mLastPosition) / dt;

            PathSetpoint setpoint = mSetpointIterator.getNextSetpoint(0, 0, .02);
            PathSetpoint realPoint = new PathSetpoint(setpoint.mSegment, dt, aCurrPosition, velocity, 0);

            double error = setpoint.mPosition - aCurrPosition;
            double pTerm = mKp * error;
            double vTerm = mKv * setpoint.mVelocity;
            double aTerm = mKa * setpoint.mAcceleration;

            double output = vTerm + aTerm + pTerm;

            sLOGGER.log(Level.DEBUG, 
                    "Current: " + aCurrPosition  + ", "
                    +  "Error: " + error + ", "
                    +  "Vel: " + velocity + ", "
                    + "p: " + pTerm + ", "
                    + "v: " + vTerm + ", "
                    + "a: " + aTerm + ", "
                    + "output: " + output);
            

            // Update smart dashbaord
            String pointInfo = mPathPoint + "," + IdealPlotSerializer.serializePathPoint(realPoint);
            mMeasuredTableEntry.setString(pointInfo);

            //mLastError = error;
            mLastPosition = aCurrPosition;
            ++mPathPoint;

            return output;
        }
    }

    public boolean isFinished()
    {
        return mSetpointIterator.isFinished();
    }
}
