package org.snobot.commands;

import java.util.List;

import org.snobot.Snobot2018;
import org.snobot.claw.IClaw;

import edu.wpi.first.wpilibj.command.Command;

public class ClawCommand extends Command
{
    private final boolean mOpen;
    private final IClaw mClaw;

    /**
     * constructor for claw autonomous command.
     * 
     * @param aOpen
     *            open is true and Close is false.
     * @param aClaw
     *            is the claw interface for the claw.
     */
    public ClawCommand(boolean aOpen, IClaw aClaw)
    {
        mOpen = aOpen;
        mClaw = aClaw;
    }

    /**
     * Parsed command that allows autonomous factory to reach claw command.
     * 
     * @param aArgs
     *            Arguments from the command text file.
     * @param aSnobot
     *            rSnobot with all the subsystems on it.
     * @return returning the claw command.
     */
    public static Command parseCommand(List<String> aArgs, Snobot2018 aSnobot)
    {
        boolean open = Boolean.parseBoolean(aArgs.get(1));
        IClaw claw = aSnobot.getClaw();
        return new ClawCommand(open, claw);
    }

    @Override
    protected boolean isFinished()
    {
        return mClaw.isOpen() == mOpen;
    }

    @Override
    protected void execute()
    {
        if (mOpen)
        {
            mClaw.open();
        }
        else
        {
            mClaw.close();
        }
    }

}
