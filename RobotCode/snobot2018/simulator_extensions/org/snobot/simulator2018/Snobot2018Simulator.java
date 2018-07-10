package org.snobot.simulator2018;

import org.snobot.PortMappings2018;
import org.snobot.RobotTypeDetector;

import com.snobot.simulator.ASimulator;
import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.interfaces.IAnalogInWrapper;
import com.snobot.simulator.robot_container.IRobotClassContainer;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.java.JavaSimulatorDataAccessor;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Snobot2018Simulator extends ASimulator
{
    private final boolean mUseCan;

    public Snobot2018Simulator()
    {
        this(RobotTypeDetector.isCan());
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

        JavaSimulatorDataAccessor accessor = (JavaSimulatorDataAccessor) DataAccessorFactory.getInstance().getSimulatorDataAccessor();
        accessor.setSpiFactory(new SnobotSpiFactory());
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

    @Override
    public void setRobot(IRobotClassContainer aRobot)
    {
        super.setRobot(aRobot);

        SmartDashboard.putNumber("SIMULATOR.ModeSwitch", 2.5);
        SmartDashboard.putNumber("SIMULATOR.PositionSwitch", 2.5);
    }

    @Override
    public void update()
    {

        IAnalogInWrapper modeWrapper = SensorActuatorRegistry.get().getAnalogIn().get(PortMappings2018.sMODE_CHOOSER_SWITCH);
        IAnalogInWrapper positionWrapper = SensorActuatorRegistry.get().getAnalogIn().get(PortMappings2018.sPOSITION_CHOOSER_SWITCH);

        modeWrapper.setVoltage(SmartDashboard.getNumber("SIMULATOR.ModeSwitch", 2.5));
        positionWrapper.setVoltage(SmartDashboard.getNumber("SIMULATOR.PositionSwitch", 4));
    }
}
