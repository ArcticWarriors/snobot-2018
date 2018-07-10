package org.snobot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snobot.claw.IClaw;
import org.snobot.test.utilities.BaseSimulatorAutonTest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class TestClawCommand extends BaseSimulatorAutonTest
{
    @Test
    public void testClawOpenCommand()
    {
        initializeRobotAndSimulator(false);

        IClaw claw = mSnobot.getClaw();
        Assertions.assertFalse(claw.isOpen());

        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestClaw/TestOpenClaw.auto");

        simulateForTime(.5, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assertions.assertTrue(group.isCompleted());
        Assertions.assertTrue(claw.isOpen());
    }

    @Test
    public void testClawCloseCommand()
    {
        initializeRobotAndSimulator(false);

        IClaw claw = mSnobot.getClaw();
        Assertions.assertFalse(claw.isOpen());

        claw.open();
        Assertions.assertTrue(claw.isOpen());

        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestClaw/TestCloseClaw.auto");

        simulateForTime(.5, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assertions.assertTrue(group.isCompleted());
        Assertions.assertFalse(claw.isOpen());
    }
}
