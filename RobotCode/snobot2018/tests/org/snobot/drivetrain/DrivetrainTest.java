package org.snobot.drivetrain;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.snobot.test.utilities.BaseSimulatorTest;

public class DrivetrainTest extends BaseSimulatorTest
{
    private static final String sDATA_FUNCTION = "getData";
    private static final String sBAD_LEFT_DISTANCE_ERROR = "Left Distance wrong: ";
    private static final String sBAD_RIGHT_DISTANCE_ERROR = "Right Distance wrong: ";

    @ParameterizedTest
    @MethodSource(sDATA_FUNCTION)
    public void testSetDrivetrainSpeedForward(boolean aUseCan)
    {
        initializeRobotAndSimulator(aUseCan);

        IDriveTrain drivetrain = mSnobot.getDrivetrain();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            drivetrain.setLeftRightSpeed(1, 1);

            Assertions.assertEquals(1, drivetrain.getLeftMotorSpeed(), 0.0001);
            Assertions.assertEquals(1, drivetrain.getRightMotorSpeed(), 0.0001);
        });

        Assertions.assertTrue(drivetrain.getLeftDistance() > 0, sBAD_LEFT_DISTANCE_ERROR + drivetrain.getLeftDistance());
        Assertions.assertTrue(drivetrain.getRightDistance() > 0, sBAD_RIGHT_DISTANCE_ERROR + drivetrain.getRightDistance());
        Assertions.assertEquals(0, drivetrain.getHeading(), 0.0001);
    }

    @ParameterizedTest
    @MethodSource(sDATA_FUNCTION)
    public void testSetDrivetrainSpeedReverse(boolean aUseCan)
    {
        initializeRobotAndSimulator(aUseCan);

        IDriveTrain drivetrain = mSnobot.getDrivetrain();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            drivetrain.setLeftRightSpeed(-1, -1);

            Assertions.assertEquals(-1, drivetrain.getLeftMotorSpeed(), 0.0001);
            Assertions.assertEquals(-1, drivetrain.getRightMotorSpeed(), 0.0001);
        });

        Assertions.assertTrue(drivetrain.getLeftDistance() < 0, sBAD_LEFT_DISTANCE_ERROR + drivetrain.getLeftDistance());
        Assertions.assertTrue(drivetrain.getRightDistance() < 0, sBAD_RIGHT_DISTANCE_ERROR + drivetrain.getRightDistance());
        Assertions.assertEquals(0, drivetrain.getHeading(), 0.0001);
    }

    @ParameterizedTest
    @MethodSource(sDATA_FUNCTION)
    public void testSetDrivetrainSpeedTurnRight(boolean aUseCan)
    {
        initializeRobotAndSimulator(aUseCan);

        IDriveTrain drivetrain = mSnobot.getDrivetrain();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            drivetrain.setLeftRightSpeed(1, -1);

            Assertions.assertEquals(1, drivetrain.getLeftMotorSpeed(), 0.0001);
            Assertions.assertEquals(-1, drivetrain.getRightMotorSpeed(), 0.0001);
        });

        Assertions.assertTrue(drivetrain.getLeftDistance() > 0, sBAD_LEFT_DISTANCE_ERROR + drivetrain.getLeftDistance());
        Assertions.assertTrue(drivetrain.getRightDistance() < 0, sBAD_RIGHT_DISTANCE_ERROR + drivetrain.getRightDistance());
        Assertions.assertTrue(drivetrain.getHeading() > 0, "Angle is wrong (" + drivetrain.getHeading() + ")");
    }

    @ParameterizedTest
    @MethodSource(sDATA_FUNCTION)
    public void testSetDrivetrainSpeedTurnLeft(boolean aUseCan)
    {
        initializeRobotAndSimulator(aUseCan);

        IDriveTrain drivetrain = mSnobot.getDrivetrain();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            drivetrain.setLeftRightSpeed(-1, 1);

            Assertions.assertEquals(-1, drivetrain.getLeftMotorSpeed(), 0.0001);
            Assertions.assertEquals(1, drivetrain.getRightMotorSpeed(), 0.0001);
        });

        Assertions.assertTrue(drivetrain.getLeftDistance() < 0, sBAD_LEFT_DISTANCE_ERROR + drivetrain.getLeftDistance());
        Assertions.assertTrue(drivetrain.getRightDistance() > 0, sBAD_RIGHT_DISTANCE_ERROR + drivetrain.getRightDistance());
        Assertions.assertTrue(drivetrain.getHeading() < 0, "Angle is wrong (" + drivetrain.getHeading() + ")");
    }

    public static Collection<Boolean> getData()
    {
        return Arrays.asList(true, false);
    }
}
