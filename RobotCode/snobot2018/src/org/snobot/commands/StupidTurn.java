package org.snobot.commands;

import java.util.List;

import org.snobot.Snobot2018;
import org.snobot.drivetrain.IDriveTrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class StupidTurn extends TimedCommand
{
    private final double mSpeed;
    private final IDriveTrain mDriveTrain;

    /**
     * Turns the robot.
     * 
     * @param aDriveTrain
     *            The robot drivetrain.
     * @param aTimeout
     *            Timed command.
     * @param aSpeed
     *            The speed for the robot.
     */
    public StupidTurn(IDriveTrain aDriveTrain, double aTimeout, double aSpeed)
    {
        super(aTimeout);
        mSpeed = aSpeed;
        mDriveTrain = aDriveTrain;

    }

    /**
     * Parses the command for the autonomous command creator.
     * 
     * @param aArgs
     *            List of args passed in from the command file.
     * @param aSnobot
     *            The robot.
     * @return StupidTurn command.
     */
    public static Command parseCommand(List<String> aArgs, Snobot2018 aSnobot)
    {
        double time = Double.parseDouble(aArgs.get(1));
        double speed = Double.parseDouble(aArgs.get(2));
        IDriveTrain snobot = aSnobot.getDrivetrain();
        return new StupidTurn(snobot, time, speed);
    }

    /**
     * Turns the robot by setting the left and right opposite.
     */
    public void execute()
    {
        mDriveTrain.setLeftRightSpeed(mSpeed, -mSpeed);
    }

    /**
     * Ends the program by setting the motors to zero.
     */
    public void end()
    {
        mDriveTrain.setLeftRightSpeed(0, 0);
    }
}
