package org.snobot.autonomous.trajectory;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.snobot.Properties2018;
import org.snobot.Snobot2018;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.Trajectory.Segment;
import com.team254.lib.trajectory.io.TextFileDeserializer;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;

/**
 * PID + Feedforward controller for following a Trajectory.
 *
 * @author Jared341
 */
public class TrajectoryFollower
{
    protected static final Logger sLOGGER = Logger.getLogger(TrajectoryFollower.class);

    private final NetworkTable mFollowerTable;

    private double mKp;
    // private double mKi; // Not currently used, but might be in the future.
    private double mKd;
    private double mKv;
    private double mKa;
    private double mLastError;
    private double mCurrentHeading = 0;
    private int mCurrentSegment;
    private Trajectory mProfile;

    /**
     * Constructor.
     * 
     * @param aName
     *            The wheel name (left or right)
     */
    public TrajectoryFollower(String aName)
    {
        mFollowerTable = NetworkTableInstance.getDefault().getTable(aName + "Trajectory");
    }

    /**
     * Sets up the drive path for the class.
     * 
     * @param aKp
     *            Proportional position gain
     * @param aKi
     *            integral position gain
     * @param aKd
     *            derivative positional gain
     * @param aKv
     *            feet forward positional gain
     * @param aKa
     *            acceleration gain
     */
    public void configure(double aKp, double aKi, double aKd, double aKv, double aKa)
    {
        mKp = aKp;
        // mKi = aKi;
        mKd = aKd;
        mKv = aKv;
        mKa = aKa;
    }

    /**
     * This resets the trajectory follower.
     */
    public void reset()
    {
        mLastError = 0.0;
        mCurrentSegment = 0;
    }

    public void setTrajectory(Trajectory aProfile)
    {
        mProfile = aProfile;
    }

    /**
     * Calculates the distance.
     * 
     * @param aDistanceSoFar
     *            gets the distance you have traveled
     * @return the distance traveled
     */
    public double calculate(double aDistanceSoFar)
    {

        if (mCurrentSegment < mProfile.getNumSegments())
        {
            Segment segment = mProfile.getSegment(mCurrentSegment);
            double error = segment.pos - aDistanceSoFar;

            mLastError = error;
            mCurrentHeading = segment.heading;
            mCurrentSegment++;
            mFollowerTable.getEntry("Distance").setNumber(aDistanceSoFar);
            mFollowerTable.getEntry("Goal").setNumber(segment.pos);
            mFollowerTable.getEntry("Error").setNumber(error);

            double pTerm = error * mKp;
            double dTerm = mKd * ((error - mLastError) / segment.dt - segment.vel);
            double vTerm = mKv * segment.vel;
            double aTerm = mKa * segment.acc;
            double output = pTerm + dTerm + vTerm + aTerm;

            if (sLOGGER.isDebugEnabled())
            {
                DecimalFormat df = new DecimalFormat("#.000");

                sLOGGER.log(Level.DEBUG,
                      "Current: " + df.format(aDistanceSoFar) + ", " 
                      + "Desired: " + df.format(segment.pos) + ", "  
                      + "p: " + df.format(pTerm) + ", "  
                      + "d: " + df.format(dTerm) + ", "  
                      + "v: " + df.format(vTerm) + ", "  
                      + "a: " + df.format(aTerm) + ", " 
                      + "output: " + output);
            }

            return output;
        }
        else
        {
            return 0;
        }
    }

    public double getHeading()
    {
        return mCurrentHeading;
    }

    public boolean isFinishedTrajectory()
    {
        return mCurrentSegment >= mProfile.getNumSegments();
    }

    public int getCurrentSegment()
    {
        return mCurrentSegment;
    }

    public int getNumSegments()
    {
        return mProfile.getNumSegments();
    }

    /**
     * This parses the command.
     * 
     * @param aCommandName
     *            The file name of the trajectory
     * @param aCommandArgs
     *            The command name for the file
     * @param aSnobot
     *            The robot
     * @return Trajectory auto modes
     */
    public Command createCommand(String aCommandName, List<String> aCommandArgs, Snobot2018 aSnobot)
    {
        String pathFile = Properties2018.sAUTON_PATH_DIRECTORY.getValue() + "/" + aCommandArgs.get(1).trim();
        TextFileDeserializer deserializer = new TextFileDeserializer();
        Path p = deserializer.deserializeFromFile(pathFile);


        return new TrajectoryPathCommand(aSnobot.getDrivetrain(), aSnobot.getPositioner(), p);
    }
}
