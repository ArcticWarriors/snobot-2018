package org.snobot.claw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snobot.test.utilities.BaseSimulatorTest;

public class ClawTest extends BaseSimulatorTest
{
    @Test
    public void testClawOpen()
    {
        initializeRobotAndSimulator(false);

        IClaw claw = mSnobot.getClaw();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            claw.open();

            Assertions.assertTrue(claw.isOpen());
        });
    }

    @Test
    public void testClawClosed()
    {
        initializeRobotAndSimulator(false);

        IClaw claw = mSnobot.getClaw();

        simulateForTime(1, () ->
        {
            runSnobotLoopWithoutControl();
            claw.close();

            Assertions.assertFalse(claw.isOpen());
        });
    }
}
