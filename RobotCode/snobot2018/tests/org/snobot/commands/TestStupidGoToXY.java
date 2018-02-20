package org.snobot.commands;

import org.junit.Assert;
import org.junit.Test;
import org.snobot.test.utilities.BaseSimulatorAutonTest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class TestStupidGoToXY extends BaseSimulatorAutonTest
{
    public TestStupidGoToXY()
    {
        super(false);
    }

    @Test
    public void testGo10FeetStraight()
    {
        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestStupidGoToXY/TestStupidGoToXY");

        mSnobot.getPositioner().setPosition(0, 0, 0);

        simulateForTime(10, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assert.assertTrue(group.isCompleted());
    }

}
