package org.snobot.autonomous.path;

import java.util.List;

import org.snobot.Properties2018;
import org.snobot.SmartDashboardNames;
import org.snobot.Snobot2018;
import org.snobot.drivetrain.IDriveTrain;
import org.snobot.lib.motion_profile.ISetpointIterator;
import org.snobot.lib.motion_profile.PathConfig;
import org.snobot.lib.motion_profile.PathGenerator;
import org.snobot.lib.motion_profile.PathSetpoint;
import org.snobot.lib.motion_profile.StaticSetpointIterator;
import org.snobot.positioner.IPositioner;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;

/**
 * uses the motion profile to follow a straight path.
 * 
 * @author Andrew
 *
 */
public class DriveStraightPath extends Command
{
    protected final NetworkTable mGoToPositionNetworkTable;

    protected final IDriveTrain mDriveTrain;
    protected final IPositioner mPositioner;
    protected final PathFollower mPathFollower;
    protected final double mDistance;
    protected double mStartDistance;

    /**
     * Calls the drive train stuff from the positionser.
     * 
     * @param aDriveTrain
     *            The robot's drive train
     * @param aPositioner
     *            The robot's positioner
     * @param aSetpointIterator
     *            A setpoint iterator for the path follower
     */
    public DriveStraightPath(IDriveTrain aDriveTrain, IPositioner aPositioner, double aDistance, ISetpointIterator aSetpointIterator)
    {
        this(aDriveTrain, aPositioner, aDistance, aSetpointIterator,
                Properties2018.sDRIVE_PATH_KP.getValue(), 
                Properties2018.sDRIVE_PATH_KV.getValue(), 
                Properties2018.sDRIVE_PATH_KA.getValue());
    }

    /**
     * Creates the drive straight path arguments.
     * 
     * @param aDriveTrain
     *            computer's drivetrain
     * @param aPositioner
     *            computer's positioner
     * @param aSetpointIterator
     *            Used by the path follower
     * @param aKp
     *            tbd
     * @param aKv
     *            tbd
     * @param aKa
     *            tbd
     */
    public DriveStraightPath(IDriveTrain aDriveTrain, IPositioner aPositioner, double aDistance, ISetpointIterator aSetpointIterator,
            double aKp, double aKv, double aKa)
    {
        mDriveTrain = aDriveTrain;
        mPositioner = aPositioner;
        mDistance = aDistance;

        mPathFollower = new PathFollower(aSetpointIterator, aKv, aKa, aKp);

        mGoToPositionNetworkTable = NetworkTableInstance.getDefault().getTable(SmartDashboardNames.sGO_TO_POSITION_TABLE_NAME);
        mGoToPositionNetworkTable.getEntry(".type").setString(SmartDashboardNames.sGO_TO_POSITION_TABLE_NAME);
    }

    @Override
    protected void initialize()
    {
        mStartDistance = mPositioner.getTotalDistance();
        mPathFollower.init();

        double startX = mPositioner.getXPosition();
        double startY = mPositioner.getYPosition();
        double startAngle = mPositioner.getOrientationDegrees();

        double dx = Math.sin(startAngle) * mDistance;
        double dy = Math.cos(startAngle) * mDistance;

        double endX = startX + dx;
        double endY = startY + dy;
        double endAngle = mPositioner.getOrientationDegrees();

        mGoToPositionNetworkTable.getEntry(SmartDashboardNames.sGO_TO_POSITION_START).setString(startX + ", " + startY + ", " + startAngle);
        mGoToPositionNetworkTable.getEntry(SmartDashboardNames.sGO_TO_POSITION_END).setString(endX + ", " + endY + ", " + endAngle);
    }

    protected double calculatePathSpeed()
    {
        double curPos = mPositioner.getTotalDistance() - mStartDistance;
        double motorSpeed = mPathFollower.calcMotorSpeed(curPos);

        return motorSpeed; // NOPMD
    }

    @Override
    protected void execute()
    {
        double motorSpeed = calculatePathSpeed();
        mDriveTrain.setLeftRightSpeed(motorSpeed, motorSpeed);
    }

    @Override
    protected boolean isFinished()
    {
        return mPathFollower.isFinished();
    }

    @Override
    protected void end()
    {
        mDriveTrain.stop();
    }

    /**
     * This parses the command.
     * 
     * @param aArgs
     *            Endpoint Velocity Acceleration
     * @param aSnobot
     *            Is the robot
     * @return the autonomous mode
     */

    public static Command parseCommand(List<String> aArgs, Snobot2018 aSnobot)
    {
        double distance = Double.parseDouble(aArgs.get(1));
        PathConfig dudePathConfig = new PathConfig(distance, // Endpoint
                Double.parseDouble(aArgs.get(2)), // Max Velocity
                Double.parseDouble(aArgs.get(3)), // Max Acceleration
                .02);

        PathGenerator dudePathGenerator = new PathGenerator();
        List<PathSetpoint> dudeList = dudePathGenerator.generate(dudePathConfig);
        ISetpointIterator dudeSetpointIterator = new StaticSetpointIterator(dudeList);

        return new DriveStraightPath(aSnobot.getDrivetrain(), aSnobot.getPositioner(), distance, dudeSetpointIterator);
    }
}
