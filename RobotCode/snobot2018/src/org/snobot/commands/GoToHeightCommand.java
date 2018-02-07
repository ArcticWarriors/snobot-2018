package org.snobot.commands;

import java.util.List;

import org.snobot.Snobot2018;
import org.snobot.elevator.IElevator;

import edu.wpi.first.wpilibj.command.Command;

public class GoToHeightCommand extends Command
{
    private final double mHeight;
    private final IElevator mElevator;
    private boolean mFinished;

    /**
     * Moves the elevator to desired height.
     * 
     * @param aHeight
     *            is height of the elevator.
     * @param aElevator
     *            is the elevator used.
     */
    public GoToHeightCommand(double aHeight, IElevator aElevator)
    {
        mHeight = aHeight;
        mElevator = aElevator;
    }

    /**
     * Static utility to parse the GoToHeightCommand command.
     * 
     * @param aArgs
     *            The argument list
     * @param aSnobot
     *            This years robot
     * @return The constructed command
     */
    public static Command parseCommand(List<String> aArgs, Snobot2018 aSnobot)
    {
        double height = Double.parseDouble(aArgs.get(1));
        IElevator snobot = aSnobot.getElevator();
        return new GoToHeightCommand(height, snobot);
    }

    @Override
    protected void initialize()

    {
        mElevator.setHeight(mHeight);
        // System.out.println("GoToHeight: " + mHeight);
    }

    @Override
    protected void execute()

    {

        mFinished = mElevator.gotoHeight();

    }

    @Override

    protected boolean isFinished()

    {
        return mFinished;

    }

    @Override
    protected void end()

    {
        // System.out.println("GoToHeightCommand: END");

    }

}
