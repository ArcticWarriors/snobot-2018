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
 * uses the motion profile to follow a straight path.
 * 
 * @author Andrew
 * 
 */
public class DriveStraightPathWithGyro extends DriveStraightPath
{
    private double mStartAngle;

    /**
     * Creates the drive straight with gyro path.
     * 
     * @param aDriveTrain
     *            The robot's drive train
     * @param aPositioner
     *            The robot's positioner
     * @param aSetpointIterator
     *            A setpoint iterator for the path follower
     */
    public DriveStraightPathWithGyro(IDriveTrain aDriveTrain, IPositioner aPositioner, double aDistance, ISetpointIterator aSetpointIterator)
    {
        super(aDriveTrain, aPositioner, aDistance, aSetpointIterator);
    }

    @Override
    protected void initialize()
    {
        mStartAngle = mPositioner.getOrientationDegrees();

        super.initialize();
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
        double distance = Double.parseDouble(aArgs.get(1));
        PathConfig dudePathConfig = new PathConfig(distance, // Endpoint
                Double.parseDouble(aArgs.get(2)), // Max Velocity
                Double.parseDouble(aArgs.get(3)), // Max Acceleration
                .02);

        PathGenerator dudePathGenerator = new PathGenerator();
        List<PathSetpoint> dudeList = dudePathGenerator.generate(dudePathConfig);
        ISetpointIterator dudeSetpointIterator = new StaticSetpointIterator(dudeList);

        return new DriveStraightPathWithGyro(aSnobot.getDrivetrain(), aSnobot.getPositioner(), distance, dudeSetpointIterator);
    }

    @Override
    protected void execute()
    {
        double motorSpeed = calculatePathSpeed();
        double angleError = mPositioner.getOrientationDegrees() - mStartAngle;
        double angleKP = Properties2018.sDRIVE_PATH_WITH_GYRO_KP.getValue();
        double addMorePower = angleKP * angleError;

        mDriveTrain.setLeftRightSpeed(motorSpeed - addMorePower, motorSpeed + addMorePower);
    }
}
