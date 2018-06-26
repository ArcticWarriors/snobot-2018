package org.snobot.drivetrain;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.snobot.test.utilities.BaseSimulatorTest;

@RunWith(value = Parameterized.class)
public class DrivetrainTest extends BaseSimulatorTest
{
    private static final String sBAD_LEFT_DISTANCE_ERROR = "Left Distance wrong: ";
    private static final String sBAD_RIGHT_DISTANCE_ERROR = "Right Distance wrong: ";

    public DrivetrainTest(boolean aUseCan)
    {
        super(aUseCan);
    }

    @Test
    public void testSetDrivetrainSpeedForward()
    {
        IDriveTrain drivetrain = mSnobot.getDrivetrain();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            drivetrain.setLeftRightSpeed(1, 1);

            Assert.assertEquals(1, drivetrain.getLeftMotorSpeed(), 0.0001);
            Assert.assertEquals(1, drivetrain.getRightMotorSpeed(), 0.0001);
        });

        Assert.assertTrue(sBAD_LEFT_DISTANCE_ERROR + drivetrain.getLeftDistance(), drivetrain.getLeftDistance() > 0);
        Assert.assertTrue(sBAD_RIGHT_DISTANCE_ERROR + drivetrain.getRightDistance(), drivetrain.getRightDistance() > 0);
        Assert.assertEquals(0, drivetrain.getHeading(), 0.0001);
    }

    @Test
    public void testSetDrivetrainSpeedReverse()
    {
        IDriveTrain drivetrain = mSnobot.getDrivetrain();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            drivetrain.setLeftRightSpeed(-1, -1);

            Assert.assertEquals(-1, drivetrain.getLeftMotorSpeed(), 0.0001);
            Assert.assertEquals(-1, drivetrain.getRightMotorSpeed(), 0.0001);
        });

        Assert.assertTrue(sBAD_LEFT_DISTANCE_ERROR + drivetrain.getLeftDistance(), drivetrain.getLeftDistance() < 0);
        Assert.assertTrue(sBAD_RIGHT_DISTANCE_ERROR + drivetrain.getRightDistance(), drivetrain.getRightDistance() < 0);
        Assert.assertEquals(0, drivetrain.getHeading(), 0.0001);
    }

    @Test
    public void testSetDrivetrainSpeedTurnRight()
    {
        IDriveTrain drivetrain = mSnobot.getDrivetrain();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            drivetrain.setLeftRightSpeed(1, -1);

            Assert.assertEquals(1, drivetrain.getLeftMotorSpeed(), 0.0001);
            Assert.assertEquals(-1, drivetrain.getRightMotorSpeed(), 0.0001);
        });

        Assert.assertTrue(sBAD_LEFT_DISTANCE_ERROR + drivetrain.getLeftDistance(), drivetrain.getLeftDistance() > 0);
        Assert.assertTrue(sBAD_RIGHT_DISTANCE_ERROR + drivetrain.getRightDistance(), drivetrain.getRightDistance() < 0);
        Assert.assertTrue("Angle is wrong (" + drivetrain.getHeading() + ")", drivetrain.getHeading() > 0);
    }

    @Test
    public void testSetDrivetrainSpeedTurnLeft()
    {
        IDriveTrain drivetrain = mSnobot.getDrivetrain();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            drivetrain.setLeftRightSpeed(-1, 1);

            Assert.assertEquals(-1, drivetrain.getLeftMotorSpeed(), 0.0001);
            Assert.assertEquals(1, drivetrain.getRightMotorSpeed(), 0.0001);
        });

        Assert.assertTrue(sBAD_LEFT_DISTANCE_ERROR + drivetrain.getLeftDistance(), drivetrain.getLeftDistance() < 0);
        Assert.assertTrue(sBAD_RIGHT_DISTANCE_ERROR + drivetrain.getRightDistance(), drivetrain.getRightDistance() > 0);
        Assert.assertTrue("Angle is wrong (" + drivetrain.getHeading() + ")", drivetrain.getHeading() < 0);
    }

    @Parameters(name = "Test: {index} IsCan={0}")
    public static Collection<Boolean> data()
    {
        return Arrays.asList(true, false);
    }
}
