package org.snobot.drivetrain;

import org.junit.Test;
import org.snobot.test.utilities.BaseSimulatorTest;

public class DrivetrainTest extends BaseSimulatorTest
{
    @Test
    public void testSetDrivetrainSpeed()
    {
        simulateForTime(30, () ->
        {
            System.out.println("Running"); // NOPMD
        });
    }

}
