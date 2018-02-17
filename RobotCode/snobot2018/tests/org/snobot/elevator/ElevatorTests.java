package org.snobot.elevator;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.snobot.Properties2018;
import org.snobot.test.utilities.BaseSimulatorTest;


@RunWith(value = Parameterized.class)
public class ElevatorTests extends BaseSimulatorTest
{
    public ElevatorTests(boolean aUseCan)
    {
        super(aUseCan);
    }

    @Test
    public void testMoveElevatorUp()
    {
        IElevator elevator = mSnobot.getElevator();

        simulateForTime(1.5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.setMotorSpeed(1.0);

            Assert.assertEquals(1.0, elevator.getSpeed(), 0.0001);
        });

        double heightAfterUp = elevator.getHeight();
        Assert.assertTrue(heightAfterUp > 0);

        simulateForTime(.5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.setMotorSpeed(-1.0);

            Assert.assertEquals(-1.0, elevator.getSpeed(), 0.0001);
        });

        double heightAfterDown = elevator.getHeight();
        Assert.assertTrue(heightAfterDown > 0);
        Assert.assertTrue(heightAfterDown < heightAfterUp);
    }

    @Test
    public void testLimits()
    {
        IElevator elevator = mSnobot.getElevator();

        // Can't move the elevator down if it is down
        simulateForTime(1.5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.setMotorSpeed(-1.0);

            Assert.assertEquals(0, elevator.getSpeed(), 0.0001);
            Assert.assertEquals(0, elevator.getHeight(), 0.0001);
        });
        
        // Move up for a dumb amount of time
        simulateForTime(5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.setMotorSpeed(1.0);
        });

        double maxHeight = Properties2018.sELEVATOR_MAX_HEIGHT.getValue();
        Assert.assertTrue(elevator.getHeight() <= maxHeight);

        // If its all the way up, can't go up no mo
        simulateForTime(1.5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.setMotorSpeed(1.0);

            Assert.assertEquals(0, elevator.getSpeed(), 0.0001);
            Assert.assertTrue(elevator.getHeight() <= maxHeight);
        });
    }

    @Test
    public void testGoToHeight()
    {
        IElevator elevator = mSnobot.getElevator();

        double deadband = Properties2018.sELEVATOR_HEIGHT_DEADBAND.getValue();

        // Go up from 0
        double height0 = 50;
        simulateForTime(3, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.gotoHeight(height0);
        });

        Assert.assertEquals(height0, elevator.getHeight(), deadband);
        Assert.assertTrue(elevator.gotoHeight(height0));

        // Go back down
        double height1 = 10;
        simulateForTime(3, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.gotoHeight(height1);
        });

        Assert.assertEquals(height1, elevator.getHeight(), deadband);
        Assert.assertTrue(elevator.gotoHeight(height1));

        // Go up to the max
        double height2 = Properties2018.sELEVATOR_MAX_HEIGHT.getValue();
        simulateForTime(5.5, () ->
        {
            runSnobotLoopWithoutControl();
            elevator.gotoHeight(height2);
        });

        Assert.assertTrue(elevator.gotoHeight(height2));
        Assert.assertEquals(height2, elevator.getHeight(), deadband);
    }

    @Parameters(name = "Test: {index} IsCan={0}")
    public static Collection<Boolean> data()
    {
        return Arrays.asList(false);
    }
}
