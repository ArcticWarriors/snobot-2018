package org.snobot.simulator2018;

import java.util.Arrays;
import java.util.Collection;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.factories.DefaultSpiSimulatorFactory;
import com.snobot.simulator.module_wrapper.interfaces.ISpiWrapper;

public class SnobotSpiFactory extends DefaultSpiSimulatorFactory
{
    private static final String sDOTSTAR_TYPE = "Dotstar";

    @Override
    public boolean create(int aPort, String aType)
    {
        if (sDOTSTAR_TYPE.equals(aType))
        {
            DotstarSimulator simulator = new DotstarSimulator(aPort);
            SensorActuatorRegistry.get().register(simulator, aPort);
            return true;
        }

        return super.create(aPort, aType);
    }

    @Override
    protected String getNameForType(ISpiWrapper aType)
    {
        if (aType instanceof DotstarSimulator)
        {
            return sDOTSTAR_TYPE;
        }

        return super.getNameForType(aType);
    }

    @Override
    public Collection<String> getAvailableTypes()
    {
        return Arrays.asList("Dotstar", "NavX", "ADXRS450", "ADXL345", "ADXL362");
    }
}
