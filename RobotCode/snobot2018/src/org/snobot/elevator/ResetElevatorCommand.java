package org.snobot.elevator;

import edu.wpi.first.wpilibj.command.Command;


public class ResetElevatorCommand extends Command
{
    private final IElevator mElevator;

    /**
     * Constructor.
     * 
     * @param aElevator
     *            IElevator to control the reset of
     */
    public ResetElevatorCommand(IElevator aElevator)
    {
        mElevator = aElevator;
    }

    @Override
    public void start()
    {
        mElevator.resetEncoders();
    }

    @Override
    protected boolean isFinished()
    {
        return true;
    }
}
