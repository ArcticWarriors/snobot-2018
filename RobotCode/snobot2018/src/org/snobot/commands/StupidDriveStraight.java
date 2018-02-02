package org.snobot.commands;

import java.util.List;

import org.snobot.Snobot2018;
import org.snobot.drivetrain.IDriveTrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 * Autonomous command that allows the robot to drive straight at a certain speed
 * for a certain period of time and stops it at the end.
 * 
 * @author Nora
 *
 */
public class StupidDriveStraight extends TimedCommand
{
    private final double mSpeed;
    private final IDriveTrain mDriveTrain;

    /**
     * Constructor.
     * 
     * @param aDriveTrain
     *            Controls the drive train which is the main driving component
     * @param aTimeout
     *            How long we will drive within the time constraints
     * @param aSpeed
     *            How fast we will drive with a value between -1 and 1
     */
    public StupidDriveStraight(IDriveTrain aDriveTrain, double aTimeout, double aSpeed)
    {
        super(aTimeout);
        mSpeed = aSpeed;
        mDriveTrain = aDriveTrain;
    }

    /**
     * Static utility to parse the StupidDriveStraight command.
     * 
     * @param aArgs
     *            The argument list
     * @param aSnobot
     *            This years robot
     * @return The constructed command
     */
    public static Command parseCommand(List<String> aArgs, Snobot2018 aSnobot)
    {
        double time = Double.parseDouble(aArgs.get(1));
        double speed = Double.parseDouble(aArgs.get(2));
        IDriveTrain snobot = aSnobot.getDrivetrain();
        return new StupidDriveStraight(snobot, time, speed);
    }

    @Override
    public void execute()
    {
        mDriveTrain.setLeftRightSpeed(mSpeed, mSpeed);
    }

    @Override
    public void end()
    {
        mDriveTrain.setLeftRightSpeed(0, 0);
    }
}
