package org.snobot.claw;

import org.junit.Assert;
import org.junit.Test;
import org.snobot.test.utilities.BaseSimulatorTest;

public class ClawTest extends BaseSimulatorTest
{
    public ClawTest()
    {
        super(false);
    }

    @Test
    public void testClawOpen()
    {
        IClaw claw = mSnobot.getClaw();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            claw.open();

            Assert.assertTrue(claw.isOpen());
        });
    }

    @Test
    public void testClawClosed()
    {
        IClaw claw = mSnobot.getClaw();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            claw.close();

            Assert.assertFalse(claw.isOpen());
        });
    }
}
