package org.snobot.test.utilities;

import org.snobot.Snobot2018;
import org.snobot.simulator2018.Snobot2018Simulator;

import com.snobot.simulator.ASimulator;
import com.snobot.simulator.DefaultDataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.SnobotLogLevel;

import edu.wpi.first.wpilibj.RobotBase;

public class BaseSimulatorTest
{
    private static boolean INITIALIZED = false;

    protected ASimulator mSimulator;
    protected Snobot2018 mSnobot;

    /**
     * Constructor.
     */
    public BaseSimulatorTest()
    {
        setup();
    }

    protected void initializeRobotAndSimulator(boolean aUseCan)
    {
        mSimulator = new Snobot2018Simulator(aUseCan);
        mSimulator.loadConfig("DoesntMatter");

        mSnobot = new Snobot2018(aUseCan);
        mSnobot.robotInit();
    }

    private final void setup()
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

    protected void runSnobotLoopWithoutControl()
    {
        mSnobot.update();
        mSnobot.updateSmartDashboard();
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
