package org.snobot.autonomous.trajectory;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.snobot.Properties2018;
import org.snobot.SmartDashboardNames;
import org.snobot.Snobot2018;
import org.snobot.drivetrain.IDriveTrain;
import org.snobot.leds.ILedManager;
import org.snobot.lib.Utilities;
import org.snobot.lib.motion_profile.trajectory.IdealSplineSerializer;
import org.snobot.lib.motion_profile.trajectory.SplineSegment;
import org.snobot.positioner.IPositioner;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.Waypoint;
import com.team254.lib.trajectory.io.TextFileDeserializer;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;

public class TrajectoryPathCommand extends Command 
{
    protected static final Logger sLOGGER = Logger.getLogger(TrajectoryPathCommand.class);

    private static final NetworkTable sTRAJECTORY_NETWORK_TABLE = NetworkTableInstance.getDefault()
            .getTable(SmartDashboardNames.sSPLINE_NAMESPACE);

    private final IDriveTrain mDrivetrain;
    private final IPositioner mPositioner;
    private final ILedManager mLedManager;
    private final TrajectoryFollower mFollowerLeft;
    private final TrajectoryFollower mFollowerRight;
    private final Path mPath;
    private final double mKTurn;

    private double mStartingLeftDistance;
    private double mStartingRightDistance;

    private final NetworkTableEntry mIdealTableEntry;
    private final NetworkTableEntry mMeasuredTableEntry;
    private final NetworkTableEntry mWaypointTableEntry;

    private double mLastLeftDistance;
    private double mLastRightDistance;

    /**
     * Creates the trajectory path command.
     * 
     * @param aDrivetrain
     *            computer's drivetain
     * @param aPositioner
     *            computer's positioner
     * @param aPath
     *            Trajectory path
     */
    public TrajectoryPathCommand(IDriveTrain aDrivetrain, IPositioner aPositioner, ILedManager aLedManager, Path aPath)
    {
        mDrivetrain = aDrivetrain;
        mPositioner = aPositioner;
        mLedManager = aLedManager;
        mPath = aPath;

        mFollowerLeft = new TrajectoryFollower("left");
        mFollowerRight = new TrajectoryFollower("right");

        sTRAJECTORY_NETWORK_TABLE.getEntry(".type").setString(SmartDashboardNames.sSPLINE_NAMESPACE);
        mIdealTableEntry = sTRAJECTORY_NETWORK_TABLE.getEntry(SmartDashboardNames.sSPLINE_IDEAL_POINTS);
        mMeasuredTableEntry = sTRAJECTORY_NETWORK_TABLE.getEntry(SmartDashboardNames.sSPLINE_REAL_POINT);
        mWaypointTableEntry = sTRAJECTORY_NETWORK_TABLE.getEntry(SmartDashboardNames.sSPLINE_WAYPOINTS);



        double kP = Properties2018.sDRIVE_PATH_KP.getValue();
        double kD = Properties2018.sDRIVE_PATH_KD.getValue();
        double kVelocity = Properties2018.sDRIVE_PATH_KV.getValue();
        double kAccel = Properties2018.sDRIVE_PATH_KA.getValue();

        mKTurn = Properties2018.sSPLINE_TURN_FACTOR.getValue();

        mFollowerLeft.configure(kP, 0, kD, kVelocity, kAccel);
        mFollowerRight.configure(kP, 0, kD, kVelocity, kAccel);

        mFollowerLeft.reset();
        mFollowerRight.reset();

        mFollowerLeft.setTrajectory(aPath.getLeftWheelTrajectory());
        mFollowerRight.setTrajectory(aPath.getRightWheelTrajectory());

        if (mIdealTableEntry.getString("").isEmpty())
        {
            sendIdealPath();
        }
    }

    @Override
    protected void initialize()
    {
        mStartingLeftDistance = mDrivetrain.getLeftDistance();
        mStartingRightDistance = mDrivetrain.getRightDistance();

        sendIdealPath();
    }

