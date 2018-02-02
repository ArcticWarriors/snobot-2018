package org.snobot.test.utilities;

import com.snobot.simulator.ASimulator;

public class SimulatorMock extends ASimulator
{
    private final boolean mUseCan;

    /**
     * Constructor.
     * 
     * @param aUseCan
     *            If CTRE modules are being used. Used to specify which
     *            simulator config to read
     */
    public SimulatorMock(boolean aUseCan)
    {
        mUseCan = aUseCan;
    }

    @Override
    public boolean loadConfig(String aConfigFile)
    {
        String actualConfigFile = null;
        if (mUseCan)
        {
            actualConfigFile = "simulator_config/simulator_config_ctre.yml";
        }
        else
        {
            actualConfigFile = "simulator_config/simulator_config_standard.yml";
        }

        return super.loadConfig(actualConfigFile);
    }
}

