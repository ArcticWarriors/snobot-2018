package org.snobot.winch;

import org.junit.Assert;
import org.junit.Test;
import org.snobot.test.utilities.BaseSimulatorTest;

public class WinchTest extends BaseSimulatorTest
{
    public WinchTest()
    {
        super(false);
    }
    
    @Test
    public void testSetWinchSpeedForward()
    {
        IWinch winch = mSnobot.getWinch();

        simulateForTime(1, () ->
        {
            winch.setMotorSpeed(1);
            runSnobotLoopWithoutControl();

            Assert.assertEquals(1, winch.getWinchSpeed(), 0.0001);
        });
    }

    @Test
    public void testWinchBackwards()
    {
        IWinch winch = mSnobot.getWinch();

        simulateForTime(1, () ->
        {
            winch.setMotorSpeed(-1);
            runSnobotLoopWithoutControl();

            Assert.assertEquals(0, winch.getWinchSpeed(), 0.0001);
        });
    }

}
