package org.snobot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snobot.test.utilities.BaseSimulatorAutonTest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class TestGoToXY extends BaseSimulatorAutonTest
{
    @Test
    public void testGo10FeetStraight()
    {
        runTest("resources/autonomous/TestAutonomous/TestGoToXY/TestGoToXY_000.csv");
    }

    @Test
    public void testGo10FeetAt45Degrees()
    {
        runTest("resources/autonomous/TestAutonomous/TestGoToXY/TestGoToXY_045.csv");
    }

    @Test
    public void testGo10FeetAt90Degrees()
    {
        runTest("resources/autonomous/TestAutonomous/TestGoToXY/TestGoToXY_090.csv");
    }

    @Test
    public void testGo10FeetAt135Degrees()
    {
        runTest("resources/autonomous/TestAutonomous/TestGoToXY/TestGoToXY_135.csv");
    }

    @Test
    public void testGo10FeetAt180Degrees()
    {
        runTest("resources/autonomous/TestAutonomous/TestGoToXY/TestGoToXY_180.csv");
    }

    @Test
    public void testGo10FeetAt225Degrees()
    {
        runTest("resources/autonomous/TestAutonomous/TestGoToXY/TestGoToXY_225.csv");
    }

    @Test
    public void testGo10FeetAt270Degrees()
    {
        runTest("resources/autonomous/TestAutonomous/TestGoToXY/TestGoToXY_270.csv");
    }

    private void runTest(String aFile)
    {
        initializeRobotAndSimulator(false);

        CommandGroup group = setupCommand(aFile);

        mSnobot.getPositioner().setPosition(0, 0, 0);

        simulateForTime(10, () ->
        {
            Scheduler.getInstance().run();
            runSnobotLoopWithoutControl();
        });

        Assertions.assertTrue(group.isCompleted());
    }

}
