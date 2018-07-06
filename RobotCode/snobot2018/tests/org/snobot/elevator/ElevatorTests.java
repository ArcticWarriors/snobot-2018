package org.snobot.elevator;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.snobot.Properties2018;
import org.snobot.test.utilities.BaseSimulatorTest;


public class ElevatorTests extends BaseSimulatorTest
{
    @ParameterizedTest
    @MethodSource("getData")
    public void testMoveElevatorUp(boolean aUseCan)
    {
        initializeRobotAndSimulator(aUseCan);

        IElevator elevator = mSnobot.getElevator();

        simulateForTime(1.5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.setMotorSpeed(1.0);

            Assertions.assertEquals(1.0, elevator.getSpeed(), 0.0001);
        });

        double heightAfterUp = elevator.getHeight();
        Assertions.assertTrue(heightAfterUp > 0);

        simulateForTime(.5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.setMotorSpeed(-1.0);

            Assertions.assertEquals(-1.0, elevator.getSpeed(), 0.0001);
        });

        double heightAfterDown = elevator.getHeight();
        Assertions.assertTrue(heightAfterDown > 0);
        Assertions.assertTrue(heightAfterDown < heightAfterUp);
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testLimits(boolean aUseCan)
    {
        initializeRobotAndSimulator(aUseCan);

        IElevator elevator = mSnobot.getElevator();

        // Can't move the elevator down if it is down
        simulateForTime(1.5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.setMotorSpeed(-1.0);

            Assertions.assertEquals(0, elevator.getSpeed(), 0.0001);
            Assertions.assertEquals(0, elevator.getHeight(), 0.0001);
        });
        
        // Move up for a dumb amount of time
        simulateForTime(5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.setMotorSpeed(1.0);
        });

        double maxHeight = Properties2018.sELEVATOR_MAX_HEIGHT.getValue();
        Assertions.assertTrue(elevator.getHeight() <= maxHeight);

        // If its all the way up, can't go up no mo
        simulateForTime(1.5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.setMotorSpeed(1.0);

            Assertions.assertEquals(0, elevator.getSpeed(), 0.0001);
            Assertions.assertTrue(elevator.getHeight() <= maxHeight);
        });
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testGoToHeight(boolean aUseCan)
    {
        initializeRobotAndSimulator(aUseCan);

        IElevator elevator = mSnobot.getElevator();

        double deadband = Properties2018.sELEVATOR_HEIGHT_DEADBAND.getValue();

        // Go up from 0
        double height0 = 50;
        simulateForTime(3, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.gotoHeight(height0);
        });

        Assertions.assertEquals(height0, elevator.getHeight(), deadband);
        Assertions.assertTrue(elevator.gotoHeight(height0));

        // Go back down
        double height1 = 10;
        simulateForTime(3, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.gotoHeight(height1);
        });

        Assertions.assertEquals(height1, elevator.getHeight(), deadband);
        Assertions.assertTrue(elevator.gotoHeight(height1));

        // Go up to the max
        double height2 = Properties2018.sELEVATOR_MAX_HEIGHT.getValue();
        simulateForTime(5.5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.gotoHeight(height2);
        });

        Assertions.assertTrue(elevator.gotoHeight(height2));
        Assertions.assertEquals(height2, elevator.getHeight(), deadband);
    }

    public static Collection<Boolean> getData()
    {
        return Arrays.asList(false);
    }
}
