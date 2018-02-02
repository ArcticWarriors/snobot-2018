package org.snobot.test.utilities;

import org.snobot.Snobot2018;

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
     * 
     * @param aUseCan
     *            If CTRE tools are being used. Used to create the simulator and
     *            robot
     */
    public BaseSimulatorTest(boolean aUseCan)
    {
        setup();

        mSimulator = new SimulatorMock(aUseCan);
        mSimulator.loadConfig("DoesntMatter");

        mSnobot = new Snobot2018(aUseCan);
        mSnobot.robotInit();
        mSnobot.initializeLogHeaders();

        mSimulator.createSimulatorComponents();
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
        mSnobot.updateLog();
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
