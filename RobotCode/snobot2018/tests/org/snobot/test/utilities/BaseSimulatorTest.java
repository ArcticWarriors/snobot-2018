package org.snobot.test.utilities;

import org.junit.Before;

import com.snobot.simulator.DefaultDataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.SnobotLogLevel;

import edu.wpi.first.wpilibj.RobotBase;

public class BaseSimulatorTest
{
    private static boolean INITIALIZED = false;

    @Before
    public void setup()
    {
        if (!INITIALIZED)
        {
            DefaultDataAccessorFactory.initalize();
            DataAccessorFactory.getInstance().getSimulatorDataAccessor().setLogLevel(SnobotLogLevel.DEBUG);

            INITIALIZED = true;
        }

        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
        RobotBase.initializeHardwareConfiguration();
        
    }

    protected void simulateForTime(double aSeconds, Runnable aTask)
    {
        simulateForTime(aSeconds, .02, aTask);
    }

    protected void simulateForTime(double aSeconds, double aUpdatePeriod, Runnable aTask)
    {
        double updateFrequency = 1 / aUpdatePeriod;

        for (int i = 0; i < updateFrequency * aSeconds; ++i)
        {
            aTask.run();
            DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateSimulatorComponents(aUpdatePeriod);
        }
    }
}
