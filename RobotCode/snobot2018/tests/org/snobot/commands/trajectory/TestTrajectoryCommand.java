package org.snobot.commands.trajectory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snobot.test.utilities.BaseSimulatorAutonTest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class TestTrajectoryCommand extends BaseSimulatorAutonTest
{
    @Test
    public void testTrajectory()
    {
        initializeRobotAndSimulator(false);

        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestTrajectories/TestMoveLeft10ft5ftsec.auto");

        simulateForTime(6, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assertions.assertTrue(group.isCompleted());
    }

}
