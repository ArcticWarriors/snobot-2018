package org.snobot.winch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snobot.test.utilities.BaseSimulatorTest;

public class WinchTest extends BaseSimulatorTest
{
    @Test
    public void testSetWinchSpeedForward()
    {
        initializeRobotAndSimulator(false);

        IWinch winch = mSnobot.getWinch();

        simulateForTime(1, () ->
        {
            winch.setMotorSpeed(1);
            runSnobotLoopWithoutControl();

            Assertions.assertEquals(1, winch.getWinchSpeed(), 0.0001);
        });
    }

    @Test
    public void testWinchBackwards()
    {
        initializeRobotAndSimulator(false);

        IWinch winch = mSnobot.getWinch();

        simulateForTime(1, () ->
        {
            winch.setMotorSpeed(-1);
            runSnobotLoopWithoutControl();

            Assertions.assertEquals(0, winch.getWinchSpeed(), 0.0001);
        });
    }

}
