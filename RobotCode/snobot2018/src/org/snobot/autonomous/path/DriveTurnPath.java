package org.snobot.autonomous.path;

import java.util.List;

import org.snobot.Properties2018;
import org.snobot.Snobot2018;
import org.snobot.drivetrain.IDriveTrain;
import org.snobot.lib.motion_profile.ISetpointIterator;
import org.snobot.lib.motion_profile.PathConfig;
import org.snobot.lib.motion_profile.PathGenerator;
import org.snobot.lib.motion_profile.PathSetpoint;
import org.snobot.lib.motion_profile.StaticSetpointIterator;
import org.snobot.positioner.IPositioner;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Uses the path follower to turn with a motion profile.
 * 
 * @author Andrew
 * 
 */
public class DriveTurnPath extends Command
{
    private final IDriveTrain mDriveTrain;
    private final IPositioner mPositioner;
    private final PathFollower mPathFollower;

    private double mStartAngle;

    /**
     * Creates Drive Turn Path.
     * 
     * @param aDriveTrain
     *            The robot's drive train
     * @param aPositioner
     *            The robot's positioner
     * @param aSetpointIterator
     *            A setpoint iterator for the path follower
     */
    public DriveTurnPath(IDriveTrain aDriveTrain, IPositioner aPositioner, ISetpointIterator aSetpointIterator)
    {
        mDriveTrain = aDriveTrain;
        mPositioner = aPositioner;

        double kP = Properties2018.sTURN_PATH_KP.getValue();
        // double kD = Properties2018.sTURN_PATH_KD.getValue();
        double kVelocity = Properties2018.sTURN_PATH_KV.getValue();
        double kAccel = Properties2018.sTURN_PATH_KA.getValue();

        mPathFollower = new PathFollower(aSetpointIterator, kVelocity, kAccel, kP);
    }

    @Override
    protected void initialize()
    {
        mStartAngle = mPositioner.getOrientationDegrees();
        mPathFollower.init();
    }

    /**
     * This parses the command.
     * 
     * @param aArgs
     *            Endpoint Velocity Acceleration
     * @param aSnobot
     *            The robot
     * @return Auto mode
     */

    public static Command parseCommand(List<String> aArgs, Snobot2018 aSnobot)
    {
        PathConfig dudePathConfig = new PathConfig(Double.parseDouble(aArgs.get(1)), // Endpoint
                Double.parseDouble(aArgs.get(2)), // Max Velocity
                Double.parseDouble(aArgs.get(3)), // Max Acceleration
                .02);

        PathGenerator dudePathGenerator = new PathGenerator();
        List<PathSetpoint> dudeList = dudePathGenerator.generate(dudePathConfig);
        ISetpointIterator dudeSetpointIterator = new StaticSetpointIterator(dudeList);

        return new DriveTurnPath(aSnobot.getDrivetrain(), aSnobot.getPositioner(), dudeSetpointIterator);
    }

    @Override
    protected void execute()// order 66
    {
        double curAngle = mPositioner.getOrientationDegrees() - mStartAngle;
        double motorSpeed = mPathFollower.calcMotorSpeed(curAngle);
        mDriveTrain.setLeftRightSpeed(motorSpeed, -motorSpeed);
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
}
