package org.snobot.commands;

import org.junit.Assert;
import org.junit.Test;
import org.snobot.claw.IClaw;
import org.snobot.test.utilities.BaseSimulatorAutonTest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class TestClawCommand extends BaseSimulatorAutonTest
{
    public TestClawCommand()
    {
        super(false);
    }

    @Test
    public void testClawOpenCommand()
    {
        IClaw claw = mSnobot.getClaw();
        Assert.assertFalse(claw.isOpen());

        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestClaw/TestOpenClaw.auto");

        simulateForTime(.5, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assert.assertTrue(group.isCompleted());
        Assert.assertTrue(claw.isOpen());
    }

    @Test
    public void testClawCloseCommand()
    {
        IClaw claw = mSnobot.getClaw();
        Assert.assertFalse(claw.isOpen());

        claw.open();
        Assert.assertTrue(claw.isOpen());

        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestClaw/TestCloseClaw.auto");

        simulateForTime(.5, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assert.assertTrue(group.isCompleted());
        Assert.assertFalse(claw.isOpen());
    }
}
