package org.snobot.commands.path;

import org.junit.Assert;
import org.junit.Test;
import org.snobot.test.utilities.BaseSimulatorAutonTest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class TestPathCommand extends BaseSimulatorAutonTest
{
    public TestPathCommand()
    {
        super(false);
    }

    @Test
    public void testPath()
    {
        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestPathDrive/TestPathDriveForwardAt5ftsec.auto");

        simulateForTime(5, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assert.assertTrue(group.isCompleted());
    }

    @Test
    public void testPathWithGyro()
    {
        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestPathDriveWithGyro/TestPathDriveForwardWithGyroAt5ftsec.auto");

        simulateForTime(5, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assert.assertTrue(group.isCompleted());
    }

}
