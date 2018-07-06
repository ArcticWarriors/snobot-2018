package org.snobot.commands.path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snobot.test.utilities.BaseSimulatorAutonTest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class TestPathCommand extends BaseSimulatorAutonTest
{

    @Test
    public void testPath()
    {
        initializeRobotAndSimulator(false);

        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestPathDrive/TestPathDriveForwardAt5ftsec.auto");

        simulateForTime(5, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assertions.assertTrue(group.isCompleted());
    }

    @Test
    public void testPathWithGyro()
    {
        initializeRobotAndSimulator(false);

        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestPathDriveWithGyro/TestPathDriveForwardWithGyroAt5ftsec.auto");

        simulateForTime(5, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assertions.assertTrue(group.isCompleted());
    }

}
