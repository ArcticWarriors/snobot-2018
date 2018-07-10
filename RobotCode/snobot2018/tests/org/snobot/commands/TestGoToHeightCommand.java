package org.snobot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snobot.Properties2018;
import org.snobot.elevator.IElevator;
import org.snobot.test.utilities.BaseSimulatorAutonTest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class TestGoToHeightCommand extends BaseSimulatorAutonTest
{
    @Test
    public void testGoToHeightRaw()
    {
        initializeRobotAndSimulator(false);

        IElevator elevator = mSnobot.getElevator();

        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestGoToHeight/TestGoToHeight_HighScale.auto");

        simulateForTime(5, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assertions.assertTrue(group.isCompleted());
        Assertions.assertEquals(72, elevator.getHeight(), Properties2018.sELEVATOR_HEIGHT_DEADBAND.getValue());
    }

    @Test
    public void testGoToHeightEnum()
    {
        initializeRobotAndSimulator(false);

        IElevator elevator = mSnobot.getElevator();

        CommandGroup group = setupCommand("resources/autonomous/TestAutonomous/TestGoToHeight/TestGoToHeight_Enum");

        simulateForTime(5, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assertions.assertTrue(group.isCompleted());

        double expectedHeight = Properties2018.sELEVATOR_SCALE_HEIGHT_LOW.getValue();
        Assertions.assertEquals(expectedHeight, elevator.getHeight(), Properties2018.sELEVATOR_HEIGHT_DEADBAND.getValue());
    }

}
