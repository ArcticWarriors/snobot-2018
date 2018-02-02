package org.snobot.simulator2018;

import com.snobot.simulator.ASimulator;
import com.snobot.simulator.jni.standard_components.SpiCallbackJni;

public class Snobot2018Simulator extends ASimulator
{
    private final boolean mUseCan;

    public Snobot2018Simulator()
    {
        this(true);
    }

    /**
     * Constructor.
     * 
     * @param aUseCan
     *            If CTRE modules are being used. Used to specify which
     *            simulator config to read
     */
    public Snobot2018Simulator(boolean aUseCan)
    {
        mUseCan = aUseCan;

        SpiCallbackJni.setSpiFactory(new SnobotSpiFactory());
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
