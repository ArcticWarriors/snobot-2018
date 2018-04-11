package org.snobot.autonomous;

import java.text.DecimalFormat;

import org.junit.Assert;
import org.junit.Test;
import org.snobot.test.utilities.BaseSimulatorTest;

import com.snobot.simulator.SensorActuatorRegistry;

public class AutonChooserSwitchTest extends BaseSimulatorTest
{
    private static final int sANALOG_PORT = 4;
    private static final DecimalFormat sDF = new DecimalFormat("0.000");

    public AutonChooserSwitchTest()
    {
        super(false);
    }

    @Test
    public void testSixPosition()
    {
        // We expect the thresholds to be at 5/6 increments, i.e.
        // 0: 0.000 - 0.833
        // 1: 0.833 - 1.666
        // 2: 1.666 - 2.500
        // 3: 2.500 - 3.333
        // 4: 3.333 - 4.166
        // 5: 4.166 - 5.000

        AutonChooserSwitch autonSwitch = new AutonChooserSwitch(6, sANALOG_PORT);

        double increment = .01;

        // Test position 0
        checkIteration(autonSwitch, 0, 0.000, 0.833, increment);
        checkIteration(autonSwitch, 1, 0.834, 1.666, increment);
        checkIteration(autonSwitch, 2, 1.667, 2.500, increment);
        checkIteration(autonSwitch, 3, 2.501, 3.333, increment);
        checkIteration(autonSwitch, 4, 3.334, 4.166, increment);
        checkIteration(autonSwitch, 5, 4.167, 5.000, increment);
    }

    @Test
    public void testThreePosition()
    {
        // We expect the thresholds to be at 2/3 increments, i.e.
        // 0: 0.000 - 1.666
        // 1: 1.666 - 3.333
        // 2: 3.333 - 5.000

        AutonChooserSwitch autonSwitch = new AutonChooserSwitch(3, sANALOG_PORT);

        double increment = .01;

        // Test position 0
        checkIteration(autonSwitch, 0, 0.000, 1.666, increment);
        checkIteration(autonSwitch, 1, 1.667, 3.333, increment);
        checkIteration(autonSwitch, 2, 3.334, 5.000, increment);
    }

    private void checkIteration(AutonChooserSwitch aSwitch, int aExpected, double aMin, double aMax, double aIncrement)
    {
        for (double voltage = aMin; voltage < aMax; voltage += aIncrement)
        {
            SensorActuatorRegistry.get().getAnalog().get(sANALOG_PORT).setVoltage(voltage);

            int position = aSwitch.getPosition();
            Assert.assertEquals("Voltage=" + sDF.format(voltage), aExpected, position);
        }
    }

}
