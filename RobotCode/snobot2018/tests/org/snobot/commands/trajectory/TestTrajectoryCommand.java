package org.snobot.commands.trajectory;

import org.junit.Assert;
import org.junit.Test;
import org.snobot.test.utilities.BaseSimulatorAutonTest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class TestTrajectoryCommand extends BaseSimulatorAutonTest
{
    public TestTrajectoryCommand()
    {
        super(false);
    }

    @Test
    public void testTrajectory()
    {
        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestTrajectories/TestMoveLeft10ft5ftsec.auto");

        simulateForTime(6, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assert.assertTrue(group.isCompleted());
    }

}