    @Override
    public void execute()
    {
        mLedManager.setTrajectoryPercentageComplete(getPercentComplete());

        double distanceL = mDrivetrain.getLeftDistance() - mStartingLeftDistance;
        double distanceR = mDrivetrain.getRightDistance() - mStartingRightDistance;

        double speedLeft = mFollowerLeft.calculate(distanceL);
        double speedRight = mFollowerRight.calculate(distanceR);

        double goalHeading = mFollowerLeft.getHeading();
        double observedHeading = mPositioner.getOrientationDegrees();

        double turn = 0;
        if (mDrivetrain.isGyroConnected())
        {
            double angleDiff = Utilities.getDifferenceInAngleDegrees(observedHeading, goalHeading);
            turn = mKTurn * angleDiff;
        }
        mDrivetrain.setLeftRightSpeed(speedLeft + turn, speedRight - turn);

        double dt = .02;

        SplineSegment segment = new SplineSegment();
        segment.mLeftSidePosition = distanceL;
        segment.mLeftSideVelocity = (distanceL - mLastLeftDistance) / dt;
        segment.mRightSidePosition = distanceR;
        segment.mRightSideVelocity = (distanceR - mLastRightDistance) / dt;
        segment.mRobotHeading = Utilities.boundAngleNeg180to180Degrees(observedHeading);
        segment.mAverageX = mPositioner.getXPosition();
        segment.mAverageY = mPositioner.getYPosition();

        String pointInfo = mFollowerLeft.getCurrentSegment() + "," + IdealSplineSerializer.serializePathPoint(segment);

        mMeasuredTableEntry.setString(pointInfo);

        mLastLeftDistance = distanceL;
        mLastRightDistance = distanceR;
    }

    public double getPercentComplete()
    {
        return 1.0 * mFollowerLeft.getCurrentSegment() / mFollowerLeft.getNumSegments();
    }

    @Override
    public boolean isFinished()
    {
        boolean finished = mFollowerLeft.isFinishedTrajectory();
        if (finished)
        {
            sLOGGER.log(Level.INFO, "***************************************** TRAJ Finished *******************");
        }
        return finished;
    }

    @Override
    protected void end()
    {
        mDrivetrain.stop();

    }

    private void sendIdealPath()
    {
        List<SplineSegment> segments = new ArrayList<SplineSegment>();

        Trajectory left = mPath.getLeftWheelTrajectory();
        Trajectory right = mPath.getRightWheelTrajectory();

        for (int i = 0; i < left.getNumSegments(); ++i)
        {
            SplineSegment segment = new SplineSegment();
            segment.mLeftSidePosition = left.getSegment(i).pos;
            segment.mLeftSideVelocity = left.getSegment(i).vel;
            segment.mRightSidePosition = right.getSegment(i).pos;
            segment.mRightSideVelocity = right.getSegment(i).vel;
            segment.mRobotHeading = Utilities.boundAngleNeg180to180Degrees(right.getSegment(i).heading);
            segment.mAverageX = (left.getSegment(i).y + right.getSegment(i).y) / 2; // Flipped
                                                                                    // on
                                                                                    // purpose
            segment.mAverageY = (left.getSegment(i).x + right.getSegment(i).x) / 2; // Flipped
                                                                                    // on
                                                                                    // purpose

            segments.add(segment);
        }

        mIdealTableEntry.setString(IdealSplineSerializer.serializePath(segments));

        StringBuilder waypointText = new StringBuilder();
        for (Waypoint waypoint : mPath.getPathConfig())
        {
            waypointText.append(waypoint.x).append(',').append(waypoint.y).append(',').append(waypoint.theta).append(',');
        }

        mWaypointTableEntry.setString(waypointText.toString());
    }
    
    /**
     * This parses the command.
     * 
     * @param aArgs
     *            File name
     * @param aSnobot
     *            The robot
     * @return Auto mode
     */
    public static Command parseCommand(List<String> aArgs, Snobot2018 aSnobot)
    {
        String pathFile = Properties2018.sAUTON_PATH_DIRECTORY.getValue() + "/" + aArgs.get(1).trim();
        TextFileDeserializer deserializer = new TextFileDeserializer();
        Path p = deserializer.deserializeFromFile(pathFile);

        return new TrajectoryPathCommand(aSnobot.getDrivetrain(), aSnobot.getPositioner(), aSnobot.getLedManager(), p);
    }
}

