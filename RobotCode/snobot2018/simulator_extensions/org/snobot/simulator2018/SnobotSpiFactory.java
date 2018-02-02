package org.snobot.simulator2018;

import java.util.Arrays;
import java.util.Collection;

import com.snobot.simulator.simulator_components.ISpiWrapper;
import com.snobot.simulator.simulator_components.components_factory.DefaultSpiSimulatorFactory;

public class SnobotSpiFactory extends DefaultSpiSimulatorFactory
{

    @Override
    protected ISpiWrapper createSimulator(int aPort, String aType)
    {
        if ("Dotstar".equals(aType))
        {
            return new DotstarSimulator(aPort);
        }

        return super.createSimulator(aPort, aType);
    }

    @Override
    public Collection<String> getAvailableTypes()
    {
        return Arrays.asList("Dotstar", "NavX", "ADXRS450", "ADXL345", "ADXL362");
    }
}
