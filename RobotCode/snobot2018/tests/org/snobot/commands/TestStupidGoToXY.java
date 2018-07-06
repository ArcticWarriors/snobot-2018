package org.snobot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snobot.test.utilities.BaseSimulatorAutonTest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class TestStupidGoToXY extends BaseSimulatorAutonTest
{

    @Test
    public void testGo10FeetStraight()
    {
        initializeRobotAndSimulator(false);

        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestStupidGoToXY/TestStupidGoToXY");

        mSnobot.getPositioner().setPosition(0, 0, 0);

        simulateForTime(10, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assertions.assertTrue(group.isCompleted());
    }

}
